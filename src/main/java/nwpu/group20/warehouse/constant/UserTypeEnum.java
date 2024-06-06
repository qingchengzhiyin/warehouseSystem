package nwpu.group20.warehouse.constant;

import lombok.Data;

/**
 * 不同类型用户
 */
public enum UserTypeEnum {
    ROLE_MANAGER(0,"经理"),
    ROLE_OPERATOR(1,"操作员"),
    ROLE_ADMIN(2,"管理员");


    int code;
    String userType;

    UserTypeEnum(int code, String userType) {
    }
}
