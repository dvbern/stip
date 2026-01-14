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
import ch.dvbern.stip.api.dokument.type.GesuchDokumentStatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
    },
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UC_gesuch_dokument_gesuch_tranche_id_dokument_typ",
            columnNames = { "gesuch_tranche_id", "dokument_typ" }
        )
    }
)
@Getter
@Setter
@OneOfDocumentTypesRequiredConstraint
public class GesuchDokument extends AbstractMandantEntity {
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "gesuch_tranche_id", foreignKey = @ForeignKey(name = "FK_gesuch_dokument_gesuch_tranche_id"))
    private GesuchTranche gesuchTranche;

    @Nullable
    @Column(name = "dokument_typ", nullable = true)
    @Enumerated(EnumType.STRING)
    private DokumentTyp dokumentTyp;

    @OneToOne(optional = true, cascade = CascadeType.ALL, orphanRemoval = true)
    @Nullable
    @JoinColumn(
        name = "custom_dokument_typ_id", foreignKey = @ForeignKey(name = "FK_gesuch_dokument_custom_dokument_typ_id"),
        nullable = true
    )
    private CustomDokumentTyp customDokumentTyp;

    @NotNull
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private GesuchDokumentStatus status = GesuchDokumentStatus.AUSSTEHEND;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "gesuchDokument")
    private List<Dokument> dokumente = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "gesuchDokument")
    private List<GesuchDokumentKommentar> gesuchDokumentKommentare = new ArrayList<>();

    public void addDokument(final Dokument dokument) {
        // Bi-Directional associations must set both sides of the relation
        dokumente.add(dokument);
        dokument.setGesuchDokument(this);
    }

    public void addGesuchKommentar(final GesuchDokumentKommentar kommentar) {
        // Bi-Directional associations must set both sides of the relation
        gesuchDokumentKommentare.add(kommentar);
        kommentar.setGesuchDokument(this);
    }
}
