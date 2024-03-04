ALTER TABLE einnahmen_kosten
  ADD COLUMN IF NOT EXISTS auswaertige_mittagessen_pro_woche INT
    CHECK ( auswaertige_mittagessen_pro_woche <= 5 AND auswaertige_mittagessen_pro_woche >= 0 );
ALTER TABLE einnahmen_kosten_aud
  ADD COLUMN IF NOT EXISTS auswaertige_mittagessen_pro_woche INT;
