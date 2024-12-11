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

package ch.dvbern.stip.api.unterschriftenblatt.entity;

import java.util.ArrayList;
import java.util.List;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.unterschriftenblatt.type.UnterschriftenblattDokumentTyp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(
    name = "unterschriftenblatt",
    indexes = {
        @Index(name = "IX_unterschriftenblatt_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
public class Unterschriftenblatt extends AbstractMandantEntity {
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "gesuch_id", foreignKey = @ForeignKey(name = "FK_unterschriftenblatt_gesuch_id"))
    private Gesuch gesuch;

    @NotNull
    @Column(name = "dokument_typ")
    @Enumerated(EnumType.STRING)
    private UnterschriftenblattDokumentTyp dokumentTyp;

    @ManyToMany
    @JoinTable(
        name = "unterschriftenblatt_dokument",
        joinColumns = @JoinColumn(
            name = "unterschriftenblatt_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_unterschriftenblatt_dokumente")
        ),
        inverseJoinColumns = @JoinColumn(
            name = "dokument_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_dokument_unterschriftenblaetter")
        ),
        indexes = {
            @Index(name = "unterschriftenblatt_dokument_unterschriftenblatt_id", columnList = "unterschriftenblatt_id"),
            @Index(name = "unterschriftenblatt_dokument_dokument_id", columnList = "dokument_id")
        }
    )
    private List<Dokument> dokumente = new ArrayList<>();
}
