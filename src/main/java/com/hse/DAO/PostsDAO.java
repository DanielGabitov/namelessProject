package com.hse.DAO;

import com.hse.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class PostsDAO {

    private final JdbcTemplate jdbcTemplate;

    private final PostMapper postMapper;

    @Autowired
    public PostsDAO(JdbcTemplate jdbcTemplate, PostMapper postMapper){
        this.jdbcTemplate = jdbcTemplate;
        this.postMapper = postMapper;
    }

    public Post getPosts(){
        return jdbcTemplate.query("SELECT * FROM posts", postMapper).get(0);
    }
}