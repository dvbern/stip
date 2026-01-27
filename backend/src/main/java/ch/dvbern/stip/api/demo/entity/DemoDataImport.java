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

package ch.dvbern.stip.api.demo.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_INPUT_MAX_LENGTH;

@Audited
@Entity
@Table(
    name = "demo_data_import",
    indexes = {
        @Index(name = "IX_demo_data_import_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
public class DemoDataImport extends AbstractMandantEntity {
    @NotNull
    @Size(max = DB_DEFAULT_STRING_INPUT_MAX_LENGTH)
    @Column(name = "kommentar", nullable = false, length = DB_DEFAULT_STRING_INPUT_MAX_LENGTH)
    private String kommentar;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
        name = "dokument_id", foreignKey = @ForeignKey(name = "FK_demo_data_import_dokument_id"), nullable = false
    )
    private Dokument dokument;
}
