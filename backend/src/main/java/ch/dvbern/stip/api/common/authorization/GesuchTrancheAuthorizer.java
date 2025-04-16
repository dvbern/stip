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
import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchstatus.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.service.GesuchTrancheStatusService;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
@Authorizer
public class GesuchTrancheAuthorizer extends BaseAuthorizer {
    private final BenutzerService benutzerService;
    private final GesuchTrancheRepository gesuchTrancheRepository;
    private final GesuchRepository gesuchRepository;
    private final SozialdienstService sozialdienstService;
    private final GesuchStatusService gesuchStatusService;
    private final GesuchTrancheStatusService gesuchTrancheStatusService;

    @Transactional
    public void canRead(final UUID gesuchTrancheId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        // Admins can always read every GesuchTranche
        if (isAdminSbOrJurist(currentBenutzer)) {
            return;
        }

        final var gesuch = gesuchRepository.requireGesuchByTrancheId(gesuchTrancheId);
        if (AuthorizerUtil.hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(gesuch, sozialdienstService)) {
            return;
        }

        if (AuthorizerUtil.isGesuchstellerOfGesuch(currentBenutzer, gesuch)) {
            return;
        }

        forbidden();
    }

    @Transactional
    public void canUpdateTrancheStatus(final UUID gesuchTrancheId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();

        if (isAdminOrSb(currentBenutzer)) {
            return;
        }

        final var gesuchTranche = gesuchTrancheRepository.findById(gesuchTrancheId);
        final var gesuch = gesuchRepository.requireGesuchByTrancheId(gesuchTrancheId);
        if (
            AuthorizerUtil.hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(gesuch, sozialdienstService) &&
            gesuchTranche.getStatus() == GesuchTrancheStatus.IN_BEARBEITUNG_GS
        ) {
            return;
        } else if (
            isGesuchsteller(currentBenutzer) &&
            !isSachbearbeiter(currentBenutzer) &&
            AuthorizerUtil.isGesuchstellerOfGesuchWithoutDelegierung(currentBenutzer, gesuch) &&
            gesuchTranche.getStatus() == GesuchTrancheStatus.IN_BEARBEITUNG_GS
        ) {
            return;
        }

        forbidden();
    }

    @Transactional
    public void canUpdateTranche(final GesuchTranche gesuchTranche) {

        if (gesuchTranche.getTyp() == GesuchTrancheTyp.AENDERUNG) {
            canUpdateAenderung(gesuchTranche);
        } else {
            canUpdateNormalTranche(gesuchTranche);

        }
    }

    @Transactional
    public void canUpdateAenderung(final GesuchTranche gesuchTranche) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var gesuch = gesuchTranche.getGesuch();

        final BooleanSupplier benutzerCanEditAenderung = () -> gesuchTrancheStatusService.benutzerCanEdit(
            currentBenutzer,
            gesuchTranche
                .getStatus()
        );

        if (!benutzerCanEditAenderung.getAsBoolean()) {
            forbidden();
        }

        final BooleanSupplier isGesuchstellerOfGesuch =
            () -> (AuthorizerUtil.isGesuchstellerOfGesuchWithoutDelegierung(currentBenutzer, gesuch));

        final BooleanSupplier isMitarbeiterOfSozialdienst = () -> AuthorizerUtil
            .hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(gesuch, sozialdienstService);

        if (
            isMitarbeiterOfSozialdienst.getAsBoolean()
            || isGesuchstellerOfGesuch.getAsBoolean()
        ) {
            return;
        }

        if (isAdminOrSb(currentBenutzer)) {
            return;
        }

