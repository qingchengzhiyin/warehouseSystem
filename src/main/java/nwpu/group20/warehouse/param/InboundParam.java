package nwpu.group20.warehouse.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class InboundParam {
    int inboundOrderId;
    String inboundOrderDescription;
    int managerId;
    int operatorId = 0;//选填
    Date createTime;
    Date updateTime;
    int isFinshed = 0;//不用填
}
