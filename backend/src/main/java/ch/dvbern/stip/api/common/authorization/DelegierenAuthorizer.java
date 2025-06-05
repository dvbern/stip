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
import ch.dvbern.stip.api.delegieren.repo.DelegierungRepository;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import ch.dvbern.stip.api.sozialdienstbenutzer.repo.SozialdienstBenutzerRepository;
import ch.dvbern.stip.api.sozialdienstbenutzer.service.SozialdienstBenutzerService;
import ch.dvbern.stip.generated.dto.DelegierterMitarbeiterAendernDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Authorizer
@ApplicationScoped
@RequiredArgsConstructor
public class DelegierenAuthorizer extends BaseAuthorizer {
    private final BenutzerService benutzerService;
    private final SozialdienstBenutzerService sozialdienstBenutzerService;
    private final FallRepository fallRepository;
    private final DelegierungRepository delegierungRepository;
    private final SozialdienstService sozialdienstService;
    private final SozialdienstBenutzerRepository sozialdienstBenutzerRepository;

    @Transactional
    public void canReadDelegierung() {
        permitAll();
    }

    @Transactional
    public void canReadFallDashboard(final UUID fallId) {
        final Fall fall = fallRepository.requireById(fallId);
        if (fall.getDelegierung() == null) {
            forbidden();
        }

        if (
            sozialdienstService
                .isCurrentBenutzerMitarbeiterOfSozialdienst(fall.getDelegierung().getSozialdienst().getId())
        ) {
            return;
        }

        forbidden();
    }

    @Transactional
    public void canDelegate(final UUID fallId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var fall = fallRepository.requireById(fallId);
        if (!AuthorizerUtil.isGesuchstellerOfWithoutDelegierung(currentBenutzer, fall)) {
            forbidden();
        }
    }

    public void canDelegierterMitarbeiterAendern(
        final UUID delegierungId,
        final DelegierterMitarbeiterAendernDto delegierterMitarbeiterAendernDto
    ) {
        final var delegierung = delegierungRepository.requireById(delegierungId);
        final var targetUser = sozialdienstBenutzerRepository.requireById(
            delegierterMitarbeiterAendernDto.getMitarbeiterId()
        );

        if (
            sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(delegierung.getSozialdienst().getId())
            && sozialdienstService
                .isBenutzerMitarbeiterOfSozialdienst(delegierung.getSozialdienst().getId(), targetUser)
        ) {
            return;
        }

        forbidden();
    }
}
