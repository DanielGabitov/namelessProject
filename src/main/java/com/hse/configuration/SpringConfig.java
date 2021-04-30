package com.hse.configuration;

import com.hse.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;


@Configuration
public class SpringConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://database:5432/postgres");
        dataSource.setUsername("HSE");
        dataSource.setPassword("password");

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @EnableWebSecurity
    @Configuration
    public static class SecurityConfig extends WebSecurityConfigurerAdapter {

        private final JwtFilter jwtFilter;

        @Autowired
        public SecurityConfig(JwtFilter jwtFilter) {
            this.jwtFilter = jwtFilter;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/api/registration", "/api/authentication").permitAll()
                    .antMatchers("/**").authenticated();
            http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        }
    }
}
