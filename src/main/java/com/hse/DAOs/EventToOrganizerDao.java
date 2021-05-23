package com.hse.DAOs;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventToOrganizerDao {
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public EventToOrganizerDao(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    public void addOrganizer(long eventId, long userId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("eventId", eventId);
        map.addValue("userId", userId);

        namedJdbcTemplate.update("INSERT INTO events_organizers (eventId, userId) VALUES (:eventId, :userId)", map);
    }

    public List<Long> getOrganizers(long eventId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("eventId", eventId);

        return namedJdbcTemplate.query("SELECT * from events_organizers WHERE eventId = :eventId", map,
                (resultSet, i) -> resultSet.getLong("userId"));
    }
}
