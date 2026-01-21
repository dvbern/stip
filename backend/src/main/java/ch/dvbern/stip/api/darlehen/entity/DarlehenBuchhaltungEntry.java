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
import ch.dvbern.stip.api.darlehen.type.DarlehenBuchhaltungEntryKategorie;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.fall.entity.Fall;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MAX_LENGTH;

@Entity
@Audited
@Table(
    name = "darlehen_buchhaltung_entry",
    indexes = {
        @Index(name = "IX_darlehen_buchhaltung_entry_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
public class DarlehenBuchhaltungEntry extends AbstractMandantEntity {
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "fall_id", nullable = false)
    private Fall fall;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "kategorie", nullable = false)
    private DarlehenBuchhaltungEntryKategorie kategorie;

    @Nullable
    @OneToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(
        name = "darlehen_buchhaltung_entry_verfuegung_id",
        foreignKey = @ForeignKey(name = "FK_darlehen_buchhaltung_entry_verfuegung_id"),
        nullable = true
    )
    private Dokument verfuegung;

    @Nullable
    @Column(name = "betrag")
    private Integer betrag;

    @Nullable
    @Size(max = DB_DEFAULT_STRING_MAX_LENGTH)
    @Column(name = "comment", length = DB_DEFAULT_STRING_MAX_LENGTH)
    private String comment;
}
