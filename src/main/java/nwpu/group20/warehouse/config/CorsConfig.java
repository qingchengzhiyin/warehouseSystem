package nwpu.group20.warehouse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 使用 allowedOriginPatterns 代替 allowedOrigins
        configuration.setAllowedOriginPatterns(Arrays.asList("*")); // 支持复杂匹配规则
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 允许的请求方法
        configuration.setAllowedHeaders(Arrays.asList("*")); // 允许的请求头
        configuration.setAllowCredentials(true); // 允许发送身份验证信息（Cookie）
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 对所有路径应用配置
        return source;
    }
}
