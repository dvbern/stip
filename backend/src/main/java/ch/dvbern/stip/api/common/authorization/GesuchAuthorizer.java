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
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.api.gesuchstatus.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.service.GesuchTrancheStatusService;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus.GESUCHSTELLER_CAN_EDIT;

@ApplicationScoped
@RequiredArgsConstructor
@Authorizer
public class GesuchAuthorizer extends BaseAuthorizer {
    private final BenutzerService benutzerService;
    private final GesuchRepository gesuchRepository;
    private final GesuchTrancheRepository gesuchTrancheRepository;
    private final GesuchStatusService gesuchStatusService;
    private final GesuchTrancheStatusService gesuchTrancheStatusService;
    private final FallRepository fallRepository;
    private final SozialdienstService sozialdienstService;
    private final GesuchService gesuchService;

    @Transactional
    public void canGetBerechnung(final UUID gesuchID) {
        final var gesuch = gesuchRepository.requireById(gesuchID);
        if (!Gesuchstatus.SACHBEARBEITER_CAN_GET_BERECHNUNG.contains(gesuch.getGesuchStatus())) {
            throw new UnauthorizedException();
        }
    }

    @Transactional
    public void canReadChanges(final UUID gesuchID) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();

        if (isAdminOrSb(currentBenutzer)) {
            return;
        }
        throw new UnauthorizedException();
    }

    @Transactional
    public void canRead(final UUID gesuchId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();

        // Admins and Sachbearbeiter can always read every Gesuch
        if (isAdminSbOrJurist(currentBenutzer)) {
            return;
        }

        final var gesuch = gesuchRepository.requireById(gesuchId);
        final BooleanSupplier isMitarbeiter = () -> AuthorizerUtil
            .hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(gesuch, sozialdienstService);

        final BooleanSupplier isGesuchsteller = () -> AuthorizerUtil.isGesuchstellerOfGesuch(currentBenutzer, gesuch);

        // Gesuchsteller can only read their own Gesuch
        if (isMitarbeiter.getAsBoolean() || isGesuchsteller.getAsBoolean()) {
            return;
        }

        throw new UnauthorizedException();
    }

    @Transactional
    public void canUpdateTranche(final UUID gesuchId, final GesuchUpdateDto gesuchUpdateDto) {
        final var gesuchTranche =
            gesuchTrancheRepository.requireById(gesuchUpdateDto.getGesuchTrancheToWorkWith().getId());
        if (gesuchTranche.getTyp() == GesuchTrancheTyp.AENDERUNG) {
            canUpdateAenderung(gesuchId, gesuchTranche);
        } else {
            canUpdateNormalTranche(gesuchId);

        }
    }

    @Transactional
    public void canUpdateTranche(final UUID gesuchId) {
        canUpdateNormalTranche(gesuchId);
    }

    @Transactional
    public void canUpdateAenderung(final UUID gesuchId, final GesuchTranche gesuchTranche) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var gesuch = gesuchRepository.requireById(gesuchId);

        final BooleanSupplier benutzerCanEditAenderung = () -> gesuchTrancheStatusService.benutzerCanEdit(
            currentBenutzer,
            gesuchTranche
                .getStatus()
        );

        final BooleanSupplier benutzerCanEditInStatusOrAenderung =
            () -> gesuchStatusService.benutzerCanEdit(currentBenutzer, gesuch.getGesuchStatus())
            || benutzerCanEditAenderung.getAsBoolean();

        final BooleanSupplier isAdminOrSBCanEdit =
            () -> isAdminOrSb(currentBenutzer) && !GESUCHSTELLER_CAN_EDIT.contains(gesuchTranche.getStatus())
            && benutzerCanEditInStatusOrAenderung.getAsBoolean();

        final BooleanSupplier isMitarbeiterAndCanEdit = () -> AuthorizerUtil
            .hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(gesuch, sozialdienstService)
        && benutzerCanEditInStatusOrAenderung.getAsBoolean();

        final BooleanSupplier isGesuchstellerAndCanEdit =
            () -> AuthorizerUtil.isGesuchstellerOfGesuchWithoutDelegierung(currentBenutzer, gesuch)
            && benutzerCanEditInStatusOrAenderung.getAsBoolean();

        if (isMitarbeiterAndCanEdit.getAsBoolean() || isGesuchstellerAndCanEdit.getAsBoolean()) {
            return;
        }

        if (isAdminOrSBCanEdit.getAsBoolean()) {
            return;
        }

        throw new UnauthorizedException();
    }

    @Transactional
    public void canUpdateNormalTranche(final UUID gesuchId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var gesuch = gesuchRepository.requireById(gesuchId);

        // TODO KSTIP-1967: Authorizer aktualisieren
        if (isAdminOrSb(currentBenutzer)) {
            return;
        }

        final BooleanSupplier benutzerCanEditInStatusOrAenderung =
            () -> gesuchStatusService.benutzerCanEdit(currentBenutzer, gesuch.getGesuchStatus());

        final BooleanSupplier isMitarbeiterAndCanEdit = () -> AuthorizerUtil
            .hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(gesuch, sozialdienstService)
        && benutzerCanEditInStatusOrAenderung.getAsBoolean();

        final BooleanSupplier isGesuchstellerAndCanEdit =
            () -> AuthorizerUtil.isGesuchstellerOfGesuchWithoutDelegierung(currentBenutzer, gesuch)
            && benutzerCanEditInStatusOrAenderung.getAsBoolean();

        if (isMitarbeiterAndCanEdit.getAsBoolean() || isGesuchstellerAndCanEdit.getAsBoolean()) {
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

        final BooleanSupplier isMitarbeiterAndCanEdit = () -> AuthorizerUtil
            .hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(gesuch, sozialdienstService)
        && gesuch.getGesuchStatus() == Gesuchstatus.IN_BEARBEITUNG_GS;

        final BooleanSupplier isGesuchstellerAndCanEdit = () -> isGesuchstellerAndNotAdmin(currentBenutzer)
        && AuthorizerUtil.isGesuchstellerOfGesuchWithoutDelegierung(currentBenutzer, gesuch)
        && gesuch.getGesuchStatus() == Gesuchstatus.IN_BEARBEITUNG_GS;

        if (isMitarbeiterAndCanEdit.getAsBoolean() || isGesuchstellerAndCanEdit.getAsBoolean()) {
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

        final BooleanSupplier isMitarbeiterAndCanEdit = () -> AuthorizerUtil
            .hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(fall.get(), sozialdienstService);

        final BooleanSupplier isGesuchstellerAndCanEdit =
            () -> Objects.equals(currentBenutzer.getId(), fall.get().getGesuchsteller().getId());

        if (isMitarbeiterAndCanEdit.getAsBoolean() || isGesuchstellerAndCanEdit.getAsBoolean()) {
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

        final BooleanSupplier isMitarbeiterAndCanEdit = () -> AuthorizerUtil
            .hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(gesuch, sozialdienstService);

        final BooleanSupplier isGesuchstellerAndCanEdit =
            () -> isSachbearbeiter(currentBenutzer) && gesuch.getGesuchStatus() == Gesuchstatus.IN_BEARBEITUNG_SB;

        if (isMitarbeiterAndCanEdit.getAsBoolean() || isGesuchstellerAndCanEdit.getAsBoolean()) {
            return;
        }

        throw new UnauthorizedException();
    }

    @Transactional
    public void canUpdateEinreichedatum(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        if (gesuchService.canUpdateEinreichedatum(gesuch)) {
            return;
        }

        throw new UnauthorizedException();
    }

    @Transactional
    public void canCreateAenderung(UUID gesuchId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var gesuch = gesuchRepository.requireById(gesuchId);

        final BooleanSupplier isMitarbeiterAndCanEdit = () -> AuthorizerUtil
            .hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(gesuch, sozialdienstService)
        && Gesuchstatus.GESUCHSTELLER_CAN_AENDERUNG_EINREICHEN.contains(gesuch.getGesuchStatus());

        final BooleanSupplier isGesuchstellerAndCanEdit = () -> isGesuchstellerAndNotAdmin(currentBenutzer)
        && AuthorizerUtil.isGesuchstellerOfGesuchWithoutDelegierung(currentBenutzer, gesuch)
        && Gesuchstatus.GESUCHSTELLER_CAN_AENDERUNG_EINREICHEN.contains(gesuch.getGesuchStatus());

        if (isMitarbeiterAndCanEdit.getAsBoolean() || isGesuchstellerAndCanEdit.getAsBoolean()) {
            return;
        }

        if (isMitarbeiterAndCanEdit.getAsBoolean() || isGesuchstellerAndCanEdit.getAsBoolean()) {
            return;
        }

        throw new UnauthorizedException();
    }
}
