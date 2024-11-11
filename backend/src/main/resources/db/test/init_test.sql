INSERT INTO benutzereinstellungen (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version,
                                   digitale_kommunikation, mandant)
VALUES ('92486d34-083a-4eb2-b676-f160a99512ba', now(), now(), 'Admin', 'Admin', 0, TRUE, 'bern'),
       ('be468db0-017e-4516-b2bb-6077bae8d78a', now(), now(), 'Admin', 'Admin', 0, TRUE, 'bern'),
       ('be468db0-017e-4516-b2bb-6077bae8d79a', now(), now(), 'Admin', 'Admin', 0, TRUE, 'bern'),
       ('be468db0-017e-4516-b2bb-6077bae8d70a', now(), now(), 'Admin', 'Admin', 0, TRUE, 'bern');


INSERT INTO benutzer (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, nachname,
                      vorname, sozialversicherungsnummer, benutzer_status, mandant,
                      benutzereinstellungen_id, dtype)
VALUES ('9477487f-3ac4-4d02-b57c-e0cefb292ae7', '2023-06-12 15:26:47.000000', '2023-06-12 15:26:50.000000', 'Admin',
        'Admin', 0, 'Adusernn', 'Aduservn', '756.3333.3333.35', 'AKTIV', 'bern',
        'be468db0-017e-4516-b2bb-6077bae8d79a', 'Benutzer');

-- This user is used as the Gesuchsteller in the tests
INSERT INTO benutzer (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, nachname,
                      vorname, sozialversicherungsnummer, benutzer_status, mandant,
                      benutzereinstellungen_id, keycloak_id, dtype)
VALUES ('9477487f-3ac4-4d02-b57c-e0cefb292ae5', '2023-06-12 15:26:47.000000', '2023-06-12 15:26:50.000000', 'Admin',
        'Admin', 0, 'Nell', 'Frédéric', '756.1234.5678.90', 'AKTIV', 'bern',
        '92486d34-083a-4eb2-b676-f160a99512ba', '9477487f-3ac4-4d02-b57c-e0cefb292ae5', 'Benutzer');

INSERT INTO benutzer (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, nachname,
                      vorname, sozialversicherungsnummer, benutzer_status, mandant,
                      benutzereinstellungen_id, keycloak_id, dtype)
VALUES ('9477487f-3ac4-4d02-b57c-e0cefb292ae3', '2023-06-12 15:26:47.000000', '2023-06-12 15:26:50.000000', 'Admin',
        'Admin', 0, 'Gesuchsteller 2', 'Hans', '756.9217.0769.85', 'AKTIV', 'bern',
        'be468db0-017e-4516-b2bb-6077bae8d78a', '9477487f-3ac4-4d02-b57c-e0cefb292ae3', 'Benutzer');
INSERT INTO rolle (timestamp_erstellt, timestamp_mutiert, version, id, keycloak_identifier, mandant, user_erstellt,
                   user_mutiert)
VALUES (now(), now(), 0, 'b497e381-16f4-4971-b2d1-3c8c809ed68b', 'Sachbearbeiter', 'bern', 'Admin', 'Admin'),
       (now(), now(), 0, '9df93c85-db0a-4699-8900-b602878426e8', 'Admin', 'bern', 'Admin', 'Admin'),
       (now(), now(), 0, 'a584a031-e8a6-4dad-9b27-1350242ff67b', 'Gesuchsteller', 'bern', 'Admin', 'Admin');

INSERT INTO benutzer (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, nachname,
                      vorname, sozialversicherungsnummer, benutzer_status, mandant,
                      benutzereinstellungen_id, dtype)
VALUES ('9477487f-3ac4-4d02-b57c-e0cefb292ae4', '2023-06-12 15:26:47.000000', '2023-06-12 15:26:50.000000', 'Admin',
        'Admin', 0, 'Test', 'Admin', '756.2222.2222.24', 'AKTIV', 'bern',
        'be468db0-017e-4516-b2bb-6077bae8d70a', 'Benutzer');

INSERT INTO benutzer_rollen (benutzer_id, rolle_id)
VALUES ('9477487f-3ac4-4d02-b57c-e0cefb292ae3', 'b497e381-16f4-4971-b2d1-3c8c809ed68b'),
       ('9477487f-3ac4-4d02-b57c-e0cefb292ae5', 'a584a031-e8a6-4dad-9b27-1350242ff67b'),
       ('9477487f-3ac4-4d02-b57c-e0cefb292ae4', '9df93c85-db0a-4699-8900-b602878426e8');

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

