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
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.gesuchstatus.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
@Authorizer
public class GesuchDokumentAuthorizer extends BaseAuthorizer {
    private final GesuchTrancheRepository gesuchTrancheRepository;
    private final BenutzerService benutzerService;
    private final GesuchStatusService gesuchStatusService;
    private final GesuchDokumentRepository gesuchDokumentRepository;
    private final SozialdienstService sozialdienstService;

    @Transactional
    public void canCreateGesuchDokument(final UUID gesuchTrancheId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var gesuchstatus = gesuchTrancheRepository.findById(gesuchTrancheId).getGesuch().getGesuchStatus();

        if (
            isGesuchsteller(currentBenutzer)
            && gesuchStatusService.benutzerCanUploadDokument(currentBenutzer, gesuchstatus)
        ) {
            return;
        }

        forbidden();
    }

    @Transactional
    public void canGesuchDokumentAblehnen(final UUID gesuchDokumentId) {
        canAblehnenOderAkzeptieren(gesuchDokumentId);
    }

    @Transactional
    public void canGesuchDokumentAkzeptieren(final UUID gesuchDokumentId) {
        canAblehnenOderAkzeptieren(gesuchDokumentId);
    }

    private void canAblehnenOderAkzeptieren(final UUID gesuchDokumentId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var gesuchDokument = gesuchDokumentRepository.requireById(gesuchDokumentId);
        final var gesuch = gesuchDokument.getGesuchTranche().getGesuch();

        if (gesuchStatusService.benutzerCanEdit(currentBenutzer, gesuch.getGesuchStatus())) {
            return;
        }

        forbidden();
    }

    @Transactional
    public void canGetGesuchDokumentKommentar(final UUID gesuchDokumentId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();

        // Admins and Sachbearbeiter can always read every Gesuch
        if (isAdminOrSb(currentBenutzer)) {
            return;
        }

        final var gesuch = gesuchDokumentRepository.requireById(gesuchDokumentId).getGesuchTranche().getGesuch();
        if (
            AuthorizerUtil.isGesuchstellerOrDelegatedToSozialdienst(
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
    public void canGetGesuchDokumentKommentareForTranche(final UUID gesuchTrancheId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        if (isAdminOrSb(currentBenutzer)) {
            return;
        }

        final var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);
        if (
            AuthorizerUtil.isGesuchstellerOrDelegatedToSozialdienst(
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
