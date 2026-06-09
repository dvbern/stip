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

package ch.dvbern.stip.api.statistik.util;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.common.type.TenantIdentifier;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.integration.gemeindelookup.domain.model.GemeindeData;
import ch.dvbern.stip.integration.gemeindelookup.domain.model.GemeindeLookupRequest;
import ch.dvbern.stip.integration.gemeindelookup.domain.port.GemeindeLookupPortFactory;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class StatistikUtil {
    public static final List<Integer> HOCHSCHULSTUFEN_BFS_KATEGORIES = List.of(8, 9, 10);

    public static int booleanToBfsCode(final boolean value) {
        return value ? 1 : 2;
    }

    public static Optional<Integer> getGemeindeBfsNummer(
        final UUID gesuchId,
        final Adresse adresse,
        final TenantIdentifier tenantIdentifier,
        final GemeindeLookupPortFactory gemeindeLookupPortFactory
    ) {
        final var gemeindeLookupRequest = GemeindeLookupRequest.builder()
            .gesuchId(gesuchId)
            .tenantIdentifier(tenantIdentifier)
            .strasse(adresse.getStrasse())
            .hausnummer(adresse.getHausnummer())
            .plz(adresse.getPlz())
            .ort(adresse.getOrt())
            .build();

        return gemeindeLookupPortFactory.getGemeindeLookupAdapter()
            .findGemeindeData(gemeindeLookupRequest)
            .map(GemeindeData::bfsNummer);
    }

    public static Integer getSemesterCount(final LocalDate ausbildungBegin, final LocalDate ausbildungEnd, int year) {
        if (ausbildungBegin.getYear() == year) {
            if (DateUtil.isFruehling(ausbildungBegin)) {
                return 2;
            }
            if (DateUtil.isHerbst(ausbildungBegin)) {
                return 1;
            }
            return null;
        }

        if (ausbildungEnd.getYear() == year) {
            if (DateUtil.isFruehling(ausbildungEnd)) {
                return 1;
            }
            if (DateUtil.isHerbst(ausbildungEnd)) {
                return 2;
            }
            return null;
        }

        return 2;
    }
}
