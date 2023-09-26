ALTER TABLE partner
	ADD COLUMN IF NOT EXISTS ausbildung_mit_einkommen_oder_erwerbstaetig BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE partner_aud
	ADD COLUMN IF NOT EXISTS ausbildung_mit_einkommen_oder_erwerbstaetig BOOLEAN;

ALTER TABLE partner
    ADD COLUMN IF NOT EXISTS fahrkosten NUMERIC(19, 2) NULL;
ALTER TABLE partner_aud
    ADD COLUMN IF NOT EXISTS fahrkosten NUMERIC(19, 2) NULL;

ALTER TABLE partner
    ADD COLUMN IF NOT EXISTS verpflegungskosten NUMERIC(19, 2) NULL;
ALTER TABLE partner_aud
		ADD COLUMN IF NOT EXISTS verpflegungskosten NUMERIC(19, 2) NULL;

ALTER TABLE partner
	ALTER COLUMN jahreseinkommen DROP NOT NULL;
