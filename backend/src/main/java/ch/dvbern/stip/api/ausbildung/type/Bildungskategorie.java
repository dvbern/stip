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

package ch.dvbern.stip.api.ausbildung.type;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public enum Bildungskategorie {
    TERTIAERSTUFE_A,
    TERTIAERSTUFE_B,
    SEKUNDARSTUFE_I,
    SEKUNDARSTUFE_II,
    // todo: clarify with Dänu & remove hopefully
    BRUECKENANGEBOT;

    public boolean isTertiaerstufe() {
        return TERTIAERSTUFEN.contains(this);
    }

    public static final Set<Bildungskategorie> TERTIAERSTUFEN = Collections.unmodifiableSet(
        EnumSet.of(
            TERTIAERSTUFE_A,
            TERTIAERSTUFE_B
        )
    );

    public static final Set<Bildungskategorie> SEKUNDARSTUFEN = Collections.unmodifiableSet(
        EnumSet.of(
            SEKUNDARSTUFE_I,
            SEKUNDARSTUFE_II
        )
    );
}
