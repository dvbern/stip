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

package ch.dvbern.stip.api.common.json;

import java.util.UUID;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.With;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@With
public class CreatedResponseBuilder {
    @SuppressWarnings({ "java:S1170", "java:S1075" } /* this is a Builder! */)
    private static final String RESOURCE_PATH = "/{id}";

    UUID id;
    Class<?> resourceClass;

    public static CreatedResponseBuilder of(
        UUID id,
        Class<?> resourceClass
    ) {
        return new CreatedResponseBuilder(id, resourceClass);
    }

    public Response build() {
        var uri = UriBuilder.fromResource(resourceClass)
            .path(RESOURCE_PATH)
            .build(id);

        return Response.status(Status.CREATED)
            .location(uri)
            .build();
    }
}
