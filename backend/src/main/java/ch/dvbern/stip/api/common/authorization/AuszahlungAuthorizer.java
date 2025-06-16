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
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.authorization.util.AuthorizerUtil;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import ch.dvbern.stip.generated.dto.AuszahlungUpdateDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Authorizer
@ApplicationScoped
@RequiredArgsConstructor
public class AuszahlungAuthorizer extends BaseAuthorizer {
    private final BenutzerService benutzerService;
    private final SozialdienstService sozialdienstService;
    private final GesuchRepository gesuchRepository;
    private final FallRepository fallRepository;

    @Transactional
    public void canCreateAuszahlungForGesuch(final UUID fallId, final AuszahlungUpdateDto auszahlungUpdateDto) {
        canUpdateAuszahlungForGesuch(fallId, auszahlungUpdateDto);
    }

    @Transactional
    public void canReadAuszahlungForGesuch(final UUID fallId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var fall = fallRepository.requireById(fallId);

        if (isSachbearbeiter(currentBenutzer)) {
            return;
        }
        if (
            AuthorizerUtil.isGesuchstellerOfOrDelegatedToSozialdienst(
                fall,
                benutzerService.getCurrentBenutzer(),
                sozialdienstService
            )
        ) {
            return;
        }

        forbidden();
    }

    @Transactional
    public void canUpdateAuszahlungForGesuch(final UUID fallId, final AuszahlungUpdateDto auszahlungUpdateDto) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var fall = fallRepository.requireById(fallId);

        if (
            !AuthorizerUtil
                .canWriteAndIsGesuchstellerOfOrDelegatedToSozialdienst(fall, currentBenutzer, sozialdienstService)
        ) {
            forbidden();
        }

        if (
            !auszahlungUpdateDto.getAuszahlungAnSozialdienst()
            || Objects.isNull(auszahlungUpdateDto.getZahlungsverbindung())
        ) {
            return;
        }

        forbidden();
    }

    @Transactional
    public void canSetFlag(final UUID fallId, boolean auszahlungAnSozialdienst) {
        final var fall = fallRepository.requireById(fallId);

        if (
            !auszahlungAnSozialdienst ||
            AuthorizerUtil
                .hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(fall, sozialdienstService)
        ) {
            return;
        }
        forbidden();
    }

}
