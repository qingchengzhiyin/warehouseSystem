package nwpu.group20.warehouse.service;

import nwpu.group20.warehouse.param.InboundDetailsParam;
import nwpu.group20.warehouse.param.InboundParam;
import nwpu.group20.warehouse.vo.InboundDetailsVo;

import java.util.List;

public interface InboundService {

    void insertInbound(InboundParam inboundParam, List<InboundDetailsParam> inboundDetailsParamList);

    void deleteInbound(int inboundId);

    List<InboundDetailsVo> selectInboundOrderById(int inboundOrderId);
}
