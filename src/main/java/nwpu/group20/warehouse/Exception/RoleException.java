package nwpu.group20.warehouse.Exception;

import lombok.Data;

@Data
public class RoleException extends RuntimeException{
    public RoleException(String msg){
        super(msg);
    }
}
