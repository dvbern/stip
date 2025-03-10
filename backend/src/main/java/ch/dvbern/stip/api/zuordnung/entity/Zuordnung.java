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

package ch.dvbern.stip.api.zuordnung.entity;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.zuordnung.type.ZuordnungType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(
    name = "zuordnung",
    indexes = {
        @Index(name = "IX_zuordnung_fall_id", columnList = "fall_id"),
        @Index(name = "IX_zuordnung_sachbearbeiter_id", columnList = "sachbearbeiter_id"),
        @Index(name = "IX_zuordnung_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
public class Zuordnung extends AbstractMandantEntity {
    @NotNull
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "fall_id", foreignKey = @ForeignKey(name = "FK_zuordnung_fall_id"), nullable = false)
    private Fall fall;

    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(
        name = "sachbearbeiter_id",
        foreignKey = @ForeignKey(name = "FK_zuordnung_sachbearbeiter_id"),
        nullable = false
    )
    private Benutzer sachbearbeiter;

    @NotNull
    @Column(nullable = false, name = "zuordnung_type")
    @Enumerated(EnumType.STRING)
    private ZuordnungType zuordnungType;
}
