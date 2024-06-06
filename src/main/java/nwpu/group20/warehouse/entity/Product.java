package nwpu.group20.warehouse.entity;

import lombok.Data;

@Data
public class Product {
    private String productId;
    private String description;
    private String provider;
    private float cost;
    private float price;
}
