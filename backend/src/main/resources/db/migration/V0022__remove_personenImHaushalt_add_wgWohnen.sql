ALTER TABLE einnahmen_kosten DROP COLUMN personen_im_haushalt;
ALTER TABLE einnahmen_kosten_aud DROP COLUMN personen_im_haushalt;

ALTER TABLE einnahmen_kosten ADD COLUMN wg_wohnend BOOLEAN;
ALTER TABLE einnahmen_kosten_aud ADD COLUMN wg_wohnend BOOLEAN;
