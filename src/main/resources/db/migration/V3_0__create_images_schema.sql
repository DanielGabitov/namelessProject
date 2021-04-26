CREATE TABLE images(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    eventName varchar(40),
    data bytea
);