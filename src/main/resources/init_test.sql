INSERT INTO benutzer (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, nachname, vorname, sozialversicherungsnummer, benutzer_status, mandant) VALUES ('9477487f-3ac4-4d02-b57c-e0cefb292ae5', '2023-06-12 15:26:47.000000', '2023-06-12 15:26:50.000000', 'Admin', 'Admin', 0, 'Nell', 'Frédéric','756.1111.1111.13','AKTIV','bern');
INSERT INTO benutzer (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, nachname, vorname, sozialversicherungsnummer, benutzer_status, mandant) VALUES ('9477487f-3ac4-4d02-b57c-e0cefb292ae3', '2023-06-12 15:26:47.000000', '2023-06-12 15:26:50.000000', 'Admin', 'Admin', 0, 'Schärer', 'Philipp','756.2222.2222.24','AKTIV','bern');

INSERT INTO fall (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, fall_nummer, mandant, gesuchsteller_id) VALUES ('4b99f69f-ec53-4ef7-bd1f-0e76e04abe7b', '2023-06-20 14:22:43.418364', '2023-06-20 14:22:43.418364', 'TODO', 'TODO', 0, 1, 'bern', '9477487f-3ac4-4d02-b57c-e0cefb292ae5');

INSERT INTO gesuchsperiode (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version,
                            gueltig_ab, gueltig_bis, einreichfrist, aufschaltdatum, mandant)
VALUES ('3fa85f64-5717-4562-b3fc-2c963f66afa6', '2023-05-31 08:35:52', '2023-05-30 08:35:43', 'Admin', 'Admin', 0,
        '2023-08-01', '2024-06-30', '2023-12-31', '2023-06-01', 'bern');

INSERT INTO ausbildungsstaette (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, name, ausbildungsland) VALUES ('9477487f-3ac4-4d02-b57c-e0cefb292ae5', '2023-06-12 15:26:47.000000', '2023-06-12 15:26:50.000000', 'Admin', 'Admin', 0, 'Universtität Bern', 'SCHWEIZ');
INSERT INTO ausbildungsgang (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, bezeichnung_de, bezeichnung_fr, ausbildungsstaette_id) VALUES ('3a8c2023-f29e-4466-a2d7-411a7d032f42', '2023-06-12 15:38:10.000000', '2023-06-12 15:38:06.000000', 'Admin', 'Admin', 0, 'Bachelor', 'Bachelor', '9477487f-3ac4-4d02-b57c-e0cefb292ae5');
