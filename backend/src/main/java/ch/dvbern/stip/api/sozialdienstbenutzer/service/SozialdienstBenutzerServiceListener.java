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

package ch.dvbern.stip.api.sozialdienstbenutzer.service;

import ch.dvbern.stip.api.benutzer.service.KeycloakBenutzerService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class SozialdienstBenutzerServiceListener {
    private final KeycloakBenutzerService keycloakBenutzerService;

    void onAfterEndTransaction(
        @Observes(during = TransactionPhase.AFTER_FAILURE) final SozialdienstBenutzerCreated event
    ) {
        LOG.info("Deleting keycloak user with id {} because transaction failed", event.getKeycloakId());
        keycloakBenutzerService.deleteKeycloakBenutzer(event.getKeycloakId());
    }
}
