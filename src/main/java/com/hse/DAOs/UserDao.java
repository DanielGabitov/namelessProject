package com.hse.DAOs;

import com.hse.enums.Specialization;
import com.hse.mappers.ApplicationMapper;
import com.hse.mappers.InviteMapper;
import com.hse.models.Application;
import com.hse.models.Invitation;
import com.hse.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@Component
public class UserDao {
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    private final RowMapper<User> userMapper;
    private final ApplicationMapper applicationMapper;
    private final InviteMapper inviteMapper;

    @Autowired
    public UserDao(NamedParameterJdbcTemplate namedJdbcTemplate, RowMapper<User> userMapper,
                   ApplicationMapper applicationMapper, InviteMapper inviteMapper) {
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.userMapper = userMapper;
        this.applicationMapper = applicationMapper;
        this.inviteMapper = inviteMapper;
    }

    public Integer getNumberOfUsersById(long userId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", userId);

        return namedJdbcTemplate.queryForObject(
                "SELECT count(id) FROM users WHERE id = :id", map, Integer.class);
    }

    public Optional<User> getUserById(Long userId) throws IllegalArgumentException {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", userId);
        return namedJdbcTemplate.query("SELECT * FROM users WHERE id=:id", map, userMapper).stream().findAny();
    }

    public Optional<User> getUserByUsername(String username) throws IllegalArgumentException {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("username", username);
        return namedJdbcTemplate.query("SELECT * FROM users WHERE username=:username", map, userMapper).stream().findAny();
    }

