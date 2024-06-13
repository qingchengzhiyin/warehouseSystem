package nwpu.group20.warehouse.param;

import lombok.Data;
import lombok.NoArgsConstructor;
import nwpu.group20.warehouse.param.finalParam.part.OutboundFinalAddParamPart2;

@Data
@NoArgsConstructor
public class OutboundDetailsParam {
    private int outboundOrderId;
    private int productId;
    private int outboundNumber;
    public OutboundDetailsParam(OutboundFinalAddParamPart2 part2){
        this.productId = part2.getProductId();
        this.outboundNumber = part2.getOutboundNumber();
    }
}
