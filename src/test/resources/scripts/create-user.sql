DELETE FROM postgres.public.users WHERE postgres.public.users.username='username';

INSERT INTO postgres.public.users (id, userrole, firstname, lastname, patronymic, username, password, specialization, rating, description)
VALUES (1, 'USER', 'name', 'name', 'name', 'username', 'password', 'ART', 1, 'description');