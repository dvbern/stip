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

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.auszahlung.entity.Zahlungsverbindung;
import ch.dvbern.stip.api.benutzer.service.RolleService;
import ch.dvbern.stip.api.benutzer.type.BenutzerStatus;
import ch.dvbern.stip.api.benutzereinstellungen.entity.Benutzereinstellungen;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.land.entity.Land;
import ch.dvbern.stip.api.land.service.LandService;
import ch.dvbern.stip.api.land.type.WellKnownLand;
import ch.dvbern.stip.api.sozialdienst.entity.Sozialdienst;
import ch.dvbern.stip.api.sozialdienst.repo.SozialdienstRepository;
import ch.dvbern.stip.api.sozialdienstbenutzer.entity.SozialdienstBenutzer;
import ch.dvbern.stip.api.sozialdienstbenutzer.repo.SozialdienstBenutzerRepository;
import ch.dvbern.stip.api.tenancy.service.TenantConfigService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Singleton;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import lombok.extern.slf4j.Slf4j;

@Singleton
@RequiredArgsConstructor
@Slf4j
public class SozialdienstSeeding extends Seeder {
    private final RolleService rolleService;
    private final ObjectMapper objectMapper;
    private final SozialdienstBenutzerRepository sozialdienstBenutzerRepository;
    private final SozialdienstRepository sozialdienstRepository;
    private final ConfigService configService;
    private final LandService landService;
    private final TenantConfigService tenantConfigService;

    @Builder
    @Jacksonized
    @Data
    static class EnvSozialdienstBenutzer {
        private String email;
        private String keycloakId;
    }

    @Data
    @Jacksonized
    @Builder
    static class EnvSozialdienst {
        String name;
        EnvSozialdienstBenutzer admin;
        List<EnvSozialdienstBenutzer> mitarbeiter;
    }

    @Override
    public int getPriority() {
        // Ensure this number is smaller than dependent data (i.e. Land)
        return 300;
    }

    @Override
    protected void seed() {
        if (sozialdienstRepository.count() != 0) {
            return;
        }

        LOG.info("Seeding Sozialdienste");
        var envSeeding = tenantConfigService.getSozialdienstSeeding().orElse(null);
        if (envSeeding == null) {
            return;
        }

        // Ensure that the JSON string does not start or end with quotation marks
        // Else Jackson interprets it as one long string and fails to deserialize to an array
        if (envSeeding.charAt(0) == '"') {
            envSeeding = envSeeding.substring(1);
        }

        if (envSeeding.charAt(envSeeding.length() - 1) == '"') {
            envSeeding = envSeeding.substring(0, envSeeding.length() - 1);
        }

        final EnvSozialdienst[] sozialdienste;

        try {
            sozialdienste = objectMapper.readValue(envSeeding, EnvSozialdienst[].class);
        } catch (JsonProcessingException e) {
            LOG.error("Failed to parse sozialdienst for seeding", e);
            return;
        }

        final var switzerland = landService.getLandByBfsCode(WellKnownLand.CHE.getLaendercodeBfs()).orElseThrow();
        Arrays.stream(sozialdienste).forEach(sozialdienst -> {
            try {
                seedSozialdienst(sozialdienst, switzerland);
            } catch (Exception e) {
                LOG.error("Unable to seed Sozialdienst: {}", sozialdienst.name);
            }
        });
    }

    void seedSozialdienst(final EnvSozialdienst envSozialdienst, final Land land) {
        final var adresse = new Adresse()
            .setStrasse("Nussbaumstrasse")
            .setHausnummer("21")
            .setOrt("Bern")
            .setPlz("3000")
            .setLand(land);
        var zahlungsverbindung = new Zahlungsverbindung()
            .setAdresse(adresse)
            .setIban("CH3908704016075473007")
            .setVorname("Max")
            .setNachname("Muster");
        final var sozialdienst = new Sozialdienst()
            .setName(envSozialdienst.getName())
            .setZahlungsverbindung(zahlungsverbindung);

        final var admin = seedSozialdienstBenutzer(envSozialdienst.getAdmin(), OidcConstants.ROLE_SOZIALDIENST_ADMIN);
        sozialdienst.setSozialdienstAdmin(admin);

        for (final var envMitarbeiter : envSozialdienst.getMitarbeiter()) {
            final var mitarbeiter =
                seedSozialdienstBenutzer(envMitarbeiter, OidcConstants.ROLE_SOZIALDIENST_MITARBEITER);
            sozialdienst.getSozialdienstBenutzers().add(mitarbeiter);
        }

        sozialdienstRepository.persistAndFlush(sozialdienst);
    }

    SozialdienstBenutzer seedSozialdienstBenutzer(EnvSozialdienstBenutzer envSozialdienstBenutzer, String role) {
        final var existingUser =
            sozialdienstBenutzerRepository.findByKeycloakId(envSozialdienstBenutzer.getKeycloakId());

        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        final var rollen = rolleService.mapOrCreateRoles(Set.of(role));
        final var sozialdienstBenutzer = new SozialdienstBenutzer();
        sozialdienstBenutzer
            .setEmail(envSozialdienstBenutzer.getEmail())
            .setVorname("Vorname")
            .setNachname("Nachname")
            .setKeycloakId(envSozialdienstBenutzer.getKeycloakId())
            .setRollen(rollen)
            .setBenutzerStatus(BenutzerStatus.AKTIV)
            .setBenutzereinstellungen(
                new Benutzereinstellungen()
                    .setDigitaleKommunikation(true)
            );

        sozialdienstBenutzerRepository.persistAndFlush(sozialdienstBenutzer);
        return sozialdienstBenutzer;
    }

    @Override
    protected List<String> getProfiles() {
        return configService.getSeedOnProfile();
    }
}
