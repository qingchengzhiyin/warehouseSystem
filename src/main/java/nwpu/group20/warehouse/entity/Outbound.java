package nwpu.group20.warehouse.entity;

import lombok.Data;

import java.util.Date;
@Data
public class Outbound {
    int outboundOrderId;
    int outboundOrderDescription;
    int managerId;
    int operatorId;
    Date CreateTime;
    Date updateTime;
    int isFinished;


}
