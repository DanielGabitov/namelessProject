CREATE TABLE events_organizers
(
    eventID bigint NOT NULL,
    userID  bigint NOT NULL,
    PRIMARY KEY (eventId, userId)
);

CREATE TABLE events_participants
(
    eventID bigint NOT NULL,
    userID  bigint NOT NULL,
    PRIMARY KEY (eventId, userId)
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