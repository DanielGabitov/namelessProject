package com.hse.mappers;

import com.hse.models.CreativeAssociation;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CreativeAssociationMapper implements RowMapper<CreativeAssociation> {
    @Override
    public CreativeAssociation mapRow(ResultSet resultSet, int i) throws SQLException {
        CreativeAssociation association = new CreativeAssociation();

        association.setId(resultSet.getLong("id"));
        association.setName(resultSet.getString("name"));
        association.setDescription(resultSet.getString("description"));
        association.setBossCreator(resultSet.getLong("bossCreator"));

        return association;
    }
}
