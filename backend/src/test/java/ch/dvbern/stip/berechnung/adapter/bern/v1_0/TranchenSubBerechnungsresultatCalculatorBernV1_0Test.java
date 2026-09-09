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

package ch.dvbern.stip.berechnung.adapter.bern.v1_0;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.common.type.Ausbildungssituation;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.geschwister.entity.Geschwister;
import ch.dvbern.stip.api.geschwister.type.GeschwisterTyp;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.berechnung.adapter.bern.v1_0.service.TranchenSubBerechnungsresultatCalculator;
import ch.dvbern.stip.generated.dto.BerechnungsStammdatenDto;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class TranchenSubBerechnungsresultatCalculatorBernV1_0Test {
    @Test
    void getTranchenSubBerechnungsresultat() {
        final Gesuch gesuch = TestUtil.getGesuchForBerechnung(UUID.randomUUID());
        final GesuchTranche gesuchTranche = gesuch.getNewestGesuchTranche().get();
        Kind kind1 = (Kind) new Kind()
            .setNachname("Testfall5")
            .setVorname("Kind1")
            .setGeburtsdatum(LocalDate.of(2013, 9, 1));
        kind1.setWohnsitzAnteilPia(70);
        kind1.setAusbildungssituation(Ausbildungssituation.VORSCHULPFLICHTIG);

        Kind kind2 = (Kind) new Kind()
            .setNachname("Testfall5")
            .setVorname("Kind2")
            .setGeburtsdatum(LocalDate.of(2019, 6, 1));
        kind2.setWohnsitzAnteilPia(80);
        kind2.setAusbildungssituation(Ausbildungssituation.VORSCHULPFLICHTIG);

        gesuchTranche.getGesuchFormular().setKinds(Set.of(kind1, kind2));

        Geschwister geschwister1 = (Geschwister) new Geschwister()
            .setNachname("Testfall5")
            .setVorname("Geschwister1")
            .setGeburtsdatum(LocalDate.of(2000, 1, 1));
        geschwister1
            .setGeschwisterTyp(GeschwisterTyp.HALB)
            .setAusbildungssituation(Ausbildungssituation.IN_AUSBILDUNG)
            .setWohnsitz(Wohnsitz.MUTTER_VATER)
            .setWohnsitzAnteilVater(BigDecimal.valueOf(50));

        Geschwister geschwister2 = (Geschwister) new Geschwister()
            .setNachname("Testfall5")
            .setVorname("Geschwister2")
            .setGeburtsdatum(LocalDate.of(2000, 1, 1));
        geschwister2
            .setGeschwisterTyp(GeschwisterTyp.HALB)
            .setAusbildungssituation(Ausbildungssituation.IN_AUSBILDUNG)
            .setElternteilPiaOfStiefHalbGeschwister(ElternTyp.VATER)
            .setWohnsitz(Wohnsitz.EIGENER_HAUSHALT);

        Geschwister geschwister3 = (Geschwister) new Geschwister()
            .setNachname("Testfall5")
            .setVorname("Geschwister3")
            .setGeburtsdatum(LocalDate.of(2000, 1, 1));
        geschwister3
            .setGeschwisterTyp(GeschwisterTyp.HALB)
            .setAusbildungssituation(Ausbildungssituation.IN_AUSBILDUNG)
            .setElternteilPiaOfStiefHalbGeschwister(ElternTyp.MUTTER)
            .setWohnsitz(Wohnsitz.EIGENER_HAUSHALT);

        Geschwister geschwister4 = (Geschwister) new Geschwister()
            .setNachname("Testfall5")
            .setVorname("Geschwister4")
            .setGeburtsdatum(LocalDate.of(2000, 1, 1));
        geschwister4
            .setGeschwisterTyp(GeschwisterTyp.HALB)
            .setAusbildungssituation(Ausbildungssituation.IN_AUSBILDUNG)
            .setWohnsitz(Wohnsitz.MUTTER_VATER)
            .setWohnsitzAnteilMutter(BigDecimal.valueOf(20));

        gesuchTranche.getGesuchFormular()
            .setGeschwisters(
                Set.of(geschwister1, geschwister2, geschwister3, geschwister4)
            );

        var tranchenSubBerechnungsresultat = TranchenSubBerechnungsresultatCalculator.getTranchenSubBerechnungsresultat(
            gesuchTranche,
            null,
            true,
            true,
            DateUtil.getGesuchDateRange(gesuch),
            gesuch.getGesuchsperiode(),
            gesuch.getGesuchGueltigkeitAb().getYear(),
            (gesuchsperiode, anzahlMonate) -> new BerechnungsStammdatenDto()
        );

        assertThat(tranchenSubBerechnungsresultat.getTotal(), is(-2572));
        assertThat(tranchenSubBerechnungsresultat.getUngekuerztTotal(), is(-9800));
        assertThat(tranchenSubBerechnungsresultat.getBerechnungsanteilKinderDerEltern().intValue(), is(35));
        assertThat(tranchenSubBerechnungsresultat.getBerechnungsanteilKinderPia().intValue(), is(75));
        assertThat(
            tranchenSubBerechnungsresultat.getFamilienBudgetresultate().get(0).getAnzahlKinderInAusbildung(),
            is(3)
        );
        assertThat(
            tranchenSubBerechnungsresultat.getFamilienBudgetresultate().get(1).getAnzahlKinderInAusbildung(),
            is(3)
        );

        assertThat(tranchenSubBerechnungsresultat.getPersonenHaushaltGroups(), hasSize(3));

        tranchenSubBerechnungsresultat = TranchenSubBerechnungsresultatCalculator.getTranchenSubBerechnungsresultat(
            gesuchTranche,
            null,
            false,
            false,
            DateUtil.getGesuchDateRange(gesuch),
            gesuch.getGesuchsperiode(),
            gesuch.getGesuchGueltigkeitAb().getYear(),
            (gesuchsperiode, anzahlMonate) -> new BerechnungsStammdatenDto()
        );

        assertThat(tranchenSubBerechnungsresultat.getTotal(), is(-1699));
        assertThat(tranchenSubBerechnungsresultat.getUngekuerztTotal(), is(-10459));
        assertThat(tranchenSubBerechnungsresultat.getBerechnungsanteilKinderDerEltern().intValue(), is(65));
        assertThat(tranchenSubBerechnungsresultat.getBerechnungsanteilKinderPia().intValue(), is(25));

        assertThat(tranchenSubBerechnungsresultat.getPersonenHaushaltGroups(), hasSize(3));
    }
}
