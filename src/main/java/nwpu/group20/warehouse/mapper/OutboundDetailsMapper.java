package nwpu.group20.warehouse.mapper;

import nwpu.group20.warehouse.param.OutboundDetailsParam;
import nwpu.group20.warehouse.vo.OutboundDetailsVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OutboundDetailsMapper {
    public void insertOutboundDetails(OutboundDetailsParam details);

    public void deleteOutboundDetailsByOrderId(int outboundOrderId);

    public List<OutboundDetailsVo> selectOutboundOrderById(int outboundOrderId);
}
