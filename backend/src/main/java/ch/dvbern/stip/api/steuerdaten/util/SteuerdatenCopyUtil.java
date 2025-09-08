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

import ch.dvbern.stip.api.common.util.OverrideUtil;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SteuerdatenCopyUtil {
    public Steuerdaten createCopy(final Steuerdaten other) {
        final var copy = new Steuerdaten();
        copyValues(other, copy);
        return copy;
    }

    private void copyValues(final Steuerdaten source, final Steuerdaten target) {
        target.setSteuerdatenTyp(source.getSteuerdatenTyp());
        target.setTotalEinkuenfte(source.getTotalEinkuenfte());
        target.setEigenmietwert(source.getEigenmietwert());
        target.setIsArbeitsverhaeltnisSelbstaendig(source.getIsArbeitsverhaeltnisSelbstaendig());
        target.setSaeule3a(source.getSaeule3a());
        target.setSaeule2(source.getSaeule2());
        target.setKinderalimente(source.getKinderalimente());
        target.setVermoegen(source.getVermoegen());
        target.setSteuernKantonGemeinde(source.getSteuernKantonGemeinde());
        target.setSteuernBund(source.getSteuernBund());
        target.setFahrkosten(source.getFahrkosten());
        target.setFahrkostenPartner(source.getFahrkostenPartner());
        target.setVerpflegung(source.getVerpflegung());
        target.setVerpflegungPartner(source.getVerpflegungPartner());
        target.setSteuerjahr(source.getSteuerjahr());
        target.setVeranlagungsStatus(source.getVeranlagungsStatus());
    }

    public Set<Steuerdaten> createCopySet(final Set<Steuerdaten> other) {
        final var copy = new LinkedHashSet<Steuerdaten>();
        for (final var entry : other) {
            copy.add(createCopy(entry));
        }

        return copy;
    }

    public void doOverrideOfSet(final Set<Steuerdaten> targetSteuerdaten, final Set<Steuerdaten> sourceSteuerdaten) {
        OverrideUtil.doOverrideOfSet(
            targetSteuerdaten,
            sourceSteuerdaten,
            SteuerdatenCopyUtil::copyValues,
            SteuerdatenCopyUtil::createCopy
        );
    }
}
