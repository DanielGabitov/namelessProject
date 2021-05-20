package com.hse.mappers;

import com.hse.enums.Specialization;
import com.hse.enums.UserRole;
import com.hse.models.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setUserRole(UserRole.valueOf(resultSet.getString("userRole")));
        user.setFirstName(resultSet.getString("name"));
        user.setLastName(resultSet.getString("secondName"));
        user.setPatronymic(resultSet.getString("patronymic"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setSpecialization(Specialization.valueOf(resultSet.getString("specialization")));
        user.setRating(resultSet.getDouble("rating"));
        user.setDescription(resultSet.getString("description"));
        return user;
    }
}
