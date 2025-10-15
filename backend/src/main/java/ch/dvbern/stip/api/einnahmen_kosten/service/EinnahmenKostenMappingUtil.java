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

package ch.dvbern.stip.api.einnahmen_kosten.service;

import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchformular.util.GesuchFormularCalculationUtil;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EinnahmenKostenMappingUtil {
    public Integer calculateVermoegen(final GesuchFormular gesuchFormular) {
        if (gesuchFormular.getEinnahmenKosten() == null) {
            return null;
        }
        Integer vermoegen = gesuchFormular.getEinnahmenKosten().getVermoegen();
        if (GesuchFormularCalculationUtil.wasGSOlderThan18(gesuchFormular)) {
            return vermoegen;
        }
        return null;
    }

    public Integer calculateSteuern(final GesuchFormular gesuchFormular) {
        if (gesuchFormular.getEinnahmenKosten() == null) {
            return null;
        }
        int totalEinkommen = 0;
        if (
            gesuchFormular.getEinnahmenKosten() != null
            && gesuchFormular.getEinnahmenKosten().getNettoerwerbseinkommen() != null
        ) {
            totalEinkommen += gesuchFormular.getEinnahmenKosten().getNettoerwerbseinkommen();
        }
        if (gesuchFormular.getPartner() != null
        // todo kstip-2779: check parter einnahmekosten here
        // && gesuchFormular.getPartner().getJahreseinkommen() != null
        ) {
            // todo kstip-2779: add parter einnahmekosten here
            // totalEinkommen += gesuchFormular.getPartner().getJahreseinkommen();
        }
        if (totalEinkommen >= 20000) {
            return (int) (totalEinkommen * 0.1);
        }
        return 0;
    }
}
