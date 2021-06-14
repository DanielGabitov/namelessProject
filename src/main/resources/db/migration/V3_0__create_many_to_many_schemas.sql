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
    userId bigint NOT NULL,
    eventId bigint NOT NULL,
    PRIMARY KEY (userId, eventId)
)