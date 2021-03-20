package com.hse.DAO;

import com.hse.model.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();

        user.setId(resultSet.getInt("id"));
        user.setUserType(User.UserTypeEnum.fromValue(resultSet.getString("usertype")));
        user.setUserName(resultSet.getString("username"));

        return user;
    }
}
