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

package ch.dvbern.stip.api.statistik.entity;

import ch.dvbern.stip.api.common.entity.AbstractTenantEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH;

@Entity
@Audited
@Getter
@Setter
@Table(
    name = "statistik",
    indexes = {
        @Index(name = "IX_statistik_tenant", columnList = "tenant"),
        @Index(name = "IX_statistik_year", columnList = "year")
    }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Statistik extends AbstractTenantEntity {
    @NotNull
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "user_triggered_creation", nullable = false)
    private String userTriggeredCreation;

    @NotNull
    @Column(name = "valid", nullable = false)
    @Builder.Default
    private boolean valid = true;

    @Nullable
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "error", nullable = true)
    private String error;

    @NotNull
    @Column(name = "year", nullable = false)
    private int year;

    @Nullable
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "filename", nullable = true)
    private String filename;

    @Nullable
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "filepath", nullable = true)
    private String filepath;

    @Nullable
    @Column(name = "filesize", nullable = true)
    private Integer filesize;

    @Nullable
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "object_id", nullable = true)
    private String objectId;
}
