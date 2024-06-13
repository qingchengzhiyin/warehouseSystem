package nwpu.group20.warehouse.security;

import nwpu.group20.warehouse.config.CorsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/**", "/home").permitAll() // 允许访问的URL
                .anyRequest().authenticated() // 其他所有请求都需要认证
                .and()
                .formLogin() // 启用表单登录
                .loginPage("/login") // 自定义登录页面
                .permitAll() // 允许访问登录页面
                .and()
                .logout() // 启用登出功能
                .permitAll() // 允许访问登出页面
                .and()
                .rememberMe()
                .key("2021302930") // 设置 Remember Me 的 key，需要足够安全
                .tokenValiditySeconds(86400) // 设置 Remember Me 的 cookie 有效期，这里为 1 天
                .rememberMeServices(rememberMeServices()) // 配置 Remember Me 服务
                .and()
                .sessionManagement() // 会话管理
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 会话创建策略
                .and()
                .csrf().disable(); // 禁用CSRF保护（仅用于测试或确定不需要CSRF的场景）

        http.addFilterBefore(new CorsFilter(corsConfigurationSource), ChannelProcessingFilter.class); // 添加 CorsFilter
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public RememberMeServices rememberMeServices() {
        TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices("2021302930", userDetailsService);
        rememberMeServices.setTokenValiditySeconds(86400); // 设置 Remember Me 的 token 有效期，这里为 1 天
        return rememberMeServices;
    }

}
