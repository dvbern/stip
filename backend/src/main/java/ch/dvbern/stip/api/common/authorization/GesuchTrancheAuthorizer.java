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

import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.authorization.util.AuthorizerUtil;
import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchstatus.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.service.GesuchTrancheStatusService;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.gesuchtranchehistory.service.GesuchTrancheHistoryService;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
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
    private final GesuchTrancheHistoryService gesuchTrancheHistoryService;

    @Transactional
    public void sbCanReadInitialTranche(final UUID trancheId) {
        GesuchTranche tranche = gesuchTrancheHistoryService.getLatestTranche(trancheId);
        Gesuch gesuch = gesuchRepository.requireById(tranche.getGesuch().getId());
        if (!gesuch.isVerfuegt()) {
            throw new BadRequestException();
        }
    }

    @Transactional
    public void sbCanRead() {
        permitAll();
    }

    @Transactional
    public void sbOrJuristCanRead() {
        permitAll();
    }

    @Transactional
    public void gsCanRead(final UUID gesuchTrancheId) {
        assertIsGesuchstellerOfGesuchTrancheOrDelegatedToSozialdienst(gesuchTrancheId);
    }

    @Transactional
    public void canDeleteAenderung(final UUID gesuchTrancheId) {
        assertIsGesuchstellerOfGesuchTrancheOrDelegatedToSozialdienst(gesuchTrancheId);
        assertGesuchTrancheIsOfTyp(gesuchTrancheId, GesuchTrancheTyp.AENDERUNG);
        assertGesuchTrancheIsInGesuchStatus(gesuchTrancheId, GesuchTrancheStatus.IN_BEARBEITUNG_GS);
    }

    @Transactional
    public void canAenderungEinreichen(final UUID gesuchTrancheId) {
        assertGesuchTrancheIsInOneOfGesuchStatus(
            gesuchTrancheId,
            GesuchTrancheStatus.GESUCHSTELLER_CAN_AENDERUNG_EINREICHEN
        );
        if (isSbOrJurist(benutzerService.getCurrentBenutzer())) {
            return;
        }

        gsCanRead(gesuchTrancheId);
    }

    @Transactional
    public void canFehlendeDokumenteEinreichen(final UUID gesuchTrancheId) {
        assertIsGesuchstellerOfGesuchTrancheOrDelegatedToSozialdienst(gesuchTrancheId);
        assertGesuchTrancheIsInGesuchStatus(gesuchTrancheId, GesuchTrancheStatus.FEHLENDE_DOKUMENTE);

        final var gesuchTranche = gesuchTrancheRepository.findById(gesuchTrancheId);

        if (
            gesuchTranche.getGesuchDokuments()
                .stream()
                .noneMatch(
                    gesuchDokument -> gesuchDokument.getStatus().equals(Dokumentstatus.AUSSTEHEND)
                    && gesuchDokument.getDokumente().isEmpty()
                )
        ) {
            return;
        }
        forbidden();
    }

    @Transactional
    public void canFehlendeDokumenteUebermitteln(final UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheRepository.findById(gesuchTrancheId);

        switch (gesuchTranche.getTyp()) {
            case AENDERUNG -> assertGesuchTrancheIsInGesuchStatus(gesuchTrancheId, GesuchTrancheStatus.UEBERPRUEFEN);
            default -> throw new BadRequestException();
        }

        final var gesuch = gesuchRepository.requireGesuchByTrancheId(gesuchTrancheId);

        if (
            gesuch.getGesuchTranchen()
                .stream()
                .flatMap(gesuchTranche1 -> gesuchTranche1.getGesuchDokuments().stream())
                .noneMatch(
                    gesuchDokument -> gesuchDokument.getStatus() == Dokumentstatus.AUSSTEHEND
                    && !gesuchDokument.getDokumente().isEmpty()
                )
        ) {
            return;
        }
        forbidden();
    }

    @Transactional
    public void canAenderungAkzeptieren(final UUID gesuchTrancheId) {
        sbCanUpdateTrancheStatus(gesuchTrancheId);
    }

    @Transactional
    public void canAenderungAblehnen(final UUID gesuchTrancheId) {
        sbCanUpdateTrancheStatus(gesuchTrancheId);
    }

    @Transactional
    public void canAenderungManuellAnpassen(final UUID gesuchTrancheId) {
        sbCanUpdateTrancheStatus(gesuchTrancheId);
    }

    @Transactional
    public void sbCanUpdateTrancheStatus(final UUID gesuchTrancheId) {
        assertGesuchTrancheIsInGesuchStatus(gesuchTrancheId, GesuchTrancheStatus.UEBERPRUEFEN);
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
        assertBenutzerCanEditGesuchTranche(gesuchTranche.getId());

        if (isSachbearbeiter(benutzerService.getCurrentBenutzer())) {
            return;
        }

        assertIsGesuchstellerOfGesuchTrancheOrDelegatedToSozialdienst(gesuchTranche.getId());
    }

    private void canUpdateNormalTranche(final GesuchTranche gesuchTranche) {
        assertBenutzerCanEditGesuch(gesuchTranche.getGesuch().getId());

        if (isSbOrJurist(benutzerService.getCurrentBenutzer())) {
            return;
        }

        assertIsGesuchstellerOfGesuchTrancheOrDelegatedToSozialdienst(gesuchTranche.getId());
    }

    public void assertBenutzerCanEditGesuchTranche(final UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);
        if (
            gesuchTrancheStatusService.benutzerCanEdit(
                benutzerService.getCurrentBenutzer(),
                gesuchTranche
                    .getStatus()
            )
        ) {
            return;
        }
        forbidden();
    }

    public void assertBenutzerCanEditGesuch(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        if (
            gesuchStatusService.benutzerCanEdit(
                benutzerService.getCurrentBenutzer(),
                gesuch.getGesuchStatus()
            )
        ) {
            return;
        }
        forbidden();
    }

    public void assertGesuchTrancheIsInOneOfGesuchStatus(
        final UUID gesuchTrancheId,
        final Set<GesuchTrancheStatus> gesuchTrancheStatusSet
    ) {
        final var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);
        if (gesuchTrancheStatusSet.contains(gesuchTranche.getStatus())) {
            return;
        }
        forbidden();
    }

    public void assertGesuchTrancheIsInGesuchStatus(
        final UUID gesuchTrancheId,
        final GesuchTrancheStatus gesuchTrancheStatus
    ) {
        assertGesuchTrancheIsInOneOfGesuchStatus(gesuchTrancheId, Set.of(gesuchTrancheStatus));
    }

    public void assertGesuchTrancheIsOfTyp(
        final UUID gesuchTrancheId,
        final GesuchTrancheTyp gesuchTrancheTyp
    ) {
        final var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);
        if (gesuchTranche.getTyp() != gesuchTrancheTyp) {
            forbidden();
        }
    }

    public void assertIsGesuchstellerOfGesuchTrancheOrDelegatedToSozialdienst(
        final UUID gesuchTrancheId
    ) {
        if (
            !AuthorizerUtil.isGesuchstellerOfGesuchOrDelegatedToSozialdienst(
                gesuchTrancheHistoryService.getLatestTranche(gesuchTrancheId).getGesuch(),
                benutzerService.getCurrentBenutzer(),
                sozialdienstService
            )
        ) {
            forbidden();
        }
    }
}
