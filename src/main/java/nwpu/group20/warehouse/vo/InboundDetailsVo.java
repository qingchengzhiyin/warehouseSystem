package nwpu.group20.warehouse.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import nwpu.group20.warehouse.util.CustomDateDeserializer;
import nwpu.group20.warehouse.util.CustomDateSerializer;

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
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    Date productionDate;
    int inboundNumberTheoretical;
    int inboundNumberTrue;
}
