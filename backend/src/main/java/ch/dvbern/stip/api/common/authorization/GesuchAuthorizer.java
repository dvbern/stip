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
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.api.gesuchhistory.repository.GesuchHistoryRepository;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranchehistory.repo.GesuchTrancheHistoryRepository;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus.SACHBEARBEITER_CAN_UPDATE_NACHFRIST;

@ApplicationScoped
@RequiredArgsConstructor
@Authorizer
public class GesuchAuthorizer extends BaseAuthorizer {
    private final BenutzerService benutzerService;
    private final GesuchRepository gesuchRepository;
    private final GesuchTrancheRepository gesuchTrancheRepository;
    private final GesuchTrancheHistoryRepository gesuchTrancheHistoryRepository;
    private final FallRepository fallRepository;
    private final SozialdienstService sozialdienstService;
    private final GesuchService gesuchService;
    private final GesuchHistoryRepository gesuchHistoryRepository;

    @Transactional
    public void canGetBerechnung(final UUID gesuchID) {
        final var gesuch = gesuchRepository.requireById(gesuchID);
        if (!Gesuchstatus.SACHBEARBEITER_CAN_GET_BERECHNUNG.contains(gesuch.getGesuchStatus())) {
            forbidden();
        }
    }

    @Transactional
    public void canReadChanges(final UUID gesuchID) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();

        if (isAdminSbOrJurist(currentBenutzer)) {
            return;
        }

        forbidden();
    }

    @Transactional
    protected Gesuch fetchGesuchOfTranche(final UUID trancheId) {
        GesuchTranche tranche = gesuchTrancheHistoryRepository.getLatestVersion(trancheId);
        // fetch the gesuchId of this tranche
        // then fetch current gesuch data
        // (because gesuch of audited tranche could still be in other state than VERFUEGT)
        final var gesuchId = tranche.getGesuch().getId();
        return gesuchRepository.requireById(gesuchId);
    }

    private void canReadGesuchOfTranche(final UUID trancheId) {
        final var gesuch = fetchGesuchOfTranche(trancheId);
        canRead(gesuch.getId());
    }

    @Transactional
    public void canReadInitialTranche(final UUID trancheId) {
        canReadGesuchOfTranche(trancheId);
        final var gesuch = fetchGesuchOfTranche(trancheId);
        if (gesuchHistoryRepository.getLatestWhereStatusChangedTo(gesuch.getId(), Gesuchstatus.VERFUEGT).isEmpty()) {
            throw new BadRequestException();
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
        final BooleanSupplier isMitarbeiter = () -> AuthorizerUtil
            .hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(gesuch, sozialdienstService);

        final BooleanSupplier isGesuchsteller = () -> AuthorizerUtil.isGesuchstellerOfGesuch(currentBenutzer, gesuch);

        // Gesuchsteller can only read their own Gesuch
        if (isMitarbeiter.getAsBoolean() || isGesuchsteller.getAsBoolean()) {
            return;
        }

        forbidden();
    }

    @Transactional
    public void canDelete(final UUID gesuchId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();

        if (isAdminOrSb(currentBenutzer) || isSuperUser(currentBenutzer)) {
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

        forbidden();
    }

    @Transactional
    public void canGesuchEinreichen(final UUID gesuchId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var gesuch = gesuchRepository.requireById(gesuchId);

        final BooleanSupplier isMitarbeiterAndCanEdit = () -> AuthorizerUtil
            .hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(gesuch, sozialdienstService);
        final BooleanSupplier isGesuchstellerAndCanEdit = () -> isGesuchsteller(currentBenutzer)
        && AuthorizerUtil.isGesuchstellerOfGesuchWithoutDelegierung(currentBenutzer, gesuch);
        if (isMitarbeiterAndCanEdit.getAsBoolean() || isGesuchstellerAndCanEdit.getAsBoolean()) {
            return;
        }

        forbidden();
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

        forbidden();
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

        forbidden();
    }

    @Transactional
    public void canUpdateEinreichedatum(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        if (gesuchService.canUpdateEinreichedatum(gesuch)) {
            return;
        }

        forbidden();
    }

    @Transactional
    public void canCreateAenderung(UUID gesuchId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var gesuch = gesuchRepository.requireById(gesuchId);

        final BooleanSupplier isMitarbeiterAndCanEdit = () -> AuthorizerUtil
            .hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(gesuch, sozialdienstService)
        && Gesuchstatus.GESUCHSTELLER_CAN_AENDERUNG_EINREICHEN.contains(gesuch.getGesuchStatus());

        final BooleanSupplier isGesuchstellerAndCanEdit = () -> isGesuchsteller(currentBenutzer)
        && AuthorizerUtil.isGesuchstellerOfGesuchWithoutDelegierung(currentBenutzer, gesuch)
        && Gesuchstatus.GESUCHSTELLER_CAN_AENDERUNG_EINREICHEN.contains(gesuch.getGesuchStatus());

        if (isMitarbeiterAndCanEdit.getAsBoolean() || isGesuchstellerAndCanEdit.getAsBoolean()) {
            return;
        }

        forbidden();
    }

    public void canGetGsDashboard() {
        permitAll();
    }

    public void gsCanGetGesuche() {
        permitAll();
    }

    public void sbCanGetGesuche() {
        permitAll();
    }

    public void sbCanReadTranche(final UUID aenderungId) {
        final var gesuchTranche = gesuchTrancheRepository.requireById(aenderungId);
        canRead(gesuchTranche.getGesuch().getId());
    }

    @Transactional
    public void canUpdateEinreichefrist(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        if (
            !SACHBEARBEITER_CAN_UPDATE_NACHFRIST.contains(gesuch.getGesuchStatus())
            || !isAdminOrSb(benutzerService.getCurrentBenutzer())
        ) {
            forbidden();
        }
    }

    @Transactional
    public void canBearbeitungAbschliessen(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        if (gesuch.getGesuchStatus() != Gesuchstatus.IN_BEARBEITUNG_GS) {
            return;
        }
        forbidden();
    }
}
