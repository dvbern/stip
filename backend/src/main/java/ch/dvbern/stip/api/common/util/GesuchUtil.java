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

package ch.dvbern.stip.api.common.util;

import java.util.Set;

import ch.dvbern.stip.api.ausbildung.util.AusbildungUnterbruchAntragUtil;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GesuchUtil {

    public boolean openAenderungAlreadyExists(final Gesuch gesuch) {
        final var allowedStates = Set.of(
            GesuchTrancheStatus.AKZEPTIERT,
            GesuchTrancheStatus.ABGELEHNT,
            GesuchTrancheStatus.MANUELLE_AENDERUNG
        );

        final var trancheInDisallowedStates = gesuch.getAenderungen()
            .filter(gesuchTranche -> !allowedStates.contains(gesuchTranche.getStatus()))
            .toList();

        return !trancheInDisallowedStates.isEmpty();
    }

    public boolean canCreateAenderung(final Gesuch gesuch) {
        return !openAenderungAlreadyExists(gesuch) && gesuchAbgeschlossen(gesuch)
        && !AusbildungUnterbruchAntragUtil.openAusbildungUnterbruchAntragExists(gesuch.getAusbildung());
    }

    public boolean gesuchAbgeschlossen(final Gesuch gesuch) {
        return Gesuchstatus.GESUCH_VERFUEGUNG_ABGESCHLOSSEN.contains(gesuch.getGesuchStatus());
    }

    public static boolean gesuchIsInStatus(final Gesuch gesuch, final Gesuchstatus gesuchStatus) {
        return gesuchIsInOneOfGesuchStatus(gesuch, Set.of(gesuchStatus));
    }

    public static boolean gesuchIsInOneOfGesuchStatus(final Gesuch gesuch, final Set<Gesuchstatus> gesuchStatusSet) {
        return gesuchStatusSet.contains(gesuch.getGesuchStatus());
    }
}
