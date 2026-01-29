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

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.jilt.Builder;
import org.jilt.BuilderStyle;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH;

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
@Builder(style = BuilderStyle.STAGED)
@NoArgsConstructor
@AllArgsConstructor
public class Dokument extends AbstractMandantEntity {
    @Nullable
    @ManyToOne
    @JoinColumn(name = "gesuch_dokument_id", foreignKey = @ForeignKey(name = "FK_dokument_gesuch_dokument_id"))
    private GesuchDokument gesuchDokument;

    @NotNull
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "filename", nullable = false, length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String filename;

    @NotNull
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "filepath", nullable = false, length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String filepath;

    @NotNull
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "filesize", nullable = false, length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String filesize;

    @NotNull
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "object_id", nullable = false, length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String objectId;
}