        forbidden();
    }

    private void canUpdateNormalTranche(final GesuchTranche gesuchTranche) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var gesuch = gesuchTranche.getGesuch();

        if (
            isAdminOrSb(currentBenutzer)
            && gesuchStatusService.benutzerCanEdit(currentBenutzer, gesuch.getGesuchStatus())
        ) {
            return;
        }

        final BooleanSupplier benutzerCanEdit =
            () -> gesuchStatusService.benutzerCanEdit(currentBenutzer, gesuch.getGesuchStatus());

        final BooleanSupplier isMitarbeiterAndCanEdit = () -> AuthorizerUtil
            .hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(gesuch, sozialdienstService)
        && benutzerCanEdit.getAsBoolean();

        final BooleanSupplier isGesuchstellerAndCanEdit =
            () -> AuthorizerUtil.isGesuchstellerOfGesuchWithoutDelegierung(currentBenutzer, gesuch)
            && benutzerCanEdit.getAsBoolean();

        if (isMitarbeiterAndCanEdit.getAsBoolean() || isGesuchstellerAndCanEdit.getAsBoolean()) {
            return;
        }

        forbidden();
    }

    @Transactional
    public void canFehlendeDokumenteUebermitteln(final UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheRepository.findById(gesuchTrancheId);
        final var gesuch = gesuchRepository.requireGesuchByTrancheId(gesuchTrancheId);

        var someGesuchDokumentsNotAcceptedOrRejected = gesuch.getGesuchTranchen()
            .stream()
            .anyMatch(
                gesuchTranche1 -> gesuchTranche1.getGesuchDokuments()
                    .stream()
                    .anyMatch(
                        gesuchDokument -> gesuchDokument.getStatus() == Dokumentstatus.AUSSTEHEND
                        && !gesuchDokument.getDokumente().isEmpty()
                    )
            );

        if (someGesuchDokumentsNotAcceptedOrRejected) {
            throw new ForbiddenException();
        }
        final var currentBenutzer = benutzerService.getCurrentBenutzer();

        if (!isAdminOrSb(currentBenutzer)) {
            throw new UnauthorizedException();
        }

        if (
            (Objects.requireNonNull(gesuchTranche.getTyp()) == GesuchTrancheTyp.TRANCHE)
            && (gesuch.getGesuchStatus() == Gesuchstatus.IN_BEARBEITUNG_SB)
        ) {
            return;
        }
        if (
            (gesuchTranche.getTyp() == GesuchTrancheTyp.AENDERUNG)
            && (gesuchTranche.getStatus() == GesuchTrancheStatus.UEBERPRUEFEN)
        ) {
            return;
        }

        throw new UnauthorizedException();
    }

    @Transactional
    public void canAenderungEinreichen(final UUID gesuchTrancheId) {
        canRead(gesuchTrancheId);
        canUpdateAenderung(gesuchTrancheRepository.requireAenderungById(gesuchTrancheId));
        final var aenderung = gesuchTrancheRepository.requireAenderungById(gesuchTrancheId);
        if (!GesuchTrancheStatus.GESUCHSTELLER_CAN_AENDERUNG_EINREICHEN.contains(aenderung.getStatus())) {
            forbidden();
        }
    }

    @Transactional
    public void canDeleteAenderung(final UUID gesuchTrancheId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var gesuchTranche = gesuchTrancheRepository.findById(gesuchTrancheId);
        final var gesuch = gesuchRepository.requireGesuchByTrancheId(gesuchTrancheId);

        final var isAuthorizedForCurrentOperation =
            (isGesuchsteller(currentBenutzer)
            && AuthorizerUtil.isGesuchstellerOfGesuchWithoutDelegierung(currentBenutzer, gesuch)) ||
            AuthorizerUtil.hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(gesuch, sozialdienstService);

        // Gesuchsteller can only update their Tranchen IN_BEARBEITUNG_GS
        if (!isAuthorizedForCurrentOperation) {
            forbidden();
        }

        if (
            (gesuchTranche.getStatus() != GesuchTrancheStatus.IN_BEARBEITUNG_GS) ||
            (gesuchTranche.getTyp() != GesuchTrancheTyp.AENDERUNG)
        ) {
            forbidden();
        }
    }

    @Transactional
    public void canAenderungFehlendeDokumenteEinreichen(final UUID gesuchTrancheId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var gesuchTranche = gesuchTrancheRepository.findById(gesuchTrancheId);
        if (
            !(AuthorizerUtil.hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(
                gesuchTranche.getGesuch(),
                sozialdienstService
            )
            || AuthorizerUtil.isGesuchstellerOfGesuchWithoutDelegierung(currentBenutzer, gesuchTranche.getGesuch()))
        ) {
            throw new UnauthorizedException();
        }
        if (gesuchTranche.getStatus() != GesuchTrancheStatus.FEHLENDE_DOKUMENTE) {
            throw new ForbiddenException();
        }
    }

    @Transactional
    public void canFehlendeDokumenteEinreichen(final UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheRepository.findById(gesuchTrancheId);
        if (
            gesuchTranche.getGesuchDokuments()
                .stream()
                .anyMatch(
                    gesuchDokument -> gesuchDokument.getStatus().equals(Dokumentstatus.AUSSTEHEND)
                    && gesuchDokument.getDokumente().isEmpty()
                )
        ) {
            forbidden();
        }
    }
}
