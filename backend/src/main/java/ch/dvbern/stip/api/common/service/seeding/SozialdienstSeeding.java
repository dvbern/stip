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
import java.util.Map;
import java.util.Set;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.benutzer.service.RolleService;
import ch.dvbern.stip.api.benutzer.type.BenutzerStatus;
import ch.dvbern.stip.api.benutzereinstellungen.entity.Benutzereinstellungen;
import ch.dvbern.stip.api.common.type.MandantIdentifier;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.land.service.LandService;
import ch.dvbern.stip.api.land.type.WellKnownLand;
import ch.dvbern.stip.api.sozialdienst.entity.Sozialdienst;
import ch.dvbern.stip.api.sozialdienst.repo.SozialdienstRepository;
import ch.dvbern.stip.api.sozialdienstbenutzer.entity.SozialdienstBenutzer;
import ch.dvbern.stip.api.sozialdienstbenutzer.repo.SozialdienstBenutzerRepository;
import ch.dvbern.stip.api.tenancy.service.TenantService;
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
    private final TenantService tenantService;
    private final LandService landService;

    private static final Map<String, String> KEYCLOAK_IDS = Map.of(
        MandantIdentifier.BERN.getIdentifier(),
        "7d115bec-5ccd-4643-8bb3-da8999017369",
        MandantIdentifier.DV.getIdentifier(),
        "8297d66c-eb83-47e7-bdf6-e494eac6aa67"
    );

    @Override
    public int getPriority() {
        // Ensure this number is smaller than dependent data (i.e. Land)
        return 300;
    }

    @Override
    protected void seed() {
        LOG.info("Seeding Sozialdienste");
        final var keycloakId = KEYCLOAK_IDS.get(tenantService.getCurrentTenantIdentifier());
        final var existingAdmin = sozialdienstBenutzerRepository.findByKeycloakId(keycloakId);

        if (existingAdmin.isPresent()) {
            return;
        }

        final var rollen = rolleService.mapOrCreateRoles(Set.of(OidcConstants.ROLE_SOZIALDIENST_ADMIN));
        final var sozialdienstAdmin = new SozialdienstBenutzer();
        sozialdienstAdmin
            .setEmail("sozialdienst-mitarbeiter-admin@mailbucket.dvbern.ch")
            .setVorname("soz-admin")
            .setNachname("e2e")
            .setKeycloakId(keycloakId)
            .setRollen(rollen)
            .setBenutzerStatus(BenutzerStatus.AKTIV)
            .setBenutzereinstellungen(
                new Benutzereinstellungen()
                    .setDigitaleKommunikation(true)
            );

        final var switzerland = landService.getLandByBfsCode(WellKnownLand.CH.getLaendercodeBfs()).orElseThrow();
        final var adresse = new Adresse()
            .setStrasse("Nussbaumstrasse")
            .setHausnummer("21")
            .setOrt("Bern")
            .setPlz("3000")
            .setLand(switzerland);
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
