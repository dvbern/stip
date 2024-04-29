ALTER TABLE gesuchsperiode
  ADD COLUMN preis_pro_mahlzeit INTEGER NOT NULL DEFAULT 0,
  ADD COLUMN max_saeule_3a INTEGER NOT NULL DEFAULT 0,
  ADD COLUMN anzahl_wochen_lehre INTEGER NOT NULL DEFAULT 0,
  ADD COLUMN anzahl_wochen_schule INTEGER NOT NULL DEFAULT 0;

ALTER TABLE gesuchsperiode
  ALTER COLUMN preis_pro_mahlzeit DROP DEFAULT,
  ALTER COLUMN max_saeule_3a DROP DEFAULT,
  ALTER COLUMN anzahl_wochen_lehre DROP DEFAULT,
  ALTER COLUMN anzahl_wochen_schule DROP DEFAULT;
