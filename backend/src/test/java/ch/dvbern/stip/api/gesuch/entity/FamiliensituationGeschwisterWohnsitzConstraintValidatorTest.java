package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.common.type.Anrede;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.geschwister.entity.Geschwister;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FamiliensituationGeschwisterWohnsitzConstraintValidatorTest {
    GesuchFormular gesuchFormular;
    FamiliensituationGeschwisterWohnsitzConstraintValidator validator;
    Geschwister geschwister1;
    Geschwister geschwister2;

    @BeforeEach
    void setUp() {
        gesuchFormular = new GesuchFormular();
        // init geschwister
        geschwister1 = new Geschwister();
        geschwister2 = new Geschwister();
        gesuchFormular.setGeschwisters(Set.of(geschwister1, geschwister2));
        validator = new FamiliensituationGeschwisterWohnsitzConstraintValidator();
    }

    @Test
    @Description("Wohnsitz 'Familie' should not be valid when both elternteils are absent")
    void familiensituation_bothParentsAbsent_wohnsitz_validationTest(){
        Familiensituation familiensituation = new Familiensituation();
        familiensituation.setElternVerheiratetZusammen(false);
        familiensituation.setElternteilUnbekanntVerstorben(true);
        familiensituation.setMutterUnbekanntVerstorben(ElternAbwesenheitsGrund.WEDER_NOCH);
        familiensituation.setVaterUnbekanntVerstorben(ElternAbwesenheitsGrund.WEDER_NOCH);
        gesuchFormular.setFamiliensituation(familiensituation);

        // WOHSNITZ.FAMILIE is not valid
        gesuchFormular.getGeschwisters().stream().findFirst().get().setWohnsitz(Wohnsitz.FAMILIE);
        gesuchFormular.getGeschwisters().stream().toList().get(1).setWohnsitz(Wohnsitz.FAMILIE);

        assertFalse(validator.isValid(gesuchFormular, null));

        // WOHSNITZ.EIGENER_HAUSHALT ist valid
        gesuchFormular.getGeschwisters().stream().findFirst().get().setWohnsitz(Wohnsitz.EIGENER_HAUSHALT);
        gesuchFormular.getGeschwisters().stream().toList().get(1).setWohnsitz(Wohnsitz.EIGENER_HAUSHALT);
        assertTrue(validator.isValid(gesuchFormular, null));

        // WOHSNITZ.MUTTER_VATER is NOT valid
        gesuchFormular.getGeschwisters().stream().findFirst().get().setWohnsitz(Wohnsitz.MUTTER_VATER);
        gesuchFormular.getGeschwisters().stream().toList().get(1).setWohnsitz(Wohnsitz.MUTTER_VATER);
        assertFalse(validator.isValid(gesuchFormular, null));
    }

    //TODO: mit Dänu abklären
    // Welche Optionen gültig, wenn beide eltern verstorben?
    // Falls VATER_MUTTER gültig: was passiert mit prozenten?
    @Test
    @Description("Wohnsitzanteil at parents should be null when both parents are absent ")
    void familiensituation_bothParentsAbsent_wohnsitzanteil_validationTest(){
        Familiensituation familiensituation = new Familiensituation();
        familiensituation.setElternVerheiratetZusammen(false);
        familiensituation.setElternteilUnbekanntVerstorben(true);
        familiensituation.setMutterUnbekanntVerstorben(ElternAbwesenheitsGrund.WEDER_NOCH);
        familiensituation.setVaterUnbekanntVerstorben(ElternAbwesenheitsGrund.WEDER_NOCH);
        gesuchFormular.setFamiliensituation(familiensituation);

        // WOHSNITZ.MUTTER_VATER is NOT valid
        gesuchFormular.getGeschwisters().stream().findFirst().get().setWohnsitz(Wohnsitz.MUTTER_VATER);
        gesuchFormular.getGeschwisters().stream().toList().get(1).setWohnsitz(Wohnsitz.MUTTER_VATER);

        gesuchFormular.getGeschwisters().stream().findFirst().get().setWohnsitzAnteilMutter(null);
        gesuchFormular.getGeschwisters().stream().findFirst().get().setWohnsitzAnteilVater(null);
        gesuchFormular.getGeschwisters().stream().toList().get(1).setWohnsitzAnteilMutter(null);
        gesuchFormular.getGeschwisters().stream().toList().get(1).setWohnsitzAnteilVater(null);

        assertFalse(validator.isValid(gesuchFormular, null));
    }


    @Test
    @Description("Wohnistz 'Familie' should not be valid when 1 elternteil is absent")
    void familiensituation_omotherAbsent_wohnsitz_validationTest(){
        Familiensituation familiensituation = new Familiensituation();
        familiensituation.setElternVerheiratetZusammen(false);
        familiensituation.setElternteilUnbekanntVerstorben(true);
        familiensituation.setMutterUnbekanntVerstorben(ElternAbwesenheitsGrund.WEDER_NOCH);
        gesuchFormular.setFamiliensituation(familiensituation);

        // WOHSNITZ.FAMILIE is not valid
        gesuchFormular.getGeschwisters().stream().findFirst().get().setWohnsitz(Wohnsitz.FAMILIE);
        gesuchFormular.getGeschwisters().stream().toList().get(1).setWohnsitz(Wohnsitz.FAMILIE);
        assertFalse(validator.isValid(gesuchFormular, null));

        // WOHSNITZ.EIGENER_HAUSHALT ist valid
        gesuchFormular.getGeschwisters().stream().findFirst().get().setWohnsitz(Wohnsitz.EIGENER_HAUSHALT);
        gesuchFormular.getGeschwisters().stream().toList().get(1).setWohnsitz(Wohnsitz.EIGENER_HAUSHALT);
        assertTrue(validator.isValid(gesuchFormular, null));

        // WOHSNITZ.MUTTER_VATER is valid
        gesuchFormular.getGeschwisters().stream().findFirst().get().setWohnsitz(Wohnsitz.MUTTER_VATER);
        gesuchFormular.getGeschwisters().stream().toList().get(1).setWohnsitz(Wohnsitz.MUTTER_VATER);

        gesuchFormular.getGeschwisters().stream().findFirst().get().setWohnsitzAnteilVater(BigDecimal.ZERO);
        gesuchFormular.getGeschwisters().stream().toList().get(1).setWohnsitzAnteilVater(BigDecimal.ZERO);

        assertTrue(validator.isValid(gesuchFormular, null));
    }

    @Test
    @Description("Wohnsitzanteil at parent A should be null when parent A is absent ")
    void familiensituation_motherAbsent_wohnsitzanteil_validationTest(){
        Familiensituation familiensituation = new Familiensituation();
        familiensituation.setElternVerheiratetZusammen(false);
        familiensituation.setElternteilUnbekanntVerstorben(true);
        familiensituation.setMutterUnbekanntVerstorben(ElternAbwesenheitsGrund.WEDER_NOCH);
        gesuchFormular.setFamiliensituation(familiensituation);

        //not valid, since only one part should be null
        gesuchFormular.getGeschwisters().stream().findFirst().get().setWohnsitz(Wohnsitz.MUTTER_VATER);
        gesuchFormular.getGeschwisters().stream().toList().get(1).setWohnsitz(Wohnsitz.MUTTER_VATER);
        gesuchFormular.getGeschwisters().stream().findFirst().get().setWohnsitzAnteilMutter(null);
        gesuchFormular.getGeschwisters().stream().findFirst().get().setWohnsitzAnteilVater(null);
        gesuchFormular.getGeschwisters().stream().toList().get(1).setWohnsitzAnteilMutter(null);
        gesuchFormular.getGeschwisters().stream().toList().get(1).setWohnsitzAnteilVater(null);
        assertFalse(validator.isValid(gesuchFormular, null));

        // valid, since other part is not null
        gesuchFormular.getGeschwisters().stream().toList().get(1).setWohnsitzAnteilVater(BigDecimal.ZERO);
        assertTrue(validator.isValid(gesuchFormular, null));

        // valid, since other part is not null
        gesuchFormular.getGeschwisters().stream().toList().get(1).setWohnsitzAnteilVater(BigDecimal.valueOf(50));
        assertTrue(validator.isValid(gesuchFormular, null));

        // valid, since other part is not null
        gesuchFormular.getGeschwisters().stream().toList().get(1).setWohnsitzAnteilVater(BigDecimal.valueOf(100));
        assertTrue(validator.isValid(gesuchFormular, null));

        //not valid, since mother is absent
        gesuchFormular.getGeschwisters().stream().toList().get(1).setWohnsitzAnteilMutter(BigDecimal.ZERO);
        assertFalse(validator.isValid(gesuchFormular, null));
        //not valid, since mother is absent
        gesuchFormular.getGeschwisters().stream().toList().get(1).setWohnsitzAnteilVater(BigDecimal.valueOf(50));
        assertFalse(validator.isValid(gesuchFormular, null));
    }

    @Test
    @Description("Wohnistz 'Familie' should not be valid when 1 elternteil is absent")
    void familiensituation_parentsNotTogehter_wohnsitz_validationTest(){
        Familiensituation familiensituation = new Familiensituation();
        familiensituation.setElternVerheiratetZusammen(false);
        familiensituation.setElternteilUnbekanntVerstorben(false);

        gesuchFormular.setFamiliensituation(familiensituation);

        // WOHSNITZ.FAMILIE is not valid
        gesuchFormular.getGeschwisters().stream().findFirst().get().setWohnsitz(Wohnsitz.FAMILIE);
        gesuchFormular.getGeschwisters().stream().toList().get(1).setWohnsitz(Wohnsitz.FAMILIE);
        assertFalse(validator.isValid(gesuchFormular, null));

        // WOHSNITZ.EIGENER_HAUSHALT ist valid
        gesuchFormular.getGeschwisters().stream().findFirst().get().setWohnsitz(Wohnsitz.EIGENER_HAUSHALT);
        gesuchFormular.getGeschwisters().stream().toList().get(1).setWohnsitz(Wohnsitz.EIGENER_HAUSHALT);
        assertTrue(validator.isValid(gesuchFormular, null));

        // WOHSNITZ.MUTTER_VATER is valid
        gesuchFormular.getGeschwisters().stream().findFirst().get().setWohnsitz(Wohnsitz.MUTTER_VATER);
        gesuchFormular.getGeschwisters().stream().toList().get(1).setWohnsitz(Wohnsitz.MUTTER_VATER);
        gesuchFormular.getGeschwisters().stream().findFirst().get().setWohnsitzAnteilVater(BigDecimal.ZERO);
        gesuchFormular.getGeschwisters().stream().toList().get(1).setWohnsitzAnteilVater(BigDecimal.ZERO);
        assertTrue(validator.isValid(gesuchFormular, null));
    }

    @Test
    @Description("Wohnsitzanteil at parent A should be null when parent A is absent ")
    void familiensituation_parentsNotTogehter_wohnsitzanteil_validationTest(){
        Familiensituation familiensituation = new Familiensituation();
        familiensituation.setElternVerheiratetZusammen(false);
        familiensituation.setElternteilUnbekanntVerstorben(false);
        gesuchFormular.setFamiliensituation(familiensituation);

        //not valid, since both parts are null
        gesuchFormular.getGeschwisters().stream().findFirst().get().setWohnsitz(Wohnsitz.MUTTER_VATER);
        gesuchFormular.getGeschwisters().stream().toList().get(1).setWohnsitz(Wohnsitz.MUTTER_VATER);
        assertTrue(validator.isValid(gesuchFormular, null));
    }

    //todo Dänu absrpache
    // wenn zusammen - dann MUTTER_VATER inkl. wohnanteilangaben nicht gültig?
    @Test
    @Description("Wohnistz 'Mutter_Vater' should not be valid when parents are together")
    void familiensituation_parentsTogehter_wohnsitz_validationTest(){
        Familiensituation familiensituation = new Familiensituation();
        familiensituation.setElternVerheiratetZusammen(true);

        gesuchFormular.setFamiliensituation(familiensituation);

        // WOHSNITZ.FAMILIE is valid
        gesuchFormular.getGeschwisters().stream().findFirst().get().setWohnsitz(Wohnsitz.FAMILIE);
        gesuchFormular.getGeschwisters().stream().toList().get(1).setWohnsitz(Wohnsitz.FAMILIE);
        assertTrue(validator.isValid(gesuchFormular, null));

        // WOHSNITZ.EIGENER_HAUSHALT ist valid
        gesuchFormular.getGeschwisters().stream().findFirst().get().setWohnsitz(Wohnsitz.EIGENER_HAUSHALT);
        gesuchFormular.getGeschwisters().stream().toList().get(1).setWohnsitz(Wohnsitz.EIGENER_HAUSHALT);
        assertTrue(validator.isValid(gesuchFormular, null));

        // WOHSNITZ.MUTTER_VATER is valid
        gesuchFormular.getGeschwisters().stream().findFirst().get().setWohnsitz(Wohnsitz.MUTTER_VATER);
        gesuchFormular.getGeschwisters().stream().toList().get(1).setWohnsitz(Wohnsitz.MUTTER_VATER);
        assertTrue(validator.isValid(gesuchFormular, null));
    }

    @Test
    @Description("Wohnsitzanteil should be null when parents are together")
    void familiensituation_parentsTogehter_wohnsitzanteil_validationTest(){
        Familiensituation familiensituation = new Familiensituation();
        familiensituation.setElternVerheiratetZusammen(true);
        familiensituation.setElternteilUnbekanntVerstorben(false);
        gesuchFormular.setFamiliensituation(familiensituation);

        //not valid, since both parts are null
        gesuchFormular.getGeschwisters().stream().findFirst().get().setWohnsitz(Wohnsitz.MUTTER_VATER);
        gesuchFormular.getGeschwisters().stream().toList().get(1).setWohnsitz(Wohnsitz.MUTTER_VATER);
        assertTrue(validator.isValid(gesuchFormular, null));
    }

}
