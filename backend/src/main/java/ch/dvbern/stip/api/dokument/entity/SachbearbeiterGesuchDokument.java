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

package ch.dvbern.stip.api.dokument.entity;

import java.util.ArrayList;
import java.util.List;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_INPUT_MAX_LENGTH;
import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH;

@Entity
@Audited
@Table(
    name = "sachbearbeiter_gesuch_dokument",
    uniqueConstraints = @UniqueConstraint(
        name = "UC_sachbearbeiter_gesuch_dokument_gesuch_type", columnNames = { "gesuch_id", "type" }
    ),
    indexes = {
        @Index(name = "IX_sachbearbeiter_gesuch_dokument_mandant", columnList = "mandant"),
        @Index(name = "IX_sachbearbeiter_gesuch_dokument_gesuch_id", columnList = "gesuch_id")
    }
)
@Getter
@Setter
public class SachbearbeiterGesuchDokument extends AbstractMandantEntity {
    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
        name = "gesuch_id", foreignKey = @ForeignKey(name = "FK_sachbearbeiter_gesuch_dokument_gesuch_id"),
        nullable = false
    )
    private Gesuch gesuch;

    @OneToMany
    @JoinTable(
        name = "sachbearbeiter_gesuch_dokument_dokument",
        joinColumns = @JoinColumn(
            name = "sachbearbeiter_gesuch_dokument_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_sachbearbeiter_gesuch_dokument_dokumente")
        ),
        inverseJoinColumns = @JoinColumn(
            name = "dokument_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_dokument_sachbearbeiter_gesuch_dokumente")
        ),
        indexes = {
            @Index(
                name = "sachbearbeiter_gesuch_dokument_dokument_sachbearbeiter_gesuch_dokument_id",
                columnList = "sachbearbeiter_gesuch_dokument_id"
            ),
            @Index(name = "sachbearbeiter_gesuch_dokument_dokument_id", columnList = "dokument_id")
        }
    )
    private List<Dokument> dokumente = new ArrayList<>();

    @NotNull
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @NotBlank
    @Column(name = "type", nullable = false, length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String type;

    @NotNull
    @Size(max = DB_DEFAULT_STRING_INPUT_MAX_LENGTH)
    @NotBlank
    @Column(name = "description", nullable = false, length = DB_DEFAULT_STRING_INPUT_MAX_LENGTH)
    private String description;
}
