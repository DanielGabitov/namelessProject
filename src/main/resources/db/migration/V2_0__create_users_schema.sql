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
    description    text
);