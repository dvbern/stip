CREATE TABLE bildungsart
(
	id					UUID         	NOT NULL,
	timestamp_erstellt	TIMESTAMP    	NOT NULL,
	timestamp_mutiert	TIMESTAMP    	NOT NULL,
	user_erstellt		VARCHAR(255) 	NOT NULL,
	user_mutiert		VARCHAR(255) 	NOT NULL,
	version				BIGINT       	NOT NULL,
	beschreibung		VARCHAR(255)	NOT NULL,
	bildungsstufe		VARCHAR(255)	NOT NULL,
	bfs					INTEGER			NOT NULL
);

CREATE TABLE bildungsart_aud
(
	id					UUID         	NOT NULL,
	timestamp_erstellt	TIMESTAMP    	NOT NULL,
	timestamp_mutiert	TIMESTAMP    	NOT NULL,
	user_erstellt		VARCHAR(255) 	NOT NULL,
	user_mutiert		VARCHAR(255) 	NOT NULL,
	version				BIGINT       	NOT NULL,
	beschreibung		VARCHAR(255)	NOT NULL,
	bildungsstufe		VARCHAR(255)	NOT NULL,
	bfs					INTEGER			NOT NULL
);
