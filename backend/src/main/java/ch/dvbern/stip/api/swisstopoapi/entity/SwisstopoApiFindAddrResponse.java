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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class SwisstopoApiFindAddrResponse {
    List<SwisstopoApiFindAddrResponseElement> results;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    public static class SwisstopoApiFindAddrResponseElement {
        SwisstopoApiFindAddrResponseElementAttributes attributes;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    public static class SwisstopoApiFindAddrResponseElementAttributes {
        String zip_label;
        Integer com_fosnr;
        String com_name;
    }
}
