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
VALUES ('3fa85f64-5717-4562-b3fc-2c963f66afa6', '2023-05-31 08:35:52', '2023-05-30 08:35:43', 'test', 'test', 0,
        '2023-08-01', '2024-06-30', '2023-12-31', '2023-07-01');

INSERT INTO gesuchsperiode (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version,
                                 gueltig_ab, gueltig_bis, einreichfrist, aufschaltdatum)
VALUES ('3fa85f64-5717-4562-b3fc-2c963f66afa5', '2023-05-31 08:35:52', '2023-05-30 08:35:43', 'test', 'test', 0,
        '2022-12-01', '2023-10-31', '2023-06-30', '2022-11-01');
