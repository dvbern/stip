CREATE TABLE einnahmen_kosten (
	id                                 UUID           NOT NULL,
	timestamp_erstellt                 TIMESTAMP      NOT NULL,
	timestamp_mutiert                  TIMESTAMP      NOT NULL,
	user_erstellt                      VARCHAR(255)   NOT NULL,
	user_mutiert                       VARCHAR(255)   NOT NULL,
	version                            BIGINT         NOT NULL,
	nettoerwerbseinkommen              NUMERIC(19, 2) NOT NULL,
	fahrkosten                         NUMERIC(19, 2) NOT NULL,
	wohnkosten                         NUMERIC(19, 2) NOT NULL,
	personenImHaushalt                 NUMERIC(19, 2) NOT NULL,
	verdienstRealisiert                BOOLEAN        NOT NULL,
	alimente                           NUMERIC(19, 2),
	zulagen                            NUMERIC(19, 2),
	renten                             NUMERIC(19, 2),
	eoLeistungen                       NUMERIC(19, 2),
	ergaenzungsleistungen              NUMERIC(19, 2),
	beitraege                          NUMERIC(19, 2),
	ausbildungskostenSekundarstufeZwei NUMERIC(19, 2),
	ausbildungskostenTertiaerstufe     NUMERIC(19, 2),
	willDarlehen                       BOOLEAN,
	mandant                            VARCHAR(255)   NOT NULL DEFAULT 'bern',
	CONSTRAINT einnahmen_kosten_pk PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS IX_einnahme_kosten_mandant ON einnahmen_kosten(mandant);

ALTER TABLE gesuch_formular
ADD COLUMN einnahmen_kosten_id UUID;
ALTER TABLE gesuch_formular_aud
ADD COLUMN einnahmen_kosten_id UUID;

CREATE INDEX IF NOT EXISTS FK_gesuch_formular_einnahmen_kosten_id ON gesuch_formular (einnahmen_kosten_id);

ALTER TABLE gesuch_formular
ADD CONSTRAINT FK_gesuch_formular_einnahmen_kosten_id
	FOREIGN KEY (einnahmen_kosten_id)
		REFERENCES einnahmen_kosten(id);

CREATE TABLE einnahmen_kosten_aud (
	id                                 UUID    NOT NULL,
	rev                                INTEGER NOT NULL,
	revtype                            SMALLINT,
	timestamp_erstellt                 TIMESTAMP,
	timestamp_mutiert                  TIMESTAMP,
	user_erstellt                      VARCHAR(255),
	user_mutiert                       VARCHAR(255),
	version                            BIGINT,
	nettoerwerbseinkommen              NUMERIC(19, 2),
	fahrkosten                         NUMERIC(19, 2),
	wohnkosten                         NUMERIC(19, 2),
	personenImHaushalt                 NUMERIC(19, 2),
	verdienstRealisiert                BOOLEAN,
	alimente                           NUMERIC(19, 2),
	zulagen                            NUMERIC(19, 2),
	renten                             NUMERIC(19, 2),
	eoLeistungen                       NUMERIC(19, 2),
	ergaenzungsleistungen              NUMERIC(19, 2),
	beitraege                          NUMERIC(19, 2),
	ausbildungskostenSekundarstufeZwei NUMERIC(19, 2),
	ausbildungskostenTertiaerstufe     NUMERIC(19, 2),
	willDarlehen                       BOOLEAN,
	mandant                            VARCHAR(255),
	CONSTRAINT einnahmen_kosten_aud_pk PRIMARY KEY (id, rev)
);

ALTER TABLE einnahmen_kosten_aud
ADD CONSTRAINT FK_einnahmen_kosten_aud_revinfo
	FOREIGN KEY (rev)
		REFERENCES revinfo(rev);

ALTER TABLE ausbildungsgang
ADD COLUMN ausbildungsrichtung VARCHAR(255) NOT NULL DEFAULT 'UNIVERSITAETEN_ETH';
ALTER TABLE ausbildungsgang_aud
ADD COLUMN ausbildungsrichtung VARCHAR(255);

UPDATE ausbildungsgang
SET ausbildungsrichtung = 'UNIVERSITAETEN_ETH'
WHERE id = '3a8c2023-f29e-4466-a2d7-411a7d032f42';
UPDATE ausbildungsgang
SET ausbildungsrichtung = 'UNIVERSITAETEN_ETH'
WHERE id = '384ba33a-eb4e-44e4-ac20-8e11101bbf3a';
UPDATE ausbildungsgang
SET ausbildungsrichtung = 'UNIVERSITAETEN_ETH'
WHERE id = 'b18d06fb-1dd4-4929-8470-e9e44b0bc4a7';
UPDATE ausbildungsgang
SET ausbildungsrichtung = 'UNIVERSITAETEN_ETH'
WHERE id = 'b319953d-f5a9-4a1a-a0f6-69bd3f745e02';
UPDATE ausbildungsgang
SET ausbildungsrichtung = 'GYMNASIALE_MATURITAETSSCHULEN'
WHERE id = '345ef0b1-b62e-4fdd-abf4-cb789d4296ee';
UPDATE ausbildungsgang
SET ausbildungsrichtung = 'SCHULEN_FUER_ALLGEMEINBILDUNG'
WHERE id = '44779764-e6d6-455c-8667-0d0b40f05dd9';
UPDATE ausbildungsgang
SET ausbildungsrichtung = 'HOEHERE_BERUFSBILDUNG'
WHERE id = 'abc07eed-19d4-4e1e-9899-6d5893556b1b';
UPDATE ausbildungsgang
SET ausbildungsrichtung = 'FACHHOCHSCHULEN'
WHERE id = '52e9577c-732a-4f1b-ae74-de2e477c01e9';
UPDATE ausbildungsgang
SET ausbildungsrichtung = 'UNIVERSITAETEN_ETH'
WHERE id = '3dac8f59-533c-4b99-9b13-210503c34595';
UPDATE ausbildungsgang
SET ausbildungsrichtung = 'UNIVERSITAETEN_ETH'
WHERE id = 'c69c6712-9bd9-4533-a685-44fddefd5937';
