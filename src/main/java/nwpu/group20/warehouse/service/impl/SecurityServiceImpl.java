package nwpu.group20.warehouse.service.impl;

import nwpu.group20.warehouse.entity.User;
import nwpu.group20.warehouse.mapper.UserMapper;
import nwpu.group20.warehouse.security.UserDetailsImpl;
import nwpu.group20.warehouse.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements UserDetailsService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private JWTUtil jwtUtil;

    public UserDetails loadUserByUsername(String userNickname) throws UsernameNotFoundException {
        User user = userMapper.loadUserInfoByUserNickname(userNickname);
        if(user == null){
            throw new UsernameNotFoundException("用户名为" + userNickname + "的账户不存在");
        }
        return new UserDetailsImpl(user);

    }
}
