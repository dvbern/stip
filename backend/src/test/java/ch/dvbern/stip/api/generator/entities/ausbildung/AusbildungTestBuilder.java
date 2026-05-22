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

package ch.dvbern.stip.api.generator.entities.ausbildung;

import java.time.LocalDate;
import java.util.ArrayList;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.entity.AusbildungBuilder;
import ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum;
import ch.dvbern.stip.api.ausbildung.type.AusbildungsStatus;
import ch.dvbern.stip.api.generator.AbstractTestBuilder;
import ch.dvbern.stip.api.generator.entities.ausbildungsgang.AusbildungsgangTestBuilder;
import ch.dvbern.stip.api.generator.entities.fall.FallTestBuilder;
import ch.dvbern.stip.api.generator.entities.land.LandTestBuilder;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;

public final class AusbildungTestBuilder extends AbstractTestBuilder<Ausbildung, AusbildungTestBuilder> {

    AusbildungTestBuilder(Ausbildung entity, LocalDate referenceDate) {
        super(entity, referenceDate);
    }

    public static AusbildungTestBuilder empty(LocalDate referenceDate) {
        Ausbildung ausbildung = AusbildungBuilder.ausbildung()
            .fall(null)
            .gesuchs(new ArrayList<>())
            .ausbildungsgang(null)
            .besuchtBMS(false)
            .alternativeAusbildungsgang(null)
            .alternativeAusbildungsstaette(null)
            .fachrichtungBerufsbezeichnung("Tester")
            .ausbildungNichtGefunden(false)
            .ausbildungBegin(referenceDate.minusMonths(1))
            .ausbildungEnd(referenceDate.plusYears(2))
            .pensum(AusbildungsPensum.VOLLZEIT)
            .ausbildungsort("Bern")
            .ausbildungsortPLZ("3000")
            .isAusbildungAusland(false)
            .land(null)
            .status(AusbildungsStatus.AKTIV)
            .ausbildungUnterbruchAntrags(new ArrayList<>())
            .build();

        return new AusbildungTestBuilder(ausbildung, referenceDate);
    }

    public static AusbildungTestBuilder standardDirectDeps(LocalDate referenceDate) {
        Ausbildung ausbildung = AusbildungBuilder.ausbildung()
            .fall(FallTestBuilder.empty(referenceDate).build())
            .gesuchs(new ArrayList<>())
            .ausbildungsgang(AusbildungsgangTestBuilder.empty(referenceDate).build())
            .besuchtBMS(false)
            .alternativeAusbildungsgang(null)
            .alternativeAusbildungsstaette(null)
            .fachrichtungBerufsbezeichnung("Tester")
            .ausbildungNichtGefunden(false)
            .ausbildungBegin(referenceDate.minusMonths(1))
            .ausbildungEnd(referenceDate.plusYears(2))
            .pensum(AusbildungsPensum.VOLLZEIT)
            .ausbildungsort("Bern")
            .ausbildungsortPLZ("3000")
            .isAusbildungAusland(false)
            .land(LandTestBuilder.swiss(referenceDate).build())
            .status(AusbildungsStatus.AKTIV)
            .ausbildungUnterbruchAntrags(new ArrayList<>())
            .build();

        return new AusbildungTestBuilder(ausbildung, referenceDate);
    }

    public static AusbildungTestBuilder standardNestedDeps(LocalDate referenceDate) {
        Ausbildung ausbildung = AusbildungBuilder.ausbildung()
            .fall(FallTestBuilder.standardDirectDeps(referenceDate).build())
            .gesuchs(new ArrayList<>())
            .ausbildungsgang(AusbildungsgangTestBuilder.standardDirectDeps(referenceDate).build())
            .besuchtBMS(false)
            .alternativeAusbildungsgang(null)
            .alternativeAusbildungsstaette(null)
            .fachrichtungBerufsbezeichnung("Tester")
            .ausbildungNichtGefunden(false)
            .ausbildungBegin(referenceDate.minusMonths(1))
            .ausbildungEnd(referenceDate.plusYears(2))
            .pensum(AusbildungsPensum.VOLLZEIT)
            .ausbildungsort("Bern")
            .ausbildungsortPLZ("3000")
            .isAusbildungAusland(false)
            .land(LandTestBuilder.swiss(referenceDate).build())
            .status(AusbildungsStatus.AKTIV)
            .ausbildungUnterbruchAntrags(new ArrayList<>())
            .build();

        return new AusbildungTestBuilder(ausbildung, referenceDate);
    }

    public AusbildungTestBuilder withGesuch(Gesuch gesuch) {
        gesuch.setAusbildung(entity);
        entity.getGesuchs().add(gesuch);
        return this;
    }
}
