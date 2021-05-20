package com.hse.DAOs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LikesDAO {
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    public LikesDAO(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    public void addLike(long userId, long eventId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);
        map.addValue("eventId", eventId);

        namedJdbcTemplate.update("INSERT INTO likes (userId, eventid) VALUES (:userId, :eventId)", map);
    }

    public void deleteLike(long userId, long eventId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);
        map.addValue("eventId", eventId);

        namedJdbcTemplate.update("DELETE FROM likes WHERE eventId = :eventId and userId = :usedId", map);
    }

    public List<Long> getEventLikes(long eventId) {

        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("eventId", eventId);

        return namedJdbcTemplate.query("SELECT * from likes WHERE eventId = :eventId", map,
                resultSet -> {
                    List<Long> users = new ArrayList<>();
                    while (resultSet.next()) {
                        users.add(resultSet.getLong("userId"));
                    }
                    return users;
                });
    }

    public List<Long> getUserLikes(long userId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);

        return namedJdbcTemplate.query("SELECT * from likes WHERE userId = :userId", map,
                resultSet -> {
                    List<Long> events = new ArrayList<>();
                    while (resultSet.next()) {
                        events.add(resultSet.getLong("userId"));
                    }
                    return events;
                });
    }
}
