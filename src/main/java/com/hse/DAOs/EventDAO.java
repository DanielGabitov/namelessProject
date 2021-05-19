package com.hse.DAOs;

import com.hse.models.Event;
import com.hse.utils.ArraySQLValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventDAO {
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    private final RowMapper<Event> eventMapper;

    @Autowired
    public EventDAO(NamedParameterJdbcTemplate namedJdbcTemplate, RowMapper<Event> eventMapper) {
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.eventMapper = eventMapper;
    }


    public List<Event> getEvent(long id) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", id);
        return namedJdbcTemplate.query("SELECT * FROM events WHERE id= :id", map, eventMapper);
    }

    public long saveEvent(Event event) {
        MapSqlParameterSource map = new MapSqlParameterSource();

        map.addValue("name", event.getName());
        map.addValue("description", event.getDescription());
        map.addValue("images", ArraySQLValue.create(event.getImages().toArray(), "varchar"));

        map.addValue("organizerIDs",
                ArraySQLValue.create(event.getOrganizerIDs().toArray(), "bigint"));

        map.addValue("participantsIDs",
                ArraySQLValue.create(event.getParticipantsIDs().toArray(), "bigint"));

        map.addValue("rating", event.getRating());
        map.addValue("geoData", event.getGeoData());
        map.addValue("specialization", event.getSpecialization().name());
        map.addValue("date", event.getDate());

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        namedJdbcTemplate.update(
        "INSERT INTO events" +
                "(name, description, images, organizerIDs, participantsIDs," +
                " rating, geoData, specialization, date)" +
                " VALUES (:name, :description , :images, :organizerIDs, :participantsIDs," +
                " :rating, :geoData, :specialization, :date)", map, keyHolder
        );
        return (long) keyHolder.getKeyList().get(0).get("id");
    }

    public void updateImageHashes(long id, List<String> imageUUIDs){
        MapSqlParameterSource map = new MapSqlParameterSource();

        map.addValue("id", id);
        map.addValue("newImages", ArraySQLValue.create(imageUUIDs.toArray(), "varchar"));

        namedJdbcTemplate.update("UPDATE events set images = images || :newImages where id = :id", map);
    }
}
