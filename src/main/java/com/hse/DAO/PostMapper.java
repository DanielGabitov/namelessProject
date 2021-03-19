package com.hse.DAO;

import com.hse.model.Post;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PostMapper implements RowMapper<Post> {
    @Override
    public Post mapRow(ResultSet resultSet, int i) throws SQLException {
        Post post = new Post();

        post.setId(resultSet.getInt("id"));
        post.setUserId(resultSet.getInt("userId"));
        post.setDescription(resultSet.getString("description"));

        return post;
    }
}
