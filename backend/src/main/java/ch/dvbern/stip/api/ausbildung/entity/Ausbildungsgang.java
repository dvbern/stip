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

package ch.dvbern.stip.api.ausbildung.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(
    name = "ausbildungsgang",
    indexes = {
        @Index(name = "IX_ausbildungsgang_mandant", columnList = "mandant"),
        @Index(name = "IX_ausbildungsgang_ausbildungsstaette_id", columnList = "ausbildungsstaette_id"),
        @Index(name = "IX_ausbildungsgang_abschluss_id", columnList = "abschluss_id")
    }
)
@Getter
@Setter
public class Ausbildungsgang extends AbstractMandantEntity {
    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(
        name = "ausbildungsstaette_id",
        foreignKey = @ForeignKey(name = "FK_ausbildungsgang_ausbildungsstaette_id"),
        nullable = false
    )
    private Ausbildungsstaette ausbildungsstaette;

    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(
        name = "abschluss_id",
        foreignKey = @ForeignKey(name = "FK_ausbildungsgang_abschluss_id"),
        nullable = false
    )
    private Abschluss abschluss;

    @NotNull
    @Column(name = "aktiv", nullable = false)
    private boolean aktiv = true;
}
