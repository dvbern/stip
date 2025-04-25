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

import java.util.List;
import java.util.UUID;
import java.util.function.BooleanSupplier;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.authorization.util.AuthorizerUtil;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import io.quarkus.security.ForbiddenException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
@Authorizer
public class DokumentAuthorizer extends BaseAuthorizer {
    private final GesuchRepository gesuchRepository;
    private final BenutzerService benutzerService;
    private final GesuchTrancheRepository gesuchTrancheRepository;
    private final GesuchDokumentRepository gesuchDokumentRepository;
    private final SozialdienstService sozialdienstService;

    @Transactional
    public void canGetDokumentDownloadToken(final UUID dokumentId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        if (isAdminSbOrJurist(currentBenutzer)) {
            return;
        }

        final var fall = gesuchRepository.requireGesuchForDokument(dokumentId).getAusbildung().getFall();
        final BooleanSupplier isMitarbeiter = () -> AuthorizerUtil
            .hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(fall, sozialdienstService);

        final BooleanSupplier isGesuchsteller = () -> AuthorizerUtil.isGesuchstellerOfFall(currentBenutzer, fall);
        if (isMitarbeiter.getAsBoolean() || isGesuchsteller.getAsBoolean()) {
            return;
        }

        forbidden();
    }

    @Transactional
    public void canUpload(final UUID gesuchTrancheId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);

        if (
            !AuthorizerUtil.isGesuchstellerOrDelegatedToSozialdienst(
                gesuchTranche.getGesuch(),
                currentBenutzer,
                sozialdienstService
            )
        ) {
            throw new ForbiddenException();
        }

        final var trancheTyp = gesuchTranche.getTyp();

        if (trancheTyp == GesuchTrancheTyp.TRANCHE) {
            AuthorizerUtil.gesuchStatusOneOfOrElseThrow(
                gesuchTranche.getGesuch(),
                List.of(Gesuchstatus.IN_BEARBEITUNG_GS, Gesuchstatus.FEHLENDE_DOKUMENTE)
            );
        } else if (trancheTyp == GesuchTrancheTyp.AENDERUNG) {
            AuthorizerUtil.gesuchTrancheStatusOneOfOrElseThrow(
                gesuchTranche,
                List.of(GesuchTrancheStatus.IN_BEARBEITUNG_GS, GesuchTrancheStatus.FEHLENDE_DOKUMENTE)
            );
        }
    }
}
