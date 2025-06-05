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

package ch.dvbern.stip.api.sap.entity;

import java.math.BigDecimal;

import ch.dvbern.stip.api.buchhaltung.type.BuchhaltungType;
import ch.dvbern.stip.api.buchhaltung.type.SapStatus;
import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(
    name = "sapdelivery",
    indexes = {
        @Index(name = "IX_sapdelivery_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
public class SapDelivery extends AbstractMandantEntity {
    @Nullable
    @Column(name = "sap_delivery_id")
    private BigDecimal sapDeliveryId;

    @Nullable
    @Enumerated(EnumType.STRING)
    @Column(name = "sap_status")
    private SapStatus sapStatus;

    @Nullable
    @Column(name = "sap_business_partner_id")
    private Integer sapBusinessPartnerId;

    @Nullable
    @Enumerated(EnumType.STRING)
    @Column(name = "pending_sap_action")
    private BuchhaltungType pendingSapAction;
}
