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

import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.type.TenantIdentifier;
import ch.dvbern.stip.integration.gemeindelookup.adapter.swisstopo.type.SwisstopoApiFindAddrResponse;
import ch.dvbern.stip.integration.gemeindelookup.domain.model.GemeindeLookupAdapterType;
import ch.dvbern.stip.integration.gemeindelookup.domain.model.GemeindeLookupRequest;
import ch.dvbern.stip.integration.gemeindelookup.domain.qualifier.GemeindeLookupQualifier;
import io.quarkus.test.InjectMock;
import io.quarkus.test.component.QuarkusComponentTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusComponentTest(SwisstopoGeimeindeDataMapperImpl.class)
public class SwisstopoGemeindeLookupAdapterTest {

    private static final UUID DEFAULT_GESUCH_ID = UUID.randomUUID();
    private static final TenantIdentifier DEFAULT_TENANT_IDENTIFIER = TenantIdentifier.BERN;
    private static final String DEFAULT_STRASSE = "Teststrasse";
    private static final String DEFAULT_HAUSNUMMER = "12A";
    private static final String DEFAULT_PLZ = "3000";
    private static final String DEFAULT_ORT = "Bern";
    private static final String DEFAULT_ORT_PLZ = String.format("%s %s", DEFAULT_ORT, DEFAULT_PLZ);

    private static final int DEFAULT_BFS_NUMMER = 351;
    private static final String DEFAULT_GEMEINDE_NAME = "Bern";

    @Inject
    @GemeindeLookupQualifier(GemeindeLookupAdapterType.SWISSTOPO)
    SwisstopoGemeindeLookupAdapter swisstopoGemeindeLookupAdapter;

    @InjectMock
    @RestClient
    SwisstopoApiRestService swisstopoApiRestService;

    @Test
    void findGemeindeData_matchByAddress_returnsGemeindeData() {
        when(swisstopoApiRestService.findAllMatchingBuildings(anyString(), anyString()))
            .thenReturn(
                response(
                    attributes(DEFAULT_ORT_PLZ, DEFAULT_BFS_NUMMER, DEFAULT_GEMEINDE_NAME),
                    attributes("8000 Zürich", 261, "Zürich")
                )
            );

        final var result = swisstopoGemeindeLookupAdapter.findGemeindeData(defaultRequest());

        assertThat(result.isPresent(), is(true));
        assertThat(result.get().bfsNummer(), is(DEFAULT_BFS_NUMMER));
        assertThat(result.get().name(), is(DEFAULT_GEMEINDE_NAME));
        verify(swisstopoApiRestService, never()).findAllMatchingBuildingsByZipLabel(anyString());
    }

