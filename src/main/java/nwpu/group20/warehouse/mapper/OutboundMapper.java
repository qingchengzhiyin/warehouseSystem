package nwpu.group20.warehouse.mapper;

import nwpu.group20.warehouse.entity.Outbound;
import nwpu.group20.warehouse.param.BoundNumberParam;
import nwpu.group20.warehouse.param.OutboundParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface OutboundMapper {
    void insertOutbound(OutboundParam outboundParam);
    void deleteOutbound(int outboundOrderId);
    Outbound selectOutboundById(int outboundOrderId);
    List<Outbound> loadAllOutbounds();

    List<Map<String, Object>> getOperatorTaskCounts();

    void completeFinish(int outboundOrderId);

    // 查询对应时间范围内的所有出库单
    List<BoundNumberParam> selectOutboundByDate(Date startTime, Date endTime);
}
