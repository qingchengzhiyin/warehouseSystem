package nwpu.group20.warehouse.param;

import lombok.Data;

import java.util.List;

@Data
public class InboundAddParam {
    private InboundParam inboundParam;
    private List<InboundDetailsParam> inboundDetailsParamList;
}
