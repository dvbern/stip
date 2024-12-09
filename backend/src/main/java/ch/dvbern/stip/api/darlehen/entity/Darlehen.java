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
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

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
public class Darlehen extends AbstractMandantEntity {
    @NotNull
    @Column(name = "will_darlehen", nullable = false)
    private Boolean willDarlehen;

    @Nullable
    @Column(name = "betrag_darlehen", nullable = true)
    private Integer betragDarlehen;

    @Nullable
    @Column(name = "betrag_bezogen_kanton", nullable = true)
    private Integer betragBezogenKanton;

    @Nullable
    @Column(name = "schulden", nullable = true)
    private Integer schulden;

    @Nullable
    @Column(name = "anzahl_betreibungen", nullable = true)
    private Integer anzahlBetreibungen;

    @Nullable
    @Column(name = "grund_nicht_berechtigt", nullable = true)
    private Boolean grundNichtBerechtigt;

    @Nullable
    @Column(name = "grund_ausbildung_zwoelf_jahre", nullable = true)
    private Boolean grundAusbildungZwoelfJahre;

    @Nullable
    @Column(name = "grund_hohe_gebuehren", nullable = true)
    private Boolean grundHoheGebuehren;

    @Nullable
    @Column(name = "grund_anschaffungen_fuer_ausbildung", nullable = true)
    private Boolean grundAnschaffungenFuerAusbildung;

    @Nullable
    @Column(name = "grund_zweitausbildung", nullable = true)
    private Boolean grundZweitausbildung;

}
