package com.sicredi.back.votos.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConfigurationProperties("spring.datasource")
@Getter
@Setter
@Slf4j
public class DBConfig {

    private String driverClassName;
    private String url;
    private String username;
    private String password;

    @Profile("dev")
    @Bean
    public String testDatabaseConnection() {
        log.info("DB connection for DEV - H2");
        log.info(driverClassName);
        log.info(url);
        return "DB Connection to MYSQL_TEST - Test instance";
    }

    @Profile("prod")
    @Bean
    public String prodDatabaseConnection() {
        log.info("DB connection for MYSQL Database: production");
        log.info(driverClassName);
        log.info(url);
        return "DB Connection to MySql production - Test instance";
    }}
