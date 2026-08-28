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

import java.util.Optional;

import ch.dvbern.stip.berechnung.domain.model.BerechnungAdapterType;
import ch.dvbern.stip.integration.gemeindelookup.domain.model.GemeindeLookupAdapterType;
import ch.dvbern.stip.integration.pdf.domain.model.PdfAdapterType;
import ch.dvbern.stip.integration.plzfetch.domain.model.PlzFetchAdapterType;
import ch.dvbern.stip.integration.steuerdaten.domain.model.SteuerdatenAdapterType;
import ch.dvbern.stip.integration.zahlung.domain.model.ZahlungAdapterType;
import io.smallrye.config.WithDefault;

public interface TenantPortConfig {
    Steuerdaten steuerdaten();

    Berechnung berechnung();

    Pdf pdf();

    GemeindeLookup gemeindeLookup();

    PlzFetch plzFetch();

    Zahlung zahlung();

    interface Steuerdaten {
        @WithDefault("false")
        Boolean enabled();

        Optional<SteuerdatenAdapterType> adapterType();
    }

    interface Berechnung {
        BerechnungAdapterType adapterType();

        int majorVersion();

        int minorVersion();
    }

    interface Pdf {
        PdfAdapterType adapterType();
    }

    interface GemeindeLookup {
        @WithDefault("swisstopo")
        GemeindeLookupAdapterType adapterType();
    }

    interface PlzFetch {
        @WithDefault("swisstopo")
        PlzFetchAdapterType adapterType();
    }

    interface Zahlung {
        ZahlungAdapterType adapterType();
    }
}
