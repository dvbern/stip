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
public class CustomGesuchDokumentTypAuthorizer extends BaseAuthorizer {
    private final CustomDokumentTypRepository customDokumentTypRepository;
    private final GesuchDokumentRepository gesuchDokumentRepository;
    private final GesuchTrancheRepository gesuchTrancheRepository;
    private final BenutzerService benutzerService;
    private final SozialdienstService sozialdienstService;
    private final GesuchDokumentAuthorizer gesuchDokumentAuthorizer;

    private void assertCanModifyCustomDokumentTypOfTranche(final UUID gesuchTrancheId) {
        final var tranche = gesuchTrancheRepository.requireById(gesuchTrancheId);
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

    public void canCreateCustomDokumentTyp(final UUID gesuchTrancheId) {
        assertCanModifyCustomDokumentTypOfTranche(gesuchTrancheId);
    }

    public void canReadCustomDokumentOfTyp(final UUID customDokumentTypId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        if (isSbOrFreigabestelleOrJurist(currentBenutzer)) {
            return;
        }

        final var customDokumentTyp = customDokumentTypRepository.requireById(customDokumentTypId);
        final var gesuch = customDokumentTyp.getGesuchDokument().getGesuchTranche().getGesuch();

        if (
            AuthorizerUtil
                .canWriteAndIsGesuchstellerOfOrDelegatedToSozialdienst(gesuch, currentBenutzer, sozialdienstService)
        ) {
            return;
        }

        forbidden();
    }

    public void assertGsCanModifyCustomDokumentOfTranche(final UUID customDokumentTypId) {
        final var customDokumentTyp = customDokumentTypRepository.findById(customDokumentTypId);
        gesuchDokumentAuthorizer
            .assertGsCanModifyDokumentOfTranche(customDokumentTyp.getGesuchDokument().getGesuchTranche().getId());
    }

    public void assertSbCanModifyCustomDokumentOfTranche(final UUID customDokumentTypId) {
        final var customDokumentTyp = customDokumentTypRepository.findById(customDokumentTypId);
        gesuchDokumentAuthorizer
            .assertSbCanModifyDokumentOfTranche(customDokumentTyp.getGesuchDokument().getGesuchTranche().getId());
    }

    public void canDeleteTyp(final UUID gesuchDokumentTypId) {
        final var customGesuchDokument =
            gesuchDokumentRepository.findByCustomDokumentTyp(gesuchDokumentTypId)
                .orElseThrow(NotFoundException::new);
        final var isAnyFileAttached = !customGesuchDokument.getDokumente().isEmpty();

        if (isAnyFileAttached) {
            forbidden();
        }

        assertCanModifyCustomDokumentTypOfTranche(customGesuchDokument.getGesuchTranche().getId());
    }

}
