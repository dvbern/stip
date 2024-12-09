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

package ch.dvbern.stip.api.einnahmen_kosten.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    name = "einnahmen_kosten",
    indexes = @Index(name = "IX_einnahme_kosten_mandant", columnList = "mandant")
)
@Getter
@Setter
public class EinnahmenKosten extends AbstractMandantEntity {
    @NotNull
    @Column(name = "nettoerwerbseinkommen", nullable = false)
    private Integer nettoerwerbseinkommen;

    @NotNull
    @Column(name = "fahrkosten", nullable = false)
    private Integer fahrkosten;

    @Nullable
    @Column(name = "wohnkosten")
    private Integer wohnkosten;

    @Nullable
    @Column(name = "wg_wohnend")
    private Boolean wgWohnend;

    @NotNull
    @Column(name = "verdienst_realisiert", nullable = false)
    private Boolean verdienstRealisiert;

    @Nullable
    @Column(name = "alimente")
    private Integer alimente;

    @Nullable
    @Column(name = "zulagen")
    private Integer zulagen;

    @NotNull
    @Column(name = "renten", nullable = false)
    private Integer renten;

    @Nullable
    @Column(name = "eo_leistungen")
    private Integer eoLeistungen;

    @Nullable
    @Column(name = "ergaenzungsleistungen")
    private Integer ergaenzungsleistungen;

    @Nullable
    @Column(name = "beitraege")
    private Integer beitraege;

    @Nullable
    @Column(name = "ausbildungskosten_sekundarstufe_zwei")
    private Integer ausbildungskostenSekundarstufeZwei;

    @Nullable
    @Column(name = "ausbildungskosten_tertiaerstufe")
    private Integer ausbildungskostenTertiaerstufe;

    @Nullable
    @Column(name = "auswaertige_mittagessen_pro_woche")
    private Integer auswaertigeMittagessenProWoche;

    @Nullable
    @Column(name = "betreuungskosten_kinder")
    private Integer betreuungskostenKinder;

    @NotNull
    @Column(name = "veranlagungscode", nullable = false)
    @Min(0)
    @Max(99)
    private Integer veranlagungsCode = 0;

    @NotNull
    @Column(name = "steuerjahr", nullable = false)
    @Min(0)
    @Max(9999)
    private Integer steuerjahr;

    @Nullable
    @Column(name = "vermoegen")
    @Max(Integer.MAX_VALUE)
    @Min(0)
    private Integer vermoegen;
}
