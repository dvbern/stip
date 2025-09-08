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

import java.util.Map;

import ch.dvbern.stip.berechnung.dto.CalculatorRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DmnRequestContextUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public Map<String, Object> toContext(final CalculatorRequest berechnungRequest) {
        // Use Jackson to serialize the BerechnungRequest to a Map<String, Object> via JSON
        // Once we have a separate DMN Server we'll send the JSON as the input directly
        return OBJECT_MAPPER.convertValue(berechnungRequest, new TypeReference<>() {});
    }
}
