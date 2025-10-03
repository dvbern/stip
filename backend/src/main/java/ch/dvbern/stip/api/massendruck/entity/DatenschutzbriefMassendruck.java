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

package ch.dvbern.stip.api.massendruck.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.datenschutzbrief.entity.Datenschutzbrief;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
    name = "datenschutzbrief_massendruck", indexes = {
        @Index(name = "IX_datenschutzbrief_massendruck_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
public class DatenschutzbriefMassendruck extends AbstractMandantEntity {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
        name = "datenschutzbrief_id",
        foreignKey = @ForeignKey(name = "FK_datenschutzbrief_massendruck_datenschutzbrief_id")
    )
    private Datenschutzbrief datenschutzbrief;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
        name = "massendruck_job_id",
        foreignKey = @ForeignKey(name = "FK_datenschutzbrief_massendruck_massendruck_job_id")
    )
    private MassendruckJob massendruckJob;
}
