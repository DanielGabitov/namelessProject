CREATE TABLE events(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name varchar(40) UNIQUE,
    description text,
    date timestamp,
    organizerId int
);