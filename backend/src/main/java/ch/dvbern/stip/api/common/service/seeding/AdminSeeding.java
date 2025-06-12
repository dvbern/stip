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

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.entity.Rolle;
import ch.dvbern.stip.api.benutzer.repo.BenutzerRepository;
import ch.dvbern.stip.api.benutzer.repo.RolleRepository;
import ch.dvbern.stip.api.benutzer.type.BenutzerStatus;
import ch.dvbern.stip.api.benutzereinstellungen.entity.Benutzereinstellungen;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.config.service.ConfigService;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Singleton
@RequiredArgsConstructor
@Slf4j
public class AdminSeeding extends Seeder {
    private final ConfigService configService;
    private final BenutzerRepository benutzerRepository;
    private final RolleRepository rolleRepository;

    @Override
    protected void seed() {
        LOG.info("Seeding Admin users");
        final var foundAdmin = benutzerRepository.findByRolle(OidcConstants.ROLE_ADMIN).findFirst();
        if (foundAdmin.isEmpty()) {
            final var adminRolle = getOrCreateRolle(OidcConstants.ROLE_ADMIN);
            final var sachbearbeiterRolle = getOrCreateRolle(OidcConstants.ROLE_SACHBEARBEITER);

            final var benutzer = new Benutzer()
                .setNachname("Seeding")
                .setVorname("Admin")
                .setBenutzerStatus(BenutzerStatus.AKTIV)
                .setRollen(Set.of(adminRolle, sachbearbeiterRolle))
                .setBenutzereinstellungen(new Benutzereinstellungen().setDigitaleKommunikation(true));
            benutzerRepository.persistAndFlush(benutzer);
        }
    }

    @Override
    protected List<String> getProfiles() {
        return configService.getSeedTestcasesOnProfile();
    }

    private Rolle getOrCreateRolle(final String rolle) {
        final var foundRoles = rolleRepository.findByKeycloakIdentifier(Set.of(rolle));
        Rolle foundOrCreatedRole;
        if (!foundRoles.isEmpty()) {
            foundOrCreatedRole = foundRoles.stream().findFirst().get();
        } else {
            foundOrCreatedRole = new Rolle().setKeycloakIdentifier(rolle);
            rolleRepository.persistAndFlush(foundOrCreatedRole);
        }

        return foundOrCreatedRole;
    }
}
