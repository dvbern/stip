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

package ch.dvbern.stip.api.personinausbildung.util;

import java.util.Objects;

import ch.dvbern.stip.api.common.util.NullDiffUtil;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.generated.dto.PersonInAusbildungUpdateDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PersonInAusbildungDiffUtil {
    public boolean hasNameChanged(
        PersonInAusbildung target,
        PersonInAusbildungUpdateDto update
    ) {
        if (NullDiffUtil.hasNullChanged(target, update)) {
            return true;
        }

        String targetNachname = null;
        String toUpdateNachname = null;

        if (target != null) {
            targetNachname = target.getNachname();
        }

        if (update != null) {
            toUpdateNachname = update.getNachname();
        }

        return Objects.equals(targetNachname, toUpdateNachname);
    }
}
