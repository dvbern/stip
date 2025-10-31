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
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.unterschriftenblatt.repo.UnterschriftenblattRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Authorizer
@ApplicationScoped
@RequiredArgsConstructor
public class UnterschriftenblattAuthorizer extends BaseAuthorizer {
    private final UnterschriftenblattRepository unterschriftenblattRepository;
    private final GesuchRepository gesuchRepository;
    private final BenutzerService benutzerService;

    @Transactional
    public void canUpload(final UUID gesuchId) {
        final var benutzer = benutzerService.getCurrentBenutzer();
        if (!isSachbearbeiter(benutzer)) {
            forbidden();
        }

        final var gesuch = gesuchRepository.requireById(gesuchId);
        if (!gesuch.isFirstVerfuegung()) {
            forbidden();
        }
        if (Gesuchstatus.SACHBEARBEITER_CAN_UPLOAD_UNTERSCHRIFTENBLATT.contains(gesuch.getGesuchStatus())) {
            return;
        }
        forbidden();
    }

    public void canGetUnterschriftenblaetter() {
        permitAll();
    }

    @Transactional
    public void canDeleteUnterschriftenblattDokument(final UUID dokumentId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var gesuchForDokument = unterschriftenblattRepository.requireByDokumentId(dokumentId).getGesuch();

        // Only SBs can delete a Unterschriftenblatt Dokument if the Gesuch was never verfuegt
        if (isSachbearbeiter(currentBenutzer) && !gesuchForDokument.isVerfuegt()) {
            return;
        }

        forbidden();
    }
}
