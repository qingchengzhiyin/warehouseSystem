package nwpu.group20.warehouse.service;

import nwpu.group20.warehouse.entity.Outbound;
import nwpu.group20.warehouse.param.BoundNumberParam;
import nwpu.group20.warehouse.param.OutboundDetailsParam;
import nwpu.group20.warehouse.param.OutboundParam;
import nwpu.group20.warehouse.param.finalParam.OutboundFinishStockParam;
import nwpu.group20.warehouse.vo.OutboundDetailsVo;

import java.util.Date;
import java.util.List;

public interface OutboundService {
    void insertOutbound(OutboundParam outboundParam, List<OutboundDetailsParam> outboundDetailsParamList);

    void deleteOutbound(int outboundOrderId);

    List<OutboundDetailsVo> selectOutboundOrderById(int outboundOrderId);

    List<OutboundFinishStockParam> completeOutbound(int outboundOrderId);

    List<Outbound> loadAllOutbounds();

    // 获取出库单
    List<BoundNumberParam> selectOutboundByDate(Date startTime, Date endTime);
}
