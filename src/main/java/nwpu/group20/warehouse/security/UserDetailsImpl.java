package nwpu.group20.warehouse.security;

import nwpu.group20.warehouse.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails {
    private String userName;
    private String password;
    private User user;

    private final Collection<? extends GrantedAuthority> authorities;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
    public User ToUser(){
        User user =new User();
        user.setUserNickname(userName);
        user.setUserPassword(password);
        return user;
    }

    public UserDetailsImpl(User user) {
        this.userName = user.getUserName(); // 假设这是用于登录的用户名
        this.password = user.getUserPassword();
        this.authorities = createAuthorities(user.getUserType()); // 根据userType创建权限
    }

    public UserDetailsImpl(String userName,String password,int authorities) {
        this.userName = userName; // 假设这是用于登录的用户名
        this.password = password;
        this.authorities = createAuthorities(authorities); // 根据userType创建权限
    }

    private Collection<? extends GrantedAuthority> createAuthorities(int userType) {
        // 这里只是一个示例，你需要根据实际的userType值来映射到对应的权限
        switch (userType) {
            case 1:
                return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
            case 2:
                return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
            // ... 添加其他类型的映射
            default:
                return Arrays.asList(new SimpleGrantedAuthority("ROLE_DEFAULT"));
        }
    }

}
