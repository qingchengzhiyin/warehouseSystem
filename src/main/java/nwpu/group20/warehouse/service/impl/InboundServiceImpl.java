package nwpu.group20.warehouse.service.impl;

import lombok.extern.slf4j.Slf4j;
import nwpu.group20.warehouse.Exception.RoleException;
import nwpu.group20.warehouse.Exception.StockException;
import nwpu.group20.warehouse.entity.Inbound;
import nwpu.group20.warehouse.entity.Stock;
import nwpu.group20.warehouse.lock.LockParam;
import nwpu.group20.warehouse.lock.RedisLock;
import nwpu.group20.warehouse.mapper.*;
import nwpu.group20.warehouse.param.BoundNumberParam;
import nwpu.group20.warehouse.param.InboundDetailsParam;
import nwpu.group20.warehouse.param.InboundParam;
import nwpu.group20.warehouse.param.finalParam.InboundDetailsUpdateParam;
import nwpu.group20.warehouse.service.InboundService;
import nwpu.group20.warehouse.vo.InboundDetailsVo;
import nwpu.group20.warehouse.vo.LocationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@Slf4j
public class InboundServiceImpl implements InboundService {
    @Autowired
    private InboundMapper inboundMapper;
    @Autowired
    private OutboundMapper outboundMapper;
    @Autowired
    private UserMapper userMapper; // 注入UserMapper
    @Autowired
    private InboundDetailsMapper inboundDetailsMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private LocationMapper locationMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private Jedis jedis;
    private static final String TASK_ZSET_KEY = "operator:tasks";

    @PostConstruct
    public void initOperators() {
        // 获取所有userType为1的操作员
        List<Integer> operatorIds = userMapper.getOperatorIds();
        // 获取所有操作员的任务量
        List<Map<String, Object>> operatorTaskCounts = inboundMapper.getOperatorTaskCounts();
        List<Map<String, Object>> outboundTaskCounts = outboundMapper.getOperatorTaskCounts();
        Map<Integer, Long> taskCounts = new HashMap<>();
        for (Map<String, Object> entry : operatorTaskCounts) {
            Integer operatorId = (Integer) entry.get("operator_id");
            Long taskCount = ((Number) entry.get("task_count")).longValue();
            taskCounts.put(operatorId, taskCount);
        }

        // 统计出库任务数量
        for (Map<String, Object> entry : outboundTaskCounts) {
            Integer operatorId = (Integer) entry.get("operator_id");
            Long taskCount = ((Number) entry.get("task_count")).longValue();
            taskCounts.put(operatorId, taskCounts.getOrDefault(operatorId, 0L) + taskCount);
        }

        for (Integer operatorId : operatorIds) {
            // 获取操作员的任务量
            Long taskCount = taskCounts.getOrDefault(operatorId, 0L);
            // 将操作员添加到Redis的zset中
            redisTemplate.opsForZSet().add(TASK_ZSET_KEY, String.valueOf(operatorId), taskCount);
        }
    }



    @Override
    public void insertInbound(InboundParam inboundParam, List<InboundDetailsParam> inboundDetailsParamList) {
        int operatorIdInt;
        int managerId = UserServiceImpl.getCurrentUserId();
        inboundParam.setManagerId(managerId);

        if (userMapper.getUserType(managerId) != 0) {
            throw new RuntimeException("不是经理");
        }

        if (inboundParam.getOperatorId() != 0) {
            // 使用传入的操作员ID
            operatorIdInt = inboundParam.getOperatorId();
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

            // 设置操作员ID到入库参数
            inboundParam.setOperatorId(operatorIdInt);
        }
        inboundParam.setCreateTime(new Date());
        inboundParam.setUpdateTime(new Date());
        // 插入入库记录
        inboundMapper.insertInbound(inboundParam);
        int inboundOrderId = inboundParam.getInboundOrderId();

        log.info(String.valueOf(inboundOrderId));
        // 插入入库详情记录
        for (InboundDetailsParam details : inboundDetailsParamList) {
            details.setInboundOrderId(inboundOrderId); // 设置入库订单ID
            inboundDetailsMapper.insertInboundDetails(details);
        }

        // 更新Redis中操作员的任务数量
        redisTemplate.opsForZSet().incrementScore(TASK_ZSET_KEY, String.valueOf(operatorIdInt), 1);
    }

