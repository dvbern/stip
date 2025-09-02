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

package ch.dvbern.stip.api.nesko.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH;
import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_SMALL_LENGTH;

@Entity
@Table(
    name = "nesko_access",
    indexes = @Index(name = "IX_nesko_access_mandant", columnList = "mandant")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NeskoAccess extends AbstractMandantEntity {
    @NotNull
    @Column(name = "fallNr", length = DB_DEFAULT_STRING_SMALL_LENGTH)
    @Size(max = DB_DEFAULT_STRING_SMALL_LENGTH)
    private String fallNr;

    @NotNull
    @Column(name = "gesuch_nr", length = DB_DEFAULT_STRING_SMALL_LENGTH)
    @Size(max = DB_DEFAULT_STRING_SMALL_LENGTH)
    private String gesuchNr;

    @NotNull
    @Column(name = "svnr", length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String svNr;
}