    public Optional<User> getUserByUsernameAndPassword(String username, String password) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("username", username);
        map.addValue("password", password);
        return namedJdbcTemplate.query("SELECT * FROM users WHERE username=:username AND password=:password", map, userMapper).stream().findAny();
    }

    public long saveUser(User user) {
        MapSqlParameterSource map = mapUserParameters(user);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        namedJdbcTemplate.update(
                "INSERT INTO users (userRole, firstName, lastName, patronymic, username, password, specialization, " +
                        "rating, description) VALUES " +
                        "(:userRole, :firstName, :lastName, :patronymic, :userName, :password, :specialization, :rating," +
                        " :description)", map, keyHolder
        );
        return (long) keyHolder.getKeyList().get(0).get("id");
    }

    public void updateUser(User newUser) {
        MapSqlParameterSource map = mapUserParameters(newUser);

        namedJdbcTemplate.update(
                "UPDATE users SET userrole = :userRole, firstname = :firstName, lastname = :lastName, " +
                        "patronymic = :patronymic, username = :userName, password = :password, " +
                        "specialization = :specialization, rating = :rating, description = :description " +
                        "WHERE username = :userName", map);
    }

    private MapSqlParameterSource mapUserParameters(User newUser) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userRole", newUser.getUserRole().name());
        map.addValue("firstName", newUser.getFirstName());
        map.addValue("lastName", newUser.getLastName());
        map.addValue("patronymic", newUser.getPatronymic());
        map.addValue("userName", newUser.getUsername());
        map.addValue("password", newUser.getPassword());
        map.addValue("specialization", newUser.getSpecialization().name());
        map.addValue("rating", newUser.getRating());
        map.addValue("description", newUser.getDescription());
        return map;
    }

    public List<User> getCreators(int offset, int size) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("offset", offset);
        map.addValue("size", size);

        return namedJdbcTemplate.query("SELECT * FROM users WHERE userRole = 'CREATOR'" +
                        " OFFSET :offset ROWS FETCH FIRST :size ROWS ONLY;",
                map, userMapper);
    }

    public List<User> getCreators(int offset, int size, EnumSet<Specialization> specializations) {
        MapSqlParameterSource map = EventDao.mapOffsetAndSize(offset, size, specializations);

        return namedJdbcTemplate.query(
                "SELECT * FROM users WHERE userRole = 'CREATOR' AND " +
                        "specialization IN (:specializations) OFFSET :offset ROWS FETCH FIRST :size ROWS ONLY;",
                map, userMapper);
    }

    public Optional<Invitation> getCreatorInvitationFromEvent(long creatorId, long eventId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("creatorId", creatorId);
        map.addValue("eventId", eventId);
        return namedJdbcTemplate.query(
                "SELECT * from creators_invites WHERE creatorid = :creatorId AND eventid = :eventId",
                map,
                inviteMapper
        ).stream().findAny();
    }

    public Optional<Application> getEventApplicationToCreator(long eventId, long creatorId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("creatorId", creatorId);
        map.addValue("eventId", eventId);

        return namedJdbcTemplate.query(
                "SELECT * from event_applications WHERE eventId = :eventId AND creatorId = :creatorId",
                map,
                applicationMapper).stream().findAny();
    }

    public List<Invitation> getCreatorInvitations(long creatorId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("creatorId", creatorId);
        return namedJdbcTemplate.query(
                "SELECT * from creators_invites WHERE creatorid = :creatorId",
                map,
                inviteMapper
        );
    }

    public Optional<Application> getCreatorEventApplication(long creatorId, long eventId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("eventId", eventId);
        map.addValue("creatorId", creatorId);
        return namedJdbcTemplate.query(
                "SELECT * from creators_invites WHERE creatorid = :creatorId AND eventid = :eventId",
                map,
                applicationMapper
        ).stream().findAny();
    }

    public List<Application> getCreatorEventApplications(long creatorId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("creatorId", creatorId);
        return namedJdbcTemplate.query(
                "SELECT * from creators_invites WHERE creatorId = :creatorId",
                map, applicationMapper);
    }

    public void sendEventApplication(long creatorId, long eventId, String message) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("creatorId", creatorId);
        map.addValue("eventId", eventId);
        map.addValue("message", message);
        map.addValue("accepted", null);
        namedJdbcTemplate.update(
                "INSERT INTO event_applications (eventid, creatorid, message, accepted)" +
                        " VALUES (:eventId, :creatorId, :message, :accepted)", map);
    }

    public void inviteCreator(long creatorId, long organizerId, long eventId, String message) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("creatorId", creatorId);
        map.addValue("organizerId", organizerId);
        map.addValue("eventId", eventId);
        map.addValue("message", message);
        namedJdbcTemplate.update(
                "INSERT INTO creators_invites (creatorid, organizerid, eventid, message, accepted)" +
                        " VALUES (:creatorId, :organizerId, :eventId, :message, NULL)", map);
    }

    public void answerInvitation(long creatorId, long eventId, boolean accept) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("creatorId", creatorId);
        map.addValue("eventId", eventId);
        map.addValue("accept", accept);
        namedJdbcTemplate.update(
                "UPDATE creators_invites SET accepted = :accept " +
                        "WHERE creatorid = :creatorId AND eventid = :eventId"
                , map);
    }

    public void answerApplication(long eventId, long creatorId, boolean accept) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("creatorId", creatorId);
        map.addValue("eventId", eventId);
        map.addValue("accept", accept);
        namedJdbcTemplate.update(
                "UPDATE event_applications SET accepted = :accept " +
                        "WHERE creatorid = :creatorId AND eventid = :eventId",
                map);
    }

    public List<Long> getAllUserIds() {
        return namedJdbcTemplate.query("SELECT * FROM users",
                new MapSqlParameterSource(),
                (resultSet, i) -> resultSet.getLong("id")
        );
    }

    public List<User> searchUsers(String username, int offset, int size) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("username", username);
        map.addValue("offset", offset);
        map.addValue("size", size);
        return namedJdbcTemplate.query("SELECT * FROM users ORDER BY word_similarity(username, :username) DESC" +
                " OFFSET :offset ROWS FETCH FIRST :size ROWS ONLY", map, userMapper);
    }
}