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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.dokument.entity;



import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.common.entity.AbstractEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.List;
import java.util.Objects;

@Audited
@Entity
@Getter
@Setter
public class GesuchDokument extends AbstractEntity {

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_gesuch_dokument_gesuch_id"))
    private Gesuch gesuch;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DokumentTyp dokumentTyp;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "gesuchDokument")
    private List<Dokument> dokumente;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GesuchDokument that = (GesuchDokument) o;
        return Objects.equals(getGesuch(), that.getGesuch()) && getDokumentTyp() == that.getDokumentTyp() && Objects.equals(getDokumente(), that.getDokumente());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getGesuch(), getDokumentTyp(), getDokumente());
    }
}
