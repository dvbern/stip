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

package ch.dvbern.stip.api.steuerdaten.util;

import java.util.LinkedHashSet;
import java.util.Set;

import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SteuerdatenCopyUtil {
    public Steuerdaten createCopy(final Steuerdaten other) {
        final var copy = new Steuerdaten();

        copy.setSteuerdatenTyp(other.getSteuerdatenTyp());
        copy.setTotalEinkuenfte(other.getTotalEinkuenfte());
        copy.setEigenmietwert(other.getEigenmietwert());
        copy.setIsArbeitsverhaeltnisSelbstaendig(other.getIsArbeitsverhaeltnisSelbstaendig());
        copy.setSaeule3a(other.getSaeule3a());
        copy.setSaeule2(other.getSaeule2());
        copy.setKinderalimente(other.getKinderalimente());
        copy.setVermoegen(other.getVermoegen());
        copy.setSteuernKantonGemeinde(other.getSteuernKantonGemeinde());
        copy.setSteuernBund(other.getSteuernBund());
        copy.setFahrkosten(other.getFahrkosten());
        copy.setFahrkostenPartner(other.getFahrkostenPartner());
        copy.setVerpflegung(other.getVerpflegung());
        copy.setVerpflegungPartner(other.getVerpflegungPartner());
        copy.setSteuerjahr(other.getSteuerjahr());
        copy.setVeranlagungsCode(other.getVeranlagungsCode());

        return copy;
    }

    public Set<Steuerdaten> createCopySet(final Set<Steuerdaten> other) {
        final var copy = new LinkedHashSet<Steuerdaten>();
        for (final var entry : other) {
            copy.add(createCopy(entry));
        }

        return copy;
    }
}
