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

package ch.dvbern.stip.api.auszahlung.entity;

import ch.dvbern.stip.api.buchhaltung.entity.Buchhaltung;
import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.common.service.NullableUnlessGenerated;
import ch.dvbern.stip.api.zahlungsverbindung.entity.Zahlungsverbindung;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
@Entity
@Table(
    name = "auszahlung",
    indexes = {
        @Index(name = "IX_auszahlung_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
@Builder(style = BuilderStyle.STAGED)
@NoArgsConstructor
@AllArgsConstructor
public class Auszahlung extends AbstractMandantEntity {
    @NullableUnlessGenerated
    @OneToOne(optional = true, cascade = CascadeType.PERSIST)
    @JoinColumn(
        name = "zahlungsverbindung_id", foreignKey = @ForeignKey(name = "FK_auszahlung_zahlungsverbindung_id"),
        nullable = true
    )
    private Zahlungsverbindung zahlungsverbindung;

    @NotNull
    @Column(name = "auszahlung_an_sozialdienst", nullable = false)
    private boolean auszahlungAnSozialdienst;

    @NullableUnlessGenerated
    @Column(name = "sap_business_partner_id", nullable = true)
    private Integer sapBusinessPartnerId;

    @NullableUnlessGenerated
    @ManyToOne(optional = true)
    @JoinColumn(
        name = "buchhaltung_id", foreignKey = @ForeignKey(name = "FK_zahlungsverbindung_buchhaltung_id"),
        nullable = true
    )
    private Buchhaltung buchhaltung;
}
