package com.hse.DAOs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SubscriptionDao {
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    public SubscriptionDao(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    public void addSubscription(Long userId, Long subscriptionId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);
        map.addValue("subscriptionId", subscriptionId);
        namedJdbcTemplate.update(
                "INSERT INTO users_subscriptions (userid, subscriptionid) VALUES (:userId, :subscriptionId)", map);
    }

    public boolean checkSubscription(Long userId, Long subscriptionId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);
        map.addValue("subscriptionId", subscriptionId);
        Integer count = namedJdbcTemplate.queryForObject(
                "SELECT count(userId) FROM users_subscriptions " +
                        "WHERE userId = :userId AND subscriptionId = :subscriptionId", map, Integer.class);
        return count != null && count > 0;
    }

    public void deleteSubscription(Long userId, Long subscriptionId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);
        map.addValue("subscriptionId", subscriptionId);

        namedJdbcTemplate.update(
                "DELETE FROM users_subscriptions WHERE userId = :userId AND subscriptionId = :subscriptionId",
                map);
    }

    public List<Long> getAllSubscriptionIds(Long userId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);

        return namedJdbcTemplate.query(
                "SELECT * FROM users_subscriptions WHERE userId = :userId",
                map,
                (resultSet, i) -> resultSet.getLong("subscriptionId"));
    }
}
