package io.github.artemnefedov;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class StoreServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreServiceApplication.class, args);
    }


    @Bean
    public ObjectMapper objectMapper(JavaTimeModule module) {

        var objMapper = new ObjectMapper();
        objMapper.registerModule(module);
        return objMapper;
    }

    @Bean
    public JavaTimeModule dateTimeModule(){
        return new JavaTimeModule();
    }
}
