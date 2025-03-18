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

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungRepository;
import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.authorization.util.AuthorizerUtil;
import ch.dvbern.stip.api.fall.service.FallService;
import ch.dvbern.stip.api.gesuchstatus.service.GesuchStatusService;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
@Authorizer
public class AusbildungAuthorizer extends BaseAuthorizer {
    private final BenutzerService benutzerService;
    private final GesuchStatusService gesuchStatusService;
    private final AusbildungRepository ausbildungRepository;
    private final SozialdienstService sozialdienstService;
    private final FallService fallService;

    private boolean isGesuchstellerOfAusbildung(final Benutzer currentBenutzer, final Ausbildung ausbildung) {
        return Objects.equals(
            ausbildung.getFall().getGesuchsteller().getId(),
            currentBenutzer.getId()
        );
    }

    @Transactional
    public void canCreate(final UUID fallId) {
        if (!fallService.hasAktiveAusbildung(fallId)) {
            return;
        }

        forbidden();
    }

    @Transactional
    public void canRead(final UUID ausbildungId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();

        // Admins and Sachbearbeiter can always read every Gesuch
        if (isAdminOrSb(currentBenutzer)) {
            return;
        }

        final var ausbildung = ausbildungRepository.requireById(ausbildungId);
        if (
            AuthorizerUtil.hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(ausbildung, sozialdienstService)
        ) {
            return;
        }

        if (isGesuchstellerOfAusbildung(currentBenutzer, ausbildung)) {
            return;
        }

        forbidden();
    }

    @Transactional
    public boolean canUpdateCheck(final UUID ausbildungId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var ausbildung = ausbildungRepository.requireById(ausbildungId);

        // Only an Sb can edit a ausbildung, and only if it has at most one gesuch, at most one tranche anD the
        // gesuch is in the state IN_BEARBEITUNG_SB or ABKLAERUNG_DURCH_RECHSTABTEILUNG

        if (!isAdminOrSb(currentBenutzer)) {
            return false;
        }

        if (ausbildung.getGesuchs().isEmpty()) {
            return true;
        }

        if (ausbildung.getGesuchs().size() > 1) {
            return false;
        }

        final var gesuch = ausbildung.getGesuchs().get(0);

        if (gesuch.getGesuchTranchen().isEmpty()) {
            return true;
        }

        if (gesuch.getGesuchTranchen().size() > 1) {
            return false;
        }

        return gesuchStatusService.benutzerCanEdit(currentBenutzer, gesuch.getGesuchStatus());
    }

    public void canUpdate(final UUID ausbildungId) {
        if (!canUpdateCheck(ausbildungId)) {
            forbidden();
        }
    }
}
