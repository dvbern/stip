CREATE TABLE gesuchsjahr
(
	id					UUID			NOT NULL,
	timestamp_erstellt	TIMESTAMP		NOT NULL,
	timestamp_mutiert	TIMESTAMP		NOT NULL,
	user_erstellt		VARCHAR(255)	NOT NULL,
	user_mutiert		VARCHAR(255)	NOT NULL,
	version				BIGINT			NOT NULL,
	bezeichnung_de		VARCHAR(255)	NOT NULL,
	bezeichnung_fr		VARCHAR(255)	NOT NULL,
	technisches_jahr	INTEGER			NOT NULL,
	gueltigkeit_status	VARCHAR(255)	NOT NULL,
	mandant				VARCHAR(255)	NOT NULL DEFAULT 'bern',
	CONSTRAINT gesuchsjahr_pk PRIMARY KEY (id)
);

CREATE TABLE gesuchsjahr_aud
(
	id					UUID			NOT NULL,
	rev					INTEGER			NOT NULL,
	revtype				SMALLINT,
	timestamp_erstellt	TIMESTAMP,
	timestamp_mutiert	TIMESTAMP,
	user_erstellt		VARCHAR(255),
	user_mutiert		VARCHAR(255),
	version				BIGINT,
	bezeichnung_de		VARCHAR(255),
	bezeichnung_fr		VARCHAR(255),
	technisches_jahr	INTEGER,
	gueltigkeit_status	VARCHAR(255),
	mandant				VARCHAR(255),
	CONSTRAINT gesuchsjahr_aud_pk PRIMARY KEY (id, rev)
);

INSERT INTO gesuchsjahr(id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, bezeichnung_de,
                        bezeichnung_fr, technisches_jahr, gueltigkeit_status)
VALUES ('9596bb3e-10ea-4493-8aed-a6ef510f806b', now(), now(), 'Migration', 'Migration', 0, 'Gesuchsjahr 24',
        'Année de la demande 23', 2024, 'PUBLIZIERT');