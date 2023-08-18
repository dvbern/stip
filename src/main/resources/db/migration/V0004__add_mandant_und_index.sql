ALTER TABLE adresse ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_adresse_mandant ON adresse (mandant);
ALTER TABLE adresse_aud ADD COLUMN mandant VARCHAR(255);

ALTER TABLE ausbildung ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_ausbildung_mandant ON ausbildung (mandant);
ALTER TABLE ausbildung_aud ADD COLUMN mandant VARCHAR(255);

ALTER TABLE ausbildungsgang ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_ausbildungsgang_mandant ON ausbildungsgang (mandant);
ALTER TABLE ausbildungsgang_aud ADD COLUMN mandant VARCHAR(255);

ALTER TABLE ausbildungsstaette ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_ausbildungsstaette_mandant ON ausbildungsstaette (mandant);
ALTER TABLE ausbildungsstaette_aud ADD COLUMN mandant VARCHAR(255);

ALTER TABLE auszahlung ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_auszahlung_mandant ON auszahlung (mandant);
ALTER TABLE auszahlung_aud ADD COLUMN mandant VARCHAR(255);

CREATE INDEX IF NOT EXISTS IX_benutzer_mandant ON benutzer (mandant);
CREATE INDEX IF NOT EXISTS IX_fall_mandant ON fall (mandant);

ALTER TABLE dokument ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_dokument_mandant ON dokument (mandant);
ALTER TABLE dokument_aud ADD COLUMN mandant VARCHAR(255);

ALTER TABLE eltern ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_eltern_mandant ON eltern (mandant);
ALTER TABLE eltern_aud ADD COLUMN mandant VARCHAR(255);

ALTER TABLE familiensituation ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_familiensituation_mandant ON familiensituation (mandant);
ALTER TABLE familiensituation_aud ADD COLUMN mandant VARCHAR(255);

ALTER TABLE geschwister ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_geschwister_mandant ON geschwister (mandant);
ALTER TABLE geschwister_aud ADD COLUMN mandant VARCHAR(255);

ALTER TABLE gesuch ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_gesuch_mandant ON gesuch (mandant);
ALTER TABLE gesuch_aud ADD COLUMN mandant VARCHAR(255);

ALTER TABLE gesuch_dokument ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_gesuch_dokument_mandant ON gesuch_dokument (mandant);
ALTER TABLE gesuch_dokument_aud ADD COLUMN mandant VARCHAR(255);

ALTER TABLE gesuch_formular ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_gesuch_formular_mandant ON gesuch_formular (mandant);
ALTER TABLE gesuch_formular_aud ADD COLUMN mandant VARCHAR(255);

ALTER TABLE gesuchsperiode ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_gesuchsperiode_mandant ON gesuchsperiode (mandant);
ALTER TABLE gesuchsperiode_aud ADD COLUMN mandant VARCHAR(255);

ALTER TABLE kind ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_kind_mandant ON kind (mandant);
ALTER TABLE kind_aud ADD COLUMN mandant VARCHAR(255);

ALTER TABLE lebenslauf_item ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_lebenslauf_item_mandant ON lebenslauf_item (mandant);
ALTER TABLE lebenslauf_item_aud ADD COLUMN mandant VARCHAR(255);

ALTER TABLE partner ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_partner_mandant ON partner (mandant);
ALTER TABLE partner_aud ADD COLUMN mandant VARCHAR(255);

ALTER TABLE person_in_ausbildung ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_person_in_ausbildung_mandant ON person_in_ausbildung (mandant);
ALTER TABLE person_in_ausbildung_aud ADD COLUMN mandant VARCHAR(255);