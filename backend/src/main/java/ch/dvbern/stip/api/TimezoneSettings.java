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

package ch.dvbern.stip.api;

import java.util.TimeZone;

import ch.dvbern.stip.api.common.util.DateUtil;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Singleton;

@Singleton
public class TimezoneSettings {
    public void setTimezone(@Observes StartupEvent startupEvent) {
        // Statically set the timezone to be Europe/Zurich, no matter what the local or passed zone is
        TimeZone.setDefault(TimeZone.getTimeZone(DateUtil.ZUERICH_ZONE));
        System.setProperty("user.timezone", DateUtil.ZUERICH_ZONE.getId());
    }
}
