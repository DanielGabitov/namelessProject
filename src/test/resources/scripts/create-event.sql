DELETE FROM events;

INSERT INTO events (id, name, description, organizerId, rating, geoData, specialization, date)
VALUES (1, 'name', 'name', 1, 1.0, 'geo', 'ART', now());