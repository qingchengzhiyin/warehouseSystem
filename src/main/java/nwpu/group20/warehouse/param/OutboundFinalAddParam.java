package nwpu.group20.warehouse.param.finalParam;

import lombok.Data;
import nwpu.group20.warehouse.param.OutboundAddParam;
import nwpu.group20.warehouse.param.OutboundDetailsParam;
import nwpu.group20.warehouse.param.OutboundParam;
import nwpu.group20.warehouse.param.OutboundFinalAddParamPart1;

import java.util.List;

@Data
public class OutboundFinalAddParam {
    private OutboundFinalAddParamPart1 outboundFinalAddParamPart1;
    private List<OutboundDetailsParam> outboundDetailsParamList;

    public OutboundAddParam toOutboundAddParam(){
        OutboundAddParam outboundAddParam = new OutboundAddParam();
        outboundAddParam.setOutboundParam(new OutboundParam(outboundFinalAddParamPart1));
        outboundAddParam.setOutboundDetailsParamList(outboundDetailsParamList);
        return outboundAddParam;
    }

}
