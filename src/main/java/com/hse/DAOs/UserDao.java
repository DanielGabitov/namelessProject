package com.hse.DAOs;

import com.hse.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final RowMapper<User> userMapper;

    @Autowired
    public UserDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate, RowMapper<User> userMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.userMapper = userMapper;
    }

    public Optional<User> getUserById(Long userId) throws IllegalArgumentException {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", userId);
        return namedParameterJdbcTemplate.query("SELECT * FROM users WHERE id=:id", map, userMapper).stream().findAny();
    }

    public Optional<User> getUserByUsername(String userName) throws IllegalArgumentException {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userName", userName);
        return namedParameterJdbcTemplate.query("SELECT * FROM users WHERE username=:userName", map, userMapper).stream().findAny();
    }

    public long saveUser(User user) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userRole", user.getUserRole().name());
        map.addValue("firstName", user.getFirstName());
        map.addValue("lastName", user.getLastName());
        map.addValue("patronymic", user.getPatronymic());
        map.addValue("userName", user.getUsername());
        map.addValue("password", user.getPassword());
        map.addValue("specialization", user.getSpecialization().name());
        map.addValue("rating", user.getRating());
        map.addValue("description", user.getDescription());
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(
                "INSERT INTO users (userRole, firstName, lastName, patronymic, username, password, specialization, " +
                        "rating, description) VALUES " +
                        "(:userRole, :firstName, :lastName, :patronymic, :userName, :password, :specialization, :rating," +
                        " :description)", map, keyHolder
        );
        return (long) keyHolder.getKeyList().get(0).get("id");
    }
}