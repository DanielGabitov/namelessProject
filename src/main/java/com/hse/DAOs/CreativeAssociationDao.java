package com.hse.DAOs;

import com.hse.mappers.CreativeAssociationMapper;
import com.hse.models.CreativeAssociation;
import com.hse.models.CreativeAssociationInvitation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CreativeAssociationDao {
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final CreativeAssociationMapper associationMapper;

    @Autowired
    public CreativeAssociationDao(NamedParameterJdbcTemplate namedJdbcTemplate,
                                  CreativeAssociationMapper associationMapper) {
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.associationMapper = associationMapper;
    }

    public long createCreativeAssociation(CreativeAssociation association) {
        MapSqlParameterSource map = new MapSqlParameterSource();

        mapCreativeAssociationParameters(association, map);

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        namedJdbcTemplate.update(
                "INSERT INTO creative_association" +
                        "(name, description, bossCreator)" +
                        " VALUES (:name, :description, :bossCreator)",
                map,
                keyHolder
        );
        return (long) keyHolder.getKeyList().get(0).get("id");
    }

    public Optional<CreativeAssociation> getCreativeAssociation(long associationId) {
        MapSqlParameterSource map = new MapSqlParameterSource();

        map.addValue("associationId", associationId);

        return namedJdbcTemplate.query(
                "SELECT * FROM creative_association WHERE id = :associationId",
                map,
                associationMapper).stream().findAny();
    }

    public void addMember(long associationId, long memberId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("creativeAssociationId", associationId);
        map.addValue("memberId", memberId);

        namedJdbcTemplate.update(
                "INSERT INTO creative_association_members " +
                        "(creativeAssociationId, memberId) " +
                        "VALUES (:creativeAssociationId, :memberId)",
                map);
    }

    public List<Long> getMembers(long associationId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("creativeAssociationId", associationId);

        return namedJdbcTemplate.query(
                "SELECT memberId FROM creative_association_members " +
                        "WHERE creativeAssociationId = :creativeAssociationId",
                map,
                (resultSet, i) -> resultSet.getLong("memberId"));
    }

    public void addImage(long associationId, String image) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", associationId);
        map.addValue("image", image);

        namedJdbcTemplate.update(
                "INSERT INTO creative_association_images (id, image) VALUES (:id, :image)",
                map);
    }

    public List<String> getImages(long associationId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", associationId);

        return namedJdbcTemplate.query(
                "SELECT * FROM creative_association_images WHERE id = :id",
                map,
                (resultSet, i) -> resultSet.getString("image"));
    }

    private void mapCreativeAssociationParameters(CreativeAssociation association, MapSqlParameterSource map) {
        map.addValue("name", association.getName());
        map.addValue("description", association.getDescription());
        map.addValue("bossCreator", association.getBossCreator());
    }

    public void addInvitation(long associationId, long creatorId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("associationId", associationId);
        map.addValue("creatorId", creatorId);

        namedJdbcTemplate.update(
                "INSERT INTO creative_association_invitations " +
                        "(creativeassociationid, invitedcreatorid, isanswered, acceptance) " +
                        "VALUES (:associationId, :creatorId, false, NULL)",
                map);
    }

    public Optional<CreativeAssociationInvitation> getInvitation(long associationId, long creatorId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("associationId", associationId);
        map.addValue("creatorId", creatorId);

        return namedJdbcTemplate.query(
                "SELECT * FROM creative_association_invitations " +
                        "WHERE creativeAssociationId = :associationId AND invitedCreatorid = :creatorId",
                map,
                (resultSet, i) -> {
                    Long associationId1 = resultSet.getLong("creativeAssociationId");
                    Long creatorId1 = resultSet.getLong("invitedCreatorId");
                    Boolean isAnswered = resultSet.getBoolean("isAnswered");
                    Boolean acceptance = resultSet.getBoolean("acceptance");
                    return new CreativeAssociationInvitation(associationId1, creatorId1, isAnswered, acceptance);
                }
        ).stream().findAny();
    }

    public void answerInvitation(long associationId, long creatorId, boolean acceptance) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("associationId", associationId);
        map.addValue("creatorId", creatorId);
        map.addValue("acceptance", acceptance);

        namedJdbcTemplate.update(
                "UPDATE creative_association_invitations SET acceptance = :acceptance, isAnswered = TRUE " +
                        "WHERE creativeAssociationId = :associationId AND invitedCreatorId = :creatorId",
                map);
    }

    public List<CreativeAssociation> getCreativeAssociations(int size, int offset){
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("offset", offset);
        map.addValue("size", size);

        return namedJdbcTemplate.query(
                "SELECT * FROM creative_association OFFSET :offset ROWS FETCH FIRST :size ROWS ONLY",
                map,
                associationMapper);
    }
}
