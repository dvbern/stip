/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

INSERT INTO gesuchsperiode (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version,
                                 gueltig_ab, gueltig_bis, einreichfrist, aufschaltdatum)
VALUES ('3fa85f64-5717-4562-b3fc-2c963f66afa6', '2023-05-31 08:35:52', '2023-05-30 08:35:43', 'Admin', 'Admin', 0,
        '2023-08-01', '2024-06-30', '2023-12-31', '2023-07-01');

INSERT INTO gesuchsperiode (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version,
                                 gueltig_ab, gueltig_bis, einreichfrist, aufschaltdatum)
VALUES ('3fa85f64-5717-4562-b3fc-2c963f66afa5', '2023-05-31 08:35:52', '2023-05-30 08:35:43', 'Admin', 'Admin', 0,
        '2022-12-01', '2023-10-31', '2023-06-30', '2022-11-01');

INSERT INTO ausbildungstaette (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, name, ausbildungsland) VALUES ('9477487f-3ac4-4d02-b57c-e0cefb292ae5', '2023-06-12 15:26:47.000000', '2023-06-12 15:26:50.000000', 'Admin', 'Admin', 0, 'Universtität Bern', 'SCHWEIZ');
INSERT INTO ausbildungstaette (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, name, ausbildungsland) VALUES ('71c1e040-6ed3-4ce6-b90f-176b173c9c20', '2023-06-12 15:26:47.000000', '2023-06-12 15:26:50.000000', 'Admin', 'Admin', 0, 'Universtität Basel', 'SCHWEIZ');
INSERT INTO ausbildungstaette (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, name, ausbildungsland) VALUES ('fdd81cd3-0d93-464a-b5e8-ef4496c9f633', '2023-06-12 15:26:47.000000', '2023-06-12 15:26:50.000000', 'Admin', 'Admin', 0, 'Gymnasium Interlaken', 'SCHWEIZ');
INSERT INTO ausbildungstaette (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, name, ausbildungsland) VALUES ('d843ba46-2b0c-4c83-997a-2ac80fab303b', '2023-06-12 15:26:47.000000', '2023-06-12 15:26:50.000000', 'Admin', 'Admin', 0, 'Haarakademie', 'SCHWEIZ');
INSERT INTO ausbildungstaette (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, name, ausbildungsland) VALUES ('92fc250e-bcf3-49f5-af20-0854376e8d79', '2023-06-12 15:26:47.000000', '2023-06-12 15:26:50.000000', 'Admin', 'Admin', 0, 'GIBB Bern', 'SCHWEIZ');
INSERT INTO ausbildungstaette (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, name, ausbildungsland) VALUES ('0f9820db-b7b3-43af-8dae-15af9601ac10', '2023-06-12 15:26:47.000000', '2023-06-12 15:26:50.000000', 'Admin', 'Admin', 0, 'Desingskolen Kolding', 'AUSLAND');
INSERT INTO ausbildungstaette (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, name, ausbildungsland) VALUES ('8da6325b-d65d-4dd2-87ce-f10cab6fb81c', '2023-06-12 15:26:47.000000', '2023-06-12 15:26:50.000000', 'Admin', 'Admin', 0, 'Hochschule für Musik und Theater Leipzig', 'AUSLAND');

INSERT INTO ausbildungsgang (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, bezeichnung_de, bezeichnung_fr, ausbildungstaette_id) VALUES ('3a8c2023-f29e-4466-a2d7-411a7d032f42', '2023-06-12 15:38:10.000000', '2023-06-12 15:38:06.000000', 'Admin', 'Admin', 0, 'Bachelor', 'Bachelor', '9477487f-3ac4-4d02-b57c-e0cefb292ae5');
INSERT INTO ausbildungsgang (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, bezeichnung_de, bezeichnung_fr, ausbildungstaette_id) VALUES ('384ba33a-eb4e-44e4-ac20-8e11101bbf3a', '2023-06-12 15:38:10.000000', '2023-06-12 15:38:06.000000', 'Admin', 'Admin', 0, 'Master', 'Master', '9477487f-3ac4-4d02-b57c-e0cefb292ae5');
INSERT INTO ausbildungsgang (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, bezeichnung_de, bezeichnung_fr, ausbildungstaette_id) VALUES ('b18d06fb-1dd4-4929-8470-e9e44b0bc4a7', '2023-06-12 15:38:10.000000', '2023-06-12 15:38:06.000000', 'Admin', 'Admin', 0, 'Master', 'Master', '71c1e040-6ed3-4ce6-b90f-176b173c9c20');
INSERT INTO ausbildungsgang (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, bezeichnung_de, bezeichnung_fr, ausbildungstaette_id) VALUES ('b319953d-f5a9-4a1a-a0f6-69bd3f745e02', '2023-06-12 15:38:10.000000', '2023-06-12 15:38:06.000000', 'Admin', 'Admin', 0, 'Bachelor', 'Bachelor', '71c1e040-6ed3-4ce6-b90f-176b173c9c20');
INSERT INTO ausbildungsgang (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, bezeichnung_de, bezeichnung_fr, ausbildungstaette_id) VALUES ('345ef0b1-b62e-4fdd-abf4-cb789d4296ee', '2023-06-12 15:38:10.000000', '2023-06-12 15:38:06.000000', 'Admin', 'Admin', 0, 'Maturität', 'Maturität', 'fdd81cd3-0d93-464a-b5e8-ef4496c9f633');
INSERT INTO ausbildungsgang (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, bezeichnung_de, bezeichnung_fr, ausbildungstaette_id) VALUES ('44779764-e6d6-455c-8667-0d0b40f05dd9', '2023-06-12 15:38:10.000000', '2023-06-12 15:38:06.000000', 'Admin', 'Admin', 0, 'Berufsvorbereitendes Schuljahr', 'Berufsvorbereitendes Schuljahr', 'd843ba46-2b0c-4c83-997a-2ac80fab303b');
INSERT INTO ausbildungsgang (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, bezeichnung_de, bezeichnung_fr, ausbildungstaette_id) VALUES ('abc07eed-19d4-4e1e-9899-6d5893556b1b', '2023-06-12 15:38:10.000000', '2023-06-12 15:38:06.000000', 'Admin', 'Admin', 0, 'Berufsmaturität', 'Berufsmaturität', '92fc250e-bcf3-49f5-af20-0854376e8d79');
INSERT INTO ausbildungsgang (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, bezeichnung_de, bezeichnung_fr, ausbildungstaette_id) VALUES ('52e9577c-732a-4f1b-ae74-de2e477c01e9', '2023-06-12 15:38:10.000000', '2023-06-12 15:38:06.000000', 'Admin', 'Admin', 0, 'Bachelor', 'Bachelor', '0f9820db-b7b3-43af-8dae-15af9601ac10');
INSERT INTO ausbildungsgang (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, bezeichnung_de, bezeichnung_fr, ausbildungstaette_id) VALUES ('3dac8f59-533c-4b99-9b13-210503c34595', '2023-06-12 15:38:10.000000', '2023-06-12 15:38:06.000000', 'Admin', 'Admin', 0, 'Bachelor', 'Bachelor', '8da6325b-d65d-4dd2-87ce-f10cab6fb81c');
INSERT INTO ausbildungsgang (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, bezeichnung_de, bezeichnung_fr, ausbildungstaette_id) VALUES ('c69c6712-9bd9-4533-a685-44fddefd5937', '2023-06-12 15:38:10.000000', '2023-06-12 15:38:06.000000', 'Admin', 'Admin', 0, 'Master', 'Master', '8da6325b-d65d-4dd2-87ce-f10cab6fb81c');
