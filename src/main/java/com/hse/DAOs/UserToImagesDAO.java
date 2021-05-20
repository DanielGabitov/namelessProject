package com.hse.DAOs;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserToImagesDAO {
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public UserToImagesDAO(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    public void addImage(long userId, String image) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);
        map.addValue("image", image);

        namedJdbcTemplate.update("INSERT INTO users_images (userId, image) VALUES (:userId, :image)", map);
    }

    public List<String> getImages(long userId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);
        return namedJdbcTemplate.query("SELECT * from users_images WHERE userId = :userId", map,
                resultSet -> {
                    List<String> images = new ArrayList<>();
                    while (resultSet.next()) {
                        images.add(resultSet.getString("image"));
                    }
                    return images;
                });
    }
}