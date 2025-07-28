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

package ch.dvbern.stip.api.lebenslauf.entity;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import ch.dvbern.stip.api.lebenslauf.type.LebenslaufAusbildungsArt;
import ch.dvbern.stip.api.util.TestUtil;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_LEBENSLAUFITEM_AUSBILDUNG_BERUFSBEZEICHNUNG_NULL_MESSAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class LebenslaufItemAusbildungBerufsbezeichnungConstraintValidatorTest {

    LebenslaufItemAusbildungBerufsbezeichnungConstraintValidator validator =
        new LebenslaufItemAusbildungBerufsbezeichnungConstraintValidator();

    @NotNull
    private static List<LebenslaufAusbildungsArt> getLebenslaufAusbildungsArtsOfBerufbezeichnung() {
        return List.of(
            LebenslaufAusbildungsArt.EIDGENOESSISCHES_BERUFSATTEST,
            LebenslaufAusbildungsArt.EIDGENOESSISCHES_FAEHIGKEITSZEUGNIS
        );
    }

    @NotNull
    private static Predicate<LebenslaufAusbildungsArt> isLebenslaufAusbildungsArtOfBerufsbezeichnung() {
        return lebenslaufAusbildungsArt -> getLebenslaufAusbildungsArtsOfBerufbezeichnung().contains(
            lebenslaufAusbildungsArt
        );
    }

    @Test
    void shouldBeValidIfBildungsartNullAndBerufsbezeichnungNull() {
        LebenslaufItem lebenslaufItem = new LebenslaufItem()
            .setAbschluss(null)
            .setBerufsbezeichnung(null);

        assertThat(validator.isValid(lebenslaufItem, TestUtil.initValidatorContext()), is(true));
    }

    @Test
    void shouldNotBeValidIfBildungsartEidgBerufsattestOrEidgFaehigkeitszeugnisAndBerufsbezeichnungNull() {
        getLebenslaufAusbildungsArtsOfBerufbezeichnung().forEach(bildungsart -> {
            LebenslaufItem lebenslaufItem = new LebenslaufItem()
                .setBildungsart(bildungsart)
                .setBerufsbezeichnung(null);
            assertThat(validator.isValid(lebenslaufItem, null), is(false));
        });
    }

    @Test
    void shouldBeValidIfBildungsartEidgBerufsattestOrEidgFaehigkeitszeugnisAndBerufsbezeichnungNotNull() {
        getLebenslaufAusbildungsArtsOfBerufbezeichnung().forEach(bildungsart -> {
            LebenslaufItem lebenslaufItem = new LebenslaufItem()
                .setBildungsart(bildungsart)
                .setBerufsbezeichnung("Berufsbezeichnung");
            assertThat(validator.isValid(lebenslaufItem, TestUtil.initValidatorContext()), is(true));
        });
    }

    @Test
    void shouldNotBeValidIfBildungsartOtherThanEidgBerufsattestOrEidgFaehigkeitszeugnisAndBerufsbezeichnungNotNull() {
        Arrays.stream(LebenslaufAusbildungsArt.values())
            .filter(isLebenslaufAusbildungsArtOfBerufsbezeichnung().negate())
            .forEach(bildungsartenOtherThanEidgBerufsattestOrEidgFaehigkeitszeugnis -> {
                var context = TestUtil.initValidatorContext();
                LebenslaufItem lebenslaufItem = new LebenslaufItem()
                    .setBildungsart(bildungsartenOtherThanEidgBerufsattestOrEidgFaehigkeitszeugnis)
                    .setBerufsbezeichnung("Berufsbezeichnung");
                assertThat(validator.isValid(lebenslaufItem, context), is(false));
                assertThat(context.getConstraintViolationCreationContexts().size(), is(1));
                assertThat(
                    context.getConstraintViolationCreationContexts().get(0).getMessage(),
                    is(VALIDATION_LEBENSLAUFITEM_AUSBILDUNG_BERUFSBEZEICHNUNG_NULL_MESSAGE)
                );
            });
    }

    @Test
    void shouldBeValidIfBildungsartOtherThanEidgBerufsattestOrEidgFaehigkeitszeugnisAndBerufsbezeichnungNull() {
        Arrays.stream(LebenslaufAusbildungsArt.values())
            .filter(isLebenslaufAusbildungsArtOfBerufsbezeichnung().negate())
            .forEach(bildungsartenOtherThanEidgBerufsattestOrEidgFaehigkeitszeugnis -> {
                LebenslaufItem lebenslaufItem = new LebenslaufItem()
                    .setBildungsart(bildungsartenOtherThanEidgBerufsattestOrEidgFaehigkeitszeugnis)
                    .setBerufsbezeichnung(null);
                assertThat(validator.isValid(lebenslaufItem, TestUtil.initValidatorContext()), is(true));
            });
    }
}
