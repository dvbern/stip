ALTER TABLE person_in_ausbildung
  ADD COLUMN vormundschaft BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE person_in_ausbildung_aud
  ADD COLUMN vormundschaft BOOLEAN;
