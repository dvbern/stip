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

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.authorization.util.AuthorizerUtil;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentHistoryRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.gesuchstatus.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

@Authorizer
@ApplicationScoped
@RequiredArgsConstructor
public class GesuchDokumentAuthorizer extends BaseAuthorizer {
    private final GesuchTrancheRepository gesuchTrancheRepository;
    private final BenutzerService benutzerService;
    private final GesuchStatusService gesuchStatusService;
    private final GesuchDokumentRepository gesuchDokumentRepository;
    private final GesuchDokumentHistoryRepository gesuchDokumentHistoryRepository;
    private final SozialdienstService sozialdienstService;
    private final DokumentRepository dokumentRepository;

    @Transactional
    public void canCreateGesuchDokument(final UUID gesuchTrancheId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);
        final var gesuch = gesuchTranche.getGesuch();

        if (
            !AuthorizerUtil
                .isGesuchstellerOfGesuchOrDelegatedToSozialdienst(gesuch, currentBenutzer, sozialdienstService)
        ) {
            forbidden();
        }

        if (
            gesuchTranche.getTyp() == GesuchTrancheTyp.TRANCHE
            && gesuchStatusService.benutzerCanUploadDokument(currentBenutzer, gesuch.getGesuchStatus())
        ) {
            return;
        }

        if (
            gesuchTranche.getTyp() == GesuchTrancheTyp.AENDERUNG
            && GesuchTrancheStatus.GESUCHSTELLER_PENDING.contains(gesuchTranche.getStatus())
        ) {
            return;
        }

        forbidden();
    }

    @Transactional
    public void canDeleteDokument(final UUID dokumentId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var dokument = dokumentRepository.findByIdOptional(dokumentId).orElseThrow(NotFoundException::new);
        final var gesuchTranche = dokument.getGesuchDokumente().get(0).getGesuchTranche();
        final var gesuch = gesuchTranche.getGesuch();

        if (
            !AuthorizerUtil
                .isGesuchstellerOfGesuchOrDelegatedToSozialdienst(gesuch, currentBenutzer, sozialdienstService)
        ) {
            forbidden();
        }

        if (
            gesuchTranche.getTyp() == GesuchTrancheTyp.TRANCHE
            && gesuchStatusService.benutzerCanDeleteDokument(currentBenutzer, gesuch.getGesuchStatus())
        ) {
            return;
        }

        if (
            gesuchTranche.getTyp() == GesuchTrancheTyp.AENDERUNG
            && GesuchTrancheStatus.GESUCHSTELLER_PENDING.contains(gesuchTranche.getStatus())
        ) {
            return;
        }

        forbidden();
    }

    @Transactional
    public void canUpdateGesuchDokument(UUID gesuchDokumentId) {
        final var gesuchDokument = gesuchDokumentRepository.requireById(gesuchDokumentId);
        final var trancheTyp = gesuchDokument.getGesuchTranche().getTyp();

        if (trancheTyp == GesuchTrancheTyp.TRANCHE) {
            AuthorizerUtil.gesuchStatusOneOfOrElseThrow(
                gesuchDokument.getGesuchTranche().getGesuch(),
                List.of(Gesuchstatus.IN_BEARBEITUNG_SB)
            );
        } else if (trancheTyp == GesuchTrancheTyp.AENDERUNG) {
            AuthorizerUtil.gesuchTrancheStatusOneOfOrElseThrow(
                gesuchDokument.getGesuchTranche(),
                List.of(GesuchTrancheStatus.UEBERPRUEFEN)
            );
        }
    }

    @Transactional
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
            AuthorizerUtil.isGesuchstellerOfGesuchOrDelegatedToSozialdienst(
                gesuch,
                currentBenutzer,
                sozialdienstService
            )
        ) {
            return;
        }

        forbidden();
    }

    @Transactional
    public void canGetGesuchDokumentForTranche(final UUID gesuchTrancheId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        if (isSbOrJurist(currentBenutzer)) {
            return;
        }

        final var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);
        if (
            AuthorizerUtil.isGesuchstellerOfGesuchOrDelegatedToSozialdienst(
                gesuchTranche.getGesuch(),
                currentBenutzer,
                sozialdienstService
            )
        ) {
            return;
        }

        forbidden();
    }
}
