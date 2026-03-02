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

package ch.dvbern.stip.api.ausbildung.util;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.entity.AusbildungUnterbruchAntrag;
import ch.dvbern.stip.api.ausbildung.type.AusbildungUnterbruchAntragStatus;
import ch.dvbern.stip.api.common.util.GesuchUtil;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AusbildungUnterbruchAntragUtil {
    public boolean openAusbildungUnterbruchAntragExists(final Ausbildung ausbildung) {
        return ausbildung.getAusbildungUnterbruchAntrags()
            .stream()
            .anyMatch(
                ausbildungUnterbruchAntrag -> AusbildungUnterbruchAntragStatus.IS_OPEN
                    .contains(ausbildungUnterbruchAntrag.getStatus())
            );
    }

    public boolean canAusbildungUnterbruchAntragAkzeptieren(
        final AusbildungUnterbruchAntrag ausbildungUnterbruchAntrag
    ) {
        return ausbildungUnterbruchAntrag.getStatus() == AusbildungUnterbruchAntragStatus.EINGEGEBEN
        && (GesuchUtil.gesuchAbgeschlossen(ausbildungUnterbruchAntrag.getGesuch())
        || GesuchUtil.gesuchIsInStatus(ausbildungUnterbruchAntrag.getGesuch(), Gesuchstatus.IN_BEARBEITUNG_SB))
        && !GesuchUtil.openAenderungAlreadyExists(ausbildungUnterbruchAntrag.getGesuch());
    }
}
