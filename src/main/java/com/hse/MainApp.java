package com.hse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class MainApp {
    public static void main(String[] args) {
        new SpringApplication(MainApp.class).run(args);
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//
//        dataSource.setDriverClassName("org.postgresql.Driver");
//        dataSource.setUrl("jdbc:postgresql://localhost:5432/test");
//        dataSource.setUsername("postgres");
//        dataSource.setPassword("password");
//
//        var template = new JdbcTemplate(dataSource);
//        var mapper   = new PostMapper();
//
//        PostsDAO dao = new PostsDAO(template);
//        dao.getPosts(mapper);
    }
}
