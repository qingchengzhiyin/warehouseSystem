package nwpu.group20.warehouse.service.impl;

import lombok.extern.slf4j.Slf4j;
import nwpu.group20.warehouse.entity.User;
import nwpu.group20.warehouse.mapper.UserMapper;
import nwpu.group20.warehouse.security.UserDetailsImpl;
import nwpu.group20.warehouse.service.UserService;
import nwpu.group20.warehouse.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;
    public List<UserVo> loadAllUsers(){
        return userMapper.loadAllUsers();
    }

    public User register(String username, String password,int userType,String userNickname) throws Exception {
        User user = userMapper.loadUserInfoByUserNickname(username);
        if (user != null) {
            log.warn("用户名" + username + "已存在");
            throw new Exception();

        }
        try {
            userMapper.insert(username, passwordEncoder.encode(password),userType,userNickname);
        } catch (DuplicateKeyException e) {
            log.warn("注册失败");
            throw new Exception();

        }
        return user;
    }

    @Override
    public void removeUserByUserId(int userId) {
        userMapper.removeUserByUserId(userId);
    }


    public User login(String userNickname,String password){
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userNickname, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info(authentication.toString());
        } catch (AuthenticationException e) {
            log.warn("[登录失败]  尝试登录失败，失败原因：{}", e.getMessage());
            return null;
        }
        // 确保authentication不为null
        if (authentication != null) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return userDetails.ToUser();
        }
        // 如果authentication为null或者未认证，这里可以返回一个null或者抛出一个异常
        return null;
    }

    public void logOut(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            // 清除SecurityContextHolder中的认证信息
            SecurityContextHolder.clearContext();

        }
    }

    @Override
    public User loadUserByUserId(int userId) {
        return userMapper.loadUserByUserId(userId);
    }

    @Override
    public void changeUserInfo(int userId, String userNickname, String userPassword, int userType, String userName) {
        userMapper.changeUserInfo(userId,userNickname,userPassword,userType,userName);
    }

}
