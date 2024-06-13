package nwpu.group20.warehouse.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import nwpu.group20.warehouse.util.CustomDateDeserializer;
import nwpu.group20.warehouse.util.CustomDateSerializer;

import java.util.Date;
@Data
public class ProductStockVo {
    private int productId;
    private String description;
    private String provider;
    private float cost;
    private float price;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Date productionDate;
    private int locationId;
    private int num;
}
