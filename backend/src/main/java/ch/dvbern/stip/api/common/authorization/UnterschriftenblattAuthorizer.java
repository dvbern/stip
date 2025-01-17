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

package ch.dvbern.stip.api.common.authorization;

import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.service.GesuchStatusService;
import io.quarkus.security.ForbiddenException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Authorizer
@ApplicationScoped
@RequiredArgsConstructor
public class UnterschriftenblattAuthorizer extends BaseAuthorizer {
    private final GesuchRepository gesuchRepository;
    private final GesuchStatusService gesuchStatusService;
    private final BenutzerService benutzerService;

    @Transactional
    public void canUpload(final UUID gesuchId) {
        final var benutzer = benutzerService.getCurrentBenutzer();
        if (!isAdminOrSb(benutzer)) {
            throw new ForbiddenException();
        }

        final var gesuch = gesuchRepository.requireById(gesuchId);
        if (!gesuchStatusService.canUploadUnterschriftenblatt(benutzer, gesuch.getGesuchStatus())) {
            throw new ForbiddenException();
        }
    }
}
