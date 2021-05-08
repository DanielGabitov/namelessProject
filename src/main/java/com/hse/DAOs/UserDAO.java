package com.hse.DAOs;

import com.hse.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDAO {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userMapper = new BeanPropertyRowMapper<>(User.class);

    @Autowired
    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User getUserById(Long userId) throws IllegalArgumentException {
        return jdbcTemplate.query("SELECT * FROM users WHERE id=?", new Object[]{userId}, userMapper)
                .stream()
                .findAny()
                .orElse(null);
    }

    public User getUserByUsername(String login) throws IllegalArgumentException {
        return jdbcTemplate.query("SELECT * FROM users WHERE login=?", new Object[]{login}, userMapper)
                .stream()
                .findAny()
                .orElse(null);
    }

    public void saveUser(User user) { //todo matching names with eventDAO
        jdbcTemplate.update(
                "INSERT INTO users (name, login, password, rating) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                user.getUserRole(), user.getName(), user.getSecondName(), user.getPatronymic(), user.getUsername(),
                user.getPassword(), user.getSpecialization(), user.getRating(), user.getDescription(), user.getPhotos(),
                user.getEventsId()
        );
    }

    public void updatePhotosHashes(long id, List<String> photosHashes){
        jdbcTemplate.update("UPDATE users SET photos = ? where id = ?", new Object[]{photosHashes, id}, userMapper);
    }
}