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

CREATE TABLE benutzer (
	id                        UUID         NOT NULL,
	timestamp_erstellt        TIMESTAMP    NOT NULL,
	timestamp_mutiert         TIMESTAMP    NOT NULL,
	user_erstellt             VARCHAR(255) NOT NULL,
	user_mutiert              VARCHAR(255) NOT NULL,
	version                   BIGINT       NOT NULL,
	sozialversicherungsnummer VARCHAR(255) NOT NULL,
	nachname                  VARCHAR(255) NOT NULL,
	vorname                   VARCHAR(255) NOT NULL,
	benutzer_status           VARCHAR(255) NOT NULL,
	mandant                   VARCHAR(255) NOT NULL,
	CONSTRAINT benutzer_pk PRIMARY KEY (id)
);

CREATE TABLE benutzer_aud (
	id                        UUID    NOT NULL,
	rev                       INTEGER NOT NULL,
	revtype                   SMALLINT,
	timestamp_erstellt        TIMESTAMP,
	timestamp_mutiert         TIMESTAMP,
	user_erstellt             VARCHAR(255),
	user_mutiert              VARCHAR(255),
	version                   BIGINT,
	sozialversicherungsnummer VARCHAR(255),
	nachname                  VARCHAR(255),
	vorname                   VARCHAR(255),
	benutzer_status           VARCHAR(255),
	mandant                   VARCHAR(255),
	CONSTRAINT benutzer_aud_pk PRIMARY KEY (id, rev)
);

ALTER TABLE benutzer_aud
ADD CONSTRAINT FK_benutzer_aud_revinfo
	FOREIGN KEY (rev)
		REFERENCES revinfo(rev);


ALTER TABLE fall ADD COLUMN gesuchsteller_id UUID;
ALTER TABLE fall_aud ADD COLUMN gesuchsteller_id UUID;

ALTER TABLE fall
ADD CONSTRAINT FK_fall_gesuchsteller_id
	FOREIGN KEY (gesuchsteller_id)
		REFERENCES benutzer(id);

ALTER TABLE fall ADD COLUMN sachbearbeiter_id UUID;
ALTER TABLE fall_aud ADD COLUMN sachbearbeiter_id UUID;

ALTER TABLE fall
ADD CONSTRAINT FK_fall_sachbearbeiter_id
	FOREIGN KEY (sachbearbeiter_id)
		REFERENCES benutzer(id);