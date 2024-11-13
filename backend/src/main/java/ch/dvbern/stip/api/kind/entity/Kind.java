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

package ch.dvbern.stip.api.kind.entity;

import java.util.UUID;

import ch.dvbern.stip.api.common.entity.AbstractPerson;
import ch.dvbern.stip.api.common.type.Ausbildungssituation;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(
    name = "kind",
    indexes = @Index(name = "IX_kind_mandant", columnList = "mandant")
)
@Getter
@Setter
public class Kind extends AbstractPerson {
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "ausbildungssituation", nullable = false)
    private Ausbildungssituation ausbildungssituation;

    @Nullable
    @Column(name = "erhaltene_alimentebeitraege")
    private Integer erhalteneAlimentebeitraege;

    @Nullable
    @Column(name = "copy_of_id")
    private UUID copyOfId;

    @NotNull
    @Column(name = "wohnsitz_anteil_pia", nullable = false)
    @Max(100)
    @Min(0)
    private Integer wohnsitzAnteilPia;
}
