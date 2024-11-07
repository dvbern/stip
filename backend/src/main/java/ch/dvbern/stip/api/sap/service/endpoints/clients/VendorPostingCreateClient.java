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

package ch.dvbern.stip.api.sap.service.endpoints.clients;

import ch.dvbern.stip.api.sap.util.RequestHeaderProviderUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@ApplicationScoped
@Produces(MediaType.TEXT_XML)
@Consumes(MediaType.TEXT_XML)
@RegisterRestClient(configKey = "AuszahlungSapVendorPostingCreateEndpoint")
@ClientHeaderParam(name = "Authorization", value = "{getAuthHeader}")
public interface VendorPostingCreateClient extends RequestHeaderProviderUtil {
    @POST
    String createVendorPosting(String xml);
}
