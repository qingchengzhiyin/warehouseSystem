package nwpu.group20.warehouse.mapper;

import nwpu.group20.warehouse.entity.User;
import nwpu.group20.warehouse.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    public List<UserVo> loadAllUsers();

    public User loadUserInfoByUserNickname(String userNickname);

    public User insert(String userName,String userPassword,int userType,String userNickname);

    public void removeUserByUserId(int userId);

    void changeUserInfo(int userId, String userNickname, String userPassword, int userType, String userName);

    User loadUserByUserId(int userId);

    List<Integer> getOperatorIds();

    int getUserType(int userId);
}
