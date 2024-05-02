CREATE TABLE zuordnung (
  id                 UUID         NOT NULL,
  timestamp_erstellt TIMESTAMP    NOT NULL,
  timestamp_mutiert  TIMESTAMP    NOT NULL,
  user_erstellt      VARCHAR(255) NOT NULL,
  user_mutiert       VARCHAR(255) NOT NULL,
  version            BIGINT       NOT NULL,
  mandant            VARCHAR(255) NOT NULL,
  fall_id            UUID         NOT NULL,
  sachbearbeiter_id  UUID         NOT NULL,
  zuordnung_type     VARCHAR(255) NOT NULL,
  CONSTRAINT zuordnung_pk PRIMARY KEY (id)
);

ALTER TABLE zuordnung
  ADD CONSTRAINT FK_zuordnung_fall_id
    FOREIGN KEY (fall_id)
      REFERENCES fall(id);

ALTER TABLE zuordnung
  ADD CONSTRAINT FK_zuordnung_sachbearbeiter_id
    FOREIGN KEY (sachbearbeiter_id)
      REFERENCES benutzer(id);

CREATE TABLE zuordnung_aud (
  id                 UUID    NOT NULL,
  rev                INTEGER NOT NULL,
  revtype            SMALLINT,
  timestamp_erstellt TIMESTAMP,
  timestamp_mutiert  TIMESTAMP,
  user_erstellt      VARCHAR(255),
  user_mutiert       VARCHAR(255),
  mandant            VARCHAR(255),
  fall_id            UUID,
  sachbearbeiter_id  UUID,
  zuordnung_type     VARCHAR(255)
);

ALTER TABLE zuordnung_aud
  ADD CONSTRAINT FK_zuordnung_aud_revinfo
    FOREIGN KEY (rev)
      REFERENCES revinfo(rev);

ALTER TABLE fall
  DROP COLUMN sachbearbeiter_id;
