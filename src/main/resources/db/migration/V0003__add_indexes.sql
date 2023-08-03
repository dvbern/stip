CREATE INDEX IF NOT EXISTS FK_gesuch_formular_auszahlung_id ON gesuch_formular (auszahlung_id);

CREATE INDEX IF NOT EXISTS IX_ausbildung_ausbildungsgang_id ON ausbildung (ausbildungsgang_id);

CREATE INDEX IF NOT EXISTS IX_ausbildung_ausbildungsstaette_id ON ausbildung (ausbildungsstaette_id);

CREATE INDEX IF NOT EXISTS IX_ausbildungsgang_ausbildungsstaette_id ON ausbildungsgang (ausbildungsstaette_id);

CREATE INDEX IF NOT EXISTS IX_auszahlung_adresse_id ON auszahlung (adresse_id);

CREATE INDEX IF NOT EXISTS IX_dokument_gesuch_dokument_id ON dokument (gesuch_dokument_id);

CREATE INDEX IF NOT EXISTS IX_eltern_adresse_id ON eltern (adresse_id);

CREATE INDEX IF NOT EXISTS IX_fall_gesuchsteller_id ON fall (gesuchsteller_id);

CREATE INDEX IF NOT EXISTS IX_fall_sachbearbeiter_id ON fall (sachbearbeiter_id);

CREATE INDEX IF NOT EXISTS IX_gesuch_dokument_gesuch_id ON gesuch_dokument (gesuch_id);

CREATE INDEX IF NOT EXISTS IX_gesuch_fall_id ON gesuch (fall_id);

CREATE INDEX IF NOT EXISTS IX_gesuch_formular_ausbildung_id ON gesuch_formular (ausbildung_id);

CREATE INDEX IF NOT EXISTS IX_gesuch_formular_familiensituation_id ON gesuch_formular (familiensituation_id);

CREATE INDEX IF NOT EXISTS IX_gesuch_formular_partner_id ON gesuch_formular (partner_id);

CREATE INDEX IF NOT EXISTS IX_gesuch_formular_person_in_ausbildung_id ON gesuch_formular (person_in_ausbildung_id);

CREATE INDEX IF NOT EXISTS IX_gesuch_gesuch_formular_freigabe_copy_id ON gesuch (gesuch_formular_freigabe_copy_id);

CREATE INDEX IF NOT EXISTS IX_gesuch_gesuch_forumular_to_work_with_id ON gesuch (gesuch_formular_to_work_with_id);

CREATE INDEX IF NOT EXISTS IX_gesuch_gesuchsperiode_id ON gesuch (gesuchsperiode_id);

CREATE INDEX IF NOT EXISTS IX_person_in_ausbildung_adresse_id ON person_in_ausbildung (adresse_id);

CREATE INDEX IF NOT EXISTS IX_partner_adresse_id ON partner (adresse_id);
