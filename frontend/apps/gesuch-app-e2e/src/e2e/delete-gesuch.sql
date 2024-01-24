DELETE FROM gesuch_tranche WHERE gesuch_id IN (
  SELECT g.id FROM gesuch AS g
  LEFT JOIN fall AS f ON (f.id = g.fall_id)
  LEFT JOIN benutzer AS b ON (b.id = f.gesuchsteller_id)
  WHERE b.vorname = 'e2e'
);
DELETE FROM gesuch WHERE id IN (
  SELECT g.id FROM gesuch AS g
  LEFT JOIN fall AS f ON (f.id = g.fall_id)
  LEFT JOIN benutzer AS b ON (b.id = f.gesuchsteller_id)
  WHERE b.vorname = 'e2e'
);
