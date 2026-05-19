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

package ch.dvbern.stip.berechnung.bern.v1;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import ch.dvbern.stip.api.ausbildung.entity.Abschluss;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.ausbildung.type.Bildungskategorie;
import ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.berechnung.dto.PersonenImHaushaltResult;
import ch.dvbern.stip.berechnung.dto.v1.BerechnungRequestV1;
import ch.dvbern.stip.berechnung.service.PersonenImHaushaltService;
import ch.dvbern.stip.berechnung.service.bern.v1.PersoenlichesBudgetCalculatorV1;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class PersoenlichesBudgetCalculatorV1Test {
    PersonenImHaushaltService personenImHaushaltService;

    @BeforeEach
    void setUp() {
        personenImHaushaltService = Mockito.mock(PersonenImHaushaltService.class);
        Mockito.when(personenImHaushaltService.calculatePersonenImHaushalt(Mockito.any()))
            .thenReturn(
                new PersonenImHaushaltResult(0, 0, 0, 0, 0)
            );
    }

    @Test
    void proKopfTeilungTest() {
        Gesuch gesuch = TestUtil.getFullGesuch();

        final var ausbildungsBegin = LocalDate.now().withMonth(9);

        gesuch.setAusbildung(
            new Ausbildung()
                .setAusbildungsgang(
                    new Ausbildungsgang()
                        .setAbschluss(
                            new Abschluss().setBfsKategorie(10)
                                .setBildungskategorie(Bildungskategorie.TERTIAERSTUFE_B)
                                .setBildungsrichtung(
                                    Bildungsrichtung.HOCHSCHULE
                                )
                        )
                )
                .setAusbildungBegin(ausbildungsBegin)
                .setAusbildungEnd(ausbildungsBegin.plusYears(2))
        );
        gesuch.getLatestGesuchTranche()
            .getGesuchFormular()
            .getFamiliensituation()
            .setGerichtlicheAlimentenregelung(true)
            .setWerZahltAlimente(
                Elternschaftsteilung.GEMEINSAM
            );
        gesuch.getLatestGesuchTranche().getGesuchFormular().setGeschwisters(Set.of());
        gesuch.getLatestGesuchTranche().getGesuchFormular().setElterns(Set.of());
        gesuch.getLatestGesuchTranche().getGesuchFormular().setSteuerdaten(Set.of());
        BerechnungRequestV1 berechnungRequestV1 = BerechnungRequestV1
            .createRequest(gesuch, gesuch.getLatestGesuchTranche(), ElternTyp.VATER, true, personenImHaushaltService);
        var result = PersoenlichesBudgetCalculatorV1.calculatePersoenlichesBudget(
            berechnungRequestV1.getInputPersoenlichesBudget(),
            Optional.empty(),
            Optional.empty(),
            berechnungRequestV1.getStammdaten()
        );
        MatcherAssert.assertThat(result.getProKopfTeilung(), Matchers.is(4));
    }
}
