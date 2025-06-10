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
import java.util.function.BooleanSupplier;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.authorization.util.AuthorizerUtil;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
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

    @Transactional
    public void canCreateAuszahlungForGesuch(UUID gesuchId) {
        canUpdateAuszahlungForGesuch(gesuchId);
    }

    @Transactional
    public void canReadAuszahlungForGesuch(UUID gesuchId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var gesuch = gesuchRepository.requireById(gesuchId);

        final BooleanSupplier isAdminOrSB = () -> isAdminOrSb(currentBenutzer);
        final BooleanSupplier isGesuchstellerOfGesuch =
            () -> AuthorizerUtil.isGesuchstellerOfGesuch(currentBenutzer, gesuch);
        final BooleanSupplier isDelegiertAndIsMitarbeiterOfSozialdienst = () -> AuthorizerUtil
            .hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(gesuch, sozialdienstService);

        if (
            !isAdminOrSB.getAsBoolean() && isGesuchstellerOfGesuch.getAsBoolean()
            || isDelegiertAndIsMitarbeiterOfSozialdienst.getAsBoolean()
        ) {
            return;
        }

        forbidden();
    }

    @Transactional
    public void canUpdateAuszahlungForGesuch(UUID gesuchId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var gesuch = gesuchRepository.requireById(gesuchId);

        final BooleanSupplier isMitarbeiterAndCanEdit = () -> AuthorizerUtil
            .hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(gesuch, sozialdienstService);
        final BooleanSupplier isGesuchstellerAndCanEdit = () -> isGesuchsteller(currentBenutzer)
        && AuthorizerUtil.isGesuchstellerOfGesuchWithoutDelegierung(currentBenutzer, gesuch);

        final BooleanSupplier hasDelegierung = () -> gesuch.getAusbildung().getFall().getDelegierung() != null;

        if (
            isMitarbeiterAndCanEdit.getAsBoolean()
            || !hasDelegierung.getAsBoolean() && isGesuchstellerAndCanEdit.getAsBoolean()
        ) {
            return;
        }

        forbidden();
    }

}
