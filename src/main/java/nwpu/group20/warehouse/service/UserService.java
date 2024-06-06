package nwpu.group20.warehouse.service;

import nwpu.group20.warehouse.entity.User;
import nwpu.group20.warehouse.vo.UserVo;

import java.util.List;

public interface UserService {
    public List<UserVo> loadAllUsers();
    public User login(String userNickname,String userPassword);

    public User register(String userName, String userPassword,int userType,String userNickname)  throws Exception ;

    public void removeUserByUserId(int userId);

    public void logOut();

    User loadUserByUserId(int productId);

    void changeUserInfo(int userId,String userNickname, String userPassword, int userType, String userName);
}
