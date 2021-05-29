package com.hse.mappers;

import com.hse.models.Application;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ApplicationMapper implements RowMapper<Application> {
    @Override
    public Application mapRow(ResultSet resultSet, int i) throws SQLException {
        Application application = new Application();
        application.setCreatorId(resultSet.getLong("creatorId"));
        application.setEventId(resultSet.getLong("eventId"));
        application.setMessage(resultSet.getString("message"));
        application.setAccepted(resultSet.getBoolean("accepted"));
        return application;
    }
}
