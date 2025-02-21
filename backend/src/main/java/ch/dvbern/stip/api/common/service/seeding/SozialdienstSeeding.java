/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.common.service.seeding;

import java.util.List;
import java.util.Set;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.benutzer.service.RolleService;
import ch.dvbern.stip.api.benutzer.type.BenutzerStatus;
import ch.dvbern.stip.api.benutzereinstellungen.entity.Benutzereinstellungen;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.sozialdienst.entity.Sozialdienst;
import ch.dvbern.stip.api.sozialdienst.repo.SozialdienstRepository;
import ch.dvbern.stip.api.sozialdienstbenutzer.entity.SozialdienstBenutzer;
import ch.dvbern.stip.api.sozialdienstbenutzer.repo.SozialdienstBenutzerRepository;
import ch.dvbern.stip.api.stammdaten.type.Land;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Singleton
@RequiredArgsConstructor
@Slf4j
public class SozialdienstSeeding extends Seeder {
    private final RolleService rolleService;
    private final SozialdienstBenutzerRepository sozialdienstBenutzerRepository;
    private final SozialdienstRepository sozialdienstRepository;
    private final ConfigService configService;
    private static final String KEYCLOAK_ID = "7a72b682-f4dc-4b72-9d26-dbdc19246b7d";

    @Override
    public int getPriority() {
        return 300;
    }

    @Override
    protected void doSeed() {
        LOG.info("Seeding Sozialdienste");
        final var existingAdmin = sozialdienstBenutzerRepository.findByKeycloakId(KEYCLOAK_ID);

        if (existingAdmin.isPresent()) {
            return;
        }

        final var rollen = rolleService.mapOrCreateRoles(Set.of(OidcConstants.ROLE_SOZIALDIENST_ADMIN));
        final var sozialdienstAdmin = new SozialdienstBenutzer();
        sozialdienstAdmin
            .setEmail("sozialdienst-mitarbeiter-admin@mailbucket.dvbern.ch")
            .setVorname("soz-admin")
            .setNachname("e2e")
            .setKeycloakId(KEYCLOAK_ID)
            .setRollen(rollen)
            .setBenutzerStatus(BenutzerStatus.AKTIV)
            .setBenutzereinstellungen(
                new Benutzereinstellungen()
                    .setDigitaleKommunikation(true)
            );
        final var adresse = new Adresse()
            .setStrasse("Nussbaumstrasse")
            .setHausnummer("21")
            .setOrt("Bern")
            .setPlz("3000")
            .setLand(Land.CH);
        final var sozialdienst = new Sozialdienst()
            .setName("[E2E] Sozialdienst")
            .setAdresse(adresse)
            .setIban("CH3908704016075473007")
            .setSozialdienstAdmin(sozialdienstAdmin);

        sozialdienstBenutzerRepository.persistAndFlush(sozialdienstAdmin);
        sozialdienstRepository.persistAndFlush(sozialdienst);
    }

    @Override
    protected List<String> getProfiles() {
        return configService.getSeedOnProfile();
    }
}
