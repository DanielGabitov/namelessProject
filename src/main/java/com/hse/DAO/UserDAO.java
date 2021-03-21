package com.hse.DAO;

import com.hse.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserDAO {

    private final JdbcTemplate jdbcTemplate;

    private final UserMapper userMapper;

    @Autowired
    public UserDAO(JdbcTemplate jdbcTemplate, UserMapper userMapper){
        this.jdbcTemplate = jdbcTemplate;
        this.userMapper = userMapper;
    }

    public User getUser(int userId) throws IllegalArgumentException{
        return jdbcTemplate.query("SELECT * FROM users WHERE id=?", new Object[]{userId}, userMapper)
                .stream()
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Could not find user with given ID in database."));
    }
}