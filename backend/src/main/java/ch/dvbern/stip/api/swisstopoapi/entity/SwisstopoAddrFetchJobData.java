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

package ch.dvbern.stip.api.swisstopoapi.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import jakarta.ws.rs.BadRequestException;
import lombok.Getter;
import org.quartz.JobDataMap;

public class SwisstopoAddrFetchJobData {
    private static final String GESUCH_ID_KEY = "gesuchId";
    private static final String HAUSNUMMER_KEY = "hausnummer";
    private static final String STRASSE_KEY = "strasse";
    private static final String PLZ_KEY = "plz";
    private static final String ORT_KEY = "ort";
    private static final String MANDANT_IDENTIFIER_KEY = "mandantIdentifier";

    @Getter
    private final UUID gesuchId;
    @Getter
    private final String hausnummer;
    @Getter
    private final String strasse;
    @Getter
    private final String plz;
    @Getter
    private final String ort;
    @Getter
    private final String mandantIdentifier;

    public SwisstopoAddrFetchJobData(
    final UUID gesuchId,
    final Adresse adresse,
    final String mandantIdentifier
    ) {
        this.gesuchId = gesuchId;
        this.hausnummer = adresse.getHausnummer();
        this.strasse = adresse.getStrasse();
        this.plz = adresse.getPlz();
        this.ort = adresse.getOrt();
        this.mandantIdentifier = mandantIdentifier;
    }

    public SwisstopoAddrFetchJobData(
    final JobDataMap map
    ) {
        if (
            !(map.containsKey(GESUCH_ID_KEY)
            && map.containsKey(HAUSNUMMER_KEY)
            && map.containsKey(STRASSE_KEY)
            && map.containsKey(PLZ_KEY)
            && map.containsKey(ORT_KEY)
            && map.containsKey(MANDANT_IDENTIFIER_KEY))
        ) {
            throw new BadRequestException("SwisstopoAddrFetchJobData: missing some required keys in the map");
        }
        this.gesuchId = (UUID) map.get(GESUCH_ID_KEY);
        this.hausnummer = (String) map.get(HAUSNUMMER_KEY);
        this.strasse = (String) map.get(STRASSE_KEY);
        this.plz = (String) map.get(PLZ_KEY);
        this.ort = (String) map.get(ORT_KEY);
        this.mandantIdentifier = (String) map.get(MANDANT_IDENTIFIER_KEY);
    }

    public JobDataMap toMap() {
        if (
            Objects.isNull(gesuchId)
            || Objects.isNull(hausnummer)
            || Objects.isNull(strasse)
            || Objects.isNull(plz)
            || Objects.isNull(ort)
            || Objects.isNull(mandantIdentifier)
        ) {
            throw new BadRequestException("SwisstopoAddrFetchJobData: fields must not be null");
        }

        final Map<String, Object> ret = new HashMap<>();
        ret.put(GESUCH_ID_KEY, this.gesuchId);
        ret.put(HAUSNUMMER_KEY, this.hausnummer);
        ret.put(STRASSE_KEY, this.strasse);
        ret.put(PLZ_KEY, this.plz);
        ret.put(ORT_KEY, this.ort);
        ret.put(MANDANT_IDENTIFIER_KEY, this.mandantIdentifier);
        return new JobDataMap(ret);
    }
}
