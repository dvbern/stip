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

package ch.dvbern.stip.api.steuerdaten.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class SteuerdatenTabBerechnungsServiceTest {
    SteuerdatenTabBerechnungsService service;

    @BeforeEach
    void setup() {
        service = new SteuerdatenTabBerechnungsService();
    }

    @Test
    void elternVerheiratetTest() {
        final var famsit = new Familiensituation()
            .setElternVerheiratetZusammen(true);

        final var calculated = service.calculateTabs(famsit);
        assertCountAndTypes(calculated, 1, SteuerdatenTyp.FAMILIE);
    }

    @Test
    void emptyIfNoFamiliensituationTest() {
        final var calculated = service.calculateTabs(null);
        assertCountAndTypes(calculated, 0);
    }

    @ParameterizedTest
    @CsvSource(
        {
            "VATER,MUTTER,1",
            "MUTTER,VATER,1",
            ",,0"
        }
    )
    void gerichtlicheAlimentenregelungTest(
        final Elternschaftsteilung elternschaftsteilung,
        final SteuerdatenTyp expectedSteuerdatenTyp,
        final int expectedCount
    ) {
        final var famsit = new Familiensituation()
            .setElternVerheiratetZusammen(false)
            .setGerichtlicheAlimentenregelung(true)
            .setWerZahltAlimente(elternschaftsteilung);

        final var calculated = service.calculateTabs(famsit);
        assertCountAndTypes(calculated, expectedCount, expectedSteuerdatenTyp);
    }

    @ParameterizedTest
    @ArgumentsSource(ElternteilUnbekanntVerstorbenTestArgumentsProvider.class)
    void elternteilUnbekanntVerstorbenTest(
        final ElternAbwesenheitsGrund mutterUnbekanntVerstorben,
        final ElternAbwesenheitsGrund vaterUnbekanntVerstorben,
        final SteuerdatenTyp[] expectedSteuerdatenTyp,
        final int expectedCount
    ) {
        final var famsit = new Familiensituation()
            .setElternVerheiratetZusammen(false)
            .setGerichtlicheAlimentenregelung(false)
            .setElternteilUnbekanntVerstorben(true)
            .setMutterUnbekanntVerstorben(mutterUnbekanntVerstorben)
            .setVaterUnbekanntVerstorben(vaterUnbekanntVerstorben);

        final var calculated = service.calculateTabs(famsit);
        assertCountAndTypes(calculated, expectedCount, expectedSteuerdatenTyp);
    }

    @Test
    void bothElternteileTest() {
        final var famsit = new Familiensituation()
            .setElternVerheiratetZusammen(false)
            .setGerichtlicheAlimentenregelung(false)
            .setElternteilUnbekanntVerstorben(false);

        final var calculated = service.calculateTabs(famsit);
        assertCountAndTypes(calculated, 2, SteuerdatenTyp.MUTTER, SteuerdatenTyp.VATER);
    }

    private static class ElternteilUnbekanntVerstorbenTestArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            // Format:
            // MutterUnbekanntGrund,
            // VaterUnbekanntGrund,
            // SteuerdatenTyp[],
            // ExpectedCount
            return Stream.of(
                Arguments.of(
                    ElternAbwesenheitsGrund.VERSTORBEN,
                    ElternAbwesenheitsGrund.VERSTORBEN,
                    new SteuerdatenTyp[0],
                    0
                ),
                Arguments.of(
                    ElternAbwesenheitsGrund.VERSTORBEN,
                    ElternAbwesenheitsGrund.WEDER_NOCH,
                    new SteuerdatenTyp[] { SteuerdatenTyp.VATER },
                    1
                ),
                Arguments.of(
                    ElternAbwesenheitsGrund.WEDER_NOCH,
                    ElternAbwesenheitsGrund.VERSTORBEN,
                    new SteuerdatenTyp[] { SteuerdatenTyp.MUTTER },
                    1
                ),
                Arguments.of(
                    null,
                    null,
                    new SteuerdatenTyp[0],
                    0
                )
            );
        }
    }

    private void assertCountAndTypes(
        final List<SteuerdatenTyp> list,
        final int count,
        final SteuerdatenTyp... steuerdatenTyps
    ) {
        final var potentialMessage = String.format(
            "\nExpected:\t%s\nGot:\t\t%s",
            Arrays.toString(list.toArray()),
            Arrays.toString(steuerdatenTyps)
        );

        assertThat(potentialMessage, list.size(), is(count));
        assertThat(
            potentialMessage,
            list.containsAll(
                Arrays
                    .stream(steuerdatenTyps)
                    .filter(Objects::nonNull)
                    .toList()
            )
        );
    }
}