    @Test
    void findGemeindeData_matchByOrt_returnsGemeindeData() {
        when(swisstopoApiRestService.findAllMatchingBuildings(anyString(), anyString()))
            .thenReturn(
                response(
                    attributes(String.format("%s %s", "3011", DEFAULT_ORT), DEFAULT_BFS_NUMMER, DEFAULT_GEMEINDE_NAME),
                    attributes(String.format("%s %s", DEFAULT_PLZ, "Andere Gemeinde"), 123, "Andere Gemeinde")
                )
            );

        final var request = GemeindeLookupRequest.builder()
            .gesuchId(DEFAULT_GESUCH_ID).tenantIdentifier(DEFAULT_TENANT_IDENTIFIER).strasse(DEFAULT_STRASSE).hausnummer(DEFAULT_HAUSNUMMER).plz("9999").ort(DEFAULT_ORT).build();

        final var result = swisstopoGemeindeLookupAdapter.findGemeindeData(request);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get().bfsNummer(), is(DEFAULT_BFS_NUMMER));
        assertThat(result.get().name(), is(DEFAULT_GEMEINDE_NAME));
        verify(swisstopoApiRestService, never()).findAllMatchingBuildingsByZipLabel(anyString());
    }

    @Test
    void findGemeindeData_lookupUsesStreetAndHausnummerLayerDef() {
        when(swisstopoApiRestService.findAllMatchingBuildings(anyString(), anyString()))
            .thenReturn(response(attributes(DEFAULT_ORT_PLZ, DEFAULT_BFS_NUMMER, DEFAULT_GEMEINDE_NAME)));

        swisstopoGemeindeLookupAdapter.findGemeindeData(defaultRequest());

        final var streetCaptor = ArgumentCaptor.forClass(String.class);
        final var layerDefsCaptor = ArgumentCaptor.forClass(String.class);
        verify(swisstopoApiRestService).findAllMatchingBuildings(streetCaptor.capture(), layerDefsCaptor.capture());

        assertThat(streetCaptor.getValue(), Matchers.equalTo(DEFAULT_STRASSE));
        assertThat(layerDefsCaptor.getValue(), containsString("ch.swisstopo.amtliches-gebaeudeadressverzeichnis"));
        assertThat(layerDefsCaptor.getValue(), containsString("adr_number ilike '" + DEFAULT_HAUSNUMMER + "'"));
    }

    @Test
    void findGemeindeData_lookupHasNoMatchingZipOrOrt_fallsBackToPlzLookup() {
        when(swisstopoApiRestService.findAllMatchingBuildings(anyString(), anyString()))
            .thenReturn(response(attributes("8000 Zürich", 261, "Zürich")));
        when(swisstopoApiRestService.findAllMatchingBuildingsByZipLabel(DEFAULT_PLZ))
            .thenReturn(response(attributes(DEFAULT_ORT_PLZ, DEFAULT_BFS_NUMMER, DEFAULT_GEMEINDE_NAME)));

        final var result = swisstopoGemeindeLookupAdapter.findGemeindeData(defaultRequest());

        assertThat(result.isPresent(), is(true));
        assertThat(result.get().bfsNummer(), is(DEFAULT_BFS_NUMMER));
        assertThat(result.get().name(), is(DEFAULT_GEMEINDE_NAME));
        verify(swisstopoApiRestService).findAllMatchingBuildingsByZipLabel(DEFAULT_PLZ);
    }

    @Test
    void findGemeindeData_lookupReturnsEmptyResults_fallsBackToPlzLookup() {
        when(swisstopoApiRestService.findAllMatchingBuildings(anyString(), anyString()))
            .thenReturn(response());
        when(swisstopoApiRestService.findAllMatchingBuildingsByZipLabel(DEFAULT_PLZ))
            .thenReturn(response(attributes(DEFAULT_ORT_PLZ, DEFAULT_BFS_NUMMER, DEFAULT_GEMEINDE_NAME)));

        final var result = swisstopoGemeindeLookupAdapter.findGemeindeData(defaultRequest());

        assertThat(result.isPresent(), is(true));
        assertThat(result.get().bfsNummer(), is(DEFAULT_BFS_NUMMER));
        assertThat(result.get().name(), is(DEFAULT_GEMEINDE_NAME));
        verify(swisstopoApiRestService).findAllMatchingBuildingsByZipLabel(DEFAULT_PLZ);
    }

    @Test
    void findGemeindeData_lookupThrowsException_fallsBackToPlzLookup() {
        when(swisstopoApiRestService.findAllMatchingBuildings(anyString(), anyString()))
            .thenThrow(new RuntimeException("Swisstopo address lookup failed"));
        when(swisstopoApiRestService.findAllMatchingBuildingsByZipLabel(DEFAULT_PLZ))
            .thenReturn(response(attributes(DEFAULT_ORT_PLZ, DEFAULT_BFS_NUMMER, DEFAULT_GEMEINDE_NAME)));

        final var result = swisstopoGemeindeLookupAdapter.findGemeindeData(defaultRequest());

        assertThat(result.isPresent(), is(true));
        assertThat(result.get().bfsNummer(), is(DEFAULT_BFS_NUMMER));
        assertThat(result.get().name(), is(DEFAULT_GEMEINDE_NAME));
        verify(swisstopoApiRestService).findAllMatchingBuildingsByZipLabel(DEFAULT_PLZ);
    }

    @Test
    void findGemeindeData_lookupTakesMostFrequentGemeinde() {
        when(swisstopoApiRestService.findAllMatchingBuildingsByZipLabel(DEFAULT_PLZ))
            .thenReturn(
                response(
                    attributes(DEFAULT_ORT_PLZ, DEFAULT_BFS_NUMMER, DEFAULT_GEMEINDE_NAME),
                    attributes(DEFAULT_ORT_PLZ, DEFAULT_BFS_NUMMER, DEFAULT_GEMEINDE_NAME),
                    attributes(String.format("%s %s", DEFAULT_ORT_PLZ, "Ostermundigen"), DEFAULT_BFS_NUMMER, "Ostermundigen")
                )
            );

        final var result = swisstopoGemeindeLookupAdapter.findGemeindeData(defaultRequest());

        assertThat(result.isPresent(), is(true));
        assertThat(result.get().bfsNummer(), is(DEFAULT_BFS_NUMMER));
        assertThat(result.get().name(), is(DEFAULT_GEMEINDE_NAME));
    }

    @Test
    void findGemeindeData_lookupReturnsEmpty_returnEmptyOptional() {
        when(swisstopoApiRestService.findAllMatchingBuildings(anyString(), anyString()))
            .thenReturn(response());
        when(swisstopoApiRestService.findAllMatchingBuildingsByZipLabel(DEFAULT_PLZ))
            .thenReturn(response());

        final var result = swisstopoGemeindeLookupAdapter.findGemeindeData(defaultRequest());

        assertThat(result, notNullValue());
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    void findGemeindeData_lookupResultMapsNullFields() {
        when(swisstopoApiRestService.findAllMatchingBuildings(anyString(), anyString()))
            .thenReturn(response());
        when(swisstopoApiRestService.findAllMatchingBuildingsByZipLabel(DEFAULT_PLZ))
            .thenReturn(response(attributes(DEFAULT_ORT_PLZ, null, null)));

        final var result = swisstopoGemeindeLookupAdapter.findGemeindeData(defaultRequest());

        assertThat(result.isPresent(), is(true));
        assertThat(result.get().bfsNummer(), nullValue());
        assertThat(result.get().name(), nullValue());
    }

    private static GemeindeLookupRequest defaultRequest() {
        return GemeindeLookupRequest.builder()
            .gesuchId(DEFAULT_GESUCH_ID)
            .tenantIdentifier(DEFAULT_TENANT_IDENTIFIER)
            .strasse(DEFAULT_STRASSE)
            .hausnummer(DEFAULT_HAUSNUMMER)
            .plz(DEFAULT_PLZ)
            .ort(DEFAULT_ORT)
            .build();
    }

    private static SwisstopoApiFindAddrResponse response(
        final SwisstopoApiFindAddrResponse.SwisstopoApiFindAddrResponseElementAttributes... attributes
    ) {
        return new SwisstopoApiFindAddrResponse(
            Stream.of(attributes)
                .map(SwisstopoGemeindeLookupAdapterTest::element)
                .toList()
        );
    }

    private static SwisstopoApiFindAddrResponse.SwisstopoApiFindAddrResponseElement element(
        final SwisstopoApiFindAddrResponse.SwisstopoApiFindAddrResponseElementAttributes attributes
    ) {
        return new SwisstopoApiFindAddrResponse.SwisstopoApiFindAddrResponseElement(attributes);
    }

    private static SwisstopoApiFindAddrResponse.SwisstopoApiFindAddrResponseElementAttributes attributes(
        final String zipLabel,
        final Integer comFosnr,
        final String comName
    ) {
        return new SwisstopoApiFindAddrResponse.SwisstopoApiFindAddrResponseElementAttributes(
            zipLabel,
            comFosnr,
            comName
        );
    }
}
