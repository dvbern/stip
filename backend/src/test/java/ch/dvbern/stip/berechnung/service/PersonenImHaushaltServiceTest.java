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

package ch.dvbern.stip.berechnung.service;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import ch.dvbern.stip.api.geschwister.entity.Geschwister;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.berechnung.dto.PersonenImHaushaltRequestBuilder;
import ch.dvbern.stip.berechnung.dto.v1.PersonenImHaushaltRequestV1Builder;
import ch.dvbern.stip.berechnung.service.bern.v1.PersonenImHaushaltCalculatorV1;
import jakarta.enterprise.inject.Instance;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

@Slf4j
class PersonenImHaushaltServiceTest {
    PersonenImHaushaltService personenImHaushaltService;

    GesuchFormular gesuchFormular;

    @BeforeEach
    void setUpEach() {
        gesuchFormular = new GesuchFormular()
            .setPersonInAusbildung(
                new PersonInAusbildung()
            )
            .setFamiliensituation(
                new Familiensituation()
            );

        final var calculators = (Instance<PersonenImHaushaltCalculator>) Mockito.mock(Instance.class);
        Mockito.doAnswer((ignored) -> Stream.of(new PersonenImHaushaltCalculatorV1())).when(calculators).stream();

        final var requestBuilders = (Instance<PersonenImHaushaltRequestBuilder>) Mockito.mock(Instance.class);
        Mockito.doAnswer((ignored) -> Stream.of(new PersonenImHaushaltRequestV1Builder()))
            .when(requestBuilders)
            .stream();

        personenImHaushaltService = new PersonenImHaushaltService(
            calculators,
            requestBuilders
        );
    }

    @Test
    void getV1Test() {
        final var gesuch = TestUtil.getGesuchForBerechnung(UUID.randomUUID());
        final var request = personenImHaushaltService.getPersonenImHaushaltRequest(
            1,
            0,
            gesuch.getNewestGesuchTranche().orElseThrow().getGesuchFormular(),
            ElternTyp.VATER
        );
        assertThat(request, is(not(nullValue())));
    }

    @Test
    void testPersonenImHaushalt() {
        final var gesuch = TestUtil.getGesuchForBerechnung(UUID.randomUUID());
        final var request = personenImHaushaltService.getPersonenImHaushaltRequest(
            1,
            0,
            gesuch.getNewestGesuchTranche().orElseThrow().getGesuchFormular(),
            ElternTyp.VATER
        );
        final var ret = personenImHaushaltService.calculatePersonenImHaushalt(request);
        assertThat(ret, is(not(nullValue())));
    }

    @Test
    void testPersonenImHaushaltElternVerheiratetZusammen() {
        gesuchFormular.getPersonInAusbildung().setWohnsitz(Wohnsitz.EIGENER_HAUSHALT);
        gesuchFormular.getFamiliensituation().setElternVerheiratetZusammen(true);
        gesuchFormular.setGeschwisters(
            Set.of(
                (Geschwister) new Geschwister()
                    .setWohnsitz(Wohnsitz.MUTTER_VATER)
                    .setWohnsitzAnteilVater(BigDecimal.valueOf(50))
                    .setWohnsitzAnteilMutter(BigDecimal.valueOf(50)),
                (Geschwister) new Geschwister()
                    .setWohnsitz(Wohnsitz.MUTTER_VATER)
                    .setWohnsitzAnteilVater(BigDecimal.valueOf(30))
                    .setWohnsitzAnteilMutter(BigDecimal.valueOf(70)),
                (Geschwister) new Geschwister()
                    .setWohnsitz(Wohnsitz.MUTTER_VATER)
                    .setWohnsitzAnteilVater(BigDecimal.valueOf(0))
                    .setWohnsitzAnteilMutter(BigDecimal.valueOf(100))
            )
        );

        final var request = personenImHaushaltService.getPersonenImHaushaltRequest(
            1,
            0,
            gesuchFormular,
            ElternTyp.VATER
        );
        final var ret = personenImHaushaltService.calculatePersonenImHaushalt(request);
        assertThat(ret.getNoBudgetsRequired(), equalTo(1));
        assertThat(ret.getKinderImHaushalt1(), equalTo(3));
        assertThat(ret.getKinderImHaushalt2(), equalTo(0));
        assertThat(ret.getPersonenImHaushalt1(), equalTo(5));
        assertThat(ret.getPersonenImHaushalt2(), equalTo(0));
    }

