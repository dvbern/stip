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

package ch.dvbern.stip.api.gesuchtranche.service;

import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatusChangeEvent;
import com.github.oxo42.stateless4j.triggers.TriggerWithParameters2;

public class GesuchTrancheStatusChangeEventTrigger
extends TriggerWithParameters2<GesuchTranche, String, GesuchTrancheStatusChangeEvent> {
    private GesuchTrancheStatusChangeEventTrigger(GesuchTrancheStatusChangeEvent underlyingTrigger) {
        super(underlyingTrigger, GesuchTranche.class, String.class);
    }

    public static GesuchTrancheStatusChangeEventTrigger createTrigger(final GesuchTrancheStatusChangeEvent event) {
        return new GesuchTrancheStatusChangeEventTrigger(event);
    }
}
