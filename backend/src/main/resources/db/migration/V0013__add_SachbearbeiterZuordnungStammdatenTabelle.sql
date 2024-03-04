CREATE TABLE sachbearbeiter_zuordnung_stammdaten
(
  id                 UUID         NOT NULL,
  timestamp_erstellt TIMESTAMP    NOT NULL,
  timestamp_mutiert  TIMESTAMP    NOT NULL,
  user_erstellt      VARCHAR(255) NOT NULL,
  user_mutiert       VARCHAR(255) NOT NULL,
  version            BIGINT       NOT NULL,
  buchstaben_de      VARCHAR(255),
  buchstaben_fr      VARCHAR(255),
  benutzer_id        UUID         NOT NULL,
  mandant            VARCHAR(255) NOT NULL,
  CONSTRAINT sachbearbeiter_zuordnung_stammdaten_pk PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS IX_sachbearbeiter_zuordnung_stammdaten_benutzer_id ON sachbearbeiter_zuordnung_stammdaten (benutzer_id);

CREATE INDEX IF NOT EXISTS IX_sachbearbeiter_zuordnung_stammdaten_mandant ON sachbearbeiter_zuordnung_stammdaten (mandant);

ALTER TABLE sachbearbeiter_zuordnung_stammdaten
  ADD CONSTRAINT FK_sachbearbeiter_zuordnung_stammdaten_benutzer_id
    FOREIGN KEY (benutzer_id)
      REFERENCES benutzer (id);

CREATE TABLE sachbearbeiter_zuordnung_stammdaten_aud
(
  id                 UUID    NOT NULL,
  rev                INTEGER NOT NULL,
  revtype            SMALLINT,
  timestamp_erstellt TIMESTAMP,
  timestamp_mutiert  TIMESTAMP,
  user_erstellt      VARCHAR(255),
  user_mutiert       VARCHAR(255),
  version            BIGINT,
  buchstaben_de      VARCHAR(255),
  buchstaben_fr      VARCHAR(255),
  benutzer_id        UUID,
  mandant            VARCHAR(255),
  CONSTRAINT sachbearbeiter_zuordnung_stammdaten_aud_pk PRIMARY KEY (id, rev)
);

ALTER TABLE sachbearbeiter_zuordnung_stammdaten_aud
  ADD CONSTRAINT FK_sachbearbeiter_zuordnung_stammdaten_aud_revinfo
    FOREIGN KEY (rev)
      REFERENCES revinfo (rev);

ALTER TABLE benutzer
  ADD COLUMN benutzer_typ VARCHAR(255) NOT NULL DEFAULT 'GESUCHSTELLER';

ALTER TABLE benutzer_aud
  ADD COLUMN benutzer_typ VARCHAR(255);
