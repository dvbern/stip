ALTER TABLE einnahmen_kosten
  ALTER COLUMN nettoerwerbseinkommen TYPE INTEGER USING nettoerwerbseinkommen::INTEGER,
  ALTER COLUMN fahrkosten TYPE INTEGER USING fahrkosten::INTEGER,
  ALTER COLUMN wohnkosten TYPE INTEGER USING wohnkosten::INTEGER,
  ALTER COLUMN alimente TYPE INTEGER USING alimente::INTEGER,
  ALTER COLUMN zulagen TYPE INTEGER USING zulagen::INTEGER,
  ALTER COLUMN renten TYPE INTEGER USING renten::INTEGER,
  ALTER COLUMN eo_leistungen TYPE INTEGER USING eo_leistungen::INTEGER,
  ALTER COLUMN ergaenzungsleistungen TYPE INTEGER USING ergaenzungsleistungen::INTEGER,
  ALTER COLUMN beitraege TYPE INTEGER USING beitraege::INTEGER,
  ALTER COLUMN ausbildungskosten_sekundarstufe_zwei TYPE INTEGER USING ausbildungskosten_sekundarstufe_zwei::INTEGER,
  ALTER COLUMN ausbildungskosten_tertiaerstufe TYPE INTEGER USING ausbildungskosten_tertiaerstufe::INTEGER,
  ALTER COLUMN betreuungskosten_kinder TYPE INTEGER USING betreuungskosten_kinder::INTEGER;

ALTER TABLE kind
  ALTER COLUMN erhaltene_alimentebeitraege TYPE INTEGER USING erhaltene_alimentebeitraege::INTEGER;

ALTER TABLE person_in_ausbildung
  ALTER COLUMN vermoegen_vorjahr TYPE INTEGER USING vermoegen_vorjahr::INTEGER;

ALTER TABLE partner
  ALTER COLUMN jahreseinkommen TYPE INTEGER USING verpflegungskosten::INTEGER,
  ALTER COLUMN verpflegungskosten TYPE INTEGER USING verpflegungskosten::INTEGER,
  ALTER COLUMN fahrkosten TYPE INTEGER USING fahrkosten::INTEGER;
