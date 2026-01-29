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
import ch.dvbern.stip.api.common.service.NullableUnlessGenerated;
import ch.dvbern.stip.api.common.validation.EinnahmenKostenAlternativeWohnformValidConstraint;
import ch.dvbern.stip.api.common.validation.EinnahmenKostenAnzahlPersonenWGValidConstraint;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    name = "einnahmen_kosten",
    indexes = @Index(name = "IX_einnahme_kosten_mandant", columnList = "mandant")
)

@Getter
@Setter
@EinnahmenKostenAnzahlPersonenWGValidConstraint
@EinnahmenKostenAlternativeWohnformValidConstraint
@AnstellungsGradRequiredConstraint
@Builder(style = BuilderStyle.STAGED)
@NoArgsConstructor
@AllArgsConstructor
public class EinnahmenKosten extends AbstractMandantEntity {
    @NotNull
    @Column(name = "nettoerwerbseinkommen", nullable = false)
    @Min(0)
    private Integer nettoerwerbseinkommen;

    @NotNull
    @Column(name = "fahrkosten", nullable = false)
    @Min(0)
    private Integer fahrkosten;

    @NullableUnlessGenerated
    @Column(name = "wohnkosten")
    @Min(0)
    private Integer wohnkosten;

    @NullableUnlessGenerated
    @Column(name = "wg_wohnend")
    private Boolean wgWohnend;

    @NullableUnlessGenerated
    @Column(name = "wg_anzahl_personen")
    @Min(0)
    private Integer wgAnzahlPersonen;

    @NullableUnlessGenerated
    @Column(name = "alternative_wohnform_wohnend")
    private Boolean alternativeWohnformWohnend;

    @NullableUnlessGenerated
    @Column(name = "unterhaltsbeitraege")
    @Min(0)
    private Integer unterhaltsbeitraege;

    @NullableUnlessGenerated
    @Column(name = "zulagen")
    @Min(0)
    private Integer zulagen;

    @NullableUnlessGenerated
    @Column(name = "renten")
    @Min(0)
    private Integer renten;

    @NullableUnlessGenerated
    @Column(name = "eo_leistungen")
    @Min(0)
    private Integer eoLeistungen;

    @NullableUnlessGenerated
    @Column(name = "ergaenzungsleistungen")
    @Min(0)
    private Integer ergaenzungsleistungen;

    @NullableUnlessGenerated
    @Column(name = "beitraege")
    @Min(0)
    private Integer beitraege;

    @NullableUnlessGenerated
    @Column(name = "ausbildungskosten")
    @Min(0)
    private Integer ausbildungskosten;

    @NullableUnlessGenerated
    @Column(name = "auswaertige_mittagessen_pro_woche")
    @Min(0)
    private Integer auswaertigeMittagessenProWoche;

    @NullableUnlessGenerated
    @Column(name = "verpflegungskosten")
    @Min(0)
    private Integer verpflegungskosten;

    @NullableUnlessGenerated
    @Column(name = "betreuungskosten_kinder")
    @Min(0)
    private Integer betreuungskostenKinder;

    @NullableUnlessGenerated
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "status_veranlagung", length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String veranlagungsStatus;

    @NotNull
    @Column(name = "steuerjahr", nullable = false)
    @Min(0)
    @Max(9999)
    private Integer steuerjahr;

    @NullableUnlessGenerated
    @Column(name = "vermoegen")
    @Max(Integer.MAX_VALUE)
    @Min(0)
    private Integer vermoegen;

    @NullableUnlessGenerated
    @Min(0)
    @Column(name = "einnahmen_bgsa")
    private Integer einnahmenBGSA;

    @NullableUnlessGenerated
    @Min(0)
    @Column(name = "taggelder_ahv_iv")
    private Integer taggelderAHVIV;

    @NullableUnlessGenerated
    @Min(0)
    @Column(name = "andere_einnahmen")
    private Integer andereEinnahmen;

    @Min(0)
    @Max(100)
    @NullableUnlessGenerated
    @Column(name = "arbeitspensum_prozent")
    private Integer arbeitspensumProzent;

}
