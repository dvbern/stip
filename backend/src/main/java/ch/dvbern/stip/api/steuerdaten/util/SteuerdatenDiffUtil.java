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

import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.generated.dto.SteuerdatenUpdateDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SteuerdatenDiffUtil {
    public static boolean hasArbeitsverhaeltnissChangedToUnselbstaendig(
        final SteuerdatenUpdateDto updateDto,
        final Steuerdaten toUpdate
    ) {
        if (updateDto.getIsArbeitsverhaeltnisSelbstaendig() == null) {
            // This shouldn't happen unless when using stubbed test data
            return false;
        }

        if (updateDto.getIsArbeitsverhaeltnisSelbstaendig().equals(toUpdate.getIsArbeitsverhaeltnisSelbstaendig())) {
            return false;
        }

        return !updateDto.getIsArbeitsverhaeltnisSelbstaendig();
    }
}
