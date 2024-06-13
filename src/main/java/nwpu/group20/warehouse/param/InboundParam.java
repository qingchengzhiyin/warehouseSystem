package nwpu.group20.warehouse.param;

import lombok.Data;

import java.util.Date;

@Data
public class InboundParam {
    int inboundOrderId;
    String inboundOrderDescription;
    int managerId;
    int operatorId = 0;//选填
    Date createTime;
    Date updateTime;
    int isFinshed = 0;//不用填

    public InboundParam(InboundFinalAddParamPart1 inboundFinalAddParamPart1){
        this.inboundOrderDescription = inboundFinalAddParamPart1.getInboundOrderDescription();
        this.operatorId = inboundFinalAddParamPart1.getOperatorId();
        this.createTime = new Date();
        this.updateTime = new Date();
        this.isFinshed = 0;
    }

    public InboundParam(String inboundOrderDescription, int operatorId) {
        this.inboundOrderDescription = inboundOrderDescription;
        this.operatorId = operatorId;
    }
}
