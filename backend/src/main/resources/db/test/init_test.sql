INSERT INTO benutzereinstellungen (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version,
                                   digitale_kommunikation, mandant)
VALUES ('92486d34-083a-4eb2-b676-f160a99512ba', now(), now(), 'Admin', 'Admin', 0, TRUE, 'bern'),
       ('be468db0-017e-4516-b2bb-6077bae8d78a', now(), now(), 'Admin', 'Admin', 0, TRUE, 'bern'),
       ('be468db0-017e-4516-b2bb-6077bae8d79a', now(), now(), 'Admin', 'Admin', 0, TRUE, 'bern'),
       ('be468db0-017e-4516-b2bb-6077bae8d70a', now(), now(), 'Admin', 'Admin', 0, TRUE, 'bern'),
       ('be468db0-017e-4516-b2bb-6077bae8d71a', now(), now(), 'Admin', 'Admin', 0, TRUE, 'bern');


INSERT INTO benutzer (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, nachname,
                      vorname, benutzer_status, mandant,
                      benutzereinstellungen_id)
VALUES ('9477487f-3ac4-4d02-b57c-e0cefb292ae7', '2023-06-12 15:26:47.000000', '2023-06-12 15:26:50.000000', 'Admin',
        'Admin', 0, 'Adusernn', 'Aduservn', 'AKTIV', 'bern',
        'be468db0-017e-4516-b2bb-6077bae8d79a');

-- This user is used as the Gesuchsteller in the tests
INSERT INTO benutzer (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, nachname,
                      vorname, benutzer_status, mandant,
                      benutzereinstellungen_id, keycloak_id)
VALUES ('9477487f-3ac4-4d02-b57c-e0cefb292ae5', '2023-06-12 15:26:47.000000', '2023-06-12 15:26:50.000000', 'Admin',
        'Admin', 0, 'Nell', 'Frédéric', 'AKTIV', 'bern',
        '92486d34-083a-4eb2-b676-f160a99512ba', '9477487f-3ac4-4d02-b57c-e0cefb292ae5');

INSERT INTO sachbearbeiter (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, nachname,
                            vorname, benutzer_status, mandant,
                            benutzereinstellungen_id, keycloak_id, funktion_de, funktion_fr, telefonnummer)
VALUES ('9477487f-3ac4-4d02-b57c-e0cefb292ae3', '2023-06-12 15:26:47.000000', '2023-06-12 15:26:50.000000', 'Admin',
        'Admin', 0, 'Gesuchsteller 2', 'Hans', 'AKTIV', 'bern',
        'be468db0-017e-4516-b2bb-6077bae8d78a', '9477487f-3ac4-4d02-b57c-e0cefb292ae3', 'Sachbearbeiter', 'Sachbearbeiter', '+41 31 111 11 11');

INSERT INTO sachbearbeiter (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, nachname,
                            vorname, benutzer_status, mandant,
                            benutzereinstellungen_id, keycloak_id, funktion_de, funktion_fr, telefonnummer)
VALUES ('9477487f-3ac4-4d02-b57c-e0cefb292ae9', '2023-06-12 15:26:47.000000', '2023-06-12 15:26:50.000000', 'Admin',
        'Admin', 0, 'Sachbearbeiter', 'Hans', 'AKTIV', 'bern',
        'be468db0-017e-4516-b2bb-6077bae8d71a', 'ea75c9be-35a0-4ae6-9383-a3459501596b', 'Sachbearbeiter', 'Sachbearbeiter', '+41 31 111 11 11');
INSERT INTO rolle (timestamp_erstellt, timestamp_mutiert, version, id, keycloak_identifier, mandant, user_erstellt,
                   user_mutiert)
VALUES (now(), now(), 0, 'b497e381-16f4-4971-b2d1-3c8c809ed68b', 'V0_Sachbearbeiter', 'bern', 'Admin', 'Admin'),
       (now(), now(), 0, '9df93c85-db0a-4699-8900-b602878426e8', 'V0_Sachbearbeiter-Admin', 'bern', 'Admin', 'Admin'),
       (now(), now(), 0, 'a584a031-e8a6-4dad-9b27-1350242ff67b', 'V0_Gesuchsteller', 'bern', 'Admin', 'Admin');

INSERT INTO sachbearbeiter (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, nachname,
                            vorname, benutzer_status, mandant,
                            benutzereinstellungen_id, funktion_de, funktion_fr, telefonnummer)
VALUES ('9477487f-3ac4-4d02-b57c-e0cefb292ae4', '2023-06-12 15:26:47.000000', '2023-06-12 15:26:50.000000', 'Admin',
        'Admin', 0, 'Test', 'Admin', 'AKTIV', 'bern',
        'be468db0-017e-4516-b2bb-6077bae8d70a', 'Admin', 'Admin', '+41 31 111 11 11');

INSERT INTO benutzer_rollen (benutzer_id, rolle_id)
VALUES ('9477487f-3ac4-4d02-b57c-e0cefb292ae3', 'b497e381-16f4-4971-b2d1-3c8c809ed68b'),
       ('9477487f-3ac4-4d02-b57c-e0cefb292ae5', 'a584a031-e8a6-4dad-9b27-1350242ff67b'),
       ('9477487f-3ac4-4d02-b57c-e0cefb292ae4', '9df93c85-db0a-4699-8900-b602878426e8'),
       ('9477487f-3ac4-4d02-b57c-e0cefb292ae9', 'b497e381-16f4-4971-b2d1-3c8c809ed68b');

INSERT INTO sachbearbeiter_zuordnung_stammdaten (id, mandant, version, timestamp_erstellt, timestamp_mutiert,
                                                 user_erstellt, user_mutiert, buchstaben_de, buchstaben_fr, benutzer_id)
VALUES ('9477487f-3ac4-4d02-b57c-e0cefb292ae4', 'bern', 0, now(), now(), 'Admin', 'Admin', 'A-Z', 'A-Z',
        '9477487f-3ac4-4d02-b57c-e0cefb292ae3');

INSERT INTO fall (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, fall_nummer, mandant,
                  gesuchsteller_id)
VALUES ('4b99f69f-ec53-4ef7-bd1f-0e76e04abe7b', '2023-06-20 14:22:43.418364', '2023-06-20 14:22:43.418364', 'TODO',
        'TODO', 0, 1, 'bern', '9477487f-3ac4-4d02-b57c-e0cefb292ae5');

-- Gesuchsperiode

DELETE
FROM gesuchsperiode
WHERE id = '3fa85f64-5717-4562-b3fc-2c963f66afa6';

DELETE
FROM gesuchsjahr
WHERE id = '9596bb3e-10ea-4493-8aed-a6ef510f806b';
