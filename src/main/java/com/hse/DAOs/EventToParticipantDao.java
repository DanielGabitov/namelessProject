package com.hse.DAOs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventToParticipantDao {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    public EventToParticipantDao(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    public void addParticipant(long eventId, long userId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("eventId", eventId);
        map.addValue("userId", userId);

        namedJdbcTemplate.update("INSERT INTO events_participants (eventId, userId) VALUES (:eventId, :userId)", map);
    }

    public List<Long> getParticipants(long eventId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("eventId", eventId);

        return namedJdbcTemplate.query("SELECT * from events_participants WHERE eventId = :eventId", map,
                (resultSet, i) -> resultSet.getLong("userId"));
    }
}
