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

package ch.dvbern.stip.api.common.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.jilt.Builder;
import org.jilt.BuilderStyle;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_NACHNAME_NOTBLANK_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_VORNAME_NOTBLANK_MESSAGE;

@MappedSuperclass
@Audited
@Getter
@Setter
@Builder(style = BuilderStyle.STAGED)
@NoArgsConstructor
@AllArgsConstructor
public class AbstractPerson extends AbstractMandantEntity {
    @NotBlank(message = VALIDATION_NACHNAME_NOTBLANK_MESSAGE)
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "nachname", nullable = false, length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String nachname;

    @NotBlank(message = VALIDATION_VORNAME_NOTBLANK_MESSAGE)
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "vorname", nullable = false, length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String vorname;

    @NotNull
    @Column(name = "geburtsdatum", nullable = false)
    private LocalDate geburtsdatum;

    public String getFullName() {
        return getVorname() + " " + getNachname();
    }
}
