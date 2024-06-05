package nwpu.group20.warehouse.service;

import nwpu.group20.warehouse.entity.User;

import java.util.List;

public interface UserService {
    public List<User> loadAllUsers();
    public User login(String userNickname,String userPassword);

    public User register(String userName, String userPassword,int userType,String userNickname)  throws Exception ;

}
