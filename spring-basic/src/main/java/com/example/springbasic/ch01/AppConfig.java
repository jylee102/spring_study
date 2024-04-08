package com.example.springbasic.ch01;


import com.example.springbasic.service.MyService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public MyBean myBean() {
        return new MyBean();
    }

    @Bean
    public MyService myService() {
        return new MyService();
    }
}
