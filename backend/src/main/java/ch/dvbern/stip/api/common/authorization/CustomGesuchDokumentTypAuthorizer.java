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
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import io.quarkus.security.ForbiddenException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus.GESUCHSTELLER_CAN_DELETE_DOKUMENTE;

@Authorizer
@ApplicationScoped
@RequiredArgsConstructor
public class CustomGesuchDokumentTypAuthorizer extends BaseAuthorizer {
    private final DokumentRepository dokumentRepository;
    private final CustomDokumentTypRepository customDokumentTypRepository;
    private final GesuchDokumentRepository gesuchDokumentRepository;
    private final GesuchTrancheRepository gesuchTrancheRepository;
    private final BenutzerService benutzerService;

    @Transactional
    public void canCreateCustomDokumentTyp(UUID trancheId) {
        final var gesuch = gesuchTrancheRepository.requireById(trancheId).getGesuch();
        canReadAllTyps();
        if (gesuch.getGesuchStatus() != Gesuchstatus.IN_BEARBEITUNG_SB) {
            throw new ForbiddenException();
        }
    }

    @Transactional
    public void canReadAllTyps() {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        if (!isAdminOrSb(currentBenutzer)) {
            throw new ForbiddenException();
        }
    }

    @Transactional
    public void canReadCustomDokumentOfTyp(UUID customDokumentTypId) {
        final var customDokuementTyp = customDokumentTypRepository.requireById(customDokumentTypId);
        final var gesuch = customDokuementTyp.getGesuchDokument().getGesuchTranche().getGesuch();
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        if (!(isAdminOrSb(currentBenutzer) || AuthorizerUtil.isGesuchstellerOfGesuch(currentBenutzer, gesuch))) {
            throw new ForbiddenException();
        }
    }

    @Transactional
    public void canUpload(final UUID customDokumentTypId) {
        final var customDokumentTyp = customDokumentTypRepository.findById(customDokumentTypId);
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var gesuch = customDokumentTyp.getGesuchDokument().getGesuchTranche().getGesuch();

        if (!AuthorizerUtil.isGesuchstellerOfGesuch(currentBenutzer, gesuch)) {
            throw new ForbiddenException();
        }
    }

    @Transactional
    public void canDeleteTyp(final UUID gesuchDokumentTypId) {
        final var customDokumentTyp = customDokumentTypRepository.requireById(gesuchDokumentTypId);
        final var gesuch = customDokumentTyp.getGesuchDokument().getGesuchTranche().getGesuch();
        final var customGesuchDokument =
            gesuchDokumentRepository.findByCustomDokumentTyp(gesuchDokumentTypId)
                .orElseThrow();

        final var notBeingEditedBySB = !(isAdminOrSb(benutzerService.getCurrentBenutzer()))
        || gesuch.getGesuchStatus() != Gesuchstatus.IN_BEARBEITUNG_SB;
        final var isAnyFileAttached = !customGesuchDokument.getDokumente().isEmpty();

        // check if gesuch is being edited by SB
        // or if GS has already attached a file to it
        if (
            notBeingEditedBySB
            || isAnyFileAttached
        ) {
            throw new ForbiddenException();
        }
    }

    @Transactional
    public void canDeleteDokument(final UUID dokumentId) {
        final var dokument = dokumentRepository.findByIdOptional(dokumentId).orElseThrow(NotFoundException::new);
        final var gesuch = dokument.getGesuchDokumente().get(0).getGesuchTranche().getGesuch();

        final var isDeleteAuthorized =
            AuthorizerUtil.isGesuchstellerOfGesuch(benutzerService.getCurrentBenutzer(), gesuch)
            && GESUCHSTELLER_CAN_DELETE_DOKUMENTE.contains(gesuch.getGesuchStatus());
        final var isDokumentAusstehend = dokument.getGesuchDokumente()
            .stream()
            .allMatch(gesuchDokument -> !gesuchDokument.getStatus().equals(Dokumentstatus.AUSSTEHEND));

        if (
            !isDeleteAuthorized
            || !isDokumentAusstehend
        ) {
            throw new ForbiddenException();
        }
    }
}
