alter table ausbildung
  drop column ausbildungsstaette_id;
alter table ausbildung
  drop column ausbildungsland;
alter table ausbildung_aud
  drop column ausbildungsstaette_id;
alter table ausbildung_aud
  drop column ausbildungsland;

alter table ausbildungsstaette
  add column name_de VARCHAR(255);
alter table ausbildungsstaette_aud
  add column name_de VARCHAR(255);
alter table ausbildungsstaette
  add column name_fr VARCHAR(255);
alter table ausbildungsstaette_aud
  add column name_fr VARCHAR(255);

update ausbildungsstaette
set name_de = name;
update ausbildungsstaette
set name_fr = name;

alter table ausbildungsstaette
  drop column name;
alter table ausbildungsstaette_aud
  drop column name;

alter table ausbildungsstaette
  drop column ausbildungsland;
alter table ausbildungsstaette_aud
  drop column ausbildungsland;

alter table ausbildungsstaette
  alter column name_de set not null;
alter table ausbildungsstaette
  alter column name_fr set not null;

alter table ausbildungsgang
  add column ausbildungsort varchar(255) not null default 'BERN';
alter table ausbildungsgang_aud
  add column ausbildungsort varchar(255);
alter table ausbildungsgang
  add column mandant varchar(255) not null default 'bern';
alter table ausbildungsgang_aud
  add column mandant varchar(255);

CREATE INDEX IF NOT EXISTS IX_ausbildungsgang_mandant ON ausbildungsgang (mandant);

-- ein Ausbildungsgang und eine Ausbildungsstätte hinzufügen
insert into ausbildungsstaette (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version,
                                name_de, name_fr)
VALUES ('a65d0b6a-4d76-11ee-be56-0242ac120002', now(), now(), 'Admin', 'Admin', 0, 'Universtität Bern',
        'Université de Berne');

insert into ausbildungsgang (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version,
                             bezeichnung_de, bezeichnung_fr, ausbildungsstaette_id, ausbildungsort, ausbildungsrichtung)
VALUES ('53fdb576-4d77-11ee-be56-0242ac120002', now(), now(), 'Admin', 'Admin', 0, 'Bachelor', 'Bachelor',
        'a65d0b6a-4d76-11ee-be56-0242ac120002', 'BERN', 'UNIVERSITAETEN_ETH');

-- alle Bestehenden Ausbildungen an die neue Ausbildungsstätte umhängen
update ausbildung
set ausbildungsgang_id = '53fdb576-4d77-11ee-be56-0242ac120002';

-- alle alten Ausbildungsstätten und Ausbildungsgänge aus der Datenbank löschen
delete
from ausbildungsgang
where id not in ('53fdb576-4d77-11ee-be56-0242ac120002');
delete
from ausbildungsstaette
where id not in ('a65d0b6a-4d76-11ee-be56-0242ac120002');

-- insert new ausbildungsgaenge and stätten
insert into ausbildungsstaette (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version,
                                name_de, name_fr)
VALUES ('b15d0b6a-4d76-11ee-be56-0242ac120002', now(), now(), 'Admin', 'Admin', 0, 'Kantonsschule Solothurn',
        'Ecole cantonale, Soleure');

insert into ausbildungsstaette (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version,
                                name_de, name_fr)
VALUES ('85ae0cc0-4d7b-11ee-be56-0242ac120002', now(), now(), 'Admin', 'Admin', 0, 'Campus Muristalden Bern',
        'Campus Muristalden Bern');

insert into ausbildungsstaette (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version,
                                name_de, name_fr)
VALUES ('c84d0b6a-4d76-11ee-be56-0242ac120002', now(), now(), 'Admin', 'Admin', 0, 'Handelsmittelschule Ilanz',
        'Ecole de commerce Ilanz');

insert into ausbildungsstaette (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version,
                                name_de, name_fr)
VALUES ('2363e76e-4d7c-11ee-be56-0242ac120002', now(), now(), 'Admin', 'Admin', 0, 'University of Tokyo',
        'University of Tokyo');

insert into ausbildungsstaette (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version,
                                name_de, name_fr)
VALUES ('28bdbc30-4d7c-11ee-be56-0242ac120002', now(), now(), 'Admin', 'Admin', 0, 'University of West London',
        'University of West London');

insert into ausbildungsstaette (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version,
                                name_de, name_fr)
VALUES ('2bfde438-4d7c-11ee-be56-0242ac120002', now(), now(), 'Admin', 'Admin', 0, 'University of Westminster',
        'University of Westminster');

insert into ausbildungsstaette (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version,
                                name_de, name_fr)
VALUES ('310c7c6e-4d7c-11ee-be56-0242ac120002', now(), now(), 'Admin', 'Admin', 0, 'Uppsala Universitet',
        'Uppsala Universitet');

insert into ausbildungsstaette (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version,
                                name_de, name_fr)
VALUES ('d3279a68-4d83-11ee-be56-0242ac120002', now(), now(), 'Admin', 'Admin', 0, 'TEKO Schweizerische Fachschule ',
        'TEKO Schweizerische Fachschule');

insert into ausbildungsgang (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version,
                             bezeichnung_de, bezeichnung_fr, ausbildungsstaette_id, ausbildungsort, ausbildungsrichtung)
VALUES ('daa23db6-4d83-11ee-be56-0242ac120002', now(), now(), 'Admin', 'Admin', 0, 'Master (konsekutiv)',
        'Master (consécutif)', 'a65d0b6a-4d76-11ee-be56-0242ac120002', 'BERN', 'UNIVERSITAETEN_ETH');

