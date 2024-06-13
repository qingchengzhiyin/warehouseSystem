package nwpu.group20.warehouse.mapper;

import nwpu.group20.warehouse.param.InboundDetailsParam;
import nwpu.group20.warehouse.param.finalParam.InboundDetailsUpdateParam;
import nwpu.group20.warehouse.vo.InboundDetailsVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface InboundDetailsMapper {
    void insertInboundDetails(InboundDetailsParam details);

    void deleteInboundDetailsByOrderId(int inboundOrderId);

    List<InboundDetailsVo> selectInboundOrderById(int inboundOrderId);

    InboundDetailsVo selectInboundDetailsById(int inboundOrderId, int productId, Date productionDate);

    void updateInboundDetails(InboundDetailsUpdateParam param);
}
