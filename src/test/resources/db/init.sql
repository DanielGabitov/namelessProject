CREATE TABLE events
(
    id             bigserial not null primary key,
    name           varchar(40),
    description    text,
    organizerId    bigint,
    geoData        varchar(100),
    specialization varchar(40),
    date           timestamp,
    passed         bool
);

CREATE EXTENSION pg_trgm;

CREATE TABLE users
(
    id             bigserial not null primary key,
    userRole       varchar(40),
    firstName      varchar(40),
    lastName       varchar(40),
    patronymic     varchar(40),
    username       varchar(40) UNIQUE,
    password       varchar(40),
    specialization varchar(40),
    rating         real,
    description    text
);

CREATE TABLE events_participants
(
    eventId       bigint NOT NULL,
    participantId bigint NOT NULL,
    PRIMARY KEY (eventId, participantId)
);

CREATE TABLE events_images
(
    eventId bigint,
    image   varchar(40)
);

CREATE TABLE users_images
(
    userId bigint,
    image  varchar(40)
);

CREATE TABLE likes
(
    userId  bigint NOT NULL,
    eventId bigint NOT NULL,
    PRIMARY KEY (userId, eventId)
);

CREATE TABLE users_subscriptions
(
    userId         bigint NOT NULL,
    subscriptionId bigint NOT NULL,
    PRIMARY KEY (userId, subscriptionId)
);

CREATE TABLE creators_invites
(
    creatorId   bigint NOT NULL,
    organizerId bigint NOT NULL,
    eventId     bigint NOT NULL,
    message     text,
    accepted    bool
);

CREATE TABLE event_applications
(
    eventId   bigint NOT NULL,
    creatorId bigint NOT NULL,
    message   text,
    accepted  bool
);


CREATE TABLE user_viewed_events
(
    userId  bigint NOT NULL,
    eventId bigint NOT NULL,
    PRIMARY KEY (userId, eventId)
);

CREATE TABLE notifications
(
    notificationId         bigserial not null primary key,
    notificationReceiverId bigint    NOT NULL,
    notificationProducerId bigint    NOT NULL,
    notificationType       varchar(60)
);

CREATE TABLE recommendations
(
    userId      bigint NOT NULL,
    eventId     bigint NOT NULL,
    coefficient float  NOT NULL,
    PRIMARY KEY (userId, eventId)
);

CREATE TABLE users_with_recommendations
(
    userId bigint PRIMARY KEY
);

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
    memberId              bigint NOT NULL,
    PRIMARY KEY (creativeAssociationId, memberId)
);

CREATE TABLE creative_association_invitations
(
    creativeAssociationId bigint NOT NULL,
    invitedCreatorId      bigint NOT NULL,
    isAnswered            boolean,
    acceptance            boolean,
    PRIMARY KEY (creativeAssociationId, invitedCreatorId)
);

CREATE TABLE user_to_creator_or_organizer_rating
(
    userId               bigint,
    organizerOrCreatorId bigint,
    rating               smallint
);