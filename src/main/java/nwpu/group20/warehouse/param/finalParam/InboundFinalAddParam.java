package nwpu.group20.warehouse.param.finalParam;

import lombok.Data;
import nwpu.group20.warehouse.param.InboundAddParam;
import nwpu.group20.warehouse.param.InboundDetailsParam;
import nwpu.group20.warehouse.param.InboundParam;
import nwpu.group20.warehouse.param.finalParam.part.InboundFinalAddParamPart1;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Data
public class InboundFinalAddParam {
    InboundFinalAddParamPart1 inboundFinalAddParamPart1;
    private List<InboundDetailsParam> inboundDetailsParamList;

    public InboundAddParam toInboundAddParam(){
        InboundAddParam inboundAddParam = new InboundAddParam();
        inboundAddParam.setInboundParam(new InboundParam(inboundFinalAddParamPart1.getInboundOrderDescription(),inboundFinalAddParamPart1.getOperatorId()));
        inboundAddParam.setInboundDetailsParamList(inboundDetailsParamList);
        return inboundAddParam;
    }
}
