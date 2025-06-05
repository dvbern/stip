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

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.sap.entity.SapDelivery;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

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
public class Auszahlung extends AbstractMandantEntity {
    @Nullable
    @OneToOne(optional = true)
    @JoinColumn(
        name = "zahlungsverbindung_id", foreignKey = @ForeignKey(name = "FK_auszahlung_zahlungsverbindung_id"),
        nullable = true
    )
    private Zahlungsverbindung zahlungsverbindung;

    @Column(name = "auszahlung_an_sozialdienst", nullable = false)
    private boolean auszahlungAnSozialdienst;

    @Nullable
    @Column(name = "sap_business_partner_id", nullable = true)
    private Integer sapBusinessPartnerId;

    @Nullable
    @OneToOne(optional = true)
    @JoinColumn(
        name = "sapdelivery_id", foreignKey = @ForeignKey(name = "FK_auszahlung_sapdelivery_id"),
        nullable = true
    )
    private SapDelivery sapDelivery;
}
