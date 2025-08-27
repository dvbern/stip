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
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
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
    name = "steuerdaten",
    indexes = {
        @Index(name = "IX_steuerdaten_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
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

    @Nullable
    @Column(name = "saeule3a")
    private Integer saeule3a;

    @Nullable
    @Column(name = "saeule2")
    private Integer saeule2;

    @NotNull
    @Column(name = "kinderalimente", nullable = false)
    private Integer kinderalimente;

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

    @Nullable
    @Column(name = "fahrkostenPartner", nullable = true)
    private Integer fahrkostenPartner;

    @NotNull
    @Column(name = "verpflegung", nullable = false)
    private Integer verpflegung;

    @Nullable
    @Column(name = "verpflegungPartner", nullable = true)
    private Integer verpflegungPartner;

    @NotNull
    @Column(name = "steuerjahr", nullable = false)
    private Integer steuerjahr;

    @NotNull
    @Column(name = "status_veranlagung", nullable = false)
    @Min(0)
    @Max(99)
    private Integer statusVeranlagung = 0;
}
