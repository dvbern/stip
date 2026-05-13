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

package ch.dvbern.stip.integration.gemeindelookup.adapter.swisstopo.service;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import ch.dvbern.stip.integration.gemeindelookup.adapter.swisstopo.type.SwisstopoApiFindAddrResponse;
import ch.dvbern.stip.integration.gemeindelookup.domain.model.GemeindeData;
import ch.dvbern.stip.integration.gemeindelookup.domain.model.GemeindeLookupAdapterType;
import ch.dvbern.stip.integration.gemeindelookup.domain.model.GemeindeLookupRequest;
import ch.dvbern.stip.integration.gemeindelookup.domain.port.GemeindeLookupPort;
import ch.dvbern.stip.integration.gemeindelookup.domain.qualifier.GemeindeLookupQualifier;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jose4j.json.internal.json_simple.JSONObject;

@Slf4j
@RequiredArgsConstructor
@RequestScoped
@GemeindeLookupQualifier(GemeindeLookupAdapterType.SWISSTOPO)
public class SwisstopoGemeindeLookupAdapter implements GemeindeLookupPort {
    private static final String ADDR_NO_SEARCH_LAYER_DEF_KEY = "ch.swisstopo.amtliches-gebaeudeadressverzeichnis";
    private static final String ADDR_NO_SEARCH_LAYER_DEF_SEARCH_STR = "adr_number ilike '%s'";

    @Inject
    @RestClient
    SwisstopoApiRestService swisstopoApiRestService;

    @Override
    public Optional<GemeindeData> findGemeindeData(final GemeindeLookupRequest request) {
        try {
            return findGemeindeDataByAddress(request)
                .or(() -> findGemeindeDataByPlz(request.plz()));
        } catch (Exception e) {
            LOG.warn(
                "Could not perform building lookup in Swisstopo data with street: {}, no: {}",
                request.strasse(),
                request.hausnummer(),
                e
            );

            return findGemeindeDataByPlz(request.plz());
        }
    }

    private Optional<GemeindeData> findGemeindeDataByAddress(final GemeindeLookupRequest request) {
        final var buildingNoSearchPartJson = new JSONObject();
        buildingNoSearchPartJson.put(
            ADDR_NO_SEARCH_LAYER_DEF_KEY,
            String.format(ADDR_NO_SEARCH_LAYER_DEF_SEARCH_STR, request.hausnummer())
        );

        final var result = swisstopoApiRestService.findAllMatchingBuildings(
            request.strasse(),
            buildingNoSearchPartJson.toString()
        );

        return result.results()
            .stream()
            .map(SwisstopoApiFindAddrResponse.SwisstopoApiFindAddrResponseElement::attributes)
            .filter(
                attributes -> attributes.zipLabel().contains(request.plz())
                || attributes.zipLabel().contains(request.ort())
            )
            .findFirst()
            .map(SwisstopoGeimeindeDataMapper::toGemeindeData);
    }

    private Optional<GemeindeData> findGemeindeDataByPlz(final String plz) {
        final var result = swisstopoApiRestService.findAllMatchingBuildingsByZipLabel(plz);

        final var counted = result.results()
            .stream()
            .map(SwisstopoApiFindAddrResponse.SwisstopoApiFindAddrResponseElement::attributes)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        if (counted.isEmpty()) {
            return Optional.empty();
        }

        final var max = Collections.max(counted.values());

        return counted.entrySet()
            .stream()
            .filter(entry -> entry.getValue().equals(max))
            .map(entry -> SwisstopoGeimeindeDataMapper.toGemeindeData(entry.getKey()))
            .findFirst();
    }
}
