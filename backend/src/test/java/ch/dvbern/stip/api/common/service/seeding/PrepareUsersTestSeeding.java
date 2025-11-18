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

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.entity.Sachbearbeiter;
import ch.dvbern.stip.api.benutzer.repo.BenutzerRepository;
import ch.dvbern.stip.api.benutzer.repo.SachbearbeiterRepository;
import ch.dvbern.stip.api.benutzer.type.BenutzerStatus;
import ch.dvbern.stip.api.benutzereinstellungen.entity.Benutzereinstellungen;
import ch.dvbern.stip.api.common.util.Constants;
import ch.dvbern.stip.api.sozialdienstbenutzer.entity.SozialdienstBenutzer;
import ch.dvbern.stip.api.sozialdienstbenutzer.repo.SozialdienstBenutzerRepository;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static ch.dvbern.stip.api.util.TestConstants.DELETE_USER_TEST_ID;
import static ch.dvbern.stip.api.util.TestConstants.FREIGABESTELLE_AND_SACHBEARBEITER_ID;
import static ch.dvbern.stip.api.util.TestConstants.FREIGABESTELLE_ID;
import static ch.dvbern.stip.api.util.TestConstants.GESUCHSTELLER_2_TEST_ID;
import static ch.dvbern.stip.api.util.TestConstants.GESUCHSTELLER_3_TEST_ID;
import static ch.dvbern.stip.api.util.TestConstants.GESUCHSTELLER_TEST_ID;
import static ch.dvbern.stip.api.util.TestConstants.JURIST_ID;
import static ch.dvbern.stip.api.util.TestConstants.SACHBEARBEITER_ADMIN_ID;
import static ch.dvbern.stip.api.util.TestConstants.SACHBEARBEITER_ID;
import static ch.dvbern.stip.api.util.TestConstants.SOZIALDIENST_ADMIN_ID;
import static ch.dvbern.stip.api.util.TestConstants.SOZIALDIENST_MITARBEITER_ID;
import static ch.dvbern.stip.api.util.TestConstants.SUPER_USER_ID;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class PrepareUsersTestSeeding extends Seeder {
    private final BenutzerRepository benutzerRepository;
    private final SachbearbeiterRepository sachbearbeiterRepository;
    private final SozialdienstBenutzerRepository sozialdienstBenutzerRepository;

    @Override
    protected void seed() {
        final var gsToInitialize = List.of(
            GESUCHSTELLER_TEST_ID,
            GESUCHSTELLER_2_TEST_ID,
            GESUCHSTELLER_3_TEST_ID,
            DELETE_USER_TEST_ID
        );

        final var sbToInitialize = List.of(
            SACHBEARBEITER_ID,
            SACHBEARBEITER_ADMIN_ID,
            FREIGABESTELLE_ID,
            FREIGABESTELLE_AND_SACHBEARBEITER_ID,
            JURIST_ID,
            SUPER_USER_ID
        );

        final var sozialdienstBenutzerIds = List.of(
            SOZIALDIENST_ADMIN_ID,
            SOZIALDIENST_MITARBEITER_ID
        );

        benutzerRepository.persist(
            gsToInitialize.stream()
                .map(
                    keycloakId -> new Benutzer()
                        .setKeycloakId(keycloakId)
                        .setVorname("")
                        .setNachname("Gesuchsteller")
                        .setBenutzerStatus(BenutzerStatus.AKTIV)
                        .setBenutzereinstellungen(new Benutzereinstellungen())
                )
        );

        sachbearbeiterRepository.persist(sbToInitialize.stream().map(keycloakId -> {
            final var sachbearbeiter = new Sachbearbeiter();
            sachbearbeiter
                .setFunktionDe("Sachbearbeiter")
                .setFunktionFr("Sachbearbeiter")
                .setTelefonnummer("+41 31 000 00 00")
                .setEmail(Constants.DVB_MAILBUCKET_MAIL)
                .setBenutzerStatus(BenutzerStatus.AKTIV)
                .setBenutzereinstellungen(new Benutzereinstellungen())
                .setKeycloakId(keycloakId)
                .setVorname("")
                .setNachname("Sachbearbeiter");
            return sachbearbeiter;
        }
        ));

        sozialdienstBenutzerRepository.persist(sozialdienstBenutzerIds.stream().map(keycloakId -> {
            final var sozialdienstBenutzer = new SozialdienstBenutzer();
            sozialdienstBenutzer
                .setKeycloakId(keycloakId)
                .setVorname("")
                .setNachname("SozialdienstBenutzer")
                .setBenutzerStatus(BenutzerStatus.AKTIV)
                .setBenutzereinstellungen(new Benutzereinstellungen());
            return sozialdienstBenutzer;
        }));
    }

    protected List<String> getProfiles() {
        return List.of("test");
    }
}
