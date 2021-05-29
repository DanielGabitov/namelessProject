package com.hse.DAOs;

import com.hse.enums.Specialization;
import com.hse.mappers.ApplicationMapper;
import com.hse.models.Application;
import com.hse.models.Event;
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
import java.util.stream.Collectors;

@Component
public class EventDao {
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    private final RowMapper<Event> eventMapper;
    private final ApplicationMapper applicationMapper;

    @Autowired
    public EventDao(NamedParameterJdbcTemplate namedJdbcTemplate, RowMapper<Event> eventMapper,
                    ApplicationMapper applicationMapper) {
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.eventMapper = eventMapper;
        this.applicationMapper = applicationMapper;
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

    public List<Event> getEvents(int offset, int size, EnumSet<Specialization> specializations) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("offset", offset);
        map.addValue("size", size);
        List<String> values = specializations.stream().map(Specialization::name).collect(Collectors.toList());;
        map.addValue("specializations", values);

        return namedJdbcTemplate.query(
                "SELECT * FROM events WHERE specialization IN (:specializations) " +
                        "OFFSET :offset ROWS FETCH FIRST :size ROWS ONLY;",
                map, eventMapper);
    }

    public List<Long> getOrganizerEvents(long organizerId){
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("organizerId", organizerId);
        return namedJdbcTemplate.query("SELECT * FROM events WHERE organizerid = :organizerId",
                map,
                (resultSet, i) -> resultSet.getLong("id"));
    }

    public List<Application> getEventApplications(long eventId){
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("eventId", eventId);
        return namedJdbcTemplate.query(
                "SELECT * from creators_invites WHERE eventid = :eventId",
                map, applicationMapper);
    }
  
    public void addParticipant(long eventId, long participantId){
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("eventId", eventId);
        map.addValue("participantId", participantId);
        namedJdbcTemplate.update(
                "INSERT INTO events_participants (eventid, participantId) " +
                    "VALUES (:eventId, :participantId)", map);
    }

    public void deleteParticipant(long eventId, long participantId){
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("eventId", eventId);
        map.addValue("participantId", participantId);
        namedJdbcTemplate.update(
                "DELETE FROM events_participants WHERE eventid = :eventId AND participantid = :participantId", map);
    }

    public boolean checkParticipant(long eventId, long participantId){
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("eventId", eventId);
        map.addValue("participantId", participantId);
        Integer count = namedJdbcTemplate.queryForObject(
                "SELECT count(participantid) FROM events_participants " +
                    "WHERE eventid = :eventId AND participantid = :participantId", map, Integer.class);
        return count != null && count > 0;
    }
}
