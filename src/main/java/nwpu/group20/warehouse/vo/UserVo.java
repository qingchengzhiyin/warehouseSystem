package nwpu.group20.warehouse.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "用户视图对象")
public class UserVo {
    @Schema(description = "用户ID", example = "1")
    private int userId;
    @Schema(description = "用户昵称", example = "nickname")
    private String userNickname;
    @Schema(description = "用户名", example = "username")
    private String userName;
    @Schema(description = "用户类型", example = "1")
    private int userType;
}
