package nwpu.group20.warehouse.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RegistParam {
    @NotBlank
    private String userName;
    @NotBlank
    private String userPassword;
    @NotNull
    private int userType;
    @NotBlank
    private String userNickname;
}
