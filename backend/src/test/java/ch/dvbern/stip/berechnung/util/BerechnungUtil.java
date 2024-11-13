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

package ch.dvbern.stip.berechnung.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import ch.dvbern.stip.berechnung.dto.DmnRequest;
import ch.dvbern.stip.berechnung.dto.v1.BerechnungRequestV1;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BerechnungUtil {
    public DmnRequest getRequest(final int fall) {
        try {
            final var resource =
                BerechnungUtil.class.getClassLoader().getResource(String.format("berechnung/fall_%d.json", fall));
            assert resource != null;
            final var inputs = Files.readString(Paths.get(resource.toURI()));
            final var mapper = new ObjectMapper();
            return mapper.readValue(inputs, BerechnungRequestV1.class);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
