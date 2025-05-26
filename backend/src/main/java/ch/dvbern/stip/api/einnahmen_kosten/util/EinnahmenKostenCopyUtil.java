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

package ch.dvbern.stip.api.einnahmen_kosten.util;

import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EinnahmenKostenCopyUtil {
    public EinnahmenKosten createCopy(final EinnahmenKosten other) {
        final var copy = new EinnahmenKosten();
        copyValues(other, copy);
        return copy;
    }

    public void copyValues(final EinnahmenKosten source, final EinnahmenKosten target) {
        target.setNettoerwerbseinkommen(source.getNettoerwerbseinkommen());
        target.setFahrkosten(source.getFahrkosten());
        target.setWohnkosten(source.getWohnkosten());
        target.setWgWohnend(source.getWgWohnend());
        target.setVerdienstRealisiert(source.getVerdienstRealisiert());
        target.setAlimente(source.getAlimente());
        target.setZulagen(source.getZulagen());
        target.setRenten(source.getRenten());
        target.setEoLeistungen(source.getEoLeistungen());
        target.setErgaenzungsleistungen(source.getErgaenzungsleistungen());
        target.setBeitraege(source.getBeitraege());
        target.setAusbildungskosten(source.getAusbildungskosten());
        target.setAuswaertigeMittagessenProWoche(source.getAuswaertigeMittagessenProWoche());
        target.setBetreuungskostenKinder(source.getBetreuungskostenKinder());
        target.setVeranlagungsCode(source.getVeranlagungsCode());
        target.setSteuerjahr(source.getSteuerjahr());
        target.setVermoegen(source.getVermoegen());
    }
}
