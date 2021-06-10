package com.hse.DAOs;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class RecommendationDao {
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public RecommendationDao(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    public void clearRecommendations(){
        MapSqlParameterSource map = new MapSqlParameterSource();
        namedJdbcTemplate.update("TRUNCATE recommendations", map);
        namedJdbcTemplate.update("TRUNCATE users_with_recommendations", map);
    }

    public void addEventRecommendation(Long userId, Long eventId, float coefficient){
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);
        map.addValue("eventId", eventId);
        map.addValue("coefficient", coefficient);
        namedJdbcTemplate.update(
        "INSERT INTO recommendations (userid, eventid, coefficient) VALUES (:userId, :eventId, :coefficient)",
            map
        );
    }

    public void markThatUserHasRecommendations(long userId){
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);
        namedJdbcTemplate.update("INSERT INTO users_with_recommendations (userid) VALUES (:userId)", map);
    }

    public boolean checkIfUserHasRecommendations(long userId){
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);
        return namedJdbcTemplate.query(
            "SELECT * FROM users_with_recommendations",
                map,
                (resultSet, i) -> resultSet.getLong("userId")
        ).stream().findAny().isPresent();
    }
}
