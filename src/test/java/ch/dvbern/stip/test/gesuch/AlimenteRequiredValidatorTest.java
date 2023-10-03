package ch.dvbern.stip.test.gesuch;

import ch.dvbern.stip.api.gesuch.entity.AlimenteRequiredWhenAlimenteRegelungConstraintValidator;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.test.util.GesuchGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class AlimenteRequiredValidatorTest {

    private final AlimenteRequiredWhenAlimenteRegelungConstraintValidator validator =
            new AlimenteRequiredWhenAlimenteRegelungConstraintValidator();

    @Test
    void noAlimenteRegelungNoAlimente() {
        GesuchFormular gesuchFormular = GesuchGenerator.createGesuch().getGesuchFormularToWorkWith();
        gesuchFormular.getEinnahmenKosten().setAlimente(null);
        gesuchFormular.getFamiliensituation().setGerichtlicheAlimentenregelung(false);

        assertThat(validator.isValid(gesuchFormular, null)).isTrue();
    }

    @Test
    void alimenteRegelungNoAlimenteViolation() {
        GesuchFormular gesuchFormular = GesuchGenerator.createGesuch().getGesuchFormularToWorkWith();
        gesuchFormular.getEinnahmenKosten().setAlimente(null);
        gesuchFormular.getFamiliensituation().setGerichtlicheAlimentenregelung(true);


        assertThat(validator.isValid(gesuchFormular, null)).isFalse();
    }

    @Test
    void alimenteRegelungAlimenteNoViolation() {
        GesuchFormular gesuchFormular = GesuchGenerator.createGesuch().getGesuchFormularToWorkWith();
        gesuchFormular.getEinnahmenKosten().setAlimente(BigDecimal.ONE);
        gesuchFormular.getFamiliensituation().setGerichtlicheAlimentenregelung(true);

        assertThat(validator.isValid(gesuchFormular, null)).isTrue();
    }

    @Test
    void noAlimenteRegelungAlimenteViolation() {
        GesuchFormular gesuchFormular = GesuchGenerator.createGesuch().getGesuchFormularToWorkWith();
        gesuchFormular.getEinnahmenKosten().setAlimente(BigDecimal.ONE);
        gesuchFormular.getFamiliensituation().setGerichtlicheAlimentenregelung(false);

        assertThat(validator.isValid(gesuchFormular, null)).isFalse();
    }


}
