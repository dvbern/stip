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

package ch.dvbern.stip.api.einnahmen_kosten;

import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchformular.util.GesuchFormularCalculationUtil;
import jakarta.annotation.Nullable;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EinnahmenKostenMappingUtil {
    @Nullable
    public Integer calculateVermoegen(final GesuchFormular gesuchFormular) {
        if (gesuchFormular.getEinnahmenKosten() == null) {
            return null;
        }
        Integer vermoegen = gesuchFormular.getEinnahmenKosten().getVermoegen(); // todo: also do it for ekPartner
        if (GesuchFormularCalculationUtil.wasGSOlderThan18(gesuchFormular)) {
            return vermoegen;
        }
        return null;
    }

    @Nullable
    public Integer calculateVermoegenForPatner(final GesuchFormular gesuchFormular) {
        if (gesuchFormular.getEinnahmenKostenPartner() == null) {
            return null;
        }
        Integer vermoegen = gesuchFormular.getEinnahmenKostenPartner().getVermoegen(); // todo: also do it for ekPartner
        if (GesuchFormularCalculationUtil.wasPartnerOlderThan18(gesuchFormular)) {
            return vermoegen;
        }
        return null;
    }

}
