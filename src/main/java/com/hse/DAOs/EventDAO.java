package com.hse.DAOs;

import com.hse.models.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventDAO {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Event> eventMapper = new BeanPropertyRowMapper<>(Event.class);

    @Autowired
    public EventDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Event> getEvent(long id) {
        return jdbcTemplate.query("SELECT * FROM events WHERE id= ?", new Object[]{id}, eventMapper);
    }

    public void saveEvent(Event event) {
        jdbcTemplate.update(
                "INSERT INTO events" +
                        "(name," +
                        " description," +
                        " imageHashes," +
                        " organizerIDs," +
                        " participantsIDs," +
                        " rating," +
                        " geoData," +
                        " specialization," +
                        " date" +
                        ")" +
                        " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                event.getName(),
                event.getDescription(),
                event.getImageHashes(),
                event.getOrganizerIDs(),
                event.getParticipantsIDs(),
                event.getRating(),
                event.getGeoData(),
                event.getSpecialization(),
                event.getDate());
    }

    public void updateImageHashes(long id, List<String> imageHashes){
        jdbcTemplate.update("UPDATE events SET imagehashes = ? where id = ?", new Object[]{imageHashes, id}, eventMapper);
    }
}
