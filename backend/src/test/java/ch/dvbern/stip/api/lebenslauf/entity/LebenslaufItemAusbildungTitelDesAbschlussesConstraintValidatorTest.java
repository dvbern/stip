package ch.dvbern.stip.api.lebenslauf.entity;

import java.util.Arrays;
import java.util.function.Predicate;

import ch.dvbern.stip.api.lebenslauf.type.LebenslaufAusbildungsArt;
import ch.dvbern.stip.api.util.TestUtil;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_LEBENSLAUFITEM_AUSBILDUNG_TITEL_DES_ABSCHLUSSES_NULL_MESSAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class LebenslaufItemAusbildungTitelDesAbschlussesConstraintValidatorTest {

    LebenslaufItemAusbildungTitelDesAbschlussesConstraintValidator validator =
        new LebenslaufItemAusbildungTitelDesAbschlussesConstraintValidator();

    @NotNull
    private static Predicate<LebenslaufAusbildungsArt> isLebenslaufAusbildungsArtNotAndererBildungsabschluss() {
        return lebenslaufAusbildungsArt ->
            lebenslaufAusbildungsArt != LebenslaufAusbildungsArt.ANDERER_BILDUNGSABSCHLUSS;
    }

    @Test
    void shouldBeValidIfBildungsartNullAndTitelDesAbschlussesNull() {
        LebenslaufItem lebenslaufItem = new LebenslaufItem()
            .setBildungsart(null)
            .setTitelDesAbschlusses(null);

        assertThat(validator.isValid(lebenslaufItem, TestUtil.initValidatorContext()), is(true));
    }

    @Test
    void shouldNotBeValidIfBildungsartAndererAusbildungsabschlussAndTitelDesAbschlussesNull() {
        LebenslaufItem lebenslaufItem = new LebenslaufItem()
            .setBildungsart(LebenslaufAusbildungsArt.ANDERER_BILDUNGSABSCHLUSS)
            .setTitelDesAbschlusses(null);
        assertThat(validator.isValid(lebenslaufItem, TestUtil.initValidatorContext()), is(false));
    }

    @Test
    void shouldBeValidIfBildungsartAndererAusbildungsabschlussAndTitelDesAbschlussesNotNull() {
        LebenslaufItem lebenslaufItem = new LebenslaufItem()
            .setBildungsart(LebenslaufAusbildungsArt.ANDERER_BILDUNGSABSCHLUSS)
            .setTitelDesAbschlusses("Fachrichtung");
        assertThat(validator.isValid(lebenslaufItem, TestUtil.initValidatorContext()), is(true));
    }

    @Test
    void shouldNotBeValidIfBildungsartOtherThanAndererAusbildungsabschlussAndTitelDesAbschlussesNotNull() {
        Arrays.stream(LebenslaufAusbildungsArt.values())
            .filter(isLebenslaufAusbildungsArtNotAndererBildungsabschluss())
            .forEach(bildungsart -> {
                var context = TestUtil.initValidatorContext();
                LebenslaufItem lebenslaufItem = new LebenslaufItem()
                    .setBildungsart(bildungsart)
                    .setTitelDesAbschlusses("Fachrichtung");
                assertThat(validator.isValid(lebenslaufItem, context), is(false));
                assertThat(context.getConstraintViolationCreationContexts().size(), is(1));
                assertThat(
                    context.getConstraintViolationCreationContexts().get(0).getMessage(),
                    is(VALIDATION_LEBENSLAUFITEM_AUSBILDUNG_TITEL_DES_ABSCHLUSSES_NULL_MESSAGE)
                );
            });
    }

    @Test
    void shouldBeValidIfBildungsartOtherThanAndererAusbildungsabschlussAndTitelDesAbschlussesNull() {
        Arrays.stream(LebenslaufAusbildungsArt.values())
            .filter(isLebenslaufAusbildungsArtNotAndererBildungsabschluss())
            .forEach(bildungsart -> {
                LebenslaufItem lebenslaufItem = new LebenslaufItem()
                    .setBildungsart(bildungsart)
                    .setTitelDesAbschlusses(null);
                assertThat(validator.isValid(lebenslaufItem, TestUtil.initValidatorContext()), is(true));
            });
    }
}
