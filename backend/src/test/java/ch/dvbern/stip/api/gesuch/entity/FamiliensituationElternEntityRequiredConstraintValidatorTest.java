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

package ch.dvbern.stip.api.gesuch.entity;

import java.util.HashSet;
import java.util.Set;

import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class FamiliensituationElternEntityRequiredConstraintValidatorTest {

    @Test
    void isValidTest() {
        FamiliensituationElternEntityRequiredConstraintValidator familiensituationElternEntityRequiredConstraintValidator =
            new FamiliensituationElternEntityRequiredConstraintValidator();

        // Empty FamSit
        assertThat(
            familiensituationElternEntityRequiredConstraintValidator.isValid(new GesuchFormular(), null),
            is(true)
        );
        // Beide Eltern sollten vorhanden sein:
        GesuchFormular gesuchFormular = new GesuchFormular();
        Familiensituation familiensituation = new Familiensituation();
        familiensituation.setElternteilUnbekanntVerstorben(false);
        familiensituation.setElternVerheiratetZusammen(true);
        gesuchFormular.setFamiliensituation(familiensituation);
        assertThat(familiensituationElternEntityRequiredConstraintValidator.isValid(gesuchFormular, null), is(false));
        // Add Mutter
        Eltern mutter = new Eltern();
        mutter.setElternTyp(ElternTyp.MUTTER);
        Set<Eltern> elternSet = new HashSet<>();
        elternSet.add(mutter);
        gesuchFormular.setElterns(elternSet);
        assertThat(familiensituationElternEntityRequiredConstraintValidator.isValid(gesuchFormular, null), is(false));
        // Add Vater
        Eltern vater = new Eltern();
        vater.setElternTyp(ElternTyp.VATER);
        elternSet.add(mutter);
        elternSet.add(vater);
        gesuchFormular.setElterns(elternSet);
        assertThat(familiensituationElternEntityRequiredConstraintValidator.isValid(gesuchFormular, null), is(true));

        // Elternteil unbekannt, Vater verstorben, Mutter Pflichtig:
        familiensituation.setElternVerheiratetZusammen(false);
        familiensituation.setGerichtlicheAlimentenregelung(false);
        familiensituation.setElternteilUnbekanntVerstorben(true);
        familiensituation.setVaterUnbekanntVerstorben(ElternAbwesenheitsGrund.VERSTORBEN);
        gesuchFormular.setFamiliensituation(familiensituation);
        assertThat(familiensituationElternEntityRequiredConstraintValidator.isValid(gesuchFormular, null), is(false));
        elternSet.clear();
        elternSet.add(mutter);
        gesuchFormular.setElterns(elternSet);
        assertThat(familiensituationElternEntityRequiredConstraintValidator.isValid(gesuchFormular, null), is(false));

        // Elternteil unbekannt, Vater verstorben, Mutter Verstorben:
        familiensituation.setMutterUnbekanntVerstorben(ElternAbwesenheitsGrund.VERSTORBEN);
        gesuchFormular.setFamiliensituation(familiensituation);
        assertThat(familiensituationElternEntityRequiredConstraintValidator.isValid(gesuchFormular, null), is(false));
        gesuchFormular.setElterns(new HashSet<>());
        assertThat(familiensituationElternEntityRequiredConstraintValidator.isValid(gesuchFormular, null), is(true));

        // Elternteil unbekannt, Vater Pflichtig, Mutter Verstorben:
        familiensituation.setMutterUnbekanntVerstorben(ElternAbwesenheitsGrund.VERSTORBEN);
        familiensituation.setVaterUnbekanntVerstorben(ElternAbwesenheitsGrund.WEDER_NOCH);
        gesuchFormular.setFamiliensituation(familiensituation);
        assertThat(familiensituationElternEntityRequiredConstraintValidator.isValid(gesuchFormular, null), is(false));
        elternSet.clear();
        elternSet.add(vater);
        gesuchFormular.setElterns(elternSet);
        assertThat(familiensituationElternEntityRequiredConstraintValidator.isValid(gesuchFormular, null), is(true));
    }

    @Test
    void familienSituationAbwesenheitValidTest_NOT_ZUSAMMEN_VERHEIRATET() {
        FamiliensituationElternEntityRequiredConstraintValidator familiensituationElternEntityRequiredConstraintValidator =
            new FamiliensituationElternEntityRequiredConstraintValidator();

        GesuchFormular gesuchFormular = new GesuchFormular();
        Familiensituation familiensituation = new Familiensituation();
        familiensituation.setElternteilUnbekanntVerstorben(true);
        familiensituation.setElternVerheiratetZusammen(false);
        gesuchFormular.setFamiliensituation(familiensituation);

        // Elternteil unbekannt, Vater und Mutter pflichitg
        familiensituation.setVaterUnbekanntVerstorben(ElternAbwesenheitsGrund.WEDER_NOCH);
        familiensituation.setMutterUnbekanntVerstorben(ElternAbwesenheitsGrund.WEDER_NOCH);
        gesuchFormular.setFamiliensituation(familiensituation);
        assertThat(familiensituationElternEntityRequiredConstraintValidator.isValid(gesuchFormular, null), is(false));

        // Elternteil unbekannt, Vater und Mutter pflichitg
        familiensituation.setVaterUnbekanntVerstorben(ElternAbwesenheitsGrund.WEDER_NOCH);
        familiensituation.setMutterUnbekanntVerstorben(ElternAbwesenheitsGrund.UNBEKANNT);
        gesuchFormular.setFamiliensituation(familiensituation);
        assertThat(familiensituationElternEntityRequiredConstraintValidator.isValid(gesuchFormular, null), is(false));
    }

    @Test
    void werZahltAlimenteTest() {
        FamiliensituationElternEntityRequiredConstraintValidator familiensituationElternEntityRequiredConstraintValidator =
            new FamiliensituationElternEntityRequiredConstraintValidator();

        // Beide Eltern sollten vorhanden sein:
        GesuchFormular gesuchFormular = new GesuchFormular();
        Familiensituation familiensituation = new Familiensituation();
        familiensituation.setElternteilUnbekanntVerstorben(false);
        familiensituation.setGerichtlicheAlimentenregelung(true);
        familiensituation.setWerZahltAlimente(Elternschaftsteilung.GEMEINSAM);
        gesuchFormular.setFamiliensituation(familiensituation);
        assertThat(familiensituationElternEntityRequiredConstraintValidator.isValid(gesuchFormular, null), is(true));
        // Add Mutter
        Eltern mutter = new Eltern();
        mutter.setElternTyp(ElternTyp.MUTTER);
        Set<Eltern> elternSet = new HashSet<>();
        elternSet.add(mutter);
        gesuchFormular.setElterns(elternSet);
        assertThat(familiensituationElternEntityRequiredConstraintValidator.isValid(gesuchFormular, null), is(false));
        // Add Vater
        Eltern vater = new Eltern();
        vater.setElternTyp(ElternTyp.VATER);
        elternSet.add(mutter);
        elternSet.add(vater);
        gesuchFormular.setElterns(elternSet);
        assertThat(familiensituationElternEntityRequiredConstraintValidator.isValid(gesuchFormular, null), is(false));

        // Mutter Zahl alimente - false
        gesuchFormular.getFamiliensituation().setWerZahltAlimente(Elternschaftsteilung.MUTTER);
        assertThat(familiensituationElternEntityRequiredConstraintValidator.isValid(gesuchFormular, null), is(false));
        // Mutter wegnehmen - true
        elternSet = new HashSet<>();
        elternSet.add(vater);
        gesuchFormular.setElterns(elternSet);
        assertThat(familiensituationElternEntityRequiredConstraintValidator.isValid(gesuchFormular, null), is(true));
        // Mutter Vater wegnehmen - false
        elternSet = new HashSet<>();
        gesuchFormular.setElterns(elternSet);
        assertThat(familiensituationElternEntityRequiredConstraintValidator.isValid(gesuchFormular, null), is(false));

        // Vater Zahl alimente - false
        gesuchFormular.setElterns(elternSet);
        gesuchFormular.getFamiliensituation().setWerZahltAlimente(Elternschaftsteilung.VATER);
        assertThat(familiensituationElternEntityRequiredConstraintValidator.isValid(gesuchFormular, null), is(false));
        // Mutter addieren- true
        elternSet = new HashSet<>();
        elternSet.add(mutter);
        gesuchFormular.setElterns(elternSet);
        assertThat(familiensituationElternEntityRequiredConstraintValidator.isValid(gesuchFormular, null), is(true));

        // Vater addieren - false
        elternSet = new HashSet<>();
        elternSet.add(vater);
        gesuchFormular.setElterns(elternSet);
        assertThat(familiensituationElternEntityRequiredConstraintValidator.isValid(gesuchFormular, null), is(false));
    }
}
