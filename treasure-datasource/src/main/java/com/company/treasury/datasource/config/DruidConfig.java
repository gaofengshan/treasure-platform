package com.company.treasury.datasource.config;

import com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(DruidDataSourceAutoConfigure.class)
public class DruidConfig {
}
