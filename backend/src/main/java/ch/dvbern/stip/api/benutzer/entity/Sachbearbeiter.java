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

package ch.dvbern.stip.api.benutzer.entity;

import java.util.Locale;

import ch.dvbern.stip.api.common.util.Constants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH;

@Audited
@Getter
@Setter
@Entity
public class Sachbearbeiter extends EmailBenutzer {
    @NotNull
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "telefonnummer", nullable = false, length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String telefonnummer;

    @NotNull
    @Size(max = Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "funktion_de", nullable = false, length = Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String funktionDe;

    @NotNull
    @Size(max = Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "funktion_fr", nullable = false, length = Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String funktionFr;

    @Transient
    public String getFunktion(final Locale locale) {
        if (locale.equals(Locale.GERMAN)) {
            return funktionDe;
        }

        if (locale.equals(Locale.FRENCH)) {
            return funktionFr;
        }

        return funktionDe;
    }
}
