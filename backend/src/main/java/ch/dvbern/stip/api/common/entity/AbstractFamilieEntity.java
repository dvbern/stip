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

package ch.dvbern.stip.api.common.entity;

import java.math.BigDecimal;

import ch.dvbern.stip.api.common.service.NullableUnlessGenerated;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.jilt.Builder;
import org.jilt.BuilderStyle;

@MappedSuperclass
@Audited
@WohnsitzAnteilRequiredConstraint
@WohnsitzAnteilBerechnungConstraint
@Getter
@Setter
@Builder(style = BuilderStyle.STAGED)
@NoArgsConstructor
@AllArgsConstructor
public class AbstractFamilieEntity extends AbstractPerson {
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "wohnsitz", nullable = false)
    private Wohnsitz wohnsitz;

    @NullableUnlessGenerated
    @Column(name = "wohnsitz_anteil_mutter")
    @DecimalMax("100.00")
    @DecimalMin("0.00")
    private BigDecimal wohnsitzAnteilMutter;

    @NullableUnlessGenerated
    @Column(name = "wohnsitz_anteil_vater")
    @DecimalMax("100.00")
    @DecimalMin("0.00")
    private BigDecimal wohnsitzAnteilVater;

    public BigDecimal getWohnsitzAnteil(ElternTyp elternTyp) {
        return switch (elternTyp) {
            case VATER -> this.getWohnsitzAnteilVater();
            case MUTTER -> this.getWohnsitzAnteilMutter();
        };
    }
}
