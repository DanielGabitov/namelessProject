package com.hse.configuration;

import com.hse.enums.UserRole;
import com.hse.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http = http.csrf().disable();

        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/registration", "/api/authentication").permitAll()
                .antMatchers("/api/users/**").hasAnyRole(UserRole.USER.name(), UserRole.CREATOR.name(), UserRole.ORGANIZER.name())
                .antMatchers("/api/events/**").hasAnyRole(UserRole.USER.name(), UserRole.CREATOR.name(), UserRole.ORGANIZER.name())
                .antMatchers("/api/images/**").hasAnyRole(UserRole.USER.name(), UserRole.CREATOR.name(), UserRole.ORGANIZER.name())
                .antMatchers("/api/feed/**").hasAnyRole(UserRole.USER.name(), UserRole.CREATOR.name(), UserRole.ORGANIZER.name())
                .antMatchers("/api/subscriptions/**").hasAnyRole(UserRole.USER.name(), UserRole.CREATOR.name(), UserRole.ORGANIZER.name())
                .antMatchers("/api/organizers/**").hasAnyRole(UserRole.ORGANIZER.name())
                .antMatchers("/api/creators/**").hasAnyRole(UserRole.CREATOR.name())
                .anyRequest().authenticated();

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}