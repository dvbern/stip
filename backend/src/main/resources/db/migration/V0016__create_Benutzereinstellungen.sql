CREATE TABLE benutzereinstellungen (
  id                    UUID          NOT NULL,
  timestamp_erstellt    TIMESTAMP     NOT NULL,
  timestamp_mutiert     TIMESTAMP     NOT NULL,
  user_erstellt         VARCHAR(255)  NOT NULL,
  user_mutiert          VARCHAR(255)  NOT NULL,
  version               BIGINT        NOT NULL,
  benutzer_id           UUID          NOT NULL,
  digitale_kommunikation BOOLEAN      NOT NULL DEFAULT TRUE,
  mandant               VARCHAR(255)  NOT NULL,
  CONSTRAINT benutzereinstellungen_pk PRIMARY KEY (id)
);

CREATE TABLE benutzereinstellungen_aud (
  id                     UUID          NOT NULL,
  rev                    INTEGER       NOT NULL,
  revtype                SMALLINT,
  timestamp_erstellt     TIMESTAMP,
  timestamp_mutiert      TIMESTAMP,
  user_erstellt          VARCHAR(255) ,
  user_mutiert           VARCHAR(255) ,
  version                BIGINT,
  benutzer_id            UUID,
  digitale_kommunikation BOOLEAN,
  mandant                VARCHAR(255)  NOT NULL,
  CONSTRAINT benutzereinstellungen_aud_pk PRIMARY KEY (id)
);

ALTER TABLE benutzereinstellungen_aud
ADD CONSTRAINT FK_benutzereinstellungen_aud_revinfo
  FOREIGN KEY (rev) REFERENCES revinfo(rev);


WITH benutzerIds AS (
  SELECT * FROM benutzer WHERE id NOT IN (
    SELECT benutzer_id FROM benutzereinstellungen
  )
) INSERT INTO benutzereinstellungen(id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, benutzer_id, digitale_kommunikation, mandant)
SELECT id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, id, true, mandant
FROM benutzerIds;

ALTER TABLE person_in_ausbildung DROP COLUMN digitale_kommunikation;
ALTER TABLE person_in_ausbildung_aud DROP COLUMN digitale_kommunikation;
