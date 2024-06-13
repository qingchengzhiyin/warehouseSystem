package nwpu.group20.warehouse.param.finalParam;

import lombok.Data;
import nwpu.group20.warehouse.param.InfoParam;
import nwpu.group20.warehouse.param.finalParam.part.UserChangeParamChangePart;

@Data
public class UserChangeParam {
    int userId;
    UserChangeParamChangePart userAddParamChangePart;
    public InfoParam toInfoParam(){
        InfoParam infoParam = new InfoParam();
        infoParam.setUserId(userId);
        infoParam.setUserNickname(userAddParamChangePart.getUserNickname());
        infoParam.setUserPassword(userAddParamChangePart.getUserPassword());
        infoParam.setUserType(userAddParamChangePart.getUserType());
        infoParam.setUserName(userAddParamChangePart.getUserName());
        return infoParam;
    }
}
