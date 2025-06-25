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

package ch.dvbern.stip.api.land.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.land.type.WellKnownLand;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_ISO2CODE_LENGTH;
import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_ISO3CODE_LENGTH;
import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH;

@Entity
@Table(
    name = "land",
    uniqueConstraints = @UniqueConstraint(
        name = "UC_land_laendercode_bfs_mandant", columnNames = { "laendercode_bfs", "mandant" }
    ),
    indexes = @Index(name = "IX_land_laendercode_bfs_mandant", columnList = "laendercode_bfs,mandant")
)
@Audited
@Getter
@Setter
public class Land extends AbstractMandantEntity {
    @NotNull
    @Column(name = "is_eu_efta", nullable = false)
    private Boolean isEuEfta;

    @NotNull
    @Column(name = "laendercode_bfs", nullable = false)
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String laendercodeBfs;

    @Nullable
    @Column(name = "iso2code", nullable = true, length = DB_DEFAULT_STRING_ISO2CODE_LENGTH)
    @Size(min = DB_DEFAULT_STRING_ISO2CODE_LENGTH, max = DB_DEFAULT_STRING_ISO2CODE_LENGTH)
    private String iso2code;

    @Nullable
    @Column(name = "iso3code", nullable = true, length = DB_DEFAULT_STRING_ISO3CODE_LENGTH)
    @Size(min = DB_DEFAULT_STRING_ISO3CODE_LENGTH, max = DB_DEFAULT_STRING_ISO3CODE_LENGTH)
    private String iso3code;

    @NotNull
    @Column(name = "de_kurzform", nullable = false)
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String deKurzform;

    @NotNull
    @Column(name = "fr_kurzform", nullable = false)
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String frKurzform;

    @NotNull
    @Column(name = "it_kurzform", nullable = false)
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String itKurzform;

    @NotNull
    @Column(name = "en_kurzform", nullable = false)
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String enKurzform;

    @NotNull
    @Column(name = "gueltig", nullable = false)
    private boolean gueltig = true;

    @Transient
    public boolean is(final WellKnownLand land) {
        if (laendercodeBfs == null) {
            return false;
        }

        return laendercodeBfs.equals(land.getLaendercodeBfs());
    }
}
