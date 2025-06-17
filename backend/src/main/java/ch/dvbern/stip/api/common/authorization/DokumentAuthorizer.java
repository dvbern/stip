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
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
@Authorizer
public class DokumentAuthorizer extends BaseAuthorizer {
    private final GesuchRepository gesuchRepository;
    private final BenutzerService benutzerService;
    private final GesuchTrancheRepository gesuchTrancheRepository;
    private final SozialdienstService sozialdienstService;

    public void canGetDokumentDownloadToken(final UUID dokumentId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        if (isSbOrJurist(currentBenutzer)) {
            return;
        }

        final var fall = gesuchRepository.requireGesuchForDokument(dokumentId).getAusbildung().getFall();

        if (
            AuthorizerUtil.canWriteAndIsGesuchstellerOfOrDelegatedToSozialdienst(
                fall,
                currentBenutzer,
                sozialdienstService
            )
        ) {
            return;
        }
        forbidden();
    }

    public void canUpload(final UUID gesuchTrancheId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);

        if (
            !AuthorizerUtil.canWriteAndIsGesuchstellerOfOrDelegatedToSozialdienst(
                gesuchTranche.getGesuch(),
                currentBenutzer,
                sozialdienstService
            )
        ) {
            forbidden();
        }

        final var trancheTyp = gesuchTranche.getTyp();

        if (
            trancheTyp == GesuchTrancheTyp.TRANCHE
            && Gesuchstatus.GESUCHSTELLER_CAN_MODIFY_DOKUMENT.contains(gesuchTranche.getGesuch().getGesuchStatus())
        ) {
            return;
        }
        if (
            trancheTyp == GesuchTrancheTyp.AENDERUNG
            && GesuchTrancheStatus.GESUCHSTELLER_CAN_MODIFY_DOKUMENT.contains(gesuchTranche.getStatus())
        ) {
            return;
        }
        forbidden();
    }
}
