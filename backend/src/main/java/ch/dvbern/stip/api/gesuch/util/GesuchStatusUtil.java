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

package ch.dvbern.stip.api.gesuch.util;

import java.util.Objects;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GesuchStatusUtil {
    public boolean gsReceivesGesuchdataOfStateEingereicht(final Gesuch gesuch) {
        // only show actual data to GS if Gesuch is NOT EINGEREICHT or VERFUEGT or further
        final var wasOnceEingereicht = Objects.nonNull(gesuch.getEinreichedatum());

        return wasOnceEingereicht && Gesuchstatus.SB_IS_EDITING_GESUCH.contains(gesuch.getGesuchStatus());
    }

    public boolean sbReceivesChanges(final Gesuch gesuch) {
        return Gesuchstatus.SACHBEARBEITER_CAN_VIEW_CHANGES.contains(gesuch.getGesuchStatus());
    }
}
