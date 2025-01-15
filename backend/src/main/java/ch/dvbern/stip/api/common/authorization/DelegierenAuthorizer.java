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
import ch.dvbern.stip.api.common.authorization.util.AuthorizerUtil;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Authorizer
@ApplicationScoped
@RequiredArgsConstructor
public class DelegierenAuthorizer extends BaseAuthorizer {
    private final BenutzerService benutzerService;
    private final GesuchRepository gesuchRepository;

    @Transactional
    public void canDelegate(final UUID gesuchId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        if (!isGesuchsteller(currentBenutzer)) {
            throw new UnauthorizedException();
        }

        final var gesuch = gesuchRepository.requireById(gesuchId);
        if (!AuthorizerUtil.isGesuchstellerOfGesuch(currentBenutzer, gesuch)) {
            throw new UnauthorizedException();
        }
    }
}
