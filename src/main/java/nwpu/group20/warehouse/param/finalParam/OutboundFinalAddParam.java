package nwpu.group20.warehouse.param.finalParam;

import io.swagger.annotations.Api;
import lombok.Data;
import nwpu.group20.warehouse.param.OutboundAddParam;
import nwpu.group20.warehouse.param.OutboundDetailsParam;
import nwpu.group20.warehouse.param.OutboundParam;
import nwpu.group20.warehouse.param.finalParam.part.OutboundFinalAddParamPart1;
import nwpu.group20.warehouse.param.finalParam.part.OutboundFinalAddParamPart2;

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
