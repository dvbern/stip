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

package ch.dvbern.stip.api.config.type;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import io.smallrye.config.WithDefault;

public interface TenantConfig {
    Set<String> subdomains();

    String frontendUri();

    Features features();

    Berechnung berechnung();

    WelcomeMail welcomeMail();

    Darlehen darlehen();

    Seeding seeding();

    Map<String, SchedulerConfig> scheduler();

    interface Features {
        boolean nesko();
    }

    interface Berechnung {
        @WithDefault("1")
        int currentMajorVersion();

        @WithDefault("0")
        int currentMinorVersion();
    }

    interface WelcomeMail {
        String kcPath();

        String kcQueryParameter();

        String kcScope();
    }

    interface Darlehen {
        Verfuegung verfuegung();

        interface Verfuegung {
            @WithDefault("ausbildungsdarlehen@mailbucket.dvbern.ch")
            String emailRecipient();
        }
    }

    interface Seeding {
        Optional<String> sozialdienste();
    }
}
