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

package ch.dvbern.stip.api.familiensituation.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.common.service.NullableUnlessGenerated;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.jilt.Builder;
import org.jilt.BuilderStyle;

@Audited
@WerZahltAlimenteRequiredFieldConstraint
@Entity
@Table(
    name = "familiensituation",
    indexes = @Index(name = "IX_familiensituation_mandant", columnList = "mandant")
)
@Getter
@Setter
@Builder(style = BuilderStyle.STAGED)
@NoArgsConstructor
@AllArgsConstructor
public class Familiensituation extends AbstractMandantEntity {
    @NotNull
    @Column(name = "eltern_verheiratet_zusammen", nullable = false)
    private Boolean elternVerheiratetZusammen;

    @NullableUnlessGenerated
    @Column(name = "elternteil_unbekannt_verstorben")
    private Boolean elternteilUnbekanntVerstorben;

    @NullableUnlessGenerated
    @Column(name = "gerichtliche_alimentenregelung")
    private Boolean gerichtlicheAlimentenregelung;

    @NullableUnlessGenerated
    @Column(name = "mutter_unbekannt_verstorben")
    @Enumerated(EnumType.STRING)
    private ElternAbwesenheitsGrund mutterUnbekanntVerstorben;

    @NullableUnlessGenerated
    @Column(name = "mutter_unbekannt_grund")
    @Enumerated(EnumType.STRING)
    private ElternUnbekanntheitsGrund mutterUnbekanntGrund;

    @NullableUnlessGenerated
    @Column(name = "mutter_wiederverheiratet")
    private Boolean mutterWiederverheiratet;

    @NullableUnlessGenerated
    @Column(name = "vater_unbekannt_verstorben")
    @Enumerated(EnumType.STRING)
    private ElternAbwesenheitsGrund vaterUnbekanntVerstorben;

    @NullableUnlessGenerated
    @Column(name = "vater_unbekannt_grund")
    @Enumerated(EnumType.STRING)
    private ElternUnbekanntheitsGrund vaterUnbekanntGrund;

    @NullableUnlessGenerated
    @Column(name = "vater_wiederverheiratet")
    private Boolean vaterWiederverheiratet;

    @NullableUnlessGenerated
    @Column(name = "wer_zahlt_alimente")
    @Enumerated(EnumType.STRING)
    private Elternschaftsteilung werZahltAlimente;
}
