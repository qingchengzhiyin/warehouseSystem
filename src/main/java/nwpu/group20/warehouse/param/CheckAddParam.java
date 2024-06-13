package nwpu.group20.warehouse.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CheckAddParam {
    @NotNull
    private int operatorId;
    private int checkId;
}
