package nwpu.group20.warehouse.param;

import lombok.Data;

import java.sql.Date;

@Data
public class BoundNumberParam {
    private Date date;
    private int totalNumber;
}
