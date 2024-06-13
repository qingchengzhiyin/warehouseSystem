package nwpu.group20.warehouse.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import nwpu.group20.warehouse.util.CustomDateDeserializer;
import nwpu.group20.warehouse.util.CustomDateSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addSerializer(Date.class, new CustomDateSerializer());
        module.addDeserializer(Date.class, new CustomDateDeserializer());

        objectMapper.registerModule(module);
        return objectMapper;
    }
}
