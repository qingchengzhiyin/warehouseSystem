package nwpu.group20.warehouse.service.impl;

import lombok.extern.slf4j.Slf4j;
import nwpu.group20.warehouse.entity.Inbound;
import nwpu.group20.warehouse.mapper.InboundDetailsMapper;
import nwpu.group20.warehouse.mapper.InboundMapper;
import nwpu.group20.warehouse.mapper.UserMapper;
import nwpu.group20.warehouse.param.InboundDetailsParam;
import nwpu.group20.warehouse.param.InboundParam;
import nwpu.group20.warehouse.service.InboundService;
import nwpu.group20.warehouse.vo.InboundDetailsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class InboundServiceImpl implements InboundService {
    @Autowired
    private InboundMapper inboundMapper;
    @Autowired
    private UserMapper userMapper; // 注入UserMapper
    @Autowired
    private InboundDetailsMapper inboundDetailsMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private static final String TASK_ZSET_KEY = "operator:tasks";

    @PostConstruct
    public void initOperators() {
        // 获取所有userType为1的操作员
        List<Integer> operatorIds = userMapper.getOperatorIds();
        // 获取所有操作员的任务量
        List<Map<String, Object>> operatorTaskCounts = inboundMapper.getOperatorTaskCounts();
        Map<Integer, Long> taskCounts = new HashMap<>();
        for (Map<String, Object> entry : operatorTaskCounts) {
            Integer operatorId = (Integer) entry.get("operator_id");
            Long taskCount = ((Number) entry.get("task_count")).longValue();
            taskCounts.put(operatorId, taskCount);
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
        int managerId = inboundParam.getManagerId();

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
        if (inbound.getIsFinshed() == 1) {
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
}
