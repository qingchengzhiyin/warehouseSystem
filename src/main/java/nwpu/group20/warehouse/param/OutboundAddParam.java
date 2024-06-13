package nwpu.group20.warehouse.param;

import lombok.Data;

import java.util.List;
@Data
public class OutboundAddParam {
    private OutboundParam outboundParam;
    private List<OutboundDetailsParam> outboundDetailsParamList;
}
