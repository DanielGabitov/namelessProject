package com.hse.DAOs;

import com.hse.models.User;
import com.hse.utils.ArraySQLValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDAO {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final RowMapper<User> userMapper;

    @Autowired
    public UserDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate, RowMapper<User> userMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.userMapper = userMapper;
    }

    public List<User> getUserById(Long userId) throws IllegalArgumentException {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", userId);
        return namedParameterJdbcTemplate.query("SELECT * FROM users WHERE id=:id", map, userMapper);
    }

    public List<User> getUserByUsername(String userName) throws IllegalArgumentException {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userName", userName);
        return namedParameterJdbcTemplate.query("SELECT * FROM users WHERE username=:userName", map, userMapper);
    }

    public long saveUser(User user) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userRole", user.getUserRole().name());
        map.addValue("name", user.getName());
        map.addValue("secondName", user.getSecondName());
        map.addValue("patronymic", user.getPatronymic());
        map.addValue("userName", user.getUsername());
        map.addValue("password", user.getPassword());
        map.addValue("specialization", user.getSpecialization().name());
        map.addValue("rating", user.getRating());
        map.addValue("description", user.getDescription());
        map.addValue("images", ArraySQLValue.create(user.getImages().toArray(), "varchar"));
        map.addValue("eventsId", ArraySQLValue.create(user.getEventsId().toArray(), "bigint"));

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(
            "INSERT INTO users (userRole, name, secondName, patronymic, username, password, specialization, " +
                "rating, description, images, eventsid) VALUES " +
                "(:userRole, :name, :secondName, :patronymic, :userName, :password, :specialization, :rating," +
                " :description, :images, :eventsId)", map, keyHolder
        );
        return (long) keyHolder.getKeyList().get(0).get("id");
    }

    public void updateImageHashes(long id, List<String> imageHashes){
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", id);
        map.addValue("newImages", ArraySQLValue.create(imageHashes.toArray(), "varchar"));
        namedParameterJdbcTemplate.update("UPDATE users set images = images || :newImages where id = :id", map);
    }
}