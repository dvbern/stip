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
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import io.quarkus.security.ForbiddenException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
@Authorizer
public class DokumentAuthorizer extends BaseAuthorizer {
    private final BenutzerService benutzerService;
    private final GesuchDokumentRepository gesuchDokumentRepository;

    private static void gesuchstatusIsNotOrElseThrow(final Gesuch gesuch, final Gesuchstatus statusToVerify) {
        if (gesuch.getGesuchStatus() != statusToVerify) {
            throw new IllegalStateException(
                "Gesuchstatus " + gesuch.getGesuchStatus().toString() + " not " + statusToVerify.toString()
            );
        }
    }

    private static void gesuchTrancheStatusIsNotOrElseThrow(
        final GesuchTranche gesuchTranche,
        final GesuchTrancheStatus statusToVerify
    ) {
        if (gesuchTranche.getStatus() != statusToVerify) {
            throw new IllegalStateException(
                "GesuchTrancheStatus " + gesuchTranche.getStatus().toString() + " not " + statusToVerify.toString()
            );
        }
    }

    @Transactional
    public void canUpdateGesuchDokument(UUID gesuchDokumentId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();

        if (!isAdminOrSb(currentBenutzer)) {
            throw new ForbiddenException();
        }
        final var gesuchDokument = gesuchDokumentRepository.requireById(gesuchDokumentId);

        final var trancheTyp = gesuchDokument.getGesuchTranche().getTyp();

        if (trancheTyp == GesuchTrancheTyp.TRANCHE) {
            gesuchstatusIsNotOrElseThrow(gesuchDokument.getGesuchTranche().getGesuch(), Gesuchstatus.IN_BEARBEITUNG_SB);
        } else if (trancheTyp == GesuchTrancheTyp.AENDERUNG) {
            gesuchTrancheStatusIsNotOrElseThrow(gesuchDokument.getGesuchTranche(), GesuchTrancheStatus.UEBERPRUEFEN);
        }
    }
}
