package com.company.treasury.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.company.treasury")
public class TreasureApplication {
    public static void main(String[] args) {
        SpringApplication.run(TreasureApplication.class, args);
    }
}
