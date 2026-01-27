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

package ch.dvbern.stip.api.steuerdaten.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.common.service.NullableUnlessGenerated;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.jilt.Builder;
import org.jilt.BuilderStyle;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH;

@Audited
@Entity
@Table(
    name = "steuerdaten",
    indexes = {
        @Index(name = "IX_steuerdaten_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
@Builder(style = BuilderStyle.STAGED)
@NoArgsConstructor
@AllArgsConstructor
public class Steuerdaten extends AbstractMandantEntity {
    @NotNull
    @Column(name = "steuerdaten_typ", nullable = false)
    @Enumerated(EnumType.STRING)
    private SteuerdatenTyp steuerdatenTyp;

    @NotNull
    @Column(name = "total_einkuenfte", nullable = false)
    private Integer totalEinkuenfte;

    @NotNull
    @Column(name = "eigenmietwert", nullable = false)
    private Integer eigenmietwert;

    @NotNull
    @Column(name = "is_arbeitsverhaeltnis_selbstaendig", nullable = false)
    private Boolean isArbeitsverhaeltnisSelbstaendig;

    @NullableUnlessGenerated
    @Column(name = "saeule3a")
    private Integer saeule3a;

    @NullableUnlessGenerated
    @Column(name = "saeule2")
    private Integer saeule2;

    @NotNull
    @Column(name = "vermoegen", nullable = false)
    private Integer vermoegen;

    @NotNull
    @Column(name = "steuernKantonGemeinde", nullable = false)
    private Integer steuernKantonGemeinde;

    @NotNull
    @Column(name = "steuernBund", nullable = false)
    private Integer steuernBund;

    @NotNull
    @Column(name = "fahrkosten", nullable = false)
    private Integer fahrkosten;

    @NullableUnlessGenerated
    @Column(name = "fahrkostenPartner", nullable = true)
    private Integer fahrkostenPartner;

    @NotNull
    @Column(name = "verpflegung", nullable = false)
    private Integer verpflegung;

    @NullableUnlessGenerated
    @Column(name = "verpflegungPartner", nullable = true)
    private Integer verpflegungPartner;

    @NotNull
    @Column(name = "steuerjahr", nullable = false)
    private Integer steuerjahr;

    @NullableUnlessGenerated
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "status_veranlagung", length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String veranlagungsStatus;
}
