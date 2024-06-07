package nwpu.group20.warehouse.entity;

import lombok.Data;

import java.util.Date;
@Data
public class Inbound {
    int inboundOrderId;
    String inboundOrderDescription;
    int managerId;
    int operatorId;
    Date createTime;
    Date updateTime;
    int isFinshed;
}
