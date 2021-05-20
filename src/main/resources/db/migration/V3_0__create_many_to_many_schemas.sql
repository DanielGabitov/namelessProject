CREATE TABLE events_organizers(
    eventID bigint,
    userID  bigint
);

CREATE TABLE events_participants(
    eventID bigint,
    userID  bigint
);

CREATE TABLE events_images(
    image   varchar(40),
    eventId bigint
);

CREATE TABLE users_images(
    image   varchar(40),
    userId  bigint
);