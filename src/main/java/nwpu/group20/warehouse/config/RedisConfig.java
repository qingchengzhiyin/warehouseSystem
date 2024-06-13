package nwpu.group20.warehouse.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
@Configuration
public class RedisConfig {

        @Value("${spring.redis.host}")
        private String redisHost;

        @Value("${spring.redis.port}")
        private int redisPort;

        @Value("${spring.redis.password:}")  // 默认值为空字符串
        private String redisPassword;

        @Bean
        public Jedis jedis() {
            Jedis jedis = new Jedis(redisHost, redisPort);
            if (redisPassword != null && !redisPassword.isEmpty()) {
                jedis.auth(redisPassword);
            }
            return jedis;
        }
    }

