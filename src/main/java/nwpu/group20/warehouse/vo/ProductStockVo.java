package nwpu.group20.warehouse.vo;

import lombok.Data;

import java.util.Date;
@Data
public class ProductStockVo {
    private int productId;
    private String description;
    private String provider;
    private float cost;
    private float price;
    private Date productionDate;
    private int locationId;
    private int num;
}
