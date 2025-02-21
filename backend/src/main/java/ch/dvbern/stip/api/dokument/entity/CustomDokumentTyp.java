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
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_INPUT_MAX_LENGTH;
import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_SMALL_LENGTH;

@Audited
@Entity
@Table(
    name = "custom_dokument_typ",
    indexes = {
        @Index(name = "IX_custom_gesuch_dokument_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
public class CustomDokumentTyp extends AbstractMandantEntity {
    @Size(max = DB_DEFAULT_STRING_SMALL_LENGTH)
    @Column(name = "type", nullable = false, length = DB_DEFAULT_STRING_SMALL_LENGTH)
    private String type;

    @Size(max = DB_DEFAULT_STRING_INPUT_MAX_LENGTH)
    @Column(name = "description", nullable = false, length = DB_DEFAULT_STRING_INPUT_MAX_LENGTH)
    private String description;

    @NotNull
    @OneToOne(optional = false, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "customDokumentTyp")
    private GesuchDokument gesuchDokument;
}