insert into ausbildungsgang (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version,
                             bezeichnung_de, bezeichnung_fr, ausbildungsstaette_id, ausbildungsort, ausbildungsrichtung)
VALUES ('dfbe424a-4d83-11ee-be56-0242ac120002', now(), now(), 'Admin', 'Admin', 0, 'Maturität', 'Maturité',
        'a65d0b6a-4d76-11ee-be56-0242ac120002', 'SOLOTHURN', 'GYMNASIALE_MATURITAETSSCHULEN');

insert into ausbildungsgang (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version,
                             bezeichnung_de, bezeichnung_fr, ausbildungsstaette_id, ausbildungsort, ausbildungsrichtung)
VALUES ('e4f5ee2a-4d83-11ee-be56-0242ac120002', now(), now(), 'Admin', 'Admin', 0, '10. Schuljahr',
        '10eme année scolaire', '85ae0cc0-4d7b-11ee-be56-0242ac120002', 'BERN', 'SCHULEN_FUER_ALLGEMEINBILDUNG');

insert into ausbildungsgang (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version,
                             bezeichnung_de, bezeichnung_fr, ausbildungsstaette_id, ausbildungsort, ausbildungsrichtung)
VALUES ('e8d2c6ee-4d83-11ee-be56-0242ac120002', now(), now(), 'Admin', 'Admin', 0, 'Berufsvorbereitendes Schuljahr',
        'Préparations professionnelles', '85ae0cc0-4d7b-11ee-be56-0242ac120002', 'BERN',
        'SCHULEN_FUER_ALLGEMEINBILDUNG');

insert into ausbildungsgang (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version,
                             bezeichnung_de, bezeichnung_fr, ausbildungsstaette_id, ausbildungsort, ausbildungsrichtung)
VALUES ('ebc81ade-4d83-11ee-be56-0242ac120002', now(), now(), 'Admin', 'Admin', 0, 'Eidg. Handelsdiplom / EFZ',
        'Diplôme commerciale fédéral / CFC', 'a65d0b6a-4d76-11ee-be56-0242ac120002', 'ILANZ', 'VOLLZEITBERUFSSCHULEN');

insert into ausbildungsgang (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version,
                             bezeichnung_de, bezeichnung_fr, ausbildungsstaette_id, ausbildungsort, ausbildungsrichtung)
VALUES ('f198b52c-4d83-11ee-be56-0242ac120002', now(), now(), 'Admin', 'Admin', 0, 'Bachelor', 'Bachelor',
        '2363e76e-4d7c-11ee-be56-0242ac120002', 'AUSLAND', 'UNIVERSITAETEN_ETH');

insert into ausbildungsgang (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version,
                             bezeichnung_de, bezeichnung_fr, ausbildungsstaette_id, ausbildungsort, ausbildungsrichtung)
VALUES ('f5c993d2-4d83-11ee-be56-0242ac120002', now(), now(), 'Admin', 'Admin', 0, 'BA Tourism', 'BA Tourism',
        '28bdbc30-4d7c-11ee-be56-0242ac120002', 'AUSLAND', 'UNIVERSITAETEN_ETH');

insert into ausbildungsgang (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version,
                             bezeichnung_de, bezeichnung_fr, ausbildungsstaette_id, ausbildungsort, ausbildungsrichtung)
VALUES ('f9f0c660-4d83-11ee-be56-0242ac120002', now(), now(), 'Admin', 'Admin', 0, 'Master of Music', 'Master of Music',
        '28bdbc30-4d7c-11ee-be56-0242ac120002', 'AUSLAND', 'UNIVERSITAETEN_ETH');

insert into ausbildungsgang (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version,
                             bezeichnung_de, bezeichnung_fr, ausbildungsstaette_id, ausbildungsort, ausbildungsrichtung)
VALUES ('fea4f5aa-4d83-11ee-be56-0242ac120002', now(), now(), 'Admin', 'Admin', 0, 'Master', 'Master',
        '2bfde438-4d7c-11ee-be56-0242ac120002', 'AUSLAND', 'UNIVERSITAETEN_ETH');

insert into ausbildungsgang (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version,
                             bezeichnung_de, bezeichnung_fr, ausbildungsstaette_id, ausbildungsort, ausbildungsrichtung)
VALUES ('03ccfa96-4d84-11ee-be56-0242ac120002', now(), now(), 'Admin', 'Admin', 0, 'Bachelor', 'Bachelor',
        '310c7c6e-4d7c-11ee-be56-0242ac120002', 'AUSLAND', 'UNIVERSITAETEN_ETH');

insert into ausbildungsgang (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version,
                             bezeichnung_de, bezeichnung_fr, ausbildungsstaette_id, ausbildungsort, ausbildungsrichtung)
VALUES ('083ed0ea-4d84-11ee-be56-0242ac120002', now(), now(), 'Admin', 'Admin', 0, 'Master (konsekutiv)',
        'Master (consécutif)', '310c7c6e-4d7c-11ee-be56-0242ac120002', 'AUSLAND', 'UNIVERSITAETEN_ETH');

insert into ausbildungsgang (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version,
                             bezeichnung_de, bezeichnung_fr, ausbildungsstaette_id, ausbildungsort, ausbildungsrichtung)
VALUES ('0c5aec68-4d84-11ee-be56-0242ac120002', now(), now(), 'Admin', 'Admin', 0,
        'Dipl. Techniker/in HF Elektrotechnik', 'Dipl. Techniker/in HF Elektrotechnik',
        'd3279a68-4d83-11ee-be56-0242ac120002', 'BERN', 'HOEHERE_BERUFSBILDUNG');

