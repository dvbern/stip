ALTER TABLE person_in_ausbildung  ADD COLUMN vormundschaft BOOLEAN NOT NULL DEFAULT TRUE;
ALTER TABLE person_in_ausbildung_aud  ADD COLUMN vormundschaft BOOLEAN;
