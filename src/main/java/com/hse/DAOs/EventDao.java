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

import java.sql.Timestamp;
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

    public Integer getNumberOfEventsById(long eventId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", eventId);
        return namedJdbcTemplate.queryForObject("SELECT count(id) FROM events WHERE id = :id", map, Integer.class);
    }

    public Optional<Event> getEvent(long id) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", id);
        return namedJdbcTemplate.query("SELECT * FROM events WHERE id= :id", map, eventMapper).stream().findAny();
    }

    public List<Long> getAllEventIds() {
        return namedJdbcTemplate.query(
                "SELECT * from events",
                new MapSqlParameterSource(),
                (resultSet, i) -> resultSet.getLong("id"));
    }


    public long createEvent(Event event) {
        MapSqlParameterSource map = new MapSqlParameterSource();

        mapEventParameters(event, map);

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        namedJdbcTemplate.update(
                "INSERT INTO events" +
                        "(name, description, organizerid, rating, geoData, specialization, date)" +
                        " VALUES (:name, :description, :organizerId, :rating, :geoData, :specialization, :date)",
                map, keyHolder
        );
        return (long) keyHolder.getKeyList().get(0).get("id");
    }

    private void mapEventParameters(Event event, MapSqlParameterSource map) {
        map.addValue("name", event.getName());
        map.addValue("description", event.getDescription());
        map.addValue("organizerId", event.getOrganizerId());
        map.addValue("rating", event.getRating());
        map.addValue("geoData", event.getGeoData());
        map.addValue("specialization", event.getSpecialization().name());
        map.addValue("date", event.getDate());
    }


    public void updateEvent(long eventId, Event event) {
        MapSqlParameterSource map = new MapSqlParameterSource();

        map.addValue("id", eventId);
        mapEventParameters(event, map);

        namedJdbcTemplate.update(
                "UPDATE events SET name = :name, description = :description, organizerid = :organizerId, " +
                        "rating = :rating, geodata = :geoData, specialization = :specialization, date = :date " +
                        "WHERE id = :id", map);
    }

    public List<Event> getAllFutureEvents(List<Long> eventIds, Timestamp time) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("events", eventIds);
        map.addValue("time", time);

        return namedJdbcTemplate.query("SELECT * from events WHERE id IN (:events) AND date > :time",
                map,
                eventMapper);
    }

    public List<Event> getAllPassedEvents(List<Long> eventIds, Timestamp time) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("events", eventIds);
        map.addValue("time", time);

        return namedJdbcTemplate.query("SELECT * from events WHERE id IN (:events) AND date < :time",
                map,
                eventMapper);
    }

    public List<Event> getAllPassedOrganizerEvents(long organizerId, Timestamp time) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("organizerId", organizerId);
        map.addValue("time", time);

        return namedJdbcTemplate.query(
                "SELECT * FROM events WHERE organizerId = :organizerId AND date < :time", map, eventMapper);
    }

    public List<Event> getAllFutureOrganizerEvents(long organizerId, Timestamp time) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("organizerId", organizerId);
        map.addValue("time", time);

        return namedJdbcTemplate.query(
                "SELECT * FROM events WHERE organizerId = :organizerId AND date > :time", map, eventMapper);
    }

    public List<Event> getEvents(int offset, int size) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("offset", offset);
        map.addValue("size", size);

        return namedJdbcTemplate.query("SELECT * FROM events OFFSET :offset ROWS FETCH FIRST :size ROWS ONLY",
                map, eventMapper);
    }

    public List<Event> getRecommendedEvents(int offset, int size, long userId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("offset", offset);
        map.addValue("size", size);
        map.addValue("userId", userId);

        return namedJdbcTemplate.query(
                "SELECT * FROM recommendations JOIN events " +
                        "ON recommendations.userid = :userId AND recommendations.eventid = events.id " +
                        "WHERE events.id NOT IN (SELECT eventid FROM likes WHERE likes.userid = :userId) " +
                        "ORDER BY coefficient DESC OFFSET :offset ROWS FETCH FIRST :size ROWS ONLY",
                map, eventMapper);
    }


    public List<Event> getEvents(int offset, int size, EnumSet<Specialization> specializations) {
        MapSqlParameterSource map = mapOffsetAndSize(offset, size, specializations);

        return namedJdbcTemplate.query(
                "SELECT * FROM events WHERE specialization IN (:specializations) " +
                        "OFFSET :offset ROWS FETCH FIRST :size ROWS ONLY;",
                map, eventMapper);
    }

    static MapSqlParameterSource mapOffsetAndSize(int offset, int size, EnumSet<Specialization> specializations) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("offset", offset);
        map.addValue("size", size);
        List<String> values = specializations.stream().map(Specialization::name).collect(Collectors.toList());
        map.addValue("specializations", values);
        return map;
    }

    public List<Event> getRecommendedEvents(int offset, int size, long userId, EnumSet<Specialization> specializations) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("offset", offset);
        map.addValue("size", size);
        map.addValue("userId", userId);
        List<String> values = specializations.stream().map(Specialization::name).collect(Collectors.toList());
        map.addValue("specializations", values);

        return namedJdbcTemplate.query(
                "SELECT * FROM recommendations JOIN events " +
                        "ON recommendations.userid = :userId AND recommendations.eventid = events.id " +
                        "WHERE events.id NOT IN (SELECT eventid FROM likes WHERE likes.userid = :userId) " +
                        "AND specialization IN (:specializations)" +
                        "ORDER BY coefficient DESC OFFSET :offset ROWS FETCH FIRST :size ROWS ONLY",
                map, eventMapper);
    }

    public List<Long> getOrganizerEvents(long organizerId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("organizerId", organizerId);
        return namedJdbcTemplate.query("SELECT * FROM events WHERE organizerid = :organizerId",
                map,
                (resultSet, i) -> resultSet.getLong("id"));
    }

    public List<Long> getCreatorApplicationEvents(long creatorId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("creatorId", creatorId);

        return namedJdbcTemplate.query("SELECT * from event_applications WHERE creatorid = :creatorId",
                map,
                (resultSet, i) -> resultSet.getLong("eventId"));
    }

    public List<Application> getEventApplications(long eventId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("eventId", eventId);
        return namedJdbcTemplate.query(
                "SELECT * from creators_invites WHERE eventid = :eventId",
                map, applicationMapper);
    }

    public void addParticipant(long eventId, long participantId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("eventId", eventId);
        map.addValue("participantId", participantId);
        namedJdbcTemplate.update(
                "INSERT INTO events_participants (eventid, participantId) " +
                        "VALUES (:eventId, :participantId)", map);
    }

    public void deleteParticipant(long eventId, long participantId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("eventId", eventId);
        map.addValue("participantId", participantId);
        namedJdbcTemplate.update(
                "DELETE FROM events_participants WHERE eventid = :eventId AND participantid = :participantId", map);
    }

    public Integer getNumberOfParticipantsOfEventByParticipantId(long eventId, long participantId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("eventId", eventId);
        map.addValue("participantId", participantId);
        return namedJdbcTemplate.queryForObject(
                "SELECT count(participantid) FROM events_participants " +
                        "WHERE eventid = :eventId AND participantid = :participantId", map, Integer.class);
    }

    public List<Event> searchEvents(String eventName, int offset, int size) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("eventName", eventName);
        map.addValue("offset", offset);
        map.addValue("size", size);
        return namedJdbcTemplate.query("SELECT *, similarity(name, :eventName) AS sml FROM events " +
                "ORDER BY sml DESC OFFSET :offset ROWS FETCH FIRST :size ROWS ONLY", map, eventMapper);
    }

    public void addViewedEvent(long userId, long eventId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);
        map.addValue("eventId", eventId);

        int numberOfRowsWithSuchIDs = namedJdbcTemplate.query(
                "SELECT eventid FROM user_viewed_events WHERE userid = :userId",
                map,
                (resultSet, i) -> resultSet.getLong("eventId")
        ).size();

        if (numberOfRowsWithSuchIDs == 1) {
            return;
        }

        namedJdbcTemplate.update(
                "INSERT INTO user_viewed_events (userid, eventid) VALUES (:userId, :eventId)",
                map
        );
    }

    public List<Long> getUserViewedEvents(long userId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);

        return namedJdbcTemplate.query(
                "SELECT eventid FROM user_viewed_events WHERE userid = :userId",
                map,
                (resultSet, i) -> resultSet.getLong("eventId")
        );
    }
}
