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

package ch.dvbern.stip.api.gesuchsperioden.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.common.entity.DateRange;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

@Audited
@Entity
@Table(indexes = {
        @Index(name = "IX_gesuchsperiode_aufschaltdatum_gueltig_bis", columnList = "aufschaltdatum,gueltig_bis"),
        @Index(name = "IX_gesuchsperiode_mandant", columnList = "mandant")
})
@Getter
@Setter
public class Gesuchsperiode extends AbstractMandantEntity {

    @NotNull
    @Embedded
    @Valid
    @AttributeOverride(name = "gueltigBis", column = @Column(name = "gueltig_bis"))
    private DateRange gueltigkeit = new DateRange();

    @Column(nullable = true)
    private LocalDate einreichfrist;

    @Column(nullable = true)
    private LocalDate aufschaltdatum;
}
