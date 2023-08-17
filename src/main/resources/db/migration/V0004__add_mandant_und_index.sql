ALTER TABLE adresse ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_adresse_mandant_id ON adresse (mandant);
ALTER TABLE adresse_aud ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';

ALTER TABLE ausbildung ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_ausbildung_mandant_id ON ausbildung (mandant);
ALTER TABLE ausbildung_aud ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';

ALTER TABLE ausbildungsgang ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_ausbildungsgang_mandant_id ON ausbildungsgang (mandant);
ALTER TABLE ausbildungsgang_aud ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';

ALTER TABLE ausbildungsstaette ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_ausbildungsstaette_mandant_id ON ausbildungsstaette (mandant);
ALTER TABLE ausbildungsstaette_aud ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';

ALTER TABLE auszahlung ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_auszahlung_mandant_id ON auszahlung (mandant);
ALTER TABLE auszahlung_aud ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';

CREATE INDEX IF NOT EXISTS IX_benutzer_mandant_id ON benutzer (mandant);

ALTER TABLE dokument ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_dokument_mandant_id ON dokument (mandant);
ALTER TABLE dokument_aud ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';

ALTER TABLE eltern ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_eltern_mandant_id ON eltern (mandant);
ALTER TABLE eltern_aud ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';

ALTER TABLE familiensituation ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_familiensituation_mandant_id ON familiensituation (mandant);
ALTER TABLE familiensituation_aud ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';

ALTER TABLE geschwister ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_geschwister_mandant_id ON geschwister (mandant);
ALTER TABLE geschwister_aud ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';

ALTER TABLE gesuch ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_gesuch_mandant_id ON gesuch (mandant);
ALTER TABLE gesuch_aud ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';

ALTER TABLE gesuch_dokument ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_gesuch_dokument_mandant_id ON gesuch_dokument (mandant);
ALTER TABLE gesuch_dokument_aud ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';

ALTER TABLE gesuch_formular ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_gesuch_formular_mandant_id ON gesuch_formular (mandant);
ALTER TABLE gesuch_formular_aud ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';

ALTER TABLE gesuchsperiode ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_gesuchsperiode_mandant_id ON gesuchsperiode (mandant);
ALTER TABLE gesuchsperiode_aud ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';

ALTER TABLE kind ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_kind_mandant_id ON kind (mandant);
ALTER TABLE kind_aud ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';

ALTER TABLE lebenslauf_item ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_lebenslauf_item_mandant_id ON lebenslauf_item (mandant);
ALTER TABLE lebenslauf_item_aud ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';

ALTER TABLE partner ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_partner_mandant_id ON partner (mandant);
ALTER TABLE partner_aud ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';

ALTER TABLE person_in_ausbildung ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';
CREATE INDEX IF NOT EXISTS IX_person_in_ausbildung_mandant_id ON person_in_ausbildung (mandant);
ALTER TABLE person_in_ausbildung_aud ADD COLUMN mandant VARCHAR(255) NOT NULL DEFAULT 'bern';