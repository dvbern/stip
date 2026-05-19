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

package ch.dvbern.stip.integration.gemeindelookup.domain.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import ch.dvbern.stip.api.common.type.TenantIdentifier;
import jakarta.ws.rs.BadRequestException;
import lombok.Builder;
import org.quartz.JobDataMap;

@Builder
public record GemeindeLookupRequest(
UUID gesuchId, TenantIdentifier tenantIdentifier, String strasse, String hausnummer, String plz, String ort
) {

    private static final String GESUCH_ID_KEY = "gesuchId";
    private static final String TENANT_IDENTIFIER_KEY = "tenantIdentifier";
    private static final String STRASSE_KEY = "strasse";
    private static final String HAUSNUMMER_KEY = "hausnummer";
    private static final String PLZ_KEY = "plz";
    private static final String ORT_KEY = "ort";

    public GemeindeLookupRequest(final JobDataMap map) {
        this(
            parseGesuchId(map),
            TenantIdentifier.of((String) map.get(TENANT_IDENTIFIER_KEY)),
            (String) map.get(HAUSNUMMER_KEY),
            (String) map.get(STRASSE_KEY),
            (String) map.get(PLZ_KEY),
            (String) map.get(ORT_KEY)
        );
    }

    private static UUID parseGesuchId(final JobDataMap map) {
        if (
            !(map.containsKey(GESUCH_ID_KEY)
            && map.containsKey(TENANT_IDENTIFIER_KEY))
            && map.containsKey(STRASSE_KEY)
            && map.containsKey(HAUSNUMMER_KEY)
            && map.containsKey(PLZ_KEY)
            && map.containsKey(ORT_KEY)
        ) {
            throw new BadRequestException("GemeindeLookupRequest: missing some required keys in the map");
        }

        return UUID.fromString((String) map.get(GESUCH_ID_KEY));
    }

    public JobDataMap toMap() {
        if (
            Objects.isNull(gesuchId)
            || Objects.isNull(tenantIdentifier)
            || Objects.isNull(strasse)
            || Objects.isNull(hausnummer)
            || Objects.isNull(plz)
            || Objects.isNull(ort)
        ) {
            throw new BadRequestException("GemeindeLookupRequest: fields must not be null");
        }

        final Map<String, Object> ret = new HashMap<>();
        ret.put(GESUCH_ID_KEY, this.gesuchId.toString());
        ret.put(STRASSE_KEY, this.strasse);
        ret.put(HAUSNUMMER_KEY, this.hausnummer);
        ret.put(PLZ_KEY, this.plz);
        ret.put(ORT_KEY, this.ort);
        ret.put(TENANT_IDENTIFIER_KEY, this.tenantIdentifier.getIdentifier());
        return new JobDataMap(ret);
    }
}