    @Test
    void testPersonenImHaushaltAlimentenRegelung() {
        gesuchFormular.getPersonInAusbildung()
            .setWohnsitz(Wohnsitz.MUTTER_VATER)
            .setWohnsitzAnteilVater(BigDecimal.valueOf(50))
            .setWohnsitzAnteilMutter(BigDecimal.valueOf(50));
        gesuchFormular.getFamiliensituation()
            .setElternVerheiratetZusammen(false)
            .setGerichtlicheAlimentenregelung(true)
            .setWerZahltAlimente(Elternschaftsteilung.MUTTER)
            .setVaterWiederverheiratet(true)
            .setMutterWiederverheiratet(false);
        gesuchFormular.setGeschwisters(
            Set.of(
                (Geschwister) new Geschwister()
                    .setWohnsitz(Wohnsitz.MUTTER_VATER)
                    .setWohnsitzAnteilVater(BigDecimal.valueOf(50))
                    .setWohnsitzAnteilMutter(BigDecimal.valueOf(50)),
                (Geschwister) new Geschwister()
                    .setWohnsitz(Wohnsitz.MUTTER_VATER)
                    .setWohnsitzAnteilVater(BigDecimal.valueOf(30))
                    .setWohnsitzAnteilMutter(BigDecimal.valueOf(70)),
                (Geschwister) new Geschwister()
                    .setWohnsitz(Wohnsitz.MUTTER_VATER)
                    .setWohnsitzAnteilVater(BigDecimal.valueOf(5))
                    .setWohnsitzAnteilMutter(BigDecimal.valueOf(95))
            )
        );

        final var request = personenImHaushaltService.getPersonenImHaushaltRequest(
            1,
            0,
            gesuchFormular,
            ElternTyp.VATER
        );
        final var ret = personenImHaushaltService.calculatePersonenImHaushalt(request);
        assertThat(ret.getNoBudgetsRequired(), equalTo(1));
        assertThat(ret.getKinderImHaushalt1(), equalTo(4));
        assertThat(ret.getKinderImHaushalt2(), equalTo(0));
        assertThat(ret.getPersonenImHaushalt1(), equalTo(6));
        assertThat(ret.getPersonenImHaushalt2(), equalTo(0));
    }

    @Test
    void testPersonenImHaushaltElternteilVaterUnbekanntVerstorben() {
        gesuchFormular.getPersonInAusbildung()
            .setWohnsitz(Wohnsitz.MUTTER_VATER)
            .setWohnsitzAnteilVater(BigDecimal.valueOf(0))
            .setWohnsitzAnteilMutter(BigDecimal.valueOf(100));
        gesuchFormular.getFamiliensituation()
            .setElternVerheiratetZusammen(false)
            .setGerichtlicheAlimentenregelung(false)
            .setElternteilUnbekanntVerstorben(true)
            .setVaterUnbekanntVerstorben(ElternAbwesenheitsGrund.VERSTORBEN)
            .setMutterUnbekanntVerstorben(ElternAbwesenheitsGrund.WEDER_NOCH)
            .setVaterWiederverheiratet(false)
            .setMutterWiederverheiratet(true);
        gesuchFormular.setGeschwisters(
            Set.of(
                (Geschwister) new Geschwister()
                    .setWohnsitz(Wohnsitz.MUTTER_VATER)
                    .setWohnsitzAnteilVater(BigDecimal.valueOf(30))
                    .setWohnsitzAnteilMutter(BigDecimal.valueOf(70))
            )
        );

        final var request = personenImHaushaltService.getPersonenImHaushaltRequest(
            1,
            0,
            gesuchFormular,
            ElternTyp.VATER
        );
        final var ret = personenImHaushaltService.calculatePersonenImHaushalt(request);
        assertThat(ret.getNoBudgetsRequired(), equalTo(1));
        assertThat(ret.getKinderImHaushalt1(), equalTo(2));
        assertThat(ret.getKinderImHaushalt2(), equalTo(0));
        assertThat(ret.getPersonenImHaushalt1(), equalTo(4));
        assertThat(ret.getPersonenImHaushalt2(), equalTo(0));
    }

