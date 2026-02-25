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
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(
    name = "gesetzlich_darlehen",
    indexes = {
        @Index(name = "IX_gesetzlich_darlehen_mandant", columnList = "mandant"),
        @Index(name = "IX_gesetzlich_darlehen_fall_id", columnList = "fall_id"),
        @Index(name = "IX_gesetzlich_darlehen_gesuch_id", columnList = "gesuch_id")
    }
)
@Getter
@Setter
public class GesetzlichDarlehen extends AbstractMandantEntity {

    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "fall_id", nullable = false, foreignKey = @ForeignKey(name = "FK_gesetzlich_darlehen_fall_id"))
    private Fall fall;

    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
        name = "gesuch_id", nullable = false, foreignKey = @ForeignKey(name = "FK_gesetzlich_darlehen_gesuch_id")
    )
    private Gesuch gesuch;

    @NotNull
    @Column(name = "betrag", nullable = false)
    private Integer betrag;

    @Nullable
    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "dokument_id")
    private Dokument verfuegung;

    @Transient
    public String getDarlehenNr() {
        // remove everything except numbers from fallNr.
        // and fill missing spots with zeros (total length of 8 required)
        final var fallNummer = this.getFall().getFallNummer();
        final int darlehenNo = Integer.parseInt(fallNummer.substring(fallNummer.lastIndexOf('.') + 1));

        return String.format("%08d", darlehenNo);
    }
}
