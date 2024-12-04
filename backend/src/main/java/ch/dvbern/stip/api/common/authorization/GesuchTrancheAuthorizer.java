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
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
@Authorizer
public class GesuchTrancheAuthorizer extends BaseAuthorizer {
    private final BenutzerService benutzerService;
    private final GesuchTrancheRepository gesuchTrancheRepository;
    private final GesuchRepository gesuchRepository;

    @Transactional
    public void canReadInitialTrancheChanges(final UUID gesuchTrancheId) {
        canRead(gesuchTrancheId);
        final var gesuch = gesuchRepository.requireGesuchByTrancheId(gesuchTrancheId);

        if (!Gesuchstatus.GESUCH_IS_VERFUEGT_OR_FURTHER.contains(gesuch.getGesuchStatus())) {
            throw new ForbiddenException();
        }
    }

    @Transactional
    public void canRead(final UUID gesuchTrancheId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        // Admins can always read every GesuchTranche
        if (isAdminSbOrJurist(currentBenutzer)) {
            return;
        }

        final var gesuch = gesuchRepository.requireGesuchByTrancheId(gesuchTrancheId);
        if (AuthorizerUtil.isGesuchstellerOfGesuch(currentBenutzer, gesuch)) {
            return;
        }

        throw new UnauthorizedException();
    }

    @Transactional
    public void canUpdate(final UUID gesuchTrancheId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();

        if (isAdminOrSb(currentBenutzer)) {
            return;
        }

        final var gesuchTranche = gesuchTrancheRepository.findById(gesuchTrancheId);
        final var gesuch = gesuchRepository.requireGesuchByTrancheId(gesuchTrancheId);
        // Gesuchsteller can only update their Tranchen IN_BEARBEITUNG_GS
        if (
            isGesuchsteller(currentBenutzer) &&
            !isSachbearbeiter(currentBenutzer) &&
            AuthorizerUtil.isGesuchstellerOfGesuch(currentBenutzer, gesuch)
        ) {
            if (gesuchTranche.getStatus() != GesuchTrancheStatus.IN_BEARBEITUNG_GS) {
                throw new UnauthorizedException();
            }
        }
    }

    @Transactional
    public void canEinreichen(final UUID gesuchTrancheId) {
        canRead(gesuchTrancheId);
        final var gesuch = gesuchRepository.requireGesuchByTrancheId(gesuchTrancheId);
        if (!Gesuchstatus.GESUCHSTELLER_CAN_AENDERUNG_EINREICHEN.contains(gesuch.getGesuchStatus())) {
            throw new UnauthorizedException();
        }
    }

    @Transactional
    public void canDeleteAenderung(final UUID gesuchTrancheId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var gesuchTranche = gesuchTrancheRepository.findById(gesuchTrancheId);
        final var gesuch = gesuchRepository.requireGesuchByTrancheId(gesuchTrancheId);

        final var isAuthorizedForCurrentOperation = isGesuchsteller(currentBenutzer) &&
        AuthorizerUtil.isGesuchstellerOfGesuch(currentBenutzer, gesuch);

        // Gesuchsteller can only update their Tranchen IN_BEARBEITUNG_GS
        if (!isAuthorizedForCurrentOperation) {
            throw new UnauthorizedException();
        }

        if (
            (gesuchTranche.getStatus() != GesuchTrancheStatus.IN_BEARBEITUNG_GS) ||
            (gesuchTranche.getTyp() != GesuchTrancheTyp.AENDERUNG)
        ) {
            throw new UnauthorizedException();
        }
    }
}
