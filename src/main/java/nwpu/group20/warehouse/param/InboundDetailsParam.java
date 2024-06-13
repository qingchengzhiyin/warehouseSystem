package nwpu.group20.warehouse.param;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import nwpu.group20.warehouse.util.CustomDateDeserializer;
import nwpu.group20.warehouse.util.CustomDateSerializer;

import java.util.Date;
@Data
public class InboundDetailsParam {
    int inboundOrderId;
    int productId;
    int locationId;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    Date productionDate;
    int inboundNumberTheoretical;
    int inboundNumberTure;
}
