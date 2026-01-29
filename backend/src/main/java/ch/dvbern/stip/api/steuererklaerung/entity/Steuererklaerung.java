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

package ch.dvbern.stip.api.steuererklaerung.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.common.service.NullableUnlessGenerated;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.jilt.Builder;
import org.jilt.BuilderStyle;

@Audited
@Entity
@Table(
    name = "steuererklaerung",
    indexes = {
        @Index(name = "IX_steuererklaerung_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
@Builder(style = BuilderStyle.STAGED)
@NoArgsConstructor
@AllArgsConstructor
public class Steuererklaerung extends AbstractMandantEntity {
    @NotNull
    @Column(name = "steuerdaten_typ", nullable = false)
    @Enumerated(EnumType.STRING)
    private SteuerdatenTyp steuerdatenTyp;

    @NotNull
    @Column(name = "steuererklaerung_in_bern", nullable = false)
    private Boolean steuererklaerungInBern;

    @NullableUnlessGenerated
    @Column(name = "ergaenzungsleistungen", nullable = true)
    @Min(0)
    private Integer ergaenzungsleistungen;

    @NullableUnlessGenerated
    @Column(name = "unterhaltsbeitraege", nullable = true)
    @Min(0)
    private Integer unterhaltsbeitraege;

    @NullableUnlessGenerated
    @Column(name = "renten", nullable = true)
    @Min(0)
    private Integer renten;

    @NullableUnlessGenerated
    @Column(name = "einnahmen_bgsa", nullable = true)
    @Min(0)
    private Integer einnahmenBGSA;

    @NullableUnlessGenerated
    @Column(name = "andere_einnahmen", nullable = true)
    @Min(0)
    private Integer andereEinnahmen;
}
