package nwpu.group20.warehouse.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
public class User {
    private int userId;
    private String userName;
    private String userNickname;
    private String userPassword;
    private int userType;//不同类型的用户


}
