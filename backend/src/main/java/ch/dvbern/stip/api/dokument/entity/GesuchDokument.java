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
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
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
    name = "gesuch_dokument",
    indexes = {
        @Index(name = "IX_gesuch_dokument_gesuch_tranche_id", columnList = "gesuch_tranche_id"),
        @Index(
            name = "IX_gesuch_dokument_gesuch_tranche_id_dokument_typ", columnList = "gesuch_tranche_id,dokument_typ"
        ),
        @Index(name = "IX_gesuch_dokument_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
public class GesuchDokument extends AbstractMandantEntity {
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "gesuch_tranche_id", foreignKey = @ForeignKey(name = "FK_gesuch_dokument_gesuch_tranche_id"))
    private GesuchTranche gesuchTranche;

    @NotNull
    @Column(name = "dokument_typ", nullable = false)
    @Enumerated(EnumType.STRING)
    private DokumentTyp dokumentTyp;

    @NotNull
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Dokumentstatus status = Dokumentstatus.AUSSTEHEND;

    @ManyToMany
    @JoinTable(
        name = "gesuch_dokument_dokument",
        joinColumns = @JoinColumn(
            name = "gesuch_dokument_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_gesuch_dokument_dokumente")
        ),
        inverseJoinColumns = @JoinColumn(
            name = "dokument_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_dokument_gesuch_dokumente")
        ),
        indexes = {
            @Index(name = "gesuch_dokument_dokument_gesuch_dokument_id", columnList = "gesuch_dokument_id"),
            @Index(name = "gesuch_dokument_dokument_dokument_id", columnList = "dokument_id")
        }
    )
    private List<Dokument> dokumente = new ArrayList<>();

    public void addDokument(final Dokument dokument) {
        // Bi-Directional associations must set both sides of the relation
        dokumente.add(dokument);
        dokument.getGesuchDokumente().add(this);
    }
}
