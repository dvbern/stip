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
import java.util.function.BooleanSupplier;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.authorization.util.AuthorizerUtil;
import ch.dvbern.stip.api.common.authorization.util.DokumentAuthorizerUtil;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.gesuchstatus.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
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
    private final DokumentRepository dokumentRepository;

    @Transactional
    public void canCreateGesuchDokument(final UUID gesuchTrancheId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);
        final var gesuch = gesuchTranche.getGesuch();

        final BooleanSupplier canBenutzerUpload =
            () -> gesuchStatusService.benutzerCanUploadDokument(currentBenutzer, gesuch.getGesuchStatus())
            || gesuchTranche.getTyp() == GesuchTrancheTyp.AENDERUNG;

        if (
            DokumentAuthorizerUtil.isDelegiertAndCanUploadOrDelete(
                gesuch,
                currentBenutzer,
                canBenutzerUpload,
                this::forbidden,
                sozialdienstService
            )
        ) {
            return;
        } else if (
            AuthorizerUtil.isGesuchstellerOfGesuch(currentBenutzer, gesuch) && canBenutzerUpload.getAsBoolean()
        ) {
            return;
        }

        forbidden();
    }

    @Transactional
    public void canDeleteDokument(final UUID dokumentId) {
        final var dokument = dokumentRepository.findByIdOptional(dokumentId).orElseThrow(NotFoundException::new);
        final var gesuchTranche = dokument.getGesuchDokumente().get(0).getGesuchTranche();
        final var gesuch = gesuchTranche.getGesuch();
        final var currentBenutzer = benutzerService.getCurrentBenutzer();

        final BooleanSupplier canBenutzerDeleteDokument =
            () -> gesuchStatusService.benutzerCanDeleteDokument(currentBenutzer, gesuch.getGesuchStatus())
            || gesuchTranche.getTyp() == GesuchTrancheTyp.AENDERUNG;

        if (
            DokumentAuthorizerUtil.isDelegiertAndCanUploadOrDelete(
                gesuch,
                currentBenutzer,
                canBenutzerDeleteDokument,
                this::forbidden,
                sozialdienstService
            )
        ) {
            return;
        } else if (
            AuthorizerUtil.isGesuchstellerOfGesuch(currentBenutzer, gesuch) && canBenutzerDeleteDokument.getAsBoolean()
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
