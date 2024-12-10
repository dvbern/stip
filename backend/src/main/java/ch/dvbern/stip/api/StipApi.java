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

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@QuarkusMain
@ApplicationPath("api/v1")
public class StipApi extends Application {
    public static void main(final String... args) {
        final var shouldClear = Boolean.parseBoolean(System.getenv("KSTIP_CLEAR_DATABASE"));

        if (shouldClear) {
            Quarkus.run(StipDbClearApplication.class, args);
        } else {
            Quarkus.run(args);
        }
    }
}
