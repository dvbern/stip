package ch.dvbern.stip.test.lebenslauf.entity;

import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItemAusbildungBerufsbezeichnungConstraintValidator;
import ch.dvbern.stip.api.lebenslauf.type.LebenslaufAusbildungsArt;
import ch.dvbern.stip.test.util.TestUtil;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

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
        return lebenslaufAusbildungsArt -> getLebenslaufAusbildungsArtsOfBerufbezeichnung().contains(lebenslaufAusbildungsArt);
    }

    @Test
    void shouldBeValidIfBildungsartNullAndBerufsbezeichnungNull() {
        LebenslaufItem lebenslaufItem = new LebenslaufItem()
            .setBildungsart(null)
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
