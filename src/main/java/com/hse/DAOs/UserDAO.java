package com.hse.DAOs;

import com.hse.models.User;
import com.hse.utils.ArraySQLValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDAO {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userMapper;

    @Autowired
    public UserDAO(JdbcTemplate jdbcTemplate, RowMapper<User> userMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userMapper = userMapper;
    }

    public User getUserById(Long userId) throws IllegalArgumentException {
        return jdbcTemplate.query("SELECT * FROM users WHERE id=?", new Object[]{userId}, userMapper)
                .stream()
                .findAny()
                .orElse(null);
    }

    public User getUserByUsername(String username) throws IllegalArgumentException {
        return jdbcTemplate.query("SELECT * FROM users WHERE username=?", new Object[]{username}, userMapper)
                .stream()
                .findAny()
                .orElse(null);
    }

    public void saveUser(User user) { //todo matching names with eventDAO
        jdbcTemplate.update(
                "INSERT INTO users (userRole, name, secondName, patronymic, username, password, specialization, " +
                        "rating, description, images, eventsid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                user.getUserRole().name(), user.getName(), user.getSecondName(), user.getPatronymic(),
                user.getUsername(), user.getPassword(), user.getSpecialization().name(), user.getRating(),
                user.getDescription(),
                ArraySQLValue.create(user.getImages().toArray(), "varchar"),
                ArraySQLValue.create(user.getEventsId().toArray(), "bigint")
        );
    }

    public void updateImageHashes(long id, List<String> imagesHashes){
        jdbcTemplate.update("UPDATE users SET images = ? where id = ?", new Object[]{imagesHashes, id}, userMapper);
    }
}