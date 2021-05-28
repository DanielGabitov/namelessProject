package com.hse.DAOs;

import com.hse.models.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EventDao {
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    private final RowMapper<Event> eventMapper;

    @Autowired
    public EventDao(NamedParameterJdbcTemplate namedJdbcTemplate, RowMapper<Event> eventMapper) {
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.eventMapper = eventMapper;
    }


    public Optional<Event> getEvent(long id) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", id);
        return namedJdbcTemplate.query("SELECT * FROM events WHERE id= :id", map, eventMapper).stream().findAny();
    }


    public long createEvent(Event event) {
        MapSqlParameterSource map = new MapSqlParameterSource();

        map.addValue("name", event.getName());
        map.addValue("description", event.getDescription());
        map.addValue("rating", event.getRating());
        map.addValue("geoData", event.getGeoData());
        map.addValue("specialization", event.getSpecialization().name());
        map.addValue("date", event.getDate());

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        namedJdbcTemplate.update(
                "INSERT INTO events" +
                        "(name, description, rating, geoData, specialization, date)" +
                        " VALUES (:name, :description , :rating, :geoData, :specialization, :date)", map, keyHolder
        );
        return (long) keyHolder.getKeyList().get(0).get("id");
    }

    public List<Event> getEvents(int offset, int size) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("offset", offset);
        map.addValue("size", size);

        return namedJdbcTemplate.query("SELECT * FROM events OFFSET :offset ROWS FETCH FIRST :size ROWS ONLY",
                map, eventMapper);
    }
}
