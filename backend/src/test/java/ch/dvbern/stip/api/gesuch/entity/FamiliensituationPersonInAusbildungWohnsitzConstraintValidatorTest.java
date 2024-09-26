package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.common.type.Anrede;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class FamiliensituationPersonInAusbildungWohnsitzConstraintValidatorTest {
    final BigDecimal ZERO_PERCENT = BigDecimal.ZERO;
    final BigDecimal FIFTY_PERCENT = BigDecimal.valueOf(50);
    final BigDecimal HUNDRED_PERCENT = BigDecimal.valueOf(100);

    GesuchFormular gesuchFormular;
    FamiliensituationPersonInAusbildungWohnsitzConstraintValidator validator;

    @BeforeEach
    void setUp() {
        gesuchFormular = new GesuchFormular();
        // init pia
        PersonInAusbildung personInAusbildung = new PersonInAusbildung();
        gesuchFormular.setPersonInAusbildung(personInAusbildung);

        validator = new FamiliensituationPersonInAusbildungWohnsitzConstraintValidator();
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
        gesuchFormular.getPersonInAusbildung().setWohnsitz(Wohnsitz.FAMILIE);
        assertFalse(validator.isValid(gesuchFormular, null));

        // WOHSNITZ.EIGENER_HAUSHALT ist valid
        gesuchFormular.getPersonInAusbildung().setWohnsitz(Wohnsitz.EIGENER_HAUSHALT);
        assertTrue(validator.isValid(gesuchFormular, null));

        // just a setup - wohnsitzanteile are covered in toher unit tests
        gesuchFormular.getPersonInAusbildung().setWohnsitzAnteilVater(HUNDRED_PERCENT);
        gesuchFormular.getPersonInAusbildung().setWohnsitzAnteilMutter(ZERO_PERCENT);
        // WOHSNITZ.MUTTER_VATER is valid
        gesuchFormular.getPersonInAusbildung().setWohnsitz(Wohnsitz.MUTTER_VATER);
        assertTrue(validator.isValid(gesuchFormular, null));
    }

    @Test
    @Description("Wohnsitzanteil at one parent should be 100 % when both parents are absent ")
    void familiensituation_bothParentsAbsent_wohnsitzanteil_validationTest(){
        Familiensituation familiensituation = new Familiensituation();
        familiensituation.setElternVerheiratetZusammen(false);
        familiensituation.setElternteilUnbekanntVerstorben(true);
        familiensituation.setMutterUnbekanntVerstorben(ElternAbwesenheitsGrund.WEDER_NOCH);
        familiensituation.setVaterUnbekanntVerstorben(ElternAbwesenheitsGrund.WEDER_NOCH);
        gesuchFormular.setFamiliensituation(familiensituation);
        // just a setup - wohnsitzanteile are covered in toher unit tests
        gesuchFormular.getPersonInAusbildung().setWohnsitzAnteilVater(HUNDRED_PERCENT);
        gesuchFormular.getPersonInAusbildung().setWohnsitzAnteilMutter(ZERO_PERCENT);

        // WOHSNITZ.MUTTER_VATER is NOT valid
        gesuchFormular.getPersonInAusbildung().setWohnsitz(Wohnsitz.MUTTER_VATER);

        // even if both are absent, one part has to be 100 %
        gesuchFormular.getPersonInAusbildung().setWohnsitzAnteilMutter(HUNDRED_PERCENT);
        gesuchFormular.getPersonInAusbildung().setWohnsitzAnteilVater(ZERO_PERCENT);
        assertTrue(validator.isValid(gesuchFormular, null));

        // inverted is also valid, we make no difference in this case
        gesuchFormular.getPersonInAusbildung().setWohnsitzAnteilMutter(ZERO_PERCENT);
        gesuchFormular.getPersonInAusbildung().setWohnsitzAnteilVater(HUNDRED_PERCENT);
        assertTrue(validator.isValid(gesuchFormular, null));

        // only 100 % and 0 % are allowed as inputs
        gesuchFormular.getPersonInAusbildung().setWohnsitzAnteilMutter(FIFTY_PERCENT);
        gesuchFormular.getPersonInAusbildung().setWohnsitzAnteilVater(FIFTY_PERCENT);
        assertFalse(validator.isValid(gesuchFormular, null));
    }


    @Test
    @Description("Wohnistz 'Familie' should not be valid when 1 elternteil is absent")
    void familiensituation_motherAbsent_wohnsitz_validationTest(){
        Familiensituation familiensituation = new Familiensituation();
        familiensituation.setElternVerheiratetZusammen(false);
        familiensituation.setElternteilUnbekanntVerstorben(true);
        familiensituation.setMutterUnbekanntVerstorben(ElternAbwesenheitsGrund.WEDER_NOCH);
        gesuchFormular.setFamiliensituation(familiensituation);
        // just a setup - wohnsitzanteile are covered in toher unit tests
        gesuchFormular.getPersonInAusbildung().setWohnsitzAnteilVater(HUNDRED_PERCENT);
        gesuchFormular.getPersonInAusbildung().setWohnsitzAnteilMutter(ZERO_PERCENT);

        // WOHSNITZ.FAMILIE is not valid
        gesuchFormular.getPersonInAusbildung().setWohnsitz(Wohnsitz.FAMILIE);
        assertFalse(validator.isValid(gesuchFormular, null));

        // WOHSNITZ.EIGENER_HAUSHALT ist valid
        gesuchFormular.getPersonInAusbildung().setWohnsitz(Wohnsitz.EIGENER_HAUSHALT);
        assertTrue(validator.isValid(gesuchFormular, null));

        // WOHSNITZ.MUTTER_VATER is valid
        gesuchFormular.getPersonInAusbildung().setWohnsitz(Wohnsitz.MUTTER_VATER);

        assertTrue(validator.isValid(gesuchFormular, null));
    }

    @Test
    @Description("Wohnsitzanteil at parent B should be 100 % when parent A is absent ")
    void familiensituation_motherAbsent_wohnsitzanteil_validationTest(){
        Familiensituation familiensituation = new Familiensituation();
        familiensituation.setElternVerheiratetZusammen(false);
        familiensituation.setElternteilUnbekanntVerstorben(true);
        familiensituation.setMutterUnbekanntVerstorben(ElternAbwesenheitsGrund.WEDER_NOCH);
        gesuchFormular.setFamiliensituation(familiensituation);

        gesuchFormular.getPersonInAusbildung().setWohnsitz(Wohnsitz.MUTTER_VATER);

        // even if both are absent, one part has to be 100 %
        gesuchFormular.getPersonInAusbildung().setWohnsitzAnteilMutter(ZERO_PERCENT);
        gesuchFormular.getPersonInAusbildung().setWohnsitzAnteilVater(HUNDRED_PERCENT);
        assertTrue(validator.isValid(gesuchFormular, null));

        // it matters, which parent is absent
        // inverted order is not valid as a consequence
        gesuchFormular.getPersonInAusbildung().setWohnsitzAnteilMutter(HUNDRED_PERCENT);
        gesuchFormular.getPersonInAusbildung().setWohnsitzAnteilVater(ZERO_PERCENT);
        assertFalse(validator.isValid(gesuchFormular, null));

        // only 100 % and 0 % are allowed as inputs
        gesuchFormular.getPersonInAusbildung().setWohnsitzAnteilMutter(FIFTY_PERCENT);
        gesuchFormular.getPersonInAusbildung().setWohnsitzAnteilVater(FIFTY_PERCENT);
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
        gesuchFormular.getPersonInAusbildung().setWohnsitz(Wohnsitz.FAMILIE);
        assertFalse(validator.isValid(gesuchFormular, null));

        // WOHSNITZ.EIGENER_HAUSHALT ist valid
        gesuchFormular.getPersonInAusbildung().setWohnsitz(Wohnsitz.EIGENER_HAUSHALT);
        assertTrue(validator.isValid(gesuchFormular, null));

        // WOHSNITZ.MUTTER_VATER is valid
        gesuchFormular.getPersonInAusbildung().setWohnsitz(Wohnsitz.MUTTER_VATER);
        assertTrue(validator.isValid(gesuchFormular, null));
    }

    @Test
    @Description("Wohnsitzanteil should be validated normally if both parents not together but existing")
    void familiensituation_parentsNotTogehter_wohnsitzanteil_validationTest(){
        Familiensituation familiensituation = new Familiensituation();
        familiensituation.setElternVerheiratetZusammen(false);
        familiensituation.setElternteilUnbekanntVerstorben(false);
        gesuchFormular.setFamiliensituation(familiensituation);

        //50% , 50% valid, since normal validation should work
        gesuchFormular.getPersonInAusbildung().setWohnsitz(Wohnsitz.MUTTER_VATER);
        gesuchFormular.getPersonInAusbildung().setWohnsitzAnteilMutter(FIFTY_PERCENT);
        gesuchFormular.getPersonInAusbildung().setWohnsitzAnteilVater(FIFTY_PERCENT);
        assertTrue(validator.isValid(gesuchFormular, null));
    }

    @Test
    @Description("Wohnistz 'Mutter_Vater' should not be valid when parents are together")
    void familiensituation_parentsTogehter_wohnsitz_validationTest(){
        Familiensituation familiensituation = new Familiensituation();
        familiensituation.setElternVerheiratetZusammen(true);

        gesuchFormular.setFamiliensituation(familiensituation);

        // WOHSNITZ.FAMILIE is valid
        gesuchFormular.getPersonInAusbildung().setWohnsitz(Wohnsitz.FAMILIE);
        assertTrue(validator.isValid(gesuchFormular, null));

        // WOHSNITZ.EIGENER_HAUSHALT ist valid
        gesuchFormular.getPersonInAusbildung().setWohnsitz(Wohnsitz.EIGENER_HAUSHALT);
        assertTrue(validator.isValid(gesuchFormular, null));

        // WOHSNITZ.MUTTER_VATER is NOT valid
        gesuchFormular.getPersonInAusbildung().setWohnsitz(Wohnsitz.MUTTER_VATER);
        assertFalse(validator.isValid(gesuchFormular, null));
    }


}
