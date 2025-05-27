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

package ch.dvbern.stip.api.stammdaten.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.stammdaten.type.Land;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Table(
    name = "land_eu_efta",
    uniqueConstraints = @UniqueConstraint(
        name = "UC_land_eu_efta_land_mandant", columnNames = { "land", "mandant" }
    ),
    indexes = @Index(name = "IX_land_mandant", columnList = "mandant,land")
)
@Audited
@Getter
@Setter
public class LandEuEfta extends AbstractMandantEntity {
    @NotNull
    @Column(name = "land", nullable = false)
    @Enumerated(EnumType.STRING)
    private Land land;

    @NotNull
    @Column(name = "is_eu_efta", nullable = false)
    private Boolean isEuEfta;

    @NotNull
    @Column(name = "laendercode_bfs", nullable = false)
    private String laendercodeBfs;

    @Nullable
    @Column(name = "iso3code", nullable = true)
    private String iso3code;

    @NotNull
    @Column(name = "de_kurzform", nullable = false)
    private String deKurzform;

    @NotNull
    @Column(name = "fr_kurzform", nullable = false)
    private String frKurzform;

    @NotNull
    @Column(name = "it_kurzform", nullable = false)
    private String itKurzform;

    @NotNull
    @Column(name = "en_kurzform", nullable = false)
    private String enKurzform;

    @NotNull
    @Column(name = "gueltig", nullable = false)
    private boolean gueltig = true;
}
