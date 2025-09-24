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

import java.util.ArrayList;
import java.util.List;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.massendruck.type.DruckauftragTyp;
import ch.dvbern.stip.api.massendruck.type.MassendruckJobStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(
    name = "massendruck_job",
    indexes = { @Index(name = "IX_massendruck_mandant", columnList = "mandant")
    }
)
public class MassendruckJob extends AbstractMandantEntity {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "massendruckJob")
    private List<DatenschutzbriefMassendruck> datenschutzbriefMassendrucks = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "massendruckJob")
    private List<VerfuegungMassendruck> verfuegungMassendrucks = new ArrayList<>();

    @NotNull
    @Enumerated(EnumType.STRING)
    private MassendruckJobStatus status;

    @NotNull
    @Column(name = "massendruck_job_number", nullable = false, updatable = false)
    private int massendruckJobNumber;

    @Transient
    public DruckauftragTyp getMassendruckTyp() {
        if (!datenschutzbriefMassendrucks.isEmpty()) {
            return DruckauftragTyp.DATENSCHUTZBRIEF;
        }
        return DruckauftragTyp.VERFUEGUNG;
    }
}
