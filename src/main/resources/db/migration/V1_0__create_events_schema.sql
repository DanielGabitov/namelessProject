CREATE TABLE events
(
    id              bigserial not null primary key,
    name            varchar(40),
    description     text,
    imageHashes     varchar(40)[],
    organizerIDs    bigint[],
    participantsIDs bigint[],
    rating          real,
    geoData         varchar(100),
    specialization  varchar(40),
    date            timestamp
);