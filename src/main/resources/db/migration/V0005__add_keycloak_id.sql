ALTER TABLE benutzer
    ADD COLUMN keycloak_id VARCHAR(255);

ALTER TABLE benutzer
    ALTER COLUMN sozialversicherungsnummer drop not null;

ALTER TABLE benutzer_aud
    ADD COLUMN keycloak_id VARCHAR(255);

ALTER TABLE benutzer
    ADD CONSTRAINT UC_benutzer_keycloak UNIQUE (keycloak_id);

CREATE UNIQUE INDEX IX_benutzer_keycloak_id ON benutzer (keycloak_id);
