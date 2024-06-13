package nwpu.group20.warehouse.service.impl;

import lombok.extern.slf4j.Slf4j;
import nwpu.group20.warehouse.Exception.RoleException;
import nwpu.group20.warehouse.Exception.StockException;
import nwpu.group20.warehouse.entity.Outbound;
import nwpu.group20.warehouse.entity.Stock;
import nwpu.group20.warehouse.lock.LockParam;
import nwpu.group20.warehouse.lock.RedisLock;
import nwpu.group20.warehouse.mapper.*;
import nwpu.group20.warehouse.param.BoundNumberParam;
import nwpu.group20.warehouse.param.OutboundDetailsParam;
import nwpu.group20.warehouse.param.OutboundParam;
import nwpu.group20.warehouse.param.finalParam.OutboundFinishStockParam;
import nwpu.group20.warehouse.service.OutboundService;
import nwpu.group20.warehouse.vo.OutboundDetailsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class OutboundServiceImpl implements OutboundService {
    @Autowired
    private OutboundMapper outboundMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OutboundDetailsMapper outboundDetailsMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private LocationMapper locationMapper;
    @Autowired
    private Jedis jedis;
    private static final String TASK_ZSET_KEY = "operator:tasks";
    @Override
    public void insertOutbound(OutboundParam outboundParam, List<OutboundDetailsParam> outboundDetailsParamList) {
        int operatorIdInt;
        int managerId = UserServiceImpl.getCurrentUserId();
        outboundParam.setManagerId(managerId);

        if (userMapper.getUserType(managerId) != 0) {
            throw new RuntimeException("不是经理");
        }

        if (outboundParam.getOperatorId() != 0) {
            // 使用传入的操作员ID
            operatorIdInt = outboundParam.getOperatorId();
            if (userMapper.getUserType(operatorIdInt) != 1) {
                throw new RuntimeException("Invalid operatorId: not an operator");
            }
        } else {
            // 从Redis中获取任务最少的操作员
            Set<String> operators = redisTemplate.opsForZSet().range(TASK_ZSET_KEY, 0, 0);
            if (operators == null || operators.isEmpty()) {
                throw new RuntimeException("No operators available");
            }
            String operatorId = operators.iterator().next();
            operatorIdInt = Integer.parseInt(operatorId);

            // 设置操作员ID到出库参数
            outboundParam.setOperatorId(operatorIdInt);
        }
        outboundParam.setCreateTime(new Date());
        outboundParam.setUpdateTime(new Date());
        // 插入出库记录
        outboundMapper.insertOutbound(outboundParam);
        int outboundOrderId = outboundParam.getOutboundOrderId();

        log.info(String.valueOf(outboundOrderId));
        // 插入出库详情记录
        for (OutboundDetailsParam details : outboundDetailsParamList) {
            details.setOutboundOrderId(outboundOrderId); // 设置出库订单ID
            outboundDetailsMapper.insertOutboundDetails(details);
        }

        // 更新Redis中操作员的任务数量
        redisTemplate.opsForZSet().incrementScore(TASK_ZSET_KEY, String.valueOf(operatorIdInt), 1);
    }

    @Override
    public void deleteOutbound(int outboundOrderId) {
        // 获取要删除的outbound记录的operatorId
        Outbound outbound = outboundMapper.selectOutboundById(outboundOrderId);
        if (outbound.getIsFinished() == 1) {
            throw new RuntimeException("订单已完成，不能删除");
        } else {
            int operatorIdInt = outbound.getOperatorId();

            // 删除出库详情记录
            outboundDetailsMapper.deleteOutboundDetailsByOrderId(outboundOrderId);

            // 删除出库记录
            outboundMapper.deleteOutbound(outboundOrderId);

            // 更新Redis中操作员的任务数量
            redisTemplate.opsForZSet().incrementScore(TASK_ZSET_KEY, String.valueOf(operatorIdInt), -1);
        }
    }

    @Override
    public List<OutboundDetailsVo> selectOutboundOrderById(int outboundOrderId) {

        return outboundDetailsMapper.selectOutboundOrderById(outboundOrderId);
    }

    @Override
    @Transactional
    public List<OutboundFinishStockParam> completeOutbound(int outboundOrderId) {
        List<OutboundFinishStockParam> outboundFinishStockParams = new ArrayList<OutboundFinishStockParam>();
        LockParam lockParam = new LockParam("STOCK-CHANGE", 100L, 10000L);
        int userId = UserServiceImpl.getCurrentUserId();
        if(userId != outboundMapper.selectOutboundById(outboundOrderId).getOperatorId()){
            throw new RoleException("不是对应操作员");
        }
        try (RedisLock redisLock = new RedisLock(lockParam, jedis)) {
            if (redisLock.lock(userId)) {
                try {
                    // 获取OutboundDetails记录
                    List<OutboundDetailsVo> outboundDetails = outboundDetailsMapper.selectOutboundOrderById(outboundOrderId);
                    if (outboundDetails != null && !outboundDetails.isEmpty()) {
                        // 检查库存是否足够
                        for (OutboundDetailsVo details : outboundDetails) {
                            int totalStock = stockMapper.getTotalStock(details.getProductId());
                            if (totalStock < details.getOutboundNumber()) {
                                throw new StockException("库存不足，无法完成出库");
                            }
                        }

                        // 优先挑生产日期近的拿，生产日期近的拿完拿第二近的
                        for (OutboundDetailsVo details : outboundDetails) {
                            List<Stock> stockList = stockMapper.getStockByProductIdOrderedByProductionDate(details.getProductId());
                            int remainingQuantity = details.getOutboundNumber();
                            for (Stock stock : stockList) {
                                if (remainingQuantity <= 0) {
                                    break;
                                }
                                int stockNumber = stock.getNumber();
                                if (stockNumber >= remainingQuantity) {
                                    stockMapper.updateStock(stock.getProductId(), stock.getProductionDate(), stock.getLocationId(), -remainingQuantity);
                                    locationMapper.changeLocationById(stock.getLocationId(), locationMapper.loadLocationById(stock.getLocationId()).getCapacityUse() - remainingQuantity);
                                    OutboundFinishStockParam outboundFinishStockParam= new OutboundFinishStockParam(stock.getProductId(),stock.getProductionDate(),stock.getLocationId(),remainingQuantity);
                                    outboundFinishStockParams.add(outboundFinishStockParam);
                                    remainingQuantity = 0;
                                } else {
                                    stockMapper.updateStock(stock.getProductId(), stock.getProductionDate(), stock.getLocationId(), -stockNumber);
                                    locationMapper.changeLocationById(stock.getLocationId(), locationMapper.loadLocationById(stock.getLocationId()).getCapacityUse() - stockNumber);
                                    OutboundFinishStockParam outboundFinishStockParam= new OutboundFinishStockParam(stock.getProductId(),stock.getProductionDate(),stock.getLocationId(),stockNumber);
                                    outboundFinishStockParams.add(outboundFinishStockParam);
                                    remainingQuantity -= stockNumber;
                                }
                            }
                        }

                        outboundMapper.completeFinish(outboundOrderId);
                    }
                    return outboundFinishStockParams;
                } finally {
                    if (!redisLock.unLock(userId)) {
                        throw new RuntimeException("Failed to release lock");
                    }
                }
            } else {
                throw new RuntimeException("Could not acquire lock");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to complete outbound", e);
        }
    }

    // 查询对应时间范围内所有的出库单记录
    @Override
    public List<BoundNumberParam> selectOutboundByDate(Date startTime, Date endTime) {
        return outboundMapper.selectOutboundByDate(startTime,endTime);
    }
    @Override
    public List<Outbound> loadAllOutbounds() {
        return outboundMapper.loadAllOutbounds();
    }
}
