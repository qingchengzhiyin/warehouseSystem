package nwpu.group20.warehouse.vo;

import lombok.Data;

import java.util.Date;

@Data
public class CheckVo {
    private int checkId;
    private int operatorId;
    private Date createTime;
    private Date updateTime;
}
