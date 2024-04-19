ALTER TABLE person_in_ausbildung DROP COLUMN quellenbesteuert;
ALTER TABLE person_in_ausbildung_aud DROP COLUMN quellenbesteuert;

ALTER TABLE person_in_ausbildung ADD COLUMN vermoegen_vorjahr NUMERIC(19, 2);
ALTER TABLE person_in_ausbildung_aud ADD COLUMN vermoegen_vorjahr NUMERIC(19, 2);
