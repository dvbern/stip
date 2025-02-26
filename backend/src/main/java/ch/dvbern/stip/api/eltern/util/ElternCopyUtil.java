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

package ch.dvbern.stip.api.eltern.util;

import java.util.LinkedHashSet;
import java.util.Set;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.adresse.util.AdresseCopyUtil;
import ch.dvbern.stip.api.common.util.AbstractPersonCopyUtil;
import ch.dvbern.stip.api.common.util.OverrideUtil;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ElternCopyUtil {
    public Eltern createCopyWithoutReferences(final Eltern other) {
        final var copy = new Eltern();
        copyValues(other, copy);
        return copy;
    }

    public void copyValues(final Eltern source, final Eltern target) {
        copyValuesWithoutReferences(source, target);
        target.setAdresse(source.getAdresse());
    }

    public void copyValuesWithoutReferences(final Eltern source, final Eltern target) {
        AbstractPersonCopyUtil.copy(source, target);
        target.setSozialversicherungsnummer(source.getSozialversicherungsnummer());
        target.setSozialhilfebeitraege(source.isSozialhilfebeitraege());
        target.setErgaenzungsleistungen(source.getErgaenzungsleistungen());
        target.setElternTyp(source.getElternTyp());
        target.setTelefonnummer(source.getTelefonnummer());
        target.setAusweisbFluechtling(source.getAusweisbFluechtling());
        target.setIdentischerZivilrechtlicherWohnsitz(source.isIdentischerZivilrechtlicherWohnsitz());
        target.setIdentischerZivilrechtlicherWohnsitzOrt(source.getIdentischerZivilrechtlicherWohnsitzOrt());
        target.setIdentischerZivilrechtlicherWohnsitzPLZ(source.getIdentischerZivilrechtlicherWohnsitzPLZ());
        target.setWohnkosten(source.getWohnkosten());
    }

    public Set<Eltern> createCopyOfSetWithoutReferences(final Set<Eltern> other) {
        final var copy = new LinkedHashSet<Eltern>();
        for (final var eltern : other) {
            copy.add(createCopyWithoutReferences(eltern));
        }

        return copy;
    }

    public void doOverrideOfSet(final Set<Eltern> targetEltern, final Set<Eltern> sourceEltern) {
        OverrideUtil.doOverrideOfSet(
            targetEltern,
            sourceEltern,
            ElternCopyUtil::copyValuesWithoutReferences,
            source -> {
                final var newTarget = new Eltern().setAdresse(new Adresse());

                ElternCopyUtil.copyValues(source, newTarget);
                AdresseCopyUtil.copyValues(source.getAdresse(), newTarget.getAdresse());

                return newTarget;
            }
        );
    }
}