    @Test
    void testPersonenImHaushaltElternteilMutterUnbekanntVerstorben() {
        gesuchFormular.getPersonInAusbildung()
            .setWohnsitz(Wohnsitz.MUTTER_VATER)
            .setWohnsitzAnteilVater(BigDecimal.valueOf(0))
            .setWohnsitzAnteilMutter(BigDecimal.valueOf(100));
        gesuchFormular.getFamiliensituation()
            .setElternVerheiratetZusammen(false)
            .setGerichtlicheAlimentenregelung(false)
            .setElternteilUnbekanntVerstorben(true)
            .setVaterUnbekanntVerstorben(ElternAbwesenheitsGrund.WEDER_NOCH)
            .setMutterUnbekanntVerstorben(ElternAbwesenheitsGrund.VERSTORBEN)
            .setVaterWiederverheiratet(true)
            .setMutterWiederverheiratet(false);
        gesuchFormular.setGeschwisters(
            Set.of(
                (Geschwister) new Geschwister()
                    .setWohnsitz(Wohnsitz.MUTTER_VATER)
                    .setWohnsitzAnteilVater(BigDecimal.valueOf(30))
                    .setWohnsitzAnteilMutter(BigDecimal.valueOf(70))
            )
        );

        final var request = personenImHaushaltService.getPersonenImHaushaltRequest(
            1,
            0,
            gesuchFormular,
            ElternTyp.VATER
        );
        final var ret = personenImHaushaltService.calculatePersonenImHaushalt(request);
        assertThat(ret.getNoBudgetsRequired(), equalTo(1));
        assertThat(ret.getKinderImHaushalt1(), equalTo(2));
        assertThat(ret.getKinderImHaushalt2(), equalTo(0));
        assertThat(ret.getPersonenImHaushalt1(), equalTo(4));
        assertThat(ret.getPersonenImHaushalt2(), equalTo(0));
    }

    @Test
    void testPersonenImHaushaltElternGetrennt() {
        gesuchFormular.getPersonInAusbildung()
            .setWohnsitz(Wohnsitz.MUTTER_VATER)
            .setWohnsitzAnteilVater(BigDecimal.valueOf(0))
            .setWohnsitzAnteilMutter(BigDecimal.valueOf(100));
        gesuchFormular.getFamiliensituation()
            .setElternVerheiratetZusammen(false)
            .setGerichtlicheAlimentenregelung(false)
            .setElternteilUnbekanntVerstorben(false)
            .setVaterWiederverheiratet(false)
            .setMutterWiederverheiratet(true);
        gesuchFormular.setGeschwisters(
            Set.of(
                (Geschwister) new Geschwister()
                    .setWohnsitz(Wohnsitz.MUTTER_VATER)
                    .setWohnsitzAnteilVater(BigDecimal.valueOf(50))
                    .setWohnsitzAnteilMutter(BigDecimal.valueOf(50)),
                (Geschwister) new Geschwister()
                    .setWohnsitz(Wohnsitz.MUTTER_VATER)
                    .setWohnsitzAnteilVater(BigDecimal.valueOf(30))
                    .setWohnsitzAnteilMutter(BigDecimal.valueOf(70)),
                (Geschwister) new Geschwister()
                    .setWohnsitz(Wohnsitz.MUTTER_VATER)
                    .setWohnsitzAnteilVater(BigDecimal.valueOf(5))
                    .setWohnsitzAnteilMutter(BigDecimal.valueOf(95)),
                (Geschwister) new Geschwister()
                    .setWohnsitz(Wohnsitz.MUTTER_VATER)
                    .setWohnsitzAnteilVater(BigDecimal.valueOf(0))
                    .setWohnsitzAnteilMutter(BigDecimal.valueOf(100)),
                (Geschwister) new Geschwister()
                    .setWohnsitz(Wohnsitz.EIGENER_HAUSHALT)
            )
        );

        var request = personenImHaushaltService.getPersonenImHaushaltRequest(
            1,
            0,
            gesuchFormular,
            ElternTyp.VATER
        );
        var ret = personenImHaushaltService.calculatePersonenImHaushalt(request);
        assertThat(ret.getNoBudgetsRequired(), equalTo(2));
        assertThat(ret.getKinderImHaushalt1(), equalTo(3));
        assertThat(ret.getKinderImHaushalt2(), equalTo(2));
        assertThat(ret.getPersonenImHaushalt1(), equalTo(4));
        assertThat(ret.getPersonenImHaushalt2(), equalTo(4));

        request = personenImHaushaltService.getPersonenImHaushaltRequest(
            1,
            0,
            gesuchFormular,
            ElternTyp.MUTTER
        );
        ret = personenImHaushaltService.calculatePersonenImHaushalt(request);
        assertThat(ret.getNoBudgetsRequired(), equalTo(2));
        assertThat(ret.getKinderImHaushalt1(), equalTo(0));
        assertThat(ret.getKinderImHaushalt2(), equalTo(5));
        assertThat(ret.getPersonenImHaushalt1(), equalTo(1));
        assertThat(ret.getPersonenImHaushalt2(), equalTo(7));
    }
}
