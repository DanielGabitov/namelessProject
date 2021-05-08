package com.hse.DAOs;

import com.hse.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class UserDAO {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userMapper = new BeanPropertyRowMapper<>(User.class);

    @Autowired
    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User getUser(Long userId) throws IllegalArgumentException {
        return jdbcTemplate.query("SELECT * FROM users WHERE id=?", new Object[]{userId}, userMapper)
                .stream()
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Could not find user with given ID in database."));
    }

    public User getUserByLogin(String login) throws IllegalArgumentException {
        return jdbcTemplate.query("SELECT * FROM users WHERE login=?", new Object[]{login}, userMapper)
                .stream()
                .findAny()
                .orElse(null);
    }

    public void saveUser(User user) {
        jdbcTemplate.update("INSERT INTO users (name, login, password, rating) VALUES (?, ?, ?, ?)", user.getName(), user.getLogin(),
                user.getPassword(), user.getRating());
    }
}
