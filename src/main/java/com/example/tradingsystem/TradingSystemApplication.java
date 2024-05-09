package com.example.tradingsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.tradingsystem.mapper")
public class TradingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradingSystemApplication.class, args);
    }

}
