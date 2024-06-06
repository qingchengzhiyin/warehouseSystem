package nwpu.group20.warehouse.vo;

import lombok.Data;

@Data
public class ProductVo {
    private String productId;
    private String description;
    private String provider;
    private float cost;
    private float price;
    private int num;
}
