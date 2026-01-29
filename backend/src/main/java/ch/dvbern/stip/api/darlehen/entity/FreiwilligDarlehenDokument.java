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

import java.util.LinkedHashSet;
import java.util.Set;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.darlehen.type.DarlehenDokumentType;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(
    name = "freiwillig_darlehen_dokument",
    indexes = {
        @Index(name = "IX_freiwillig_darlehen_dokument_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
public class FreiwilligDarlehenDokument extends AbstractMandantEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "dokument_type")
    private DarlehenDokumentType dokumentType;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "freiwillig_darlehen_dokument_id")
    private Set<Dokument> dokumente = new LinkedHashSet<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "freiwillig_darlehen_id", nullable = false)
    private FreiwilligDarlehen freiwilligDarlehen;
}
