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
import java.util.function.BooleanSupplier;

import ch.dvbern.stip.api.auszahlung.service.ZahlungsverbindungReferenceCheckerService;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.authorization.util.AuthorizerUtil;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import ch.dvbern.stip.generated.dto.AuszahlungUpdateDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;

@Authorizer
@ApplicationScoped
@RequiredArgsConstructor
public class AuszahlungAuthorizer extends BaseAuthorizer {
    private final BenutzerService benutzerService;
    private final SozialdienstService sozialdienstService;
    private final GesuchRepository gesuchRepository;
    private final FallRepository fallRepository;
    private final ZahlungsverbindungReferenceCheckerService zahlungsverbindungReferenceCheckerService;

    @Transactional
    public void canCreateAuszahlungForGesuch(final UUID fallId, final AuszahlungUpdateDto auszahlungUpdateDto) {
        canUpdateAuszahlungForGesuch(fallId, auszahlungUpdateDto);
    }

    @Transactional
    public void canReadAuszahlungForGesuch(final UUID fallId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var fall = fallRepository.requireById(fallId);

        final BooleanSupplier isSB = () -> isSachbearbeiter(currentBenutzer);
        final BooleanSupplier isGesuchstellerOfGesuch =
            () -> AuthorizerUtil.isGesuchstellerOfIgnoreDelegation(fall, currentBenutzer);
        final BooleanSupplier isDelegiertAndIsMitarbeiterOfSozialdienst = () -> AuthorizerUtil
            .hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(fall, sozialdienstService);

        if (
            isSB.getAsBoolean() || isGesuchstellerOfGesuch.getAsBoolean()
            || isDelegiertAndIsMitarbeiterOfSozialdienst.getAsBoolean()
        ) {
            return;
        }

        forbidden();
    }

    @Transactional
    public void canUpdateAuszahlungForGesuch(final UUID fallId, final AuszahlungUpdateDto auszahlungUpdateDto) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var fall = fallRepository.requireById(fallId);

        // update should not be possible in this endpoint if flag = true
        if (auszahlungUpdateDto.getAuszahlungAnSozialdienst()) {
            forbidden();
        }

        preventUpdateIfZahlungsverbindungOfDelegatedSozialdienst(fallId);

        final BooleanSupplier isMitarbeiterAndCanEdit = () -> AuthorizerUtil
            .hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(fall, sozialdienstService);
        final BooleanSupplier isGesuchstellerAndCanEdit =
            () -> AuthorizerUtil.isGesuchstellerOfWithoutDelegierung(currentBenutzer, fall);

        final BooleanSupplier hasDelegierung = () -> fall.getDelegierung() != null;

        if (
            isMitarbeiterAndCanEdit.getAsBoolean()
            || !hasDelegierung.getAsBoolean() && isGesuchstellerAndCanEdit.getAsBoolean()
        ) {
            return;
        }

        forbidden();
    }

    @Transactional
    public void canUpdateFlag(final UUID fallId, final AuszahlungUpdateDto auszahlungUpdateDto) {
        final var fall = fallRepository.requireById(fallId);

        // auszahlung will be created, flag is set to default value (false)
        if (Objects.isNull(fall.getAuszahlung()) && !auszahlungUpdateDto.getAuszahlungAnSozialdienst()) {
            return;
        }

        // auszahlung already exists & flag does not change
        if (
            Objects.nonNull(fall.getAuszahlung())
            && fall.getAuszahlung().isAuszahlungAnSozialdienst() == (auszahlungUpdateDto.getAuszahlungAnSozialdienst())
        ) {
            return;
        }

        canSetFlag(fallId);
    }

    @Transactional
    public void canSetFlag(final UUID fallId) {
        final var fall = fallRepository.requireById(fallId);

        if (
            AuthorizerUtil
                .hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(fall, sozialdienstService)
        ) {
            return;
        }
        throw new BadRequestException();
    }

    private void preventUpdateIfZahlungsverbindungOfDelegatedSozialdienst(final UUID fallId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var fall = fallRepository.requireById(fallId);
        if (
            AuthorizerUtil.isGesuchstellerOfOrDelegatedToSozialdienst(fall, currentBenutzer, sozialdienstService)
            && zahlungsverbindungReferenceCheckerService.isCurrentZahlungsverbindungOfDelegatedSozialdienst(fallId)
        ) {
            forbidden();
        }
    }

}
