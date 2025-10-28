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
import ch.dvbern.stip.api.dokument.service.RequiredDokumentService;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.gesuchtranchehistory.service.GesuchTrancheHistoryService;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.SetUtils;

@ApplicationScoped
@RequiredArgsConstructor
@Authorizer
public class GesuchTrancheAuthorizer extends BaseAuthorizer {
    private final BenutzerService benutzerService;
    private final GesuchTrancheRepository gesuchTrancheRepository;
    private final GesuchRepository gesuchRepository;
    private final SozialdienstService sozialdienstService;
    private final GesuchTrancheHistoryService gesuchTrancheHistoryService;
    private final RequiredDokumentService requiredDokumentService;

    @Transactional
    public void canReadInitialTranche(final UUID trancheId) {
        GesuchTranche tranche = gesuchTrancheHistoryService.getLatestTranche(trancheId);
        Gesuch gesuch = gesuchRepository.requireById(tranche.getGesuch().getId());
        if (!gesuch.isVerfuegt()) {
            throw new IllegalStateException("Gesuch was not Verfuegt");
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
        assertCanWriteAndIsGesuchstellerOfGesuchTrancheOrDelegatedToSozialdienst(gesuchTrancheId);
        assertGesuchTrancheIsOfTyp(gesuchTrancheId, GesuchTrancheTyp.AENDERUNG);
        assertGesuchTrancheIsInGesuchTrancheStatus(gesuchTrancheId, GesuchTrancheStatus.IN_BEARBEITUNG_GS);
    }

    @Transactional
    public void canAenderungEinreichen(final UUID gesuchTrancheId) {
        assertGesuchTrancheIsInOneOfGesuchTrancheStatus(
            gesuchTrancheId,
            GesuchTrancheStatus.GESUCHSTELLER_CAN_AENDERUNG_EINREICHEN
        );
        if (isSbOrFreigabestelleOrJurist(benutzerService.getCurrentBenutzer())) {
            return;
        }

        assertCanWriteAndIsGesuchstellerOfGesuchTrancheOrDelegatedToSozialdienst(gesuchTrancheId);
    }

    @Transactional
    public void canFehlendeDokumenteEinreichen(final UUID gesuchTrancheId) {
        assertCanWriteAndIsGesuchstellerOfGesuchTrancheOrDelegatedToSozialdienst(gesuchTrancheId);
        assertGesuchTrancheIsInGesuchTrancheStatus(gesuchTrancheId, GesuchTrancheStatus.FEHLENDE_DOKUMENTE);

        final var gesuchTranche = gesuchTrancheRepository.findById(gesuchTrancheId);

        if (
            requiredDokumentService
                .getGSCanFehlendeDokumenteEinreichen(gesuchTranche.getGesuch(), benutzerService.getCurrentBenutzer())
        ) {
            return;
        }

        forbidden();
    }

    @Transactional
    public void sbCanUpdateGueltigkeitOfAenderung(final UUID aenderungId) {
        final var gesuchTranche = gesuchTrancheRepository.findById(aenderungId);

        if (gesuchTranche.getTyp() != GesuchTrancheTyp.AENDERUNG) {
            throw new IllegalStateException();
        }
        assertGesuchTrancheIsInGesuchTrancheStatus(aenderungId, GesuchTrancheStatus.UEBERPRUEFEN);

        if (isSachbearbeiter(benutzerService.getCurrentBenutzer())) {
            return;
        }

        forbidden();
    }

    @Transactional
    public void canFehlendeDokumenteUebermitteln(final UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheRepository.findById(gesuchTrancheId);

        if (gesuchTranche.getTyp() == GesuchTrancheTyp.AENDERUNG) {
            assertGesuchTrancheIsInGesuchTrancheStatus(gesuchTrancheId, GesuchTrancheStatus.UEBERPRUEFEN);
        } else {
            throw new IllegalStateException();
        }

        final var gesuch = gesuchRepository.requireGesuchByTrancheId(gesuchTrancheId);

        if (requiredDokumentService.getSBCanFehlendeDokumenteUebermitteln(gesuch)) {
            return;
        }

        forbidden();
    }

    @Transactional
    public void canAenderungAkzeptieren(final UUID gesuchTrancheId) {
        assertGesuchTrancheIsInGesuchTrancheStatus(gesuchTrancheId, GesuchTrancheStatus.UEBERPRUEFEN);
    }

    @Transactional
    public void canAenderungAblehnen(final UUID gesuchTrancheId) {
        assertGesuchTrancheIsInGesuchTrancheStatus(gesuchTrancheId, GesuchTrancheStatus.UEBERPRUEFEN);
    }

    @Transactional
    public void canAenderungManuellAnpassen(final UUID gesuchTrancheId) {
        assertGesuchTrancheIsInGesuchTrancheStatus(gesuchTrancheId, GesuchTrancheStatus.UEBERPRUEFEN);
    }

    @Transactional
    public void canUpdateTrancheGS(final GesuchTranche gesuchTranche) {
        if (gesuchTranche.getTyp() == GesuchTrancheTyp.AENDERUNG) {
            canUpdateAenderungsTrancheGS(gesuchTranche);
        } else {
            canUpdateNormalTrancheGS(gesuchTranche);
        }
    }

    @Transactional
    public void canUpdateTrancheSB(final UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);
        canUpdateAenderungsTrancheSB(gesuchTranche);
    }

