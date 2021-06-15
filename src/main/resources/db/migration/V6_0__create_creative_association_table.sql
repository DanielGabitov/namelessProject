CREATE TABLE creative_association
(
    id          bigserial NOT NULL primary key,
    name        varchar(40),
    description text,
    bossCreator bigint
);

CREATE TABLE creative_association_images
(
    id    bigint NOT NULL,
    image varchar(40)
);

CREATE TABLE creative_association_members
(
    creativeAssociationId bigint NOT NULL,
    memberId bigint NOT NULL,
    PRIMARY KEY (creativeAssociationId, memberId)
);

CREATE TABLE creative_association_invitations
(
    creativeAssociationId bigint NOT NULL,
    invitedCreatorId bigint NOT NULL,
    isAnswered boolean,
    acceptance boolean,
    PRIMARY KEY (creativeAssociationId, invitedCreatorId)
);