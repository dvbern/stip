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

package ch.dvbern.stip.api.dokument.util;

import java.util.Set;

import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DokumentOfJahreswertUtil {
    private static final Set<DokumentTyp> DOKUMENTE_ON_JAHRESWERTE = Set.of(
        DokumentTyp.STEUERERKLAERUNG_ERGAENZUNGSLEISTUNGEN_FAMILIE,
        DokumentTyp.STEUERERKLAERUNG_ERGAENZUNGSLEISTUNGEN_MUTTER,
        DokumentTyp.STEUERERKLAERUNG_ERGAENZUNGSLEISTUNGEN_VATER,
        DokumentTyp.STEUERERKLAERUNG_EINNAHMEN_BGSA_FAMILIE,
        DokumentTyp.STEUERERKLAERUNG_EINNAHMEN_BGSA_MUTTER,
        DokumentTyp.STEUERERKLAERUNG_EINNAHMEN_BGSA_VATER,
        DokumentTyp.STEUERERKLAERUNG_ANDERE_EINNAHMEN_FAMILIE,
        DokumentTyp.STEUERERKLAERUNG_ANDERE_EINNAHMEN_MUTTER,
        DokumentTyp.STEUERERKLAERUNG_ANDERE_EINNAHMEN_VATER,
        DokumentTyp.EK_LOHNABRECHNUNG,
        DokumentTyp.EK_BELEG_KINDERZULAGEN,
        DokumentTyp.EK_ENTSCHEID_ERGAENZUNGSLEISTUNGEN_EO,
        DokumentTyp.EK_VERFUEGUNG_ERGAENZUNGSLEISTUNGEN,
        DokumentTyp.EK_VERFUEGUNG_GEMEINDE_INSTITUTION,
        DokumentTyp.EK_BELEG_EINNAHMEN_BGSA,
        DokumentTyp.EK_BELEG_TAGGELDER_AHV_IV,
        DokumentTyp.EK_BELEG_ANDERE_EINNAHMEN,
        DokumentTyp.EK_VERMOEGEN,
        DokumentTyp.EK_PARTNER_LOHNABRECHNUNG,
        DokumentTyp.EK_PARTNER_BELEG_KINDERZULAGEN,
        DokumentTyp.EK_PARTNER_ENTSCHEID_ERGAENZUNGSLEISTUNGEN_EO,
        DokumentTyp.EK_PARTNER_VERFUEGUNG_ERGAENZUNGSLEISTUNGEN,
        DokumentTyp.EK_PARTNER_VERFUEGUNG_GEMEINDE_INSTITUTION,
        DokumentTyp.EK_PARTNER_BELEG_EINNAHMEN_BGSA,
        DokumentTyp.EK_PARTNER_BELEG_TAGGELDER_AHV_IV,
        DokumentTyp.EK_PARTNER_BELEG_ANDERE_EINNAHMEN,
        DokumentTyp.EK_PARTNER_VERMOEGEN
    );

    public boolean isDokumentOfJahreswert(final DokumentTyp dokumentTyp) {
        return DOKUMENTE_ON_JAHRESWERTE.contains(dokumentTyp);
    }

    public Set<DokumentTyp> getDokumentTypsOfJahreswerte() {
        return DOKUMENTE_ON_JAHRESWERTE;
    }
}
