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

package ch.dvbern.stip.api.swisstopoapi.service;

import ch.dvbern.stip.api.swisstopoapi.entity.SwisstopoApiFindAddrResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/find")
@RegisterRestClient(configKey = "swisstopo-rest-api")
public interface SwisstopoApiRestService {
    String LAYER_VALUE_AMTLICHES_GEBAEUDEADRESSVERZEICHNIS = "ch.swisstopo.amtliches-gebaeudeadressverzeichnis";
    String SEARCH_FIELD_VALUE_STREET_NAME_LABEL = "stn_label";
    String SEARCH_FIELD_VALUE_ZIP_LABEL = "zip_label";

    default SwisstopoApiFindAddrResponse findAllMatchingBuildings(String searchText, String layerDefs) {
        return findAllMatchingBuildings(
            LAYER_VALUE_AMTLICHES_GEBAEUDEADRESSVERZEICHNIS,
            SEARCH_FIELD_VALUE_STREET_NAME_LABEL,
            searchText,
            layerDefs
        );
    }

    default SwisstopoApiFindAddrResponse findAllMatchingBuildingsByZipLabel(String searchText) {
        return findAllMatchingBuildings(
            LAYER_VALUE_AMTLICHES_GEBAEUDEADRESSVERZEICHNIS,
            SEARCH_FIELD_VALUE_ZIP_LABEL,
            searchText
        );
    }

    @GET
    SwisstopoApiFindAddrResponse findAllMatchingBuildings(
        @QueryParam("layer") String layer,
        @QueryParam("searchField") String searchField,
        @QueryParam("searchText") String searchText,
        @QueryParam("layerDefs") String layerDefs
    );

    @GET
    SwisstopoApiFindAddrResponse findAllMatchingBuildings(
        @QueryParam("layer") String layer,
        @QueryParam("searchField") String searchField,
        @QueryParam("searchText") String searchText
    );
}
