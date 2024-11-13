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

package ch.dvbern.stip.api.plz.entity;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Table(
    name = "plz",
    uniqueConstraints = @UniqueConstraint(
        name = "UC_plz_ort_kantonskuerzel", columnNames = { "plz", "ort", "kantonskuerzel" }
    ),
    indexes = @Index(name = "IX_plz_kantonskuerzel", columnList = "kantonskuerzel")
)
@Audited
@Getter
@Setter
public class Plz extends AbstractEntity {
    @NotNull
    @Column(name = "plz", nullable = false)
    private String plz;

    @NotNull
    @Column(name = "ort", nullable = false)
    private String ort;

    @NotNull
    @Column(name = "kantonskuerzel", nullable = false)
    private String kantonskuerzel;
}
