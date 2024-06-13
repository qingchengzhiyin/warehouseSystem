package nwpu.group20.warehouse.security;

import nwpu.group20.warehouse.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails {
    private int userId;
    private String userName;
    private String password;
    private User user;

    private final Collection<? extends GrantedAuthority> authorities;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public User ToUser(){
        User user =new User();
        user.setUserNickname(userName);
        user.setUserPassword(password);
        user.setUserId(userId);
        return user;
    }

    public UserDetailsImpl(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.password = user.getUserPassword();
        this.authorities = createAuthorities(user.getUserType()); // 根据userType创建权限
    }

    public UserDetailsImpl(int userId,String userName,String password,int authorities) {
        this.userId= userId;
        this.userName = userName;
        this.password = password;
        this.authorities = createAuthorities(authorities); // 根据userType创建权限
    }

    private Collection<? extends GrantedAuthority> createAuthorities(int userType) {
        // 这里只是一个示例，你需要根据实际的userType值来映射到对应的权限
        switch (userType) {
            case 0:
                return Arrays.asList(new SimpleGrantedAuthority("ROLE_MANAGER"));
            case 1:
                return Arrays.asList(new SimpleGrantedAuthority("ROLE_OPERATOR"));
            case 2:
                return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
            default:
                return Arrays.asList(new SimpleGrantedAuthority("ROLE_DEFAULT"));
        }
    }

}
