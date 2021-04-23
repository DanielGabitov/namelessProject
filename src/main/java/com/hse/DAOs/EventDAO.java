package com.hse.DAOs;

import com.hse.models.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class EventDAO {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Event> eventMapper = new BeanPropertyRowMapper<>(Event.class);

    @Autowired
    public EventDAO(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public Event getEvent(int eventId) throws IllegalArgumentException{
        return jdbcTemplate.query("SELECT * FROM events WHERE id=?", new Object[]{eventId}, eventMapper)
                .stream()
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Could not find event with given ID in database."));
    }

    public void saveEvent(Event event){
        jdbcTemplate.update("INSERT INTO events (name, description, date, organizerid) VALUES (?, ?, ?, ?)",
                event.getName(), event.getDescription(), event.getDate(), event.getOrganizerId());
    }
}
