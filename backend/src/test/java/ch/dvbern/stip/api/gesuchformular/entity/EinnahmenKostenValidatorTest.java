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

package ch.dvbern.stip.api.gesuchformular.entity;

import java.time.LocalDate;
import java.time.Year;
import java.util.HashSet;
import java.util.List;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.bildungskategorie.entity.Bildungskategorie;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.generator.entities.GesuchGenerator;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchsjahr.entity.Gesuchsjahr;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
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
        gesuchFormular.setTranche(new GesuchTranche().setGesuch(new Gesuch().setAusbildung(new Ausbildung())));
        return gesuchFormular;
    }

    private static boolean validateGesuchFormularProperty(
        Validator validator,
        GesuchFormular gesuch,
        String propertyName
    ) {
        return !validator.validate(gesuch)
            .stream()
            .map(validationError -> validationError.getPropertyPath().toString())
            .filter(x -> x.toLowerCase().contains(propertyName))
            .findFirst()
            .isPresent();
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
    void testAusbildungskostenStufeRequiredConstraintValidator() {
        AusbildungskostenStufeRequiredConstraintValidator ausbildungskostenStufeRequiredConstraintValidator =
            new AusbildungskostenStufeRequiredConstraintValidator();
        Gesuch gesuch = new Gesuch();
        gesuch.setAusbildung(new Ausbildung());
        gesuch.getAusbildung().setAusbildungsgang(new Ausbildungsgang());
        gesuch.getAusbildung().getAusbildungsgang().setBildungskategorie(new Bildungskategorie().setBfs(1));
        GesuchTranche gesuchTranche = new GesuchTranche();
        gesuchTranche.setGesuch(gesuch);
        gesuch.setGesuchTranchen(List.of(gesuchTranche));
        GesuchFormular gesuchFormular = prepareGesuchFormularMitEinnahmenKosten();
        gesuchTranche.setGesuchFormular(gesuchFormular);
        gesuchFormular.setTranche(gesuchTranche);
        assertThat(ausbildungskostenStufeRequiredConstraintValidator.isValid(gesuchFormular, null))
            .isFalse();
        gesuchFormular.getEinnahmenKosten().setAusbildungskostenSekundarstufeZwei(1);
        assertThat(ausbildungskostenStufeRequiredConstraintValidator.isValid(gesuchFormular, null))
            .isTrue();
        gesuchFormular.getAusbildung().getAusbildungsgang().setBildungskategorie(new Bildungskategorie().setBfs(10));
        assertThat(
            ausbildungskostenStufeRequiredConstraintValidator.isValid(
                gesuchFormular,
                TestUtil.initValidatorContext()
            )
        )
            .isFalse();
        gesuchFormular.getEinnahmenKosten().setAusbildungskostenSekundarstufeZwei(1);
        assertThat(
            ausbildungskostenStufeRequiredConstraintValidator.isValid(
                gesuchFormular,
                TestUtil.initValidatorContext()
            )
        )
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
    void veranlagungsCodeRequiredValidationTest() {
        final var factory = Validation.buildDefaultValidatorFactory();
        final var validator = factory.getValidator();
        final String propertyName = "veranlagungscode";
        GesuchFormular gesuch = prepareGesuchFormularMitEinnahmenKosten();
        boolean isValid = false;

        gesuch.setEinnahmenKosten(new EinnahmenKosten().setVeranlagungsCode(null));
        isValid = validateGesuchFormularProperty(validator, gesuch, propertyName);
        assertThat(isValid).isFalse();

        gesuch.getEinnahmenKosten().setVeranlagungsCode(0);
        isValid = validateGesuchFormularProperty(validator, gesuch, propertyName);
        assertThat(isValid).isTrue();

        gesuch.getEinnahmenKosten().setVeranlagungsCode(99);
        isValid = validateGesuchFormularProperty(validator, gesuch, propertyName);
        assertThat(isValid).isTrue();

        gesuch.getEinnahmenKosten().setVeranlagungsCode(100);
        isValid = validateGesuchFormularProperty(validator, gesuch, propertyName);
        assertThat(isValid).isFalse();
    }

    @Test
    void steuerjahrRequiredValidationTest() {
        final var factory = Validation.buildDefaultValidatorFactory();
        final var validator = factory.getValidator();
        final String propertyName = "steuerjahr";
        GesuchTranche tranche = GesuchGenerator.initGesuchTranche();
        GesuchFormular gesuchFormular = prepareGesuchFormularMitEinnahmenKosten();

        gesuchFormular.setTranche(tranche);
        boolean isValid = false;

        gesuchFormular.setEinnahmenKosten(new EinnahmenKosten().setSteuerjahr(null));
        isValid = validateGesuchFormularProperty(validator, gesuchFormular, propertyName);
        assertThat(isValid).isFalse();

        gesuchFormular.getEinnahmenKosten().setSteuerjahr(0);
        isValid = validateGesuchFormularProperty(validator, gesuchFormular, propertyName);
        assertThat(isValid).isTrue();
    }

    @Test
    void steuerjahrIsCurrentorPastValidationTest() {
        GesuchTranche tranche = GesuchGenerator.initGesuchTranche();
        tranche.setGesuchFormular(new GesuchFormular());
        GesuchFormular gesuchFormular = tranche.getGesuchFormular();
        gesuchFormular.setTranche(tranche);
        gesuchFormular.setEinnahmenKosten(new EinnahmenKosten());

        final var temporalValidator = new EinnahmenKostenSteuerjahrInPastOrCurrentConstraintValidator();
        gesuchFormular.getEinnahmenKosten().setSteuerjahr(Year.now().getValue());
        assertThat(temporalValidator.isValid(gesuchFormular, null)).isFalse();

        gesuchFormular.getEinnahmenKosten().setSteuerjahr(Year.now().getValue() + 1);
        assertThat(temporalValidator.isValid(gesuchFormular, null)).isFalse();

        gesuchFormular.getEinnahmenKosten().setSteuerjahr(Year.MIN_VALUE);
        assertThat(temporalValidator.isValid(gesuchFormular, null)).isTrue();

        gesuchFormular.getEinnahmenKosten().setSteuerjahr(Year.MAX_VALUE);
        assertThat(temporalValidator.isValid(gesuchFormular, null)).isFalse();

        gesuchFormular.getEinnahmenKosten()
            .setSteuerjahr(tranche.getGesuch().getGesuchsperiode().getGesuchsjahr().getTechnischesJahr());
        assertThat(temporalValidator.isValid(gesuchFormular, null)).isFalse();
        gesuchFormular.getEinnahmenKosten()
            .setSteuerjahr(tranche.getGesuch().getGesuchsperiode().getGesuchsjahr().getTechnischesJahr() + 1);
        assertThat(temporalValidator.isValid(gesuchFormular, null)).isFalse();
        gesuchFormular.getEinnahmenKosten()
            .setSteuerjahr(tranche.getGesuch().getGesuchsperiode().getGesuchsjahr().getTechnischesJahr() - 1);
        assertThat(temporalValidator.isValid(gesuchFormular, null)).isTrue();

        gesuchFormular.getEinnahmenKosten().setSteuerjahr(0);
        assertThat(temporalValidator.isValid(gesuchFormular, null)).isTrue();
    }

    @Test
    void vermoegenNonNegativeValueValidationTest() {
        final var factory = Validation.buildDefaultValidatorFactory();
        final var validator = factory.getValidator();
        final String propertyName = "vermoegen";
        GesuchFormular gesuch = prepareGesuchFormularMitEinnahmenKosten();
        boolean isValid = false;

        // test negative value
        gesuch.setEinnahmenKosten(new EinnahmenKosten().setVermoegen(-1));

        isValid = validateGesuchFormularProperty(validator, gesuch, propertyName);
        assertThat(isValid).isFalse();
        gesuch.setEinnahmenKosten(new EinnahmenKosten().setVermoegen(0));

        isValid = validateGesuchFormularProperty(validator, gesuch, propertyName);
        assertThat(isValid).isTrue();
    }

    @Test
    void vermoegenConstraintValidatorTest() {
        final var validator = new EinnahmenKostenVermoegenRequiredConstraintValidator();
        GesuchFormular gesuch = prepareGesuchFormularMitEinnahmenKosten();
        // setup
        gesuch.setTranche(
            new GesuchTranche().setGesuch(
                new Gesuch()
                    .setGesuchsperiode(new Gesuchsperiode().setGesuchsjahr(new Gesuchsjahr().setTechnischesJahr(2024)))
            )
        );
        gesuch.setPersonInAusbildung(
            (PersonInAusbildung) new PersonInAusbildung().setZivilstand(Zivilstand.LEDIG)
                .setGeburtsdatum(LocalDate.of(1995, 8, 5))
        );

        // genau 18 Jahre alt
        gesuch.setPersonInAusbildung(
            (PersonInAusbildung) new PersonInAusbildung().setZivilstand(Zivilstand.LEDIG)
                .setGeburtsdatum(LocalDate.of(2023, 12, 31).minusYears(18))
        );
        gesuch.setEinnahmenKosten(new EinnahmenKosten().setVermoegen(0));
        assertThat(validator.isValid(gesuch, null)).isTrue();
        gesuch.setEinnahmenKosten(new EinnahmenKosten().setVermoegen(null));
        assertThat(validator.isValid(gesuch, null)).isFalse();

        // reset value
        gesuch.setEinnahmenKosten(new EinnahmenKosten().setVermoegen(null));
        // fast 18 Jahre alt
        gesuch.setPersonInAusbildung(
            (PersonInAusbildung) new PersonInAusbildung().setZivilstand(Zivilstand.LEDIG)
                .setGeburtsdatum(LocalDate.of(2024, 1, 1).minusYears(18))
        );
        assertThat(validator.isValid(gesuch, null)).isTrue();
        gesuch.setEinnahmenKosten(new EinnahmenKosten().setVermoegen(0));
        assertThat(validator.isValid(gesuch, null)).isFalse();

        // reset value
        gesuch.setEinnahmenKosten(new EinnahmenKosten().setVermoegen(null));
        // unter 18 Jahre alt
        gesuch.setPersonInAusbildung(
            (PersonInAusbildung) new PersonInAusbildung().setZivilstand(Zivilstand.LEDIG)
                .setGeburtsdatum(LocalDate.of(2023, 12, 31).minusYears(5))
        );
        assertThat(validator.isValid(gesuch, null)).isTrue();
        gesuch.setEinnahmenKosten(new EinnahmenKosten().setVermoegen(0));
        assertThat(validator.isValid(gesuch, null)).isFalse();
    }
}
