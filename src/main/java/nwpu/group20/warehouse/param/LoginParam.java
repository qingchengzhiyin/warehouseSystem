package nwpu.group20.warehouse.param;

import lombok.Data;
import org.apache.ibatis.annotations.Param;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class LoginParam {
    @NotBlank
    private String userNickname;
    @NotBlank
    private String userPassword;

}
