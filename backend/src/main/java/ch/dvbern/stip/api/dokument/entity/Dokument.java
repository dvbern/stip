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
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MID_LENGTH;

@Audited
@Entity
@Table(
    name = "dokument",
    indexes = {
        @Index(name = "IX_dokument_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
public class Dokument extends AbstractMandantEntity {
    @NotNull
    @ManyToMany(mappedBy = "dokumente")
    private List<GesuchDokument> gesuchDokumente = new ArrayList<>();

    @NotNull
    @Size(max = DB_DEFAULT_MID_LENGTH)
    @Column(name = "filename", nullable = false)
    private String filename;

    @NotNull
    @Size(max = DB_DEFAULT_MID_LENGTH)
    @Column(name = "filepath", nullable = false)
    private String filepath;

    @NotNull
    @Size(max = DB_DEFAULT_MID_LENGTH)
    @Column(name = "filesize", nullable = false)
    private String filesize;

    @NotNull
    @Size(max = DB_DEFAULT_MID_LENGTH)
    @Column(name = "object_id", nullable = false)
    private String objectId;
}
