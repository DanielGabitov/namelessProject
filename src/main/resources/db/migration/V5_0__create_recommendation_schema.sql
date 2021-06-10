CREATE TABLE recommendations
(
    userId bigint NOT NULL,
    eventId bigint NOT NULL,
    coefficient float NOT NULL,
    PRIMARY KEY (userId, eventId)
);

CREATE TABLE users_with_recommendations
(
    userId bigint PRIMARY KEY
);