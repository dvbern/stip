package ch.dvbern.stip.berechnung.service;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import ch.dvbern.stip.api.geschwister.entity.Geschwister;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.util.TestUtil;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

@QuarkusTest
@Slf4j
class PersonenImHaushaltServiceTest {
    @Inject
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

