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
import java.util.stream.Stream;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.auszahlung.entity.Zahlungsverbindung;
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
import ch.dvbern.stip.api.tenancy.service.TenantConfigService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
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
    private final TenantConfigService tenantConfigService;

//    @Jacksonized
//    @Builder
//    @Data
//    sealed static class EnvSozialdienstBenutzer {
//        private String email;
//        private String keycloakId;
//    }
//
//    @Jacksonized
//    @Builder
//    static final class EnvSozialdienstAdmin extends EnvSozialdienstBenutzer {
//    }
//
//    @Jacksonized
//    @Builder
//    static final class EnvSozialdienstMitarbeiter extends EnvSozialdienstBenutzer {
//    }

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
        List<EnvSozialdienstBenutzer> admins;
        List<EnvSozialdienstBenutzer> mitarbeiter;
    }

    @Override
    public int getPriority() {
        return 300;
    }

    @Override
    protected void seed() {
        LOG.info("Seeding Sozialdienste");
        final var envSeeding = tenantConfigService.getSozialdienstSeeding();
        final EnvSozialdienst[] sozialdienste;

        try {
            sozialdienste = objectMapper.readValue(envSeeding, EnvSozialdienst[].class);
        } catch (Exception e) {
            throw new RuntimeException("Unable to parse sozialdienste seeding values", e);
        }

        Arrays.stream(sozialdienste).forEach(sozialdienst -> {
            try {
                seedSozialdienst(sozialdienst);
            } catch (Exception e) {
                LOG.error("Unable to seed Sozialdienst: {}", sozialdienst.name);
            }
        });
    }

    @Transactional(TxType.REQUIRES_NEW)
    void seedSozialdienst(EnvSozialdienst envSozialdienst) {
        final var adresse = new Adresse()
            .setStrasse("Nussbaumstrasse")
            .setHausnummer("21")
            .setOrt("Bern")
            .setPlz("3000")
            .setLand(Land.CH);
        var zahlungsverbindung = new Zahlungsverbindung()
            .setAdresse(adresse)
            .setIban("CH3908704016075473007")
            .setVorname("Max")
            .setNachname("Muster");
        final var sozialdienst = new Sozialdienst()
            .setName(envSozialdienst.getName())
            .setZahlungsverbindung(zahlungsverbindung);
        sozialdienstRepository.persist(sozialdienst);

        final Stream<EnvSozialdienstBenutzer> benutzerLists =
            Stream.concat(
                envSozialdienst.admins.stream(),
                envSozialdienst.mitarbeiter.stream()
            );

        final var results = benutzerLists.map(envBenutzer -> {
            try {
                final var benutzer = seedSozialdienstBenutzer(envBenutzer);

//                final var ok = switch (envBenutzer) {
//                    case EnvSozialdienstAdmin ignored -> {
//                        sozialdienst.setSozialdienstAdmin(benutzer);
//                        yield true;
//                    }
//                    case EnvSozialdienstMitarbeiter ignored -> {
//                        sozialdienst.getSozialdienstBenutzers().add(benutzer);
//                        yield true;
//                    }
//                    case EnvSozialdienstBenutzer ignored -> throw new RuntimeException("Invalid benutzertype");
//                };
                sozialdienstRepository.persist(sozialdienst);

//                return ok;
                return true;
            } catch (Exception e) {
                LOG.error("Unable to seed sozialdienstbenutzer for Sozialdienst: {}", envSozialdienst.name);
                return false;
            }
        }).toList();

        if (results.isEmpty()) {
            LOG.warn("Sozialdienst seeding for [{}] was empty", sozialdienst.getName());
        } else if (results.contains(false)) {
            LOG.error("Sozialdienst seeding for [{}] failed", sozialdienst.getName());
        } else {
            LOG.info(
                "Sozialdienst seeding for [{}] finished, seeded {} sozialdienste",
                sozialdienst.getName(),
                results.size()
            );
        }
    }

    @Transactional(TxType.REQUIRES_NEW)
    SozialdienstBenutzer seedSozialdienstBenutzer(EnvSozialdienstBenutzer envSozialdienstBenutzer) {
        final var existingUser = sozialdienstBenutzerRepository.findByKeycloakId(envSozialdienstBenutzer.getKeycloakId());

        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        final var rollen = rolleService.mapOrCreateRoles(Set.of(OidcConstants.ROLE_SOZIALDIENST_ADMIN));
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
