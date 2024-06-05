package nwpu.group20.warehouse.security;

import nwpu.group20.warehouse.entity.User;
import nwpu.group20.warehouse.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String userNickname) throws UsernameNotFoundException {
        // 从数据库中加载用户
        User user = userMapper.loadUserInfoByUserNickname(userNickname);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + userNickname);
        }

        // 创建并返回UserDetails对象，通常使用org.springframework.security.core.userdetails.User
        return new UserDetailsImpl (
                user.getUserNickname(), // 用户名
                user.getUserPassword(), // 密码（通常是加密后的）
                user.getUserType() // 用户的权限，你可能需要转换你的数据库模型到GrantedAuthority列表
        );
    }
}
