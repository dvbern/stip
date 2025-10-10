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
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH;

@Audited
@Entity
@Table(
    name = "einnahmen_kosten",
    indexes = @Index(name = "IX_einnahme_kosten_mandant", columnList = "mandant")
)
@Getter
@Setter
@AnstellungsGradRequiredConstraint
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

    @Nullable
    @Column(name = "unterhaltsbeitraege")
    private Integer unterhaltsbeitraege;

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
    @Column(name = "ausbildungskosten")
    private Integer ausbildungskosten;

    // todo kstip-2779 - validierung pia only
    @Nullable
    @Column(name = "auswaertige_mittagessen_pro_woche")
    private Integer auswaertigeMittagessenProWoche;

    // todo kstip-2779 - validierung partner only
    @Nullable
    @Column(name = "verpflegungskosten")
    private Integer verpflegungskosten;

    @Nullable
    @Column(name = "betreuungskosten_kinder")
    private Integer betreuungskostenKinder;

    @Nullable
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "status_veranlagung", length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String veranlagungsStatus;

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

    @Nullable
    @Min(0)
    @Column(name = "einnahmen_bgsa")
    private Integer einnahmenBGSA;

    @Nullable
    @Min(0)
    @Column(name = "taggeld_ahv_iv")
    private Integer taggeldAHVIV;

    @Nullable
    @Min(0)
    @Column(name = "andere_einnahmen")
    private Integer andereEinnahmen;

    @Min(0)
    @Max(100)
    @Nullable
    @Column(name = "arbeitspensum_prozent")
    private Integer arbeitspensumProzent;
}
