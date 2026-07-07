package com.company.treasury.datasource.mybatis;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class MyBatisConfig {

    /**
     * MyBatis databaseIdProvider
     * 用于多数据库方言适配（Oracle / 达梦 DM8）
     */
    @Bean
    public DatabaseIdProvider databaseIdProvider() {
        VendorDatabaseIdProvider provider = new VendorDatabaseIdProvider();
        Properties props = new Properties();
        props.setProperty("Oracle", "oracle");
        props.setProperty("DM DBMS", "dm");
        provider.setProperties(props);
        return provider;
    }
}
