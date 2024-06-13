package nwpu.group20.warehouse.vo;

import lombok.Data;

@Data
public class CheckDetailsVo {
    private int checkId;
    private int productId;
    private int locationId;
    private int checkNumberTheory;
    private int checkNumber;

}
