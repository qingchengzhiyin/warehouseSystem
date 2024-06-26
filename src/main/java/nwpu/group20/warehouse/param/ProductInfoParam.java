package nwpu.group20.warehouse.param;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductInfoParam {
    private String description;
    private String provider;
    private float cost;
    private float price;
}
