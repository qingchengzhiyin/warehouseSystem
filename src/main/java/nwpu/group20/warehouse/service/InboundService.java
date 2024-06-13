package nwpu.group20.warehouse.service;

import nwpu.group20.warehouse.entity.Inbound;
import nwpu.group20.warehouse.param.BoundNumberParam;
import nwpu.group20.warehouse.param.InboundDetailsParam;
import nwpu.group20.warehouse.param.InboundParam;
import nwpu.group20.warehouse.param.finalParam.InboundDetailsUpdateParam;
import nwpu.group20.warehouse.vo.InboundDetailsVo;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface InboundService {

    void insertInbound(InboundParam inboundParam, List<InboundDetailsParam> inboundDetailsParamList);

    void deleteInbound(int inboundId);

    List<InboundDetailsVo> selectInboundOrderById(int inboundOrderId);

   // void completeInbound(int inboundOrderId);

    @Transactional
    void completeInbound(List<InboundDetailsUpdateParam> inboundDetailsUpdateParams);

    List<Inbound> loadAllInbounds();

    // 获取入库单
    List<BoundNumberParam> selectInboundByDate(Date startTime, Date endTime);
}
