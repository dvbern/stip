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
import ch.dvbern.stip.api.dokument.repo.DokumentHistoryRepository;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
@Authorizer
public class DokumentAuthorizer extends BaseAuthorizer {
    private final GesuchRepository gesuchRepository;
    private final BenutzerService benutzerService;
    private final SozialdienstService sozialdienstService;
    private final DokumentRepository dokumentRepository;
    private final DokumentHistoryRepository dokumentHistoryRepository;

    public void canGetDokumentDownloadToken(final UUID dokumentId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        if (isSbOrFreigabestelleOrJurist(currentBenutzer)) {
            return;
        }

        var dokument = dokumentRepository.findById(dokumentId);
        if (Objects.isNull(dokument)) {
            dokument = dokumentHistoryRepository.findInHistoryById(dokumentId);
        }
        final var fall = dokument.getGesuchDokument().getGesuchTranche().getGesuch().getAusbildung().getFall();

        if (
            AuthorizerUtil.canReadAndIsGesuchstellerOfOrDelegatedToSozialdienst(
                fall,
                currentBenutzer,
                sozialdienstService
            )
        ) {
            return;
        }
        forbidden();
    }
}