    @Transactional
    public void canUpdateTrancheSB(final GesuchTranche gesuchTranche) {
        if (gesuchTranche.getTyp() == GesuchTrancheTyp.AENDERUNG) {
            canUpdateAenderungsTrancheSB(gesuchTranche);
        } else {
            canUpdateNormalTrancheSB(gesuchTranche);
        }
    }

    private void canUpdateAenderungsTrancheGS(final GesuchTranche gesuchTranche) {
        assertGesuchTrancheIsInOneOfGesuchTrancheStatus(
            gesuchTranche.getId(),
            GesuchTrancheStatus.GESUCHSTELLER_CAN_EDIT
        );

        assertCanWriteAndIsGesuchstellerOfGesuchTrancheOrDelegatedToSozialdienst(gesuchTranche.getId());
    }

    private void canUpdateAenderungsTrancheSB(final GesuchTranche gesuchTranche) {
        assertGesuchTrancheIsInOneOfGesuchTrancheStatus(
            gesuchTranche.getId(),
            GesuchTrancheStatus.SACHBEARBEITER_CAN_EDIT
        );
    }

    private void canUpdateNormalTrancheGS(final GesuchTranche gesuchTranche) {
        assertGesuchOfTrancheIsInOneOfGesuchstatus(gesuchTranche.getId(), Gesuchstatus.GESUCHSTELLER_CAN_EDIT);

        assertCanWriteAndIsGesuchstellerOfGesuchTrancheOrDelegatedToSozialdienst(gesuchTranche.getId());
    }

    private void canUpdateNormalTrancheSB(final GesuchTranche gesuchTranche) {
        assertGesuchOfTrancheIsInOneOfGesuchstatus(
            gesuchTranche.getId(),
            SetUtils.union(Gesuchstatus.SACHBEARBEITER_CAN_EDIT, Gesuchstatus.JURIST_CAN_EDIT)
        );
    }

    public void assertGesuchTrancheIsInOneOfGesuchTrancheStatus(
        final UUID gesuchTrancheId,
        final Set<GesuchTrancheStatus> gesuchTrancheStatusSet
    ) {
        final var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);
        if (gesuchTrancheStatusSet.contains(gesuchTranche.getStatus())) {
            return;
        }
        forbidden();
    }

    public void assertGesuchTrancheIsInGesuchTrancheStatus(
        final UUID gesuchTrancheId,
        final GesuchTrancheStatus gesuchTrancheStatus
    ) {
        assertGesuchTrancheIsInOneOfGesuchTrancheStatus(gesuchTrancheId, Set.of(gesuchTrancheStatus));
    }

    public void assertGesuchOfTrancheIsInOneOfGesuchstatus(
        final UUID gesuchTrancheId,
        final Set<Gesuchstatus> gesuchstatusSet
    ) {
        final var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);
        if (gesuchstatusSet.contains(gesuchTranche.getGesuch().getGesuchStatus())) {
            return;
        }
        forbidden();
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

    public void assertCanWriteAndIsGesuchstellerOfGesuchTrancheOrDelegatedToSozialdienst(
        final UUID gesuchTrancheId
    ) {
        if (
            !AuthorizerUtil.canWriteAndIsGesuchstellerOfOrDelegatedToSozialdienst(
                gesuchTrancheHistoryService.getLatestTranche(gesuchTrancheId).getGesuch(),
                benutzerService.getCurrentBenutzer(),
                sozialdienstService
            )
        ) {
            forbidden();
        }
    }

    public void assertIsGesuchstellerOfGesuchTrancheOrDelegatedToSozialdienst(
        final UUID gesuchTrancheId
    ) {
        if (
            !AuthorizerUtil.canReadAndIsGesuchstellerOfOrDelegatedToSozialdienst(
                gesuchTrancheHistoryService.getLatestTranche(gesuchTrancheId).getGesuch().getAusbildung().getFall(),
                benutzerService.getCurrentBenutzer(),
                sozialdienstService
            )
        ) {
            forbidden();
        }
    }
}
