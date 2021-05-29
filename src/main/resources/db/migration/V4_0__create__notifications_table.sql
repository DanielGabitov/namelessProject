CREATE TABLE notifications
(
    notificationId         bigserial not null primary key,
    notificationReceiverId bigint    NOT NULL,
    notificationProducerId bigint    NOT NULL,
    notificationType       varchar(40)
);