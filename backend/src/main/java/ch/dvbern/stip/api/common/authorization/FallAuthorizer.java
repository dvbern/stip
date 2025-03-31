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

import java.util.Objects;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.authorization.util.AuthorizerUtil;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
@Authorizer
public class FallAuthorizer extends BaseAuthorizer {
    private final BenutzerService benutzerService;
    private final FallRepository fallRepository;
    private final SozialdienstService sozialdienstService;

    public void canCreate() {
        permitAll();
    }

    @Transactional
    public void sbCanGet() {
        if (isAdminSbOrJurist(benutzerService.getCurrentBenutzer())) {
            return;
        }

        forbidden();
    }

    @Transactional
    public void gsCanGet() {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        if (!isGesuchsteller(currentBenutzer)) {
            forbidden();
        }

        final var fallOpt = fallRepository.findFallForGsOptional(currentBenutzer.getId());
        if (fallOpt.isEmpty()) {
            return;
        }

        final var fall = fallOpt.get();
        if (AuthorizerUtil.hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(fall, sozialdienstService)) {
            return;
        }

        // Gesuchsteller can only read their own Fall
        if (Objects.equals(fall.getGesuchsteller().getId(), currentBenutzer.getId())) {
            return;
        }

        forbidden();
    }
}
