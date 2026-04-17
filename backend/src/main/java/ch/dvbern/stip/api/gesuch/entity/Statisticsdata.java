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

package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.common.validation.NullOrNotBlank;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH;

@Audited
@Entity
@Table(
    name = "statisticsdata",
    indexes = {
        @Index(name = "IX_statisticsdata_gesuch_id", columnList = "gesuch_id"),
        @Index(name = "IX_statisticsdata_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Statisticsdata extends AbstractMandantEntity {
    @NotNull
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "gesuch_id")
    private Gesuch gesuch;

    @Nullable
    @NullOrNotBlank
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "com_name", length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private @Valid String gemeindeName;

    @Nullable
    @Column(name = "com_fosnr")
    private @Valid Integer gemeindeBfsNr;
}
