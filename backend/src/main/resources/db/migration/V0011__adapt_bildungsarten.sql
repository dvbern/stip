ALTER TABLE lebenslauf_item
  RENAME COLUMN beschreibung TO taetigkeits_beschreibung;
ALTER TABLE lebenslauf_item_aud
  RENAME COLUMN beschreibung TO taetigkeits_beschreibung;

ALTER TABLE lebenslauf_item
  ALTER COLUMN taetigkeits_beschreibung DROP NOT NULL;


ALTER TABLE lebenslauf_item
  ADD COLUMN IF NOT EXISTS berufsbezeichnung VARCHAR(255) NULL;
ALTER TABLE lebenslauf_item_aud
  ADD COLUMN IF NOT EXISTS berufsbezeichnung VARCHAR(255) NULL;

ALTER TABLE lebenslauf_item
  ADD COLUMN IF NOT EXISTS ausbildung_abgeschlossen BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE lebenslauf_item_aud
  ADD COLUMN IF NOT EXISTS ausbildung_abgeschlossen BOOLEAN;

ALTER TABLE lebenslauf_item
  ADD COLUMN IF NOT EXISTS fachrichtung VARCHAR(255) NULL;
ALTER TABLE lebenslauf_item_aud
  ADD COLUMN IF NOT EXISTS fachrichtung VARCHAR(255) NULL;

ALTER TABLE lebenslauf_item
  ADD COLUMN IF NOT EXISTS titel_des_abschlusses VARCHAR(255) NULL;
ALTER TABLE lebenslauf_item_aud
  ADD COLUMN IF NOT EXISTS titel_des_abschlusses VARCHAR(255) NULL;

-- Migrate existing bildungsarten
UPDATE lebenslauf_item
SET bildungsart = 'FACHMATURITAET'
WHERE bildungsart = 'SCHULEN_FUER_ALLGEMEINBILDUNG'
   OR bildungsart = 'VOLLZEITBERUFSSCHULEN'
   OR bildungsart = 'HOEHERE_BERUFSBILDUNG';

UPDATE lebenslauf_item
SET bildungsart = 'BACHELOR_FACHHOCHSCHULE'
WHERE bildungsart = 'FACHHOCHSCHULEN';

UPDATE lebenslauf_item
SET bildungsart = 'BACHELOR_HOCHSCHULE_UNI'
WHERE bildungsart = 'UNIVERSITAETEN_ETH';

-- set correct beschreibungen where needed from bildungsart

UPDATE lebenslauf_item
SET berufsbezeichnung = 'CHANGE_ME'
WHERE bildungsart = 'EIDGENOESSISCHES_BERUFSATTEST'
   OR bildungsart = 'EIDGENOESSISCHES_FAEHIGKEITSZEUGNIS';

UPDATE lebenslauf_item
SET fachrichtung = 'CHANGE_ME'
WHERE bildungsart = 'BACHELOR_HOCHSCHULE_UNI'
   OR bildungsart = 'BACHELOR_FACHHOCHSCHULE'
   OR bildungsart = 'MASTER';

UPDATE lebenslauf_item
SET titel_des_abschlusses = 'CHANGE_ME'
WHERE bildungsart = 'ANDERER_BILDUNGSABSCHLUSS'
   OR bildungsart = 'BACHELOR_FACHHOCHSCHULE'
   OR bildungsart = 'MASTER';
