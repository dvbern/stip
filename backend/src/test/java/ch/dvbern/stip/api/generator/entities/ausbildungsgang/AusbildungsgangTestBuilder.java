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

package ch.dvbern.stip.api.generator.entities.ausbildungsgang;

import java.time.LocalDate;
import java.util.List;

import ch.dvbern.stip.api.ausbildung.entity.Abschluss;
import ch.dvbern.stip.api.ausbildung.entity.AbschlussBuilder;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.ausbildung.entity.AusbildungsgangBuilder;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsstaette;
import ch.dvbern.stip.api.ausbildung.entity.AusbildungsstaetteBuilder;
import ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie;
import ch.dvbern.stip.api.ausbildung.type.AusbildungsstaetteNummerTyp;
import ch.dvbern.stip.api.ausbildung.type.Bildungskategorie;
import ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung;
import ch.dvbern.stip.api.ausbildung.type.FerienTyp;
import ch.dvbern.stip.api.generator.AbstractTestBuilder;

public final class AusbildungsgangTestBuilder extends AbstractTestBuilder<Ausbildungsgang, AusbildungsgangTestBuilder> {
    AusbildungsgangTestBuilder(Ausbildungsgang entity, LocalDate referenceDate) {
        super(entity, referenceDate);
    }

    public static AusbildungsgangTestBuilder empty(LocalDate referenceDate) {
        Ausbildungsgang ausbildungsgang = AusbildungsgangBuilder.ausbildungsgang()
            .ausbildungsstaette(null)
            .abschluss(null)
            .aktiv(true)
            .build();

        return new AusbildungsgangTestBuilder(ausbildungsgang, referenceDate);
    }

    public static AusbildungsgangTestBuilder standardDirectDeps(LocalDate referenceDate) {
        Ausbildungsgang ausbildungsgang = AusbildungsgangBuilder.ausbildungsgang()
            .ausbildungsstaette(null)
            .abschluss(null)
            .aktiv(true)
            .build();

        Ausbildungsstaette ausbildungsstaette = AusbildungsstaetteBuilder.ausbildungsstaette()
            .nameDe("Universität Test")
            .nameFr("Université de Test")
            .nummerTyp(AusbildungsstaetteNummerTyp.CH_SHIS)
            .aktiv(true)
            .ausbildungsgaenge(List.of(ausbildungsgang))
            .nummer("0010")
            .build();

        Abschluss abschluss = AbschlussBuilder.abschluss()
            .bezeichnungDe("Tester")
            .bezeichnungFr("Tester")
            .ausbildungskategorie(Ausbildungskategorie.UNIVERSITAET_ETH)
            .bildungskategorie(Bildungskategorie.TERTIAERSTUFE_A)
            .bildungsrichtung(Bildungsrichtung.HOCHSCHULE)
            .bfsKategorie(9)
            .berufsbefaehigenderAbschluss(true)
            .ferien(FerienTyp.SCHULE)
            .askForBerufsmaturitaet(false)
            .aktiv(true)
            .ausbildungsgaenge(List.of(ausbildungsgang))
            .build();

        ausbildungsgang.setAusbildungsstaette(ausbildungsstaette);
        ausbildungsgang.setAbschluss(abschluss);

        return new AusbildungsgangTestBuilder(ausbildungsgang, referenceDate);
    }
}
