package nwpu.group20.warehouse.param;

import lombok.Data;

import java.util.Date;
@Data
public class InboundDetailsParam {
    int inboundOrderId;
    int productId;
    int locationId;
    Date productionDate;
    int inboundNumberTheoretical;
    int inboundNumberTure;
}
