package com.hse.DAOs;

import com.hse.enums.Specialization;
import com.hse.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@Component
public class UserDao {
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    private final RowMapper<User> userMapper;

    @Autowired
    public UserDao(NamedParameterJdbcTemplate namedJdbcTemplate, RowMapper<User> userMapper) {
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.userMapper = userMapper;
    }

    public Optional<User> getUserById(Long userId) throws IllegalArgumentException {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", userId);
        return namedJdbcTemplate.query("SELECT * FROM users WHERE id=:id", map, userMapper).stream().findAny();
    }

    public Optional<User> getUserByUsername(String username) throws IllegalArgumentException {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("username", username);
        return namedJdbcTemplate.query("SELECT * FROM users WHERE username=:username", map, userMapper).stream().findAny();
    }

    public Optional<User> getUserByUsernameAndPassword(String username, String password) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("username", username);
        map.addValue("password", password);
        return namedJdbcTemplate.query("SELECT * FROM users WHERE username=:username AND password=:password", map, userMapper).stream().findAny();
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

        namedJdbcTemplate.update(
                "INSERT INTO users (userRole, firstName, lastName, patronymic, username, password, specialization, " +
                        "rating, description) VALUES " +
                        "(:userRole, :firstName, :lastName, :patronymic, :userName, :password, :specialization, :rating," +
                        " :description)", map, keyHolder
        );
        return (long) keyHolder.getKeyList().get(0).get("id");
    }

    public List<User> getCreators(int offset, int size){
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("offset", offset);
        map.addValue("size", size);

        return namedJdbcTemplate.query("SELECT * FROM users WHERE userRole = 'CREATOR'" +
                        " OFFSET :offset ROWS FETCH FIRST :size ROWS ONLY;",
                map, userMapper);
    }

    public List<User> getCreators(int offset, int size, EnumSet<Specialization> specializations){
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("offset", offset);
        map.addValue("size", size);
        List<String> values = new ArrayList<>();
        for (var x : specializations){
            values.add(x.name());
        }
        map.addValue("specializations", values);

        return namedJdbcTemplate.query(
                "SELECT * FROM users WHERE userRole = 'CREATOR' AND " +
                    "specialization IN (:specializations) OFFSET :offset ROWS FETCH FIRST :size ROWS ONLY;",
                map, userMapper);
    }
}