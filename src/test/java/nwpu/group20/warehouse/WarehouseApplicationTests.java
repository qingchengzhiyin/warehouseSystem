package nwpu.group20.warehouse;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

@SpringBootTest
class WarehouseApplicationTests {
    private Jedis jedis;
    @Test
    void contextLoads() {
        jedis = new Jedis("localhost",6379);
        jedis.set("testjedis","1");
        jedis.close();
    }

}
