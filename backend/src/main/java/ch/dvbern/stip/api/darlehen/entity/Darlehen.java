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

package ch.dvbern.stip.api.darlehen.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.common.service.NullableUnlessGenerated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@DarlehenValidationConstraint
@Audited
@Entity
@Table(
    name = "darlehen",
    indexes = {
        @Index(name = "IX_darlehen_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
@Builder(style = BuilderStyle.STAGED)
@NoArgsConstructor
@AllArgsConstructor
public class Darlehen extends AbstractMandantEntity {
    @NotNull
    @Column(name = "will_darlehen", nullable = false)
    private Boolean willDarlehen;

    @NullableUnlessGenerated
    @Min(value = 0)
    @Column(name = "betrag_darlehen", nullable = true)
    private Integer betragDarlehen;

    @NullableUnlessGenerated
    @Min(value = 0)
    @Column(name = "betrag_bezogen_kanton", nullable = true)
    private Integer betragBezogenKanton;

    @Min(value = 0)
    @NullableUnlessGenerated
    @Column(name = "schulden", nullable = true)
    private Integer schulden;

    @Min(value = 0)
    @NullableUnlessGenerated
    @Column(name = "anzahl_betreibungen", nullable = true)
    private Integer anzahlBetreibungen;

    @NullableUnlessGenerated
    @Column(name = "grund_nicht_berechtigt", nullable = true)
    private Boolean grundNichtBerechtigt;

    @NullableUnlessGenerated
    @Column(name = "grund_ausbildung_zwoelf_jahre", nullable = true)
    private Boolean grundAusbildungZwoelfJahre;

    @NullableUnlessGenerated
    @Column(name = "grund_hohe_gebuehren", nullable = true)
    private Boolean grundHoheGebuehren;

    @NullableUnlessGenerated
    @Column(name = "grund_anschaffungen_fuer_ausbildung", nullable = true)
    private Boolean grundAnschaffungenFuerAusbildung;

    @NullableUnlessGenerated
    @Column(name = "grund_zweitausbildung", nullable = true)
    private Boolean grundZweitausbildung;

}
