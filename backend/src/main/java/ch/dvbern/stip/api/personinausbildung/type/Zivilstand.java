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

package ch.dvbern.stip.api.personinausbildung.type;

import java.util.Arrays;
import java.util.List;

public enum Zivilstand {
    LEDIG,
    VERHEIRATET,
    KONKUBINAT,
    EINGETRAGENE_PARTNERSCHAFT,
    GESCHIEDEN_GERICHTLICH,
    AUFGELOESTE_PARTNERSCHAFT,
    VERWITWET;

    public static List<Zivilstand> getZivilstandsNoPartnerschaft() {
        return Arrays.asList(LEDIG, GESCHIEDEN_GERICHTLICH, AUFGELOESTE_PARTNERSCHAFT, VERWITWET);
    }

    public static List<Zivilstand> getZivilstandsWithPartnerschaft() {
        return Arrays.asList(VERHEIRATET, KONKUBINAT, EINGETRAGENE_PARTNERSCHAFT);
    }

    public boolean hasPartnerschaft() {
        return getZivilstandsWithPartnerschaft().contains(this);
    }

}
