package com.hse.mappers;

import com.hse.enums.Specialization;
import com.hse.models.Event;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EventMapper implements RowMapper<Event> {
    @Override
    public Event mapRow(ResultSet resultSet, int i) throws SQLException {
        Event event = new Event();
        event.setId(resultSet.getLong("id"));
        event.setName(resultSet.getString("name"));
        event.setDescription(resultSet.getString("description"));
        event.setOrganizerId(resultSet.getLong("organizerId"));
        event.setGeoData(resultSet.getString("geoData"));
        event.setSpecialization(Specialization.valueOf(resultSet.getString("specialization")));
        event.setDate(resultSet.getTimestamp("date"));
        return event;
    }
}
