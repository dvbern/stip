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

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.authorization.util.AuthorizerUtil;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchstatus.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
@Authorizer
public class GesuchAuthorizer extends BaseAuthorizer {
    private final BenutzerService benutzerService;
    private final GesuchRepository gesuchRepository;
    private final GesuchTrancheRepository gesuchTrancheRepository;
    private final GesuchStatusService gesuchStatusService;
    private final FallRepository fallRepository;
    private final SozialdienstService sozialdienstService;

    @Transactional
    public void canGetBerechnung(final UUID gesuchID) {
        final var gesuch = gesuchRepository.requireById(gesuchID);
        if (!Gesuchstatus.GESUCHSTELLER_CAN_GET_BERECHNUNG.contains(gesuch.getGesuchStatus())) {
            throw new UnauthorizedException();
        }
    }

    @Transactional
    public void canRead(final UUID gesuchId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();

        // Admins and Sachbearbeiter can always read every Gesuch
        if (isAdminSbOrJurist(currentBenutzer)) {
            return;
        }

        final var gesuch = gesuchRepository.requireById(gesuchId);
        if (AuthorizerUtil.hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(gesuch, sozialdienstService)) {
            return;
        }

        // Gesuchsteller can only read their own Gesuch
        if (AuthorizerUtil.isGesuchstellerOfGesuch(currentBenutzer, gesuch)) {
            return;
        }

        throw new UnauthorizedException();
    }

    @Transactional
    public void canUpdate(final UUID gesuchId, final GesuchUpdateDto gesuchUpdateDto) {
        final var gesuchTranche =
            gesuchTrancheRepository.requireById(gesuchUpdateDto.getGesuchTrancheToWorkWith().getId());
        canUpdate(gesuchId, gesuchTranche.getTyp() == GesuchTrancheTyp.AENDERUNG);
    }

    @Transactional
    public void canUpdate(final UUID gesuchId) {
        canUpdate(gesuchId, false);
    }

    @Transactional
    public void canUpdate(final UUID gesuchId, final boolean aenderung) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();

        if (isAdminOrSb(currentBenutzer)) {
            return;
        }

        final var gesuch = gesuchRepository.requireById(gesuchId);

        final BooleanSupplier benutzerCanEditInStatusOrAenderung =
            () -> gesuchStatusService.benutzerCanEdit(currentBenutzer, gesuch.getGesuchStatus()) || aenderung;

        if (
            AuthorizerUtil.hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(gesuch, sozialdienstService)
            && benutzerCanEditInStatusOrAenderung.getAsBoolean()
        ) {
            return;
        } else if (
            AuthorizerUtil.isGesuchstellerOfGesuch(currentBenutzer, gesuch) &&
            benutzerCanEditInStatusOrAenderung.getAsBoolean()
        ) {
            return;
        }

        throw new UnauthorizedException();
    }

    @Transactional
    public void canDelete(final UUID gesuchId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();

        if (isAdminOrSb(currentBenutzer)) {
            return;
        }

        // TODO KSTIP-1057: Check if Gesuchsteller can delete their Gesuch
        final var gesuch = gesuchRepository.requireById(gesuchId);

        if (
            AuthorizerUtil.hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(gesuch, sozialdienstService)
            && gesuch.getGesuchStatus() == Gesuchstatus.IN_BEARBEITUNG_GS
        ) {
            return;
        } else if (
            isGesuchstellerAndNotAdmin(currentBenutzer) &&
            AuthorizerUtil.isGesuchstellerOfGesuch(currentBenutzer, gesuch) &&
            gesuch.getGesuchStatus() == Gesuchstatus.IN_BEARBEITUNG_GS
        ) {
            return;
        }

        throw new UnauthorizedException();
    }

    @Transactional
    public void canCreate() {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var fall = fallRepository.findFallForGsOptional(currentBenutzer.getId());

        if (fall.isEmpty()) {
            return;
        }

        if (
            AuthorizerUtil.hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(fall.get(), sozialdienstService)
        ) {
            return;
        } else if (Objects.equals(currentBenutzer.getId(), fall.get().getGesuchsteller().getId())) {
            return;
        }

        throw new UnauthorizedException();
    }

    @Transactional
    public void canCreateTranche(final UUID gesuchId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();

        if (isAdmin(currentBenutzer)) {
            return;
        }

        final var gesuch = gesuchRepository.requireById(gesuchId);

        if (AuthorizerUtil.hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(gesuch, sozialdienstService)) {
            return;
        } else if (isSachbearbeiter(currentBenutzer) && gesuch.getGesuchStatus() == Gesuchstatus.IN_BEARBEITUNG_SB) {
            return;
        }

        throw new UnauthorizedException();
    }
}
