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

CREATE TABLE revinfo
(
    rev      SERIAL PRIMARY KEY,
    revtstmp BIGINT
);

ALTER sequence revinfo_rev_seq RENAME TO revinfo_seq;
ALTER sequence revinfo_seq start with 1 increment by 50;

CREATE TABLE fall
(
    id                 UUID         NOT NULL,
    timestamp_erstellt TIMESTAMP    NOT NULL,
    timestamp_mutiert  TIMESTAMP    NOT NULL,
    user_erstellt      VARCHAR(255) NOT NULL,
    user_mutiert       VARCHAR(255) NOT NULL,
    version            BIGINT       NOT NULL,
    fall_nummer        BIGINT       NOT NULL,
    mandant            VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE fall_aud
(
    id                 UUID    NOT NULL,
    rev                INTEGER NOT NULL,
    revtype            SMALLINT,
    timestamp_erstellt TIMESTAMP,
    timestamp_mutiert  TIMESTAMP,
    user_erstellt      VARCHAR(255),
    user_mutiert       VARCHAR(255),
    fall_nummer        BIGINT,
    mandant            VARCHAR(255),
    PRIMARY KEY (id, rev)
);

ALTER TABLE fall_aud
    ADD CONSTRAINT FK_fall_aud_revinfo
        FOREIGN KEY (rev)
            REFERENCES revinfo (rev);

CREATE TABLE gesuchsperiode
(
    id                 UUID         NOT NULL,
    timestamp_erstellt TIMESTAMP    NOT NULL,
    timestamp_mutiert  TIMESTAMP    NOT NULL,
    user_erstellt      VARCHAR(255) NOT NULL,
    user_mutiert       VARCHAR(255) NOT NULL,
    version            BIGINT       NOT NULL,
    gueltig_ab         DATE         NOT NULL,
    gueltig_bis        DATE         NOT NULL,
    einreichfrist      DATE,
    aufschaltdatum     DATE,
    PRIMARY KEY (id)
);

CREATE TABLE gesuchsperiode_aud
(
    id                 UUID    NOT NULL,
    rev                INTEGER NOT NULL,
    revtype            SMALLINT,
    timestamp_erstellt TIMESTAMP,
    timestamp_mutiert  TIMESTAMP,
    user_erstellt      VARCHAR(255),
    user_mutiert       VARCHAR(255),
    gueltig_ab         DATE,
    gueltig_bis        DATE,
    einreichfrist      DATE,
    aufschaltdatum     DATE,
    PRIMARY KEY (id, rev)
);

ALTER TABLE gesuchsperiode_aud
    ADD CONSTRAINT FK_gesuchsperiode_aud_revinfo
        FOREIGN KEY (rev)
            REFERENCES revinfo (rev);

CREATE TABLE adresse
(
    id                 UUID         NOT NULL,
    timestamp_erstellt TIMESTAMP    NOT NULL,
    timestamp_mutiert  TIMESTAMP    NOT NULL,
    user_erstellt      VARCHAR(255) NOT NULL,
    user_mutiert       VARCHAR(255) NOT NULL,
    version            BIGINT       NOT NULL,
    co_adresse         VARCHAR(255),
    hausnummer         VARCHAR(255),
    land               VARCHAR(255) NOT NULL,
    organisation       VARCHAR(255),
    ort                VARCHAR(255) NOT NULL,
    plz                VARCHAR(255) NOT NULL,
    strasse            VARCHAR(255) NOT NULL,
    CONSTRAINT adresse_pk PRIMARY KEY (id) /* uberall name geben */
);

CREATE TABLE adresse_aud
(
    id                 UUID    NOT NULL,
    rev                INTEGER NOT NULL,
    revtype            SMALLINT,
    timestamp_erstellt TIMESTAMP,
    timestamp_mutiert  TIMESTAMP,
    user_erstellt      VARCHAR(255),
    user_mutiert       VARCHAR(255),
    co_adresse         VARCHAR(255),
    hausnummer         VARCHAR(255),
    land               VARCHAR(255),
    organisation       VARCHAR(255),
    ort                VARCHAR(255),
    plz                VARCHAR(255),
    strasse            VARCHAR(255),
    PRIMARY KEY (id, rev)
);

ALTER TABLE adresse_aud
    ADD CONSTRAINT FK_adresse_aud_revinfo
        FOREIGN KEY (rev)
            REFERENCES revinfo (rev);

CREATE TABLE person_in_ausbildung
(
    id                                    UUID         NOT NULL,
    timestamp_erstellt                    TIMESTAMP    NOT NULL,
    timestamp_mutiert                     TIMESTAMP    NOT NULL,
    user_erstellt                         VARCHAR(255) NOT NULL,
    user_mutiert                          VARCHAR(255) NOT NULL,
    version                               BIGINT       NOT NULL,
    adresse_id                            UUID         NOT NULL,
    sozialversicherungsnummer             VARCHAR(255) NOT NULL,
    anrede                                VARCHAR(255) NOT NULL,
    name                                  VARCHAR(255) NOT NULL,
    vorname                               VARCHAR(255) NOT NULL,
    identischer_zivilrechtlicher_wohnsitz BOOLEAN      NOT NULL,
    izv_ort                               VARCHAR(255),
    izv_plz                               VARCHAR(255),
    email                                 VARCHAR(255) NOT NULL,
    telefonnummer                         VARCHAR(255) NOT NULL,
    geburtsdatum                          DATE         NOT NULL,
    nationalitaet                         VARCHAR(255) NOT NULL DEFAULT 'CH',
    heimatort                             VARCHAR(255),
    niederlassungsstatus                  VARCHAR(255),
    zivilstand                            VARCHAR(255),
    wohnsitz                              VARCHAR(255) NOT NULL,
    sozialhilfebeitraege                  BOOLEAN      NOT NULL,
    quellenbesteuert                      BOOLEAN      NOT NULL,
    kinder                                BOOLEAN      NOT NULL,
    digitale_kommunikation                BOOLEAN      NOT NULL DEFAULT TRUE,
    vorgaenger_id                         UUID,
    PRIMARY KEY (id)
);

ALTER TABLE person_in_ausbildung
    ADD CONSTRAINT FK_person_in_ausbildung_adresse_id
        FOREIGN KEY (adresse_id)
            REFERENCES adresse (id);

CREATE TABLE person_in_ausbildung_aud
(
    id                                    UUID    NOT NULL,
    rev                                   INTEGER NOT NULL,
    revtype                               SMALLINT,
    timestamp_erstellt                    TIMESTAMP,
    timestamp_mutiert                     TIMESTAMP,
    user_erstellt                         VARCHAR(255),
    user_mutiert                          VARCHAR(255),
    version                               BIGINT,
    adresse_id                            UUID,
    sozialversicherungsnummer             VARCHAR(255),
    anrede                                VARCHAR(255),
    name                                  VARCHAR(255),
    vorname                               VARCHAR(255),
    identischer_zivilrechtlicher_wohnsitz BOOLEAN,
    izv_ort                               VARCHAR(255),
    izv_plz                               VARCHAR(255),
    email                                 VARCHAR(255),
    telefonnummer                         VARCHAR(255),
    geburtsdatum                          DATE,
    nationalitaet                         VARCHAR(255),
    heimatort                             VARCHAR(255),
    niederlassungsstatus                  VARCHAR(255),
    zivilstand                            VARCHAR(255),
    wohnsitz                              VARCHAR(255),
    sozialhilfebeitraege                  BOOLEAN,
    quellenbesteuert                      BOOLEAN,
    kinder                                BOOLEAN,
    digitale_kommunikation                BOOLEAN,
    vorgaenger_id                         UUID,
    PRIMARY KEY (id)
);

ALTER TABLE person_in_ausbildung_aud
    ADD CONSTRAINT FK_person_in_ausbildung_aud_revinfo
        FOREIGN KEY (rev)
            REFERENCES revinfo (rev);

CREATE TABLE person_in_ausbildung_container
(
    id                         UUID         NOT NULL,
    timestamp_erstellt         TIMESTAMP    NOT NULL,
    timestamp_mutiert          TIMESTAMP    NOT NULL,
    user_erstellt              VARCHAR(255) NOT NULL,
    user_mutiert               VARCHAR(255) NOT NULL,
    version                    BIGINT       NOT NULL,
    person_in_ausbildung_gs_id UUID,
    person_in_ausbildung_sb_id UUID,
    vorgaenger_id              UUID,
    PRIMARY KEY (id)
);

ALTER TABLE person_in_ausbildung_container
    ADD CONSTRAINT FK_person_in_ausbildung_container_person_in_ausbildung_gs_id
        FOREIGN KEY (person_in_ausbildung_gs_id)
            REFERENCES person_in_ausbildung (id);

ALTER TABLE person_in_ausbildung_container
    ADD CONSTRAINT FK_person_in_ausbildung_container_person_in_ausbildung_sb_id
        FOREIGN KEY (person_in_ausbildung_sb_id)
            REFERENCES person_in_ausbildung (id);

CREATE TABLE person_in_ausbildung_container_aud
(
    id                         UUID    NOT NULL,
    rev                        INTEGER NOT NULL,
    revtype                    SMALLINT,
    timestamp_erstellt         TIMESTAMP,
    timestamp_mutiert          TIMESTAMP,
    user_erstellt              VARCHAR(255),
    user_mutiert               VARCHAR(255),
    version                    BIGINT,
    person_in_ausbildung_gs_id UUID,
    person_in_ausbildung_sb_id UUID,
    vorgaenger_id              UUID,
    PRIMARY KEY (id)
);

ALTER TABLE person_in_ausbildung_container_aud
    ADD CONSTRAINT FK_person_in_ausbildung_container_aud_revinfo
        FOREIGN KEY (rev)
            REFERENCES revinfo (rev);

CREATE TABLE gesuch
(
    id                                UUID         NOT NULL,
    timestamp_erstellt                TIMESTAMP    NOT NULL,
    timestamp_mutiert                 TIMESTAMP    NOT NULL,
    user_erstellt                     VARCHAR(255) NOT NULL,
    user_mutiert                      VARCHAR(255) NOT NULL,
    version                           BIGINT       NOT NULL,
    vorgaenger_id                     UUID,
    gesuch_nummer                     INTEGER      NOT NULL,
    gesuch_status                     VARCHAR(255) NOT NULL,
    gesuch_status_aenderung_datum     TIMESTAMP    NOT NULL,
    gesuchsperiode_id                 UUID         NOT NULL,
    fall_id                           UUID         NOT NULL,
    person_in_ausbildung_container_id UUID,
    PRIMARY KEY (id)
);

ALTER TABLE gesuch
    ADD CONSTRAINT FK_gesuch_gesuchsperiode_id
        FOREIGN KEY (gesuchsperiode_id)
            REFERENCES gesuchsperiode (id);

ALTER TABLE gesuch
    ADD CONSTRAINT FK_gesuch_fall_id
        FOREIGN KEY (fall_id)
            REFERENCES fall (id);

ALTER TABLE gesuch
    ADD CONSTRAINT FK_gesuch_person_in_ausbildung_container_id
        FOREIGN KEY (person_in_ausbildung_container_id)
            REFERENCES person_in_ausbildung_container (id);

CREATE TABLE gesuch_aud
(
    id                                UUID    NOT NULL,
    rev                               INTEGER NOT NULL,
    revtype                           SMALLINT,
    timestamp_erstellt                TIMESTAMP,
    timestamp_mutiert                 TIMESTAMP,
    user_erstellt                     VARCHAR(255),
    user_mutiert                      VARCHAR(255),
    vorgaenger_id                     UUID,
    gesuch_nummer                     INTEGER,
    gesuch_status                     VARCHAR(255),
    gesuch_status_aenderung_datum     TIMESTAMP,
    gesuchsperiode_id                 UUID,
    fall_id                           UUID,
    person_in_ausbildung_container_id UUID,
    PRIMARY KEY (id, rev)
);

ALTER TABLE gesuch_aud
    ADD CONSTRAINT FK_gesuch_aud_revinfo
        FOREIGN KEY (rev)
            REFERENCES revinfo (rev);

CREATE TABLE eltern
(
    id                              UUID         NOT NULL,
    timestamp_erstellt              TIMESTAMP    NOT NULL,
    timestamp_mutiert               TIMESTAMP    NOT NULL,
    user_erstellt                   VARCHAR(255) NOT NULL,
    user_mutiert                    VARCHAR(255) NOT NULL,
    version                         BIGINT       NOT NULL,
    geschlecht                      VARCHAR(255) NOT NULL,
    name                            VARCHAR(255) NOT NULL,
    vorname                         VARCHAR(255) NOT NULL,
    sozialversicherungsnummer       VARCHAR(255) NOT NULL,
    telefonnummer                   VARCHAR(255) NOT NULL,
    geburtsdatum                    DATE         NOT NULL,
    sozialhilfebeitraege_ausbezahlt BOOLEAN      NOT NULL,
    ausweisb_fluechtling            BOOLEAN      NOT NULL,
    ergaenzungsleistung_ausbezahlt  BOOLEAN      NOT NULL,
    vorgaenger_id                   UUID,
    PRIMARY KEY (id)
);

CREATE TABLE eltern_aud
(
    id                              UUID    NOT NULL,
    rev                             INTEGER NOT NULL,
    revtype                         SMALLINT,
    timestamp_erstellt              TIMESTAMP,
    timestamp_mutiert               TIMESTAMP,
    user_erstellt                   VARCHAR(255),
    user_mutiert                    VARCHAR(255),
    version                         BIGINT,
    geschlecht                      VARCHAR(255),
    name                            VARCHAR(255),
    vorname                         VARCHAR(255),
    sozialversicherungsnummer       VARCHAR(255),
    telefonnummer                   VARCHAR(255),
    geburtsdatum                    DATE,
    sozialhilfebeitraege_ausbezahlt BOOLEAN,
    ausweisb_fluechtling            BOOLEAN,
    ergaenzungsleistung_ausbezahlt  BOOLEAN,
    vorgaenger_id                   UUID,
    PRIMARY KEY (id)
);

ALTER TABLE eltern_aud
    ADD CONSTRAINT FK_eltern_aud_revinfo
        FOREIGN KEY (rev)
            REFERENCES revinfo (rev);

CREATE TABLE eltern_container
(
    id                 UUID         NOT NULL,
    timestamp_erstellt TIMESTAMP    NOT NULL,
    timestamp_mutiert  TIMESTAMP    NOT NULL,
    user_erstellt      VARCHAR(255) NOT NULL,
    user_mutiert       VARCHAR(255) NOT NULL,
    version            BIGINT       NOT NULL,
    gesuch_id          UUID         NOT NULL,
    eltern_gs_id       UUID,
    eltern_sb_id       UUID,
    vorgaenger_id      UUID,
    PRIMARY KEY (id)
);

ALTER TABLE eltern_container
    ADD CONSTRAINT FK_eltern_container_eltern_gs_id
        FOREIGN KEY (eltern_gs_id)
            REFERENCES eltern (id);

ALTER TABLE eltern_container
    ADD CONSTRAINT FK_eltern_container_eltern_sb_id
        FOREIGN KEY (eltern_sb_id)
            REFERENCES eltern (id);

ALTER TABLE eltern_container
    ADD CONSTRAINT FK_eltern_container_gesuch_id
        FOREIGN KEY (gesuch_id)
            REFERENCES gesuch (id);

CREATE TABLE eltern_container_aud
(
    id                 UUID    NOT NULL,
    rev                INTEGER NOT NULL,
    revtype            SMALLINT,
    timestamp_erstellt TIMESTAMP,
    timestamp_mutiert  TIMESTAMP,
    user_erstellt      VARCHAR(255),
    user_mutiert       VARCHAR(255),
    version            BIGINT,
    gesuch_id          UUID,
    eltern_gs_id       UUID,
    eltern_sb_id       UUID,
    vorgaenger_id      UUID,

    PRIMARY KEY (id)
);

ALTER TABLE eltern_container_aud
    ADD CONSTRAINT FK_eltern_container_aud_revinfo
        FOREIGN KEY (rev)
            REFERENCES revinfo (rev);