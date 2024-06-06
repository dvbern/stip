package ch.dvbern.stip.api.gesuch.entity;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Year;
import java.util.HashSet;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.bildungsart.entity.Bildungsart;
import ch.dvbern.stip.api.bildungsart.type.Bildungsstufe;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.util.TestUtil;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EinnahmenKostenValidatorTest {

    private static GesuchFormular prepareGesuchFormularMitEinnahmenKosten() {
        GesuchFormular gesuchFormular = new GesuchFormular();
        gesuchFormular.setEinnahmenKosten(new EinnahmenKosten());
        PersonInAusbildung personInAusbildung = new PersonInAusbildung();
        gesuchFormular.setPersonInAusbildung(personInAusbildung);
        Kind kind = new Kind();
        final var kindSet = new HashSet<Kind>();
        kindSet.add(kind);
        gesuchFormular.setKinds(kindSet);
        return gesuchFormular;
    }

    private static boolean validateGesuchFormularProperty(Validator validator, GesuchFormular gesuch, String propertyName) {
        return !validator.validate(gesuch).stream().map(validationError -> validationError.getPropertyPath().toString()).filter(x -> x.toLowerCase().contains(propertyName)).findFirst().isPresent();
    }

    @Test
    void testEinnahmenKostenZulagenRequiredConstraintValidator() {
        EinnahmenKostenZulagenRequiredConstraintValidator einnahmenKostenZulagenRequiredConstraintValidator =
            new EinnahmenKostenZulagenRequiredConstraintValidator();
        GesuchFormular gesuchFormular = prepareGesuchFormularMitEinnahmenKosten();
        assertThat(einnahmenKostenZulagenRequiredConstraintValidator.isValid(gesuchFormular, null))
            .isFalse();
        gesuchFormular.getEinnahmenKosten().setZulagen(1);
        assertThat(einnahmenKostenZulagenRequiredConstraintValidator.isValid(gesuchFormular, null))
            .isTrue();
    }

    @Test
    void testEinnahmenKostenDarlehenRequiredConstraintValidator() {
        EinnahmenKostenDarlehenRequiredConstraintValidator einnahmenKostenDarlehenRequiredConstraintValidator =
            new EinnahmenKostenDarlehenRequiredConstraintValidator();
        // Geburtsdatum null soll keine Validation Fehler verfen als nicht validbar
        GesuchFormular gesuchFormular = prepareGesuchFormularMitEinnahmenKosten();
        assertThat(einnahmenKostenDarlehenRequiredConstraintValidator.isValid(gesuchFormular, null))
            .isTrue();
        // Minderjaehrig
        gesuchFormular.getPersonInAusbildung().setGeburtsdatum(LocalDate.now().minusYears(17));
        assertThat(einnahmenKostenDarlehenRequiredConstraintValidator.isValid(gesuchFormular, null))
            .isTrue();
        // Volljaehrig ohne darlehen Antwort
        gesuchFormular.getPersonInAusbildung().setGeburtsdatum(LocalDate.now().minusYears(18));
        assertThat(einnahmenKostenDarlehenRequiredConstraintValidator.isValid(gesuchFormular, null))
            .isFalse();
        // Volljaehrig mit Darlehen Antwort
        gesuchFormular.getEinnahmenKosten().setWillDarlehen(false);
        assertThat(einnahmenKostenDarlehenRequiredConstraintValidator.isValid(gesuchFormular, null))
            .isTrue();
    }

    @Test
    void testAusbildungskostenStufeRequiredConstraintValidator() {
        AusbildungskostenStufeRequiredConstraintValidator ausbildungskostenStufeRequiredConstraintValidator =
            new AusbildungskostenStufeRequiredConstraintValidator();
        GesuchFormular gesuchFormular = prepareGesuchFormularMitEinnahmenKosten();
        Ausbildung ausbildung = new Ausbildung();
        ausbildung.setAusbildungsgang(new Ausbildungsgang());
        ausbildung.getAusbildungsgang().setBildungsart(new Bildungsart().setBildungsstufe(Bildungsstufe.SEKUNDAR_2));
        gesuchFormular.setAusbildung(ausbildung);
        assertThat(ausbildungskostenStufeRequiredConstraintValidator.isValid(gesuchFormular, null))
            .isFalse();
        gesuchFormular.getEinnahmenKosten().setAusbildungskostenSekundarstufeZwei(1);
        assertThat(ausbildungskostenStufeRequiredConstraintValidator.isValid(gesuchFormular, null))
            .isTrue();
        gesuchFormular.getAusbildung().getAusbildungsgang().setBildungsart(new Bildungsart().setBildungsstufe(Bildungsstufe.TERTIAER));
        assertThat(ausbildungskostenStufeRequiredConstraintValidator.isValid(
            gesuchFormular,
            TestUtil.initValidatorContext()))
            .isFalse();
        gesuchFormular.getEinnahmenKosten().setAusbildungskostenTertiaerstufe(1);
        assertThat(ausbildungskostenStufeRequiredConstraintValidator.isValid(
            gesuchFormular,
            TestUtil.initValidatorContext()))
            .isTrue();
    }

    @Test
    void wohnkostenRequiredTest() {
        final var validator = new EinnahmenKostenWohnkostenRequiredConstraintValidator();

        final var pia = new PersonInAusbildung();
        pia.setWohnsitz(Wohnsitz.EIGENER_HAUSHALT);
        final var gesuch = new GesuchFormular()
            .setPersonInAusbildung(pia)
            .setEinnahmenKosten(
                new EinnahmenKosten()
                    .setWohnkosten(null)
            );

        assertThat(validator.isValid(gesuch, null)).isFalse();

        gesuch.getEinnahmenKosten().setWohnkosten(1);
        assertThat(validator.isValid(gesuch, null)).isTrue();
    }

    @Test
    void veranlagungsCodeRequiredValidationTest(){
        final var factory = Validation.buildDefaultValidatorFactory();
        final var validator = factory.getValidator();
        final String propertyName = "veranlagungscode";
        GesuchFormular gesuch = prepareGesuchFormularMitEinnahmenKosten();
        boolean isValid = false;

        gesuch.setEinnahmenKosten(new EinnahmenKosten().setVeranlagungscode(null));
        isValid = validateGesuchFormularProperty(validator,gesuch,propertyName);
        assertThat(isValid).isFalse();

        gesuch.getEinnahmenKosten().setVeranlagungscode(0);
        isValid = validateGesuchFormularProperty(validator,gesuch,propertyName);
        assertThat(isValid).isTrue();

        gesuch.getEinnahmenKosten().setVeranlagungscode(99);
        isValid = validateGesuchFormularProperty(validator,gesuch,propertyName);
        assertThat(isValid).isTrue();

        gesuch.getEinnahmenKosten().setVeranlagungscode(100);
        isValid = validateGesuchFormularProperty(validator,gesuch,propertyName);
        assertThat(isValid).isFalse();
    }

    @Test
    void steuerjahrRequiredValidationTest(){
        final var factory = Validation.buildDefaultValidatorFactory();
        final var validator = factory.getValidator();
        final String propertyName = "steuerjahr";
        GesuchFormular gesuch = prepareGesuchFormularMitEinnahmenKosten();
        boolean isValid = false;

        gesuch.setEinnahmenKosten(new EinnahmenKosten().setSteuerjahr(null));
        isValid = validateGesuchFormularProperty(validator,gesuch,propertyName);
        assertThat(isValid).isFalse();

        gesuch.getEinnahmenKosten().setSteuerjahr(0);
        isValid = validateGesuchFormularProperty(validator,gesuch,propertyName);
        assertThat(isValid).isTrue();
    }

    @Test
    void steuerjahrIsCurrentorPastValidationTest(){
        final var temporalValidator = new EinnahmenKostenSteuerjahrInPastOrCurrentConstraintValidator();
        assertThat(temporalValidator.isValid(0,null)).isTrue();
        assertThat(temporalValidator.isValid(Year.now().getValue(),null)).isTrue();
        assertThat(temporalValidator.isValid(Year.now().getValue() + 1,null)).isFalse();
        assertThat(temporalValidator.isValid(Year.MIN_VALUE,null)).isTrue();
        assertThat(temporalValidator.isValid(Year.MAX_VALUE,null)).isFalse();
    }

    @Test
    void steuerjahrDefaultValueIsLastYearTest(){
        GesuchFormular gesuch = prepareGesuchFormularMitEinnahmenKosten();
        assertThat(gesuch.getEinnahmenKosten().getSteuerjahr()).isEqualTo((Year.now().getValue() -1));
    }
    @Test
    void vermoegenValidationTest(){
        final var factory = Validation.buildDefaultValidatorFactory();
        final var validator = factory.getValidator();
        final String propertyName = "vermoegen";
        GesuchFormular gesuch = prepareGesuchFormularMitEinnahmenKosten();
        boolean isValid = false;

        gesuch.setEinnahmenKosten(new EinnahmenKosten().setVermoegen(null));
        isValid = validateGesuchFormularProperty(validator,gesuch,propertyName);
        assertThat(isValid).isFalse();

        gesuch.setEinnahmenKosten(new EinnahmenKosten().setVermoegen(-2));
        isValid = validateGesuchFormularProperty(validator,gesuch,propertyName);
        assertThat(isValid).isFalse();

        gesuch.getEinnahmenKosten().setVermoegen(0);
        isValid = validateGesuchFormularProperty(validator,gesuch,propertyName);
        assertThat(isValid).isTrue();

        gesuch.getEinnahmenKosten().setVermoegen(Integer.MAX_VALUE);
        isValid = validateGesuchFormularProperty(validator,gesuch,propertyName);
        assertThat(isValid).isTrue();
    }

}
