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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.berechnung.adapter.bern.v1_0.service.PersoenlichesBudgetCalculator;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatDto;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatDto;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PersoenlichesBudgetCalculatorBernV1_0Test {

    @Test
    void calculatePersoenlichesBudgetTest() {
        final Gesuch gesuch = TestUtil.getGesuchForBerechnung(UUID.randomUUID());
        final GesuchFormular gesuchFormular = gesuch.getNewestGesuchTranche().get().getGesuchFormular();

        final List<FamilienBudgetresultatDto> familienBudgetresultats = new ArrayList<>();
        familienBudgetresultats.add(
            new FamilienBudgetresultatDto().vorname("a")
                .anrechenbareElterlicheLeistung(400)
                .einnahmeUeberschuss(0)
                .anzahlKinderInAusbildung(1)
                .steuerdatenTyp(SteuerdatenTyp.VATER)
        );
        familienBudgetresultats.add(
            new FamilienBudgetresultatDto().vorname("b")
                .anrechenbareElterlicheLeistung(1000)
                .einnahmeUeberschuss(0)
                .anzahlKinderInAusbildung(1)
                .steuerdatenTyp(SteuerdatenTyp.MUTTER)
        );
        final List<Kind> kindsImHaushalt = gesuchFormular.getKinds().stream().toList();
        final int anzahlMonateGueltigkeit = 10;
        final DateRange gesuchsDateRange = DateUtil.getGesuchDateRange(gesuch);
        final int gesuchsjahr = gesuch.getGesuchGueltigkeitAb().getYear();
        final PersoenlichesBudgetresultatDto persoenlichesBudgetresultatDto =
            PersoenlichesBudgetCalculator.calculatePersoenlichesBudget(
                gesuchFormular,
                familienBudgetresultats,
                kindsImHaushalt,
                anzahlMonateGueltigkeit,
                gesuchsDateRange,
                gesuch.getGesuchsperiode(),
                gesuchsjahr
            );

        assertThat(persoenlichesBudgetresultatDto.getTotal(), is(-8716));
        assertThat(persoenlichesBudgetresultatDto.getEinnahmenMinusKosten(), is(-20918));
        assertThat(persoenlichesBudgetresultatDto.getAnzahlPersonenImHaushalt(), is(2));
        assertThat(persoenlichesBudgetresultatDto.getProKopfTeilung(), is(2));
        assertThat(persoenlichesBudgetresultatDto.getTotalNachProKopfTeilung(), is(-10459));

        assertThat(persoenlichesBudgetresultatDto.getEinnahmen().getTotal(), is(22232));
        assertThat(persoenlichesBudgetresultatDto.getEinnahmen().getNettoerwerbseinkommenTotal(), is(19832));

        assertThat(persoenlichesBudgetresultatDto.getEinnahmen().getNettoerwerbseinkommenTotal(), is(19832));
    }
}
