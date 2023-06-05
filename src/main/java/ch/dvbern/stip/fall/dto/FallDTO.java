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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.fall.dto;

import ch.dvbern.stip.fall.model.Fall;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Value
@Builder
@Jacksonized
public class FallDTO {

    @NotNull
    private UUID id;
    @NotNull
    private long fallNummer;

    @NotNull
    private String mandant;

    public FallDTO (UUID id, long fallNummer, String mandant) {
        this.id = id;
        this.fallNummer = fallNummer;
        this.mandant = mandant;
    }

    public static FallDTO from(Fall changed) {
        return new FallDTO(changed.getId(), changed.getFallNummer(), changed.getMandant());
    }

    public void apply(Fall fall) {
        fall.setFallNummer(fallNummer);
        fall.setMandant(mandant);
    }
}
