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

package ch.dvbern.stip.api.buchhaltung.entity;

import java.util.ArrayList;
import java.util.List;

import ch.dvbern.stip.api.buchhaltung.type.BuchhaltungType;
import ch.dvbern.stip.api.buchhaltung.type.SapStatus;
import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.sap.entity.SapDelivery;
import ch.dvbern.stip.api.zahlungsverbindung.entity.Zahlungsverbindung;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MAX_LENGTH;

@Audited
@SapDeliverysLengthConstraint
@Entity
@Table(
    name = "buchhaltung",
    indexes = {
        @Index(name = "IX_buchhaltung_gesuch_id", columnList = "gesuch_id"),
        @Index(name = "IX_buchhaltung_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
public class Buchhaltung extends AbstractMandantEntity {
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "buchhaltung_type", nullable = false)
    private BuchhaltungType buchhaltungType;

    @NotNull
    @Column(name = "betrag", nullable = false)
    private Integer betrag;

    @NotNull
    @Column(name = "saldo", nullable = false)
    private Integer saldo;

    @Nullable
    @Column(name = "stipendium")
    private Integer stipendium;

    @Nullable
    @OneToMany(mappedBy = "buchhaltung", fetch = FetchType.EAGER)
    private List<SapDelivery> sapDeliverys = new ArrayList<>();

    @NotNull
    @Size(max = DB_DEFAULT_STRING_MAX_LENGTH)
    @Column(name = "comment", length = DB_DEFAULT_STRING_MAX_LENGTH)
    private String comment;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(
        name = "gesuch_id",
        foreignKey = @ForeignKey(name = "FK_buchhaltung_gesuch")
    )
    private Gesuch gesuch;

    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "fall_id", foreignKey = @ForeignKey(name = "FK_buchhaltung_fall_id"))
    private Fall fall;

    @Nullable
    @OneToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "zahlungsverbindung_id")
    private Zahlungsverbindung zahlungsverbindung;

    @Transient
    public SapStatus getSapStatus() {
        return switch (this.getBuchhaltungType()) {
            case SALDOAENDERUNG, STIPENDIUM -> null;
            // case BUSINESSPARTNER_CREATE -> sapDeliverys.isEmpty() ? null : sapDeliverys.getFirst().getSapStatus();
            case AUSZAHLUNG_INITIAL, AUSZAHLUNG_REMAINDER, BUSINESSPARTNER_CREATE -> {
                if (sapDeliverys.isEmpty()) {
                    yield SapStatus.IN_PROGRESS;
                }
                if (sapDeliverys.stream().anyMatch(sapDelivery -> sapDelivery.getSapStatus() == SapStatus.SUCCESS)) {
                    yield SapStatus.SUCCESS;
                }

                if (
                    sapDeliverys.stream().anyMatch(sapDelivery -> sapDelivery.getSapStatus() == SapStatus.IN_PROGRESS)
                ) {
                    yield SapStatus.IN_PROGRESS;
                }

                if (sapDeliverys.size() < SapDeliverysLengthConstraintValidator.MAX_SAP_DELIVERYS_AUSZAHLUNG) {
                    yield SapStatus.IN_PROGRESS;
                }
                yield SapStatus.FAILURE;
            }
        };
    }
}
