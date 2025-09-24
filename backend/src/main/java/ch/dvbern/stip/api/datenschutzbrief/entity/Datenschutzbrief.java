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

package ch.dvbern.stip.api.datenschutzbrief.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_NACHNAME_NOTBLANK_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_VORNAME_NOTBLANK_MESSAGE;

@Audited
@Entity
@Table(
    name = "datenschutzbrief",
    indexes = {
        @Index(name = "IX_datenschutzbrief_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
public class Datenschutzbrief extends AbstractMandantEntity {
    @NotNull
    @Column(name = "is_versendet")
    private boolean isVersendet = false;

    @NotNull
    @Column(name = "eltern_typ", nullable = false)
    @Enumerated(EnumType.STRING)
    private ElternTyp elternTyp;

    @NotBlank(message = VALIDATION_NACHNAME_NOTBLANK_MESSAGE)
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "nachname", nullable = false, length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String nachname;

    @NotBlank(message = VALIDATION_VORNAME_NOTBLANK_MESSAGE)
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "vorname", nullable = false, length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String vorname;

    @NotNull
    @JoinColumn(
        name = "gesuch_id", foreignKey = @ForeignKey(name = "FK_datenschutzbrief_gesuch_id"),
        nullable = false
    )
    @ManyToOne(fetch = FetchType.LAZY)
    private Gesuch gesuch;
}
