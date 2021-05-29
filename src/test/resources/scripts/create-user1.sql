DELETE FROM postgres.public.users WHERE postgres.public.users.username='username1';

INSERT INTO postgres.public.users (id, userrole, firstname, lastname, patronymic, username, password, specialization, rating, description)
VALUES (2, 'CREATOR', 'name', 'name', 'name', 'username1', 'password', 'ART', 1, 'description');