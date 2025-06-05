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
import ch.dvbern.stip.api.dokument.repo.CustomDokumentTypRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.gesuchstatus.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.service.GesuchTrancheStatusService;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Authorizer
@ApplicationScoped
@RequiredArgsConstructor
public class CustomGesuchDokumentTypAuthorizer extends BaseAuthorizer {
    private final CustomDokumentTypRepository customDokumentTypRepository;
    private final GesuchDokumentRepository gesuchDokumentRepository;
    private final GesuchTrancheRepository gesuchTrancheRepository;
    private final BenutzerService benutzerService;
    private final GesuchStatusService gesuchStatusService;
    private final SozialdienstService sozialdienstService;
    private final GesuchTrancheStatusService gesuchTrancheStatusService;

    @Transactional
    public void canCreateCustomDokumentTyp(UUID trancheId) {
        final var tranche = gesuchTrancheRepository.requireById(trancheId);
        final var gesuch = tranche.getGesuch();

        if (tranche.getTyp() == GesuchTrancheTyp.AENDERUNG && tranche.getStatus() == GesuchTrancheStatus.UEBERPRUEFEN) {
            return;
        }

        if (
            tranche.getTyp() == GesuchTrancheTyp.TRANCHE && gesuch.getGesuchStatus() == Gesuchstatus.IN_BEARBEITUNG_SB
        ) {
            return;
        }

        forbidden();
    }

    @Transactional
    public void canReadCustomDokumentOfTyp(UUID customDokumentTypId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        if (isSbOrJurist(currentBenutzer)) {
            return;
        }

        final var customDokumentTyp = customDokumentTypRepository.requireById(customDokumentTypId);
        final var gesuch = customDokumentTyp.getGesuchDokument().getGesuchTranche().getGesuch();

        if (
            AuthorizerUtil
                .isGesuchstellerOfGesuchOrDelegatedToSozialdienst(gesuch, currentBenutzer, sozialdienstService)
        ) {
            return;
        }

        forbidden();
    }

    @Transactional
    public void canUpload(final UUID customDokumentTypId) {
        final var customDokumentTyp = customDokumentTypRepository.findById(customDokumentTypId);
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var gesuchTranche = customDokumentTyp.getGesuchDokument().getGesuchTranche();
        final var gesuch = gesuchTranche.getGesuch();

        if (
            AuthorizerUtil
                .isGesuchstellerOfGesuchOrDelegatedToSozialdienst(gesuch, currentBenutzer, sozialdienstService)
            && (gesuchStatusService.benutzerCanUploadDokument(currentBenutzer, gesuch.getGesuchStatus())
            || gesuchTranche.getTyp() == GesuchTrancheTyp.AENDERUNG)
        ) {
            return;
        }

        forbidden();
    }

    @Transactional
    public void canDeleteTyp(final UUID gesuchDokumentTypId) {
        final var customGesuchDokument =
            gesuchDokumentRepository.findByCustomDokumentTyp(gesuchDokumentTypId)
                .orElseThrow();

        final var isAnyFileAttached = !customGesuchDokument.getDokumente().isEmpty();

        if (isAnyFileAttached) {
            forbidden();
        }

        final var currentTranche = customGesuchDokument.getGesuchTranche();
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var isAenderung = currentTranche.getTyp().equals(GesuchTrancheTyp.AENDERUNG);

        if (isAenderung && !gesuchTrancheStatusService.benutzerCanEdit(currentBenutzer, currentTranche.getStatus())) {
            forbidden();
        }

        final var isTranche = currentTranche.getTyp().equals(GesuchTrancheTyp.TRANCHE);

        final var customDokumentTyp = customDokumentTypRepository.requireById(gesuchDokumentTypId);
        final var gesuch = customDokumentTyp.getGesuchDokument().getGesuchTranche().getGesuch();
        if (isTranche && !gesuchStatusService.benutzerCanDeleteDokument(currentBenutzer, gesuch.getGesuchStatus())) {
            forbidden();
        }
    }
}
