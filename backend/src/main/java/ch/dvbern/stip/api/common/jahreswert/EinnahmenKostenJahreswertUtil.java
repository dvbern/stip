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

package ch.dvbern.stip.api.common.jahreswert;

import java.util.List;

import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EinnahmenKostenJahreswertUtil {
    public void synchroniseJahresfelder(
        final EinnahmenKosten source,
        final List<EinnahmenKosten> targets
    ) {
        if (source == null) {
            return;
        }

        for (final var target : targets) {
            target.setNettoerwerbseinkommen(source.getNettoerwerbseinkommen());
            target.setArbeitspensumProzent(source.getArbeitspensumProzent());
            target.setZulagen(source.getZulagen());
            target.setEoLeistungen(source.getEoLeistungen());
            target.setErgaenzungsleistungen(source.getErgaenzungsleistungen());
            target.setBeitraege(source.getBeitraege());
            target.setEinnahmenBGSA(source.getEinnahmenBGSA());
            target.setTaggelderAHVIV(source.getTaggelderAHVIV());
            target.setAndereEinnahmen(source.getAndereEinnahmen());
            target.setVermoegen(source.getVermoegen());
        }
    }
}