    @Override
    public void deleteInbound(int inboundOrderId) {
        // 获取要删除的inbound记录的operatorId
        Inbound inbound = inboundMapper.selectInboundById(inboundOrderId);
        if (inbound.getIsFinished() == 1) {
            throw new RuntimeException("订单已完成，不能删除");
        } else {
            int operatorIdInt = inbound.getOperatorId();

            // 删除入库详情记录
            inboundDetailsMapper.deleteInboundDetailsByOrderId(inboundOrderId);

            // 删除入库记录
            inboundMapper.deleteInbound(inboundOrderId);

            // 更新Redis中操作员的任务数量
            redisTemplate.opsForZSet().incrementScore(TASK_ZSET_KEY, String.valueOf(operatorIdInt), -1);
        }
    }

    @Override
    public List<InboundDetailsVo> selectInboundOrderById(int inboundOrderId) {
        return inboundDetailsMapper.selectInboundOrderById(inboundOrderId);

    }

    @Override
    @Transactional
    public void completeInbound(List<InboundDetailsUpdateParam> inboundDetailsUpdateParams) {
        LockParam lockParam = new LockParam("STOCK-CHANGE", 100L, 10000L);
        int userId = UserServiceImpl.getCurrentUserId();

        // 验证操作员权限
        for (InboundDetailsUpdateParam param : inboundDetailsUpdateParams) {
            if (userId != inboundMapper.selectInboundById(param.getInboundOrderId()).getOperatorId()) {
                throw new RoleException("不是对应操作员");
            }
            break;
        }

        try (RedisLock redisLock = new RedisLock(lockParam, jedis)) {
            if (redisLock.lock(userId)) {
                try {
                    for (InboundDetailsUpdateParam param : inboundDetailsUpdateParams) {
                        // 更新入库单状态
                        inboundMapper.completeFinish(param.getInboundOrderId());

                        // 获取InboundDetails记录
                        InboundDetailsVo details = inboundDetailsMapper.selectInboundDetailsById(param.getInboundOrderId(), param.getProductId(), param.getProductionDate());

                        // 更新入库详细信息
                        if (details != null) {
                            inboundDetailsMapper.updateInboundDetails(param);
                        }

                        // 更新库存和库位容量
                        if (details != null) {
                            Stock existingStock = stockMapper.getStock(details.getProductId(), details.getProductionDate(), param.getLocationId());
                            if (existingStock != null) {
                                stockMapper.updateStock(details.getProductId(), details.getProductionDate(), param.getLocationId(), param.getInboundNumberTrue());
                            } else {
                                Stock stock = new Stock();
                                stock.setProductId(details.getProductId());
                                stock.setProductionDate(details.getProductionDate());
                                stock.setLocationId(param.getLocationId());
                                stock.setNumber(param.getInboundNumberTrue());
                                stock.setStatus(0); // 刚入未过期
                                stockMapper.addSingleStock(stock);
                            }

                            LocationVo location = locationMapper.loadLocationById(param.getLocationId());
                            float newCapacityUse = location.getCapacityUse() + param.getInboundNumberTrue();
                            if (newCapacityUse > location.getCapacity()) {
                                throw new StockException("Capacity use exceeds the capacity for locationId: " + location.getLocationId());
                            }
                            locationMapper.changeLocationById(param.getLocationId(), newCapacityUse);
                        }
                    }
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
            throw new RuntimeException("Failed to complete inbound", e);
        }
    }

    // 查询对应时间范围内所有的入库单记录
    @Override
    public List<BoundNumberParam> selectInboundByDate(Date startTime, Date endTime) {
        return inboundMapper.selectInboundByDate(startTime,endTime);
    }

    @Override
    public List<Inbound> loadAllInbounds() {
        return inboundMapper.loadAllInbounds();
    }
}
