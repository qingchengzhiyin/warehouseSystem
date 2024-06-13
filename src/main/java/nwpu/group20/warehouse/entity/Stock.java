package nwpu.group20.warehouse.entity;

import lombok.Data;

import java.util.Date;
@Data
public class Stock {
    int productId;
    Date productionDate;
    int locationId;
    int number;
    int status;
}
