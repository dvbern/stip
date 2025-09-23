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
import ch.dvbern.stip.api.verfuegung.entity.Verfuegung;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_NACHNAME_NOTBLANK_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_VORNAME_NOTBLANK_MESSAGE;

@Entity
@Table(
    name = "verfuegung_massendruck", indexes = {
        @Index(name = "IX_verfuegung_massendruck_mandant", columnList = "mandant")
    }
)
public class VerfuegungMassendruck extends AbstractMandantEntity {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "verfuegung_id", foreignKey = @ForeignKey(name = "FK_verfuegung_massendruck_verfuegung_id"))
    private Verfuegung verfuegung;

    @NotBlank(message = VALIDATION_NACHNAME_NOTBLANK_MESSAGE)
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "nachname", nullable = false, length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String nachname;

    @NotBlank(message = VALIDATION_VORNAME_NOTBLANK_MESSAGE)
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "vorname", nullable = false, length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String vorname;

    @NotNull
    @Column(name = "is_versendet")
    private boolean isVersendet = false;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
        name = "massendruck_job_id", foreignKey = @ForeignKey(name = "FK_verfuegung_massendruck_massendruck_job_id")
    )
    private MassendruckJob massendruckJob;

}
