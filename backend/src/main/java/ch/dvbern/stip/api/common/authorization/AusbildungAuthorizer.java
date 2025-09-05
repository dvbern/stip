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

import ch.dvbern.stip.api.ausbildung.repo.AusbildungRepository;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.authorization.util.AuthorizerUtil;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.fall.service.FallService;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
@Authorizer
public class AusbildungAuthorizer extends BaseAuthorizer {
    private final BenutzerService benutzerService;
    private final AusbildungRepository ausbildungRepository;
    private final SozialdienstService sozialdienstService;
    private final FallService fallService;
    private final FallRepository fallRepository;

    @Transactional
    public void canCreate(final UUID fallId) {
        if (
            !fallService.hasAktiveAusbildung(fallId)
            && AuthorizerUtil.canWriteAndIsGesuchstellerOfOrDelegatedToSozialdienst(
                fallRepository.requireById(fallId),
                benutzerService.getCurrentBenutzer(),
                sozialdienstService
            )
        ) {
            return;
        }

        forbidden();
    }

    @Transactional
    public void canRead(final UUID ausbildungId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();

        // Admins, Sachbearbeiter, Freigabestelle or Jurist can always read every Gesuch
        if (isSbOrFreigabestelleOrJurist(currentBenutzer)) {
            return;
        }

        if (
            AuthorizerUtil.canWriteAndIsGesuchstellerOfOrDelegatedToSozialdienst(
                ausbildungRepository.requireById(ausbildungId),
                benutzerService.getCurrentBenutzer(),
                sozialdienstService
            )
        ) {
            return;
        }
        forbidden();
    }

    @Transactional
    public boolean canUpdateCheck(final UUID ausbildungId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var ausbildung = ausbildungRepository.requireById(ausbildungId);

        // If a Folgegesuch was created, we can't update the Ausbildung anymore
        if (ausbildung.getGesuchs().stream().anyMatch(gesuch -> !gesuch.isErstgesuch())) {
            return false;
        }

        final var gesuch = ausbildung.getGesuchs().get(0);

        if (
            isSachbearbeiter(currentBenutzer) && Gesuchstatus.SACHBEARBEITER_CAN_EDIT.contains(gesuch.getGesuchStatus())
        ) {
            return true;
        }

        if (isJurist(currentBenutzer) && Gesuchstatus.JURIST_CAN_EDIT.contains(gesuch.getGesuchStatus())) {
            return true;
        }

        return Objects.isNull(gesuch.getEinreichedatum())
        && AuthorizerUtil.canWriteAndIsGesuchstellerOfOrDelegatedToSozialdienst(
            ausbildung,
            benutzerService.getCurrentBenutzer(),
            sozialdienstService
        );
    }

    public void canUpdate(final UUID ausbildungId) {
        if (!canUpdateCheck(ausbildungId)) {
            forbidden();
        }
    }
}
