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
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentHistoryRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

@Authorizer
@ApplicationScoped
@RequiredArgsConstructor
public class GesuchDokumentAuthorizer extends BaseAuthorizer {
    private final GesuchTrancheRepository gesuchTrancheRepository;
    private final BenutzerService benutzerService;
    private final GesuchDokumentRepository gesuchDokumentRepository;
    private final GesuchDokumentHistoryRepository gesuchDokumentHistoryRepository;
    private final SozialdienstService sozialdienstService;
    private final DokumentRepository dokumentRepository;

    public void assertSbCanModifyDokumentOfTranche(final UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);
        final var gesuch = gesuchTranche.getGesuch();
        final var currentBenutzer = benutzerService.getCurrentBenutzer();

        if (!isSachbearbeiter(currentBenutzer)) {
            forbidden();
        }

        if (
            gesuchTranche.getTyp() == GesuchTrancheTyp.TRANCHE
            && Gesuchstatus.SACHBEARBEITER_CAN_MODIFY_DOKUMENT.contains(gesuch.getGesuchStatus())
        ) {
            return;
        }

        if (
            gesuchTranche.getTyp() == GesuchTrancheTyp.AENDERUNG
            && GesuchTrancheStatus.SACHBEARBEITER_CAN_MODIFY_DOKUMENT.contains(gesuchTranche.getStatus())
        ) {
            return;
        }

        forbidden();
    }

    public void assertGsCanModifyDokumentOfTranche(final UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);
        final var gesuch = gesuchTranche.getGesuch();
        final var currentBenutzer = benutzerService.getCurrentBenutzer();

        if (
            !AuthorizerUtil
                .canWriteAndIsGesuchstellerOfOrDelegatedToSozialdienst(gesuch, currentBenutzer, sozialdienstService)
        ) {
            forbidden();
        }

        if (
            gesuchTranche.getTyp() == GesuchTrancheTyp.TRANCHE
            && Gesuchstatus.GESUCHSTELLER_CAN_MODIFY_DOKUMENT.contains(gesuch.getGesuchStatus())
        ) {
            return;
        }

        if (
            gesuchTranche.getTyp() == GesuchTrancheTyp.AENDERUNG
            && GesuchTrancheStatus.GESUCHSTELLER_CAN_MODIFY_DOKUMENT.contains(gesuchTranche.getStatus())
        ) {
            return;
        }

        forbidden();
    }

    public void assertGsCanDeleteDokumentOfTranche(final UUID dokumentId) {
        final var dokument = dokumentRepository.findByIdOptional(dokumentId).orElseThrow(NotFoundException::new);
        final var gesuchDokument = dokument.getGesuchDokument();
        final var gesuchTranche = gesuchDokument.getGesuchTranche();

        assertGsCanModifyDokumentOfTranche(gesuchTranche.getId());
        if (gesuchDokument.getStatus() == Dokumentstatus.AUSSTEHEND) {
            return;
        }

        forbidden();
    }

    public void assertSbCanDeleteDokumentOfTranche(final UUID dokumentId) {
        final var dokument = dokumentRepository.findByIdOptional(dokumentId).orElseThrow(NotFoundException::new);
        final var gesuchDokument = dokument.getGesuchDokument();
        final var gesuchTranche = gesuchDokument.getGesuchTranche();

        assertSbCanModifyDokumentOfTranche(gesuchTranche.getId());
    }

    public void canUpdateGesuchDokument(UUID gesuchDokumentId) {
        final var gesuchDokument = gesuchDokumentRepository.requireById(gesuchDokumentId);
        final var trancheTyp = gesuchDokument.getGesuchTranche().getTyp();

        if (
            trancheTyp == GesuchTrancheTyp.TRANCHE
            && gesuchDokument.getGesuchTranche().getGesuch().getGesuchStatus() == Gesuchstatus.IN_BEARBEITUNG_SB
        ) {
            return;
        }
        if (
            trancheTyp == GesuchTrancheTyp.AENDERUNG
            && gesuchDokument.getGesuchTranche().getStatus() == GesuchTrancheStatus.UEBERPRUEFEN
        ) {
            return;
        }
        forbidden();
    }

    public void canGetGesuchDokumentKommentar(final UUID gesuchDokumentId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        // Sachbearbeiter and Jurists can always read every Gesuch
        if (isSbOrJurist(currentBenutzer)) {
            return;
        }

        var gesuchDokument = gesuchDokumentRepository.findById(gesuchDokumentId);
        if (Objects.isNull(gesuchDokument)) {
            gesuchDokument = gesuchDokumentHistoryRepository.findInHistoryById(gesuchDokumentId);
        }

        final var gesuch = gesuchDokument.getGesuchTranche().getGesuch();
        if (
            AuthorizerUtil.canReadAndIsGesuchstellerOfOrDelegatedToSozialdienst(
                gesuch.getAusbildung().getFall(),
                currentBenutzer,
                sozialdienstService
            )
        ) {
            return;
        }

        forbidden();
    }

    public void canGetGesuchDokumentForTranche(final UUID gesuchTrancheId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        if (isSbOrJurist(currentBenutzer)) {
            return;
        }

        final var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);
        if (
            AuthorizerUtil.canReadAndIsGesuchstellerOfOrDelegatedToSozialdienst(
                gesuchTranche.getGesuch().getAusbildung().getFall(),
                currentBenutzer,
                sozialdienstService
            )
        ) {
            return;
        }

        forbidden();
    }
}
