package nwpu.group20.warehouse.mapper;

import nwpu.group20.warehouse.entity.Inbound;
import nwpu.group20.warehouse.param.BoundNumberParam;
import nwpu.group20.warehouse.param.InboundParam;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface InboundMapper {
    int insertInbound(InboundParam inboundParam);

    void deleteInbound(int inboundId);

    Inbound selectInboundById(int inboundId);

@MapKey("operator_id")
    List<Map<String, Object>> getOperatorTaskCounts();

    void completeFinish(int inboundOrderId);

    List<Inbound> loadAllInbounds();

    // 查询对应时间范围内的所有入库单
    List<BoundNumberParam> selectInboundByDate(Date startTime, Date endTime);
}
