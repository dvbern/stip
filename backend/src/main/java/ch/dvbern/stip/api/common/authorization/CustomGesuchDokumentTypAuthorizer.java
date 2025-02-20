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
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import io.quarkus.security.ForbiddenException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

@Authorizer
@ApplicationScoped
@RequiredArgsConstructor
public class CustomGesuchDokumentTypAuthorizer extends BaseAuthorizer {
    private final DokumentRepository dokumentRepository;
    private final GesuchDokumentRepository gesuchDokumentRepository;
    private final GesuchTrancheRepository gesuchTrancheRepository;
    private final BenutzerService benutzerService;

    @Transactional
    public void canUpload(final UUID gesuchTrancheId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);
        final var gesuch = gesuchTranche.getGesuch();
        if (!AuthorizerUtil.isGesuchstellerOfGesuch(currentBenutzer, gesuch)) {
            throw new ForbiddenException();
        }
    }

    @Transactional
    public void canDeleteTyp(final UUID gesuchTrancheId, final UUID gesuchDokumentTypId) {
        final var gesuch = gesuchTrancheRepository.requireById(gesuchTrancheId).getGesuch();
        final var customGesuchDokument =
            gesuchDokumentRepository.findByCustomDokumentType(gesuchDokumentTypId)
                .orElseThrow();

        final var isNotBeingEditedBySB = !gesuch.getGesuchStatus().equals(Gesuchstatus.IN_BEARBEITUNG_SB)
        || !isAdminOrSb(benutzerService.getCurrentBenutzer());
        final var fehlendeDokumenteUebermitteltToGS = gesuch.getGesuchStatus().equals(Gesuchstatus.FEHLENDE_DOKUMENTE);
        final var filesAttached = !customGesuchDokument.getDokumente().isEmpty();

        if (
            isNotBeingEditedBySB
            || fehlendeDokumenteUebermitteltToGS && filesAttached
        ) {
            throw new ForbiddenException();
        }
    }

    @Transactional
    public void canDeleteDokument(final UUID dokumentId) {
        final var dokument = dokumentRepository.findByIdOptional(dokumentId).orElseThrow(NotFoundException::new);
        final var isCustomDokument =
            dokument.getGesuchDokumente().stream().anyMatch(x -> Objects.nonNull(x.getCustomDokumentTyp()));

        if (
            isAdminOrSb(benutzerService.getCurrentBenutzer())
            && isCustomDokument
        ) {
            throw new ForbiddenException();
        }
    }
}
