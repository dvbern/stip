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

package ch.dvbern.stip.api.beschwerdeentscheid.service;

import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.authorization.Authorizer;
import ch.dvbern.stip.api.common.authorization.BaseAuthorizer;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
@Authorizer
public class BeschwerdeEntscheidAuthorizer extends BaseAuthorizer {
    private final BenutzerService benutzerService;
    private final GesuchRepository gesuchRepository;

    @Transactional
    public void canCreate(final UUID gesuchId) {
        canRead();
        final var gesuch = gesuchRepository.requireById(gesuchId);
        if (!Gesuchstatus.SACHBEARBEITER_CAN_CREATE_BESCHWERDE_ENTSCHEID.contains(gesuch.getGesuchStatus())) {
            forbidden();
        }
    }

    @Transactional
    public void canRead() {
        if (!isAdminOrSb(benutzerService.getCurrentBenutzer())) {
            throw new UnauthorizedException();
        }
    }

}
