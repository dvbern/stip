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

package ch.dvbern.stip.gesuchsperiode.dto;

import ch.dvbern.stip.gesuchsperiode.model.Gesuchsperiode;
import ch.dvbern.stip.types.DateRange;
import lombok.Value;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Value
public class GesuchsperiodeDTO {
    @NotNull
    private UUID id;

    @NotNull
    private LocalDate gueltigAb;

    @NotNull
    private LocalDate gueltigBis;

    private LocalDate einreichfrist;

    private LocalDate aufschaltdatum;

    public static GesuchsperiodeDTO from(Gesuchsperiode changed) {
        return new GesuchsperiodeDTO(changed.getId(), changed.getGueltigkeit().getGueltigAb(), changed.getGueltigkeit().getGueltigBis(), changed.getEinreichfrist(), changed.getAufschaltdatum());
    }

    public void apply(Gesuchsperiode gesuchsperiode) {
        gesuchsperiode.setGueltigkeit(new DateRange(this.gueltigAb, this.gueltigBis));
        gesuchsperiode.setEinreichfrist(this.einreichfrist);
        gesuchsperiode.setAufschaltdatum(this.aufschaltdatum);
    }
}
