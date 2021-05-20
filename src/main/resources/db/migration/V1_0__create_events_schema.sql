CREATE TABLE events
(
    id              bigserial not null primary key,
    name            varchar(40),
    description     text,
    rating          real,
    geoData         varchar(100),
    specialization  varchar(40),
    date            timestamp
);