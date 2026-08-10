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

package ch.dvbern.stip.integration.steuerdaten.domain.model;

import ch.dvbern.stip.api.common.entity.AbstractTenantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    name = "steuerdaten_access",
    indexes = @Index(name = "IX_steuerdaten_access_tenant", columnList = "tenant")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SteuerdatenAccess extends AbstractTenantEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "steuerdaten_adapter", nullable = false)
    private SteuerdatenAdapterType steuerdatenAdapterType;

    @NotNull
    @Column(name = "fallNr", nullable = false, length = DB_DEFAULT_STRING_SMALL_LENGTH)
    @Size(max = DB_DEFAULT_STRING_SMALL_LENGTH)
    private String fallNr;

    @NotNull
    @Column(name = "gesuch_nr", nullable = false, length = DB_DEFAULT_STRING_SMALL_LENGTH)
    @Size(max = DB_DEFAULT_STRING_SMALL_LENGTH)
    private String gesuchNr;

    @NotNull
    @Column(name = "svnr", nullable = false)
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String svNr;
}
