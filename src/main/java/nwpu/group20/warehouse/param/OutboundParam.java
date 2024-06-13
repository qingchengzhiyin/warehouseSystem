package nwpu.group20.warehouse.param;

import lombok.Data;
import nwpu.group20.warehouse.param.finalParam.part.OutboundFinalAddParamPart1;

import java.util.Date;

@Data
public class OutboundParam {
    private int outboundOrderId;
    private String outboundOrderDescription;
    private int managerId;
    private int operatorId;
    private Date createTime;
    private Date updateTime;
    private int isFinished;
    public OutboundParam(OutboundFinalAddParamPart1 part1){
        this.outboundOrderDescription = part1.getOutboundOrderDescription();
        this.operatorId = part1.getOperatorId();
        this.createTime = new Date();
        this.updateTime = new Date();
        this.isFinished = 0;

    }
}
