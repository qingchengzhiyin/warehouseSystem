package nwpu.group20.warehouse.param;

import lombok.Data;

@Data
public class ProductInfoParam {
    private String description;
    private String provider;
    private float cost;
    private float price;
}
