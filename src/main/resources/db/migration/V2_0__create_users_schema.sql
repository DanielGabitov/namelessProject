CREATE TABLE users
(
    id             bigserial not null primary key,
    userRole       varchar(40),
    name           varchar(40),
    secondName     varchar(40),
    patronymic     varchar(40),
    username       varchar(40),
    password       varchar(40),
    specialization varchar(40),
    rating         real,
    description    text,
    photos         bigint[],
    eventsId       bigint[]
);