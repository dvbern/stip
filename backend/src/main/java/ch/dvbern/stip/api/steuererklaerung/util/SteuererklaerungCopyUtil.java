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

package ch.dvbern.stip.api.steuererklaerung.util;

import java.util.LinkedHashSet;
import java.util.Set;

import ch.dvbern.stip.api.steuererklaerung.entity.Steuererklaerung;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SteuererklaerungCopyUtil {
    public Steuererklaerung createCopy(final Steuererklaerung other) {
        final var copy = new Steuererklaerung();
        copy.setSteuerdatenTyp(other.getSteuerdatenTyp());
        copy.setSteuererklaerungInBern(other.getSteuererklaerungInBern());

        return copy;
    }

    public Set<Steuererklaerung> createCopySet(final Set<Steuererklaerung> other) {
        final var copy = new LinkedHashSet<Steuererklaerung>();
        for (final var entry : other) {
            copy.add(createCopy(entry));
        }

        return copy;
    }
}
