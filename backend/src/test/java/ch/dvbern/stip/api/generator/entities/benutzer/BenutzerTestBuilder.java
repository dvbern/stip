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

package ch.dvbern.stip.api.generator.entities.benutzer;

import java.time.LocalDate;
import java.util.HashSet;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.entity.BenutzerBuilder;
import ch.dvbern.stip.api.benutzer.type.BenutzerStatus;
import ch.dvbern.stip.api.benutzereinstellungen.entity.Benutzereinstellungen;
import ch.dvbern.stip.api.generator.AbstractTestBuilder;

public final class BenutzerTestBuilder extends AbstractTestBuilder<Benutzer, BenutzerTestBuilder> {
    BenutzerTestBuilder(Benutzer entity, LocalDate referenceDate) {
        super(entity, referenceDate);
    }

    public static BenutzerTestBuilder empty(LocalDate referenceDate) {
        Benutzer benutzer = BenutzerBuilder.benutzer()
            .nachname("Benutzer")
            .vorname("Test")
            .nutzungsbedingungenAkzeptiert(true)
            .benutzerStatus(BenutzerStatus.AKTIV)
            .rollen(new HashSet<>())
            .benutzereinstellungen(new Benutzereinstellungen())
            .build();
        return new BenutzerTestBuilder(benutzer, referenceDate);
    }
}
