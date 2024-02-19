CREATE TABLE gesuch_tranche
(
  id                 UUID         NOT NULL,
  timestamp_erstellt TIMESTAMP    NOT NULL,
  timestamp_mutiert  TIMESTAMP    NOT NULL,
  user_erstellt      VARCHAR(255) NOT NULL,
  user_mutiert       VARCHAR(255) NOT NULL,
  version            BIGINT       NOT NULL,
  gueltig_ab         DATE         NOT NULL,
  gueltig_bis        DATE         NOT NULL,
  gesuch_id          UUID         NOT NULL,
  gesuch_formular_id UUID,
  CONSTRAINT gesuch_tranche_pk PRIMARY KEY (id)
);

ALTER TABLE gesuch_tranche
  ADD CONSTRAINT FK_gesuch_tranche_gesuch_formular_id
    FOREIGN KEY (gesuch_formular_id)
      REFERENCES gesuch_formular (id);

ALTER TABLE gesuch_tranche
  ADD CONSTRAINT FK_gesuch_tranche_gesuch_id
    FOREIGN KEY (gesuch_id)
      REFERENCES gesuch (id);

CREATE INDEX IF NOT EXISTS IX_gesuch_tranche_gesuch_id ON gesuch_tranche (gesuch_id);
CREATE INDEX IF NOT EXISTS IX_gesuch_tranche_gesuch_formular_id ON gesuch_tranche (gesuch_formular_id);


CREATE TABLE gesuch_tranche_aud
(
  id                 UUID    NOT NULL,
  rev                INTEGER NOT NULL,
  revtype            SMALLINT,
  timestamp_erstellt TIMESTAMP,
  timestamp_mutiert  TIMESTAMP,
  user_erstellt      VARCHAR(255),
  user_mutiert       VARCHAR(255),
  version            BIGINT,
  gueltig_ab         DATE,
  gueltig_bis        DATE,
  gesuch_id          UUID,
  gesuch_formular_id UUID,
  CONSTRAINT gesuch_tranche_aud_pk PRIMARY KEY (id, rev)
);

ALTER TABLE gesuch_tranche_aud
  ADD CONSTRAINT FK_gesuch_tranche_aud_revinfo
    FOREIGN KEY (rev)
      REFERENCES revinfo (rev);


ALTER TABLE gesuch
  DROP COLUMN gesuch_formular_freigabe_copy_id;

ALTER TABLE gesuch
  DROP COLUMN gesuch_formular_to_work_with_id;

ALTER TABLE gesuch_aud
  DROP COLUMN gesuch_formular_freigabe_copy_id;

ALTER TABLE gesuch_aud
  DROP COLUMN gesuch_formular_to_work_with_id;

DELETE
FROM gesuch_dokument;
DELETE
FROM gesuch_dokument_aud;
DELETE
FROM dokument;
DELETE
FROM dokument_aud;
DELETE
FROM lebenslauf_item;
DELETE
FROM lebenslauf_item_aud;
DELETE
FROM geschwister;
DELETE
FROM geschwister_aud;
DELETE
FROM eltern;
DELETE
FROM eltern_aud;
DELETE
FROM kind;
DELETE
FROM kind_aud;
DELETE
FROM gesuch;
DELETE
FROM gesuch_aud;
DELETE
FROM gesuch_formular;
DELETE
FROM gesuch_formular_aud;
DELETE
FROM person_in_ausbildung;
DELETE
FROM person_in_ausbildung_aud;
DELETE
FROM partner;
DELETE
FROM partner_aud;
DELETE
FROM familiensituation;
DELETE
FROM familiensituation_aud;
DELETE
FROM einnahmen_kosten;
DELETE
FROM einnahmen_kosten_aud;
DELETE
FROM auszahlung;
DELETE
FROM auszahlung_aud;
DELETE
FROM ausbildung;
DELETE
FROM ausbildung_aud;
DELETE
FROM adresse;
DELETE
FROM adresse_aud;


