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

    public void addSubscription(long userId, long subscriptionId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);
        map.addValue("subscriptionId", subscriptionId);
        namedJdbcTemplate.update(
                "INSERT INTO users_subscriptions (userid, subscriptionid) VALUES (:userId, :subscriptionId)", map);
    }

    public Integer getNumberOfUserSubscriptionsWithSuchId(long userId, long subscriptionId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);
        map.addValue("subscriptionId", subscriptionId);
        return namedJdbcTemplate.queryForObject(
                "SELECT count(userId) FROM users_subscriptions " +
                        "WHERE userId = :userId AND subscriptionId = :subscriptionId", map, Integer.class);
    }

    public void deleteSubscription(long userId, long subscriptionId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);
        map.addValue("subscriptionId", subscriptionId);

        namedJdbcTemplate.update(
                "DELETE FROM users_subscriptions WHERE userId = :userId AND subscriptionId = :subscriptionId", map);
    }

    public List<Long> getAllSubscriptionIds(long userId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);

        return namedJdbcTemplate.query(
                "SELECT * FROM users_subscriptions WHERE userId = :userId",
                map,
                (resultSet, i) -> resultSet.getLong("subscriptionId"));
    }
}
