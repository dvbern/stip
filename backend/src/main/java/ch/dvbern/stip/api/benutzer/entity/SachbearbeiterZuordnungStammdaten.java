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

package ch.dvbern.stip.api.benutzer.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH;

@Entity
@Table(
    name = "sachbearbeiter_zuordnung_stammdaten",
    indexes = {
        @Index(name = "IX_sachbearbeiter_zuordnung_stammdaten_mandant", columnList = "mandant"),
        @Index(name = "IX_sachbearbeiter_zuordnung_stammdaten_benutzer_id", columnList = "benutzer_id")
    }
)
@Audited
@Getter
@Setter
public class SachbearbeiterZuordnungStammdaten extends AbstractMandantEntity {
    @Nullable
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "buchstaben_de", length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @BuchstabenRangeConstraint
    private String buchstabenDe;

    @Nullable
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "buchstaben_fr", length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @BuchstabenRangeConstraint
    private String buchstabenFr;

    @NotNull
    @OneToOne(optional = false, fetch = FetchType.LAZY, orphanRemoval = false)
    @JoinColumn(
        name = "benutzer_id", foreignKey = @ForeignKey(name = "FK_sachbearbeiter_zuordnung_stammdaten_benutzer_id"),
        nullable = false
    )
    private Sachbearbeiter benutzer;
}
