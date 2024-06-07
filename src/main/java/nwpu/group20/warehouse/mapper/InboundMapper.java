package nwpu.group20.warehouse.mapper;

import nwpu.group20.warehouse.entity.Inbound;
import nwpu.group20.warehouse.param.InboundParam;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface InboundMapper {
    int insertInbound(InboundParam inboundParam);

    void deleteInbound(int inboundId);

    Inbound selectInboundById(int inboundId);

@MapKey("operator_id")
    List<Map<String, Object>> getOperatorTaskCounts();
}
