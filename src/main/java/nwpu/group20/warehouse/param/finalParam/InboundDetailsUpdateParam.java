package nwpu.group20.warehouse.param.finalParam;

import lombok.Data;

import java.util.Date;

@Data
public class InboundDetailsUpdateParam {
    private int inboundOrderId;
    private int productId;
    private Date productionDate;
    private int locationId;
    private int inboundNumberTrue;
}
