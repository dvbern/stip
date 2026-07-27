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

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.common.type.Ausbildungssituation;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.berechnung.adapter.bern.v1_0.service.TranchenBerechnungsresultatCalculator;
import ch.dvbern.stip.generated.dto.BerechnungsStammdatenDto;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class TranchenBerechnungsresultatCalculatorBernV1_0Test {

    @Test
    void getTranchenBerechnungsresultatTest() {
        final Gesuch gesuch = TestUtil.getGesuchForBerechnung(UUID.randomUUID());
        final GesuchTranche gesuchTranche = gesuch.getNewestGesuchTranche().get();

        var tranchenBerechnungsresultats = TranchenBerechnungsresultatCalculator.getTranchenBerechnungsresultat(
            gesuchTranche,
            DateUtil.getGesuchDateRange(gesuch),
            gesuch.getGesuchsperiode(),
            gesuch.getGesuchGueltigkeitAb().getYear(),
            (gesuchsperiode, anzahlMonate) -> new BerechnungsStammdatenDto()
        );

        assertThat(tranchenBerechnungsresultats, hasSize(2));

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

        tranchenBerechnungsresultats = TranchenBerechnungsresultatCalculator.getTranchenBerechnungsresultat(
            gesuchTranche,
            DateUtil.getGesuchDateRange(gesuch),
            gesuch.getGesuchsperiode(),
            gesuch.getGesuchGueltigkeitAb().getYear(),
            (gesuchsperiode, anzahlMonate) -> new BerechnungsStammdatenDto()
        );

        assertThat(tranchenBerechnungsresultats, hasSize(4));
    }
}
