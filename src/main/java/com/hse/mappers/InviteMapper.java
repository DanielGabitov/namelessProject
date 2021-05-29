package com.hse.mappers;

import com.hse.models.Invitation;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class InviteMapper implements RowMapper<Invitation> {
    @Override
    public Invitation mapRow(ResultSet resultSet, int i) throws SQLException {
        Invitation invitation = new Invitation();
        invitation.setCreatorId(resultSet.getLong("creatorId"));
        invitation.setEventId(resultSet.getLong("eventId"));
        invitation.setOrganizerId(resultSet.getLong("organizerId"));
        invitation.setMessage(resultSet.getString("message"));
        invitation.setAccepted(resultSet.getBoolean("accepted"));
        return invitation;
    }
}
