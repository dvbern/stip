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

package ch.dvbern.stip.api.common.service.seeding;

import java.util.List;

public abstract class Seeder {
    public static final int DEFAULT_PRIORITY = 1_000;

    /**
     * Returns the priority in which this Seeder should execute. Higher number means executes earlier
     */
    public int getPriority() {
        return DEFAULT_PRIORITY;
    }

    protected abstract void seed();

    /**
     * Returns a list of profiles on which to run seeding.
     */
    protected abstract List<String> getProfiles();
}