-- INSERT INTO gesuchsjahr(id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, bezeichnung_de,
--                         bezeichnung_fr, technisches_jahr, gueltigkeit_status, mandant)
-- VALUES ('9596bb3e-10ea-4493-8aed-a6ef510f806b', now(), now(), 'Migration', 'Migration', 0, 'Gesuchsjahr 24',
--         'Année de la demande 23', 2024, 'PUBLIZIERT', 'bern');
--
-- INSERT INTO gesuchsperiode (id,
--                             timestamp_erstellt,
--                             timestamp_mutiert,
--                             user_erstellt,
--                             user_mutiert,
--                             version,
--                             mandant,
--                             bezeichnung_de,
--                             bezeichnung_fr,
--                             fiskaljahr,
--                             gesuchsjahr_id,
--                             gesuchsperiode_start,
--                             gesuchsperiode_stopp,
--                             aufschalttermin_start,
--                             aufschalttermin_stopp,
--                             einreichefrist_normal,
--                             einreichefrist_reduziert,
--                             ausbKosten_sek_ii,
--                             ausbKosten_tertiaer,
--                             freibetrag_vermoegen,
--                             freibetrag_erwerbseinkommen,
--                             einkommensfreibetrag,
--                             elternbeteiligungssatz,
--                             vermogen_satz_angerechnet,
--                             integrationszulage,
--                             limite_ek_freibetrag_integrationszulage,
--                             stip_limite_minimalstipendium,
--                             person_1,
--                             personen_2,
--                             personen_3,
--                             personen_4,
--                             personen_5,
--                             personen_6,
--                             personen_7,
--                             pro_weitere_person,
--                             kinder_00_18,
--                             jugendliche_erwachsene_19_25,
--                             erwachsene_26_99,
--                             wohnkosten_fam_1pers,
--                             wohnkosten_fam_2pers,
--                             wohnkosten_fam_3pers,
--                             wohnkosten_fam_4pers,
--                             wohnkosten_fam_5pluspers,
--                             wohnkosten_persoenlich_1pers,
--                             wohnkosten_persoenlich_2pers,
--                             wohnkosten_persoenlich_3pers,
--                             wohnkosten_persoenlich_4pers,
--                             wohnkosten_persoenlich_5pluspers,
--                             gueltigkeit_status,
--                             preis_pro_mahlzeit,
--                             max_saeule_3a,
--                             anzahl_wochen_lehre,
--                             anzahl_wochen_schule)
-- VALUES ('3fa85f64-5717-4562-b3fc-2c963f66afa6',
--         '2023-05-31 08:35:52',
--         '2023-05-30 08:35:43',
--         'Admin',
--         'Admin',
--         0,
--         'bern',
--         'Test DE',
--         'Test FR',
--         '2024',
--         '9596bb3e-10ea-4493-8aed-a6ef510f806b',
--         '2023-08-01',
--         '2024-07-31',
--         '2024-01-01',
--         '2024-12-31',
--         current_date,
--         current_date,
--         1,
--         1,
--         1,
--         1,
--         1,
--         1,
--         1,
--         1,
--         1,
--         1,
--         1,
--         1,
--         1,
--         1,
--         1,
--         1,
--         1,
--         1,
--         1,
--         1,
--         1,
--         1,
--         1,
--         1,
--         1,
--         1,
--         1,
--         1,
--         1,
--         1,
--         1,
--         'ENTWURF',
--         1,
--         1,
--         1,
--         1);


-- INSERT INTO ausbildungsstaette (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version,
--                                 name_de, name_fr)
-- VALUES ('9477487f-3ac4-4d02-b57c-e0cefb292ae5', '2023-06-12 15:26:47.000000', '2023-06-12 15:26:50.000000', 'Admin',
--         'Admin', 0, 'Universtität Bern', 'Université de Berne');
-- INSERT INTO ausbildungsgang (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version,
--                              bezeichnung_de, bezeichnung_fr, ausbildungsstaette_id, ausbildungsrichtung,
--                              mandant)
-- VALUES ('3a8c2023-f29e-4466-a2d7-411a7d032f42', '2023-06-12 15:38:10.000000', '2023-06-12 15:38:06.000000', 'Admin',
--         'Admin', 0, 'Bachelor', 'Bachelor', '9477487f-3ac4-4d02-b57c-e0cefb292ae5', 'UNIVERSITAETEN_ETH',
--         'bern');
