package nwpu.group20.warehouse.vo;

import lombok.Data;

import java.util.Date;
@Data
public class InboundDetailsVo {
    int inboundOrderId;
    String inboundOrderDescription;
    int managerId;
    int operatorId;
    Date createTime;
    Date updateTime;
    int isFinished;
    int productId;
    int locationId;
    Date productionDate;
    int inbound_number_theoretical;
    int inbound_number_true;
}
