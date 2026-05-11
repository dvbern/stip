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

package ch.dvbern.stip.integration.steuerdaten.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SteuerdatenPortData {
    private Integer totalEinkuenfte;
    private Integer eigenmietwert;
    private Boolean isArbeitsverhaeltnisSelbstaendig;
    private Integer saeule3a;
    private Integer saeule2;
    private Integer vermoegen;
    private Integer steuernKantonGemeinde;
    private Integer steuernBund;
    private Integer steuerJahr;
    private String veranlagungsStatus;
    private Integer fahrkosten;
    private Integer fahrkostenPartner;
    private Integer verpflegung;
    private Integer verpflegungPartner;
}
