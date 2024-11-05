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

        copy.setNettoerwerbseinkommen(other.getNettoerwerbseinkommen());
        copy.setFahrkosten(other.getFahrkosten());
        copy.setWohnkosten(other.getWohnkosten());
        copy.setWgWohnend(other.getWgWohnend());
        copy.setVerdienstRealisiert(other.getVerdienstRealisiert());
        copy.setAlimente(other.getAlimente());
        copy.setZulagen(other.getZulagen());
        copy.setRenten(other.getRenten());
        copy.setEoLeistungen(other.getEoLeistungen());
        copy.setErgaenzungsleistungen(other.getErgaenzungsleistungen());
        copy.setBeitraege(other.getBeitraege());
        copy.setAusbildungskostenSekundarstufeZwei(other.getAusbildungskostenSekundarstufeZwei());
        copy.setAusbildungskostenTertiaerstufe(other.getAusbildungskostenTertiaerstufe());
        copy.setWillDarlehen(other.getWillDarlehen());
        copy.setAuswaertigeMittagessenProWoche(other.getAuswaertigeMittagessenProWoche());
        copy.setBetreuungskostenKinder(other.getBetreuungskostenKinder());
        copy.setVeranlagungsCode(other.getVeranlagungsCode());
        copy.setSteuerjahr(other.getSteuerjahr());
        copy.setVermoegen(other.getVermoegen());

        return copy;
    }
}
