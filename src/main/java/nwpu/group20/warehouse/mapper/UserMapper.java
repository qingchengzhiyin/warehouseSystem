package nwpu.group20.warehouse.mapper;

import nwpu.group20.warehouse.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    public List<User> loadAllUsers();

    public User loadUserInfoByUserNickname(String userNickname);

    public User insert(String userName,String userPassword,int userType,String userNickname);
}
