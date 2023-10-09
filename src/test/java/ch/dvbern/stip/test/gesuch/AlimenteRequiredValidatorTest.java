package ch.dvbern.stip.test.gesuch;

import ch.dvbern.stip.api.gesuch.entity.AlimenteRequiredWhenAlimenteRegelungConstraintValidator;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.service.GesuchFormularMapper;
import ch.dvbern.stip.test.generator.entities.GesuchGenerator;
import ch.dvbern.stip.test.util.TestUtil;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class AlimenteRequiredValidatorTest {

    @Inject
    GesuchFormularMapper gesuchFormularMapper;

    private final AlimenteRequiredWhenAlimenteRegelungConstraintValidator validator =
            new AlimenteRequiredWhenAlimenteRegelungConstraintValidator();

    @Test
    void noAlimenteRegelungNoAlimente() {
        GesuchFormular gesuchFormular = gesuchFormularMapper.partialUpdate(GesuchGenerator.createGesuch().getGesuchFormularToWorkWith(), new GesuchFormular());
        gesuchFormular.getEinnahmenKosten().setAlimente(null);
        gesuchFormular.getFamiliensituation().setGerichtlicheAlimentenregelung(false);

        assertThat(validator.isValid(gesuchFormular, null)).isTrue();
    }

    @Test
    void alimenteRegelungNoAlimenteViolation() {
        GesuchFormular gesuchFormular = gesuchFormularMapper.partialUpdate(GesuchGenerator.createGesuch().getGesuchFormularToWorkWith(), new GesuchFormular());
        gesuchFormular.getEinnahmenKosten().setAlimente(null);
        gesuchFormular.getFamiliensituation().setGerichtlicheAlimentenregelung(true);


        assertThat(validator.isValid(gesuchFormular, null)).isFalse();
    }

    @Test
    void alimenteRegelungAlimenteNoViolation() {
        GesuchFormular gesuchFormular = gesuchFormularMapper.partialUpdate(GesuchGenerator.createGesuch().getGesuchFormularToWorkWith(), new GesuchFormular());
        gesuchFormular.getEinnahmenKosten().setAlimente(BigDecimal.ONE);
        gesuchFormular.getFamiliensituation().setGerichtlicheAlimentenregelung(true);

        assertThat(validator.isValid(gesuchFormular, null)).isTrue();
    }

    @Test
    void noAlimenteRegelungAlimenteViolation() {
        GesuchFormular gesuchFormular = gesuchFormularMapper.partialUpdate(GesuchGenerator.createGesuch().getGesuchFormularToWorkWith(), new GesuchFormular());
        gesuchFormular.getEinnahmenKosten().setAlimente(BigDecimal.ONE);
        gesuchFormular.getFamiliensituation().setGerichtlicheAlimentenregelung(false);

        assertThat(validator.isValid(gesuchFormular, TestUtil.initValidatorContext())).isFalse();
    }
}
