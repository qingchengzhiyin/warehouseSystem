package nwpu.group20.warehouse.param.finalParam;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import nwpu.group20.warehouse.util.CustomDateDeserializer;
import nwpu.group20.warehouse.util.CustomDateSerializer;

import java.util.Date;
@Data
@AllArgsConstructor
public class OutboundFinishStockParam {
    int productId;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    Date productionDate;
    int locationId;
    int num;
}
