INSERT INTO gesuchsjahr(id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, bezeichnung_de,
                        bezeichnung_fr, technisches_jahr, gueltigkeit_status)
VALUES ('9596bb3e-10ea-4493-8aed-a6ef510f806b', now(), now(), 'Migration', 'Migration', 0, 'Gesuchsjahr 24',
        'Année de la demande 23', 2024, 'PUBLIZIERT');

INSERT INTO gesuchsperiode(id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, mandant,
                           bezeichnung_de, bezeichnung_fr, fiskaljahr, gesuchsjahr_id, gesuchsperiode_start,
                           gesuchsperiode_stopp, aufschalttermin_start, aufschalttermin_stopp, einreichefrist_normal,
                           einreichefrist_reduziert, ausbkosten_sek_ii, ausbkosten_tertiaer, freibetrag_vermoegen,
                           freibetrag_erwerbseinkommen, einkommensfreibetrag, elternbeteiligungssatz,
                           vermoegensfreibetrag, vermogen_satz_angerechnet, integrationszulage,
                           limite_ek_freibetrag_integrationszulag, stip_limite_minimalstipendium, person_1, personen_2,
                           personen_3, personen_4, personen_5, personen_6, personen_7, pro_weitere_person, kinder_00_18,
                           jugendliche_erwachsene_19_25, erwachsene_26_99, wohnkosten_fam_1pers, wohnkosten_fam_2pers,
                           wohnkosten_fam_3pers, wohnkosten_fam_4pers, wohnkosten_fam_5pluspers,
                           wohnkosten_persoenlich_1pers, wohnkosten_persoenlich_2pers, wohnkosten_persoenlich_3pers,
                           wohnkosten_persoenlich_4pers, wohnkosten_persoenlich_5pluspers, gueltigkeit_status)
VALUES ('c261e5f8-2217-4b14-81b0-e42ca59b5010', now(), now(), 'Migration', 'Migration', 0, 'bern', 'Frühling 2023',
        'Printemps 2023', '2023', '9596bb3e-10ea-4493-8aed-a6ef510f806b', '2022-08-01', '2023-07-31', '2022-01-01',
        '2022-12-31', '2022-07-31', '2022-12-31', 2000, 3000, 0, 6000, 6000, 50, 30000, 15, 2400, 13200, 500, 11724,
        17940, 21816, 25080, 28368, 31656, 34944, 3288, 1400, 4600, 5400, 10009, 13536, 16260, 19932, 25260, 13536,
        16260, 16260, 19932, 25260, 'PUBLIZIERT');

INSERT INTO gesuchsperiode(id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, mandant,
                           bezeichnung_de, bezeichnung_fr, fiskaljahr, gesuchsjahr_id, gesuchsperiode_start,
                           gesuchsperiode_stopp, aufschalttermin_start, aufschalttermin_stopp, einreichefrist_normal,
                           einreichefrist_reduziert, ausbkosten_sek_ii, ausbkosten_tertiaer, freibetrag_vermoegen,
                           freibetrag_erwerbseinkommen, einkommensfreibetrag, elternbeteiligungssatz,
                           vermoegensfreibetrag, vermogen_satz_angerechnet, integrationszulage,
                           limite_ek_freibetrag_integrationszulag, stip_limite_minimalstipendium, person_1, personen_2,
                           personen_3, personen_4, personen_5, personen_6, personen_7, pro_weitere_person, kinder_00_18,
                           jugendliche_erwachsene_19_25, erwachsene_26_99, wohnkosten_fam_1pers, wohnkosten_fam_2pers,
                           wohnkosten_fam_3pers, wohnkosten_fam_4pers, wohnkosten_fam_5pluspers,
                           wohnkosten_persoenlich_1pers, wohnkosten_persoenlich_2pers, wohnkosten_persoenlich_3pers,
                           wohnkosten_persoenlich_4pers, wohnkosten_persoenlich_5pluspers, gueltigkeit_status)
VALUES ('752140e1-6300-4177-b7df-0979c72cc21e', now(), now(), 'Migration', 'Migration', 0, 'bern', 'Herbst 2023/24',
        'Automne 2023/24', '2023', '9596bb3e-10ea-4493-8aed-a6ef510f806b', '2023-08-01', '2024-06-30', '2023-01-01',
        '2023-12-31', '2023-07-31', '2023-12-31', 2000, 3000, 0, 6000, 6000, 50, 30000, 15, 2400, 13200, 500, 11724,
        17940, 21816, 25080, 28368, 31656, 34944, 3288, 1400, 4600, 5400, 10009, 13536, 16260, 19932, 25260, 13536,
        16260, 16260, 19932, 25260, 'PUBLIZIERT');
