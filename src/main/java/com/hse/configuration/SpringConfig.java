package com.hse.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;


@Configuration
public class SpringConfig {

    @Value("${spring.datasource.name}")
    private String dataBaseName;

    @Value("${spring.datasource.password}")
    private String dataBasePassword;

    @Value("${spring.datasource.url}")
    private String dataBaseURL;

    @Value("${spring.datasource.driver-class-name}")
    private String dataBaseDriverClassName;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(dataBaseDriverClassName);
        dataSource.setUrl(dataBaseURL);
        dataSource.setUsername(dataBaseName);
        dataSource.setPassword(dataBasePassword);
        return dataSource;
    }

    @Bean
    public NamedParameterJdbcTemplate NamedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource());
    }
}
