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

import ch.dvbern.stip.api.generator.entities.GesuchGenerator;
import ch.dvbern.stip.api.gesuchformular.service.GesuchFormularMapper;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDto;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class AlimenteRequiredValidatorTest {

    private final AlimenteRequiredWhenAlimenteRegelungConstraintValidator validator =
        new AlimenteRequiredWhenAlimenteRegelungConstraintValidator();
    @Inject
    GesuchFormularMapper gesuchFormularMapper;

    @Test
    void noAlimenteRegelungNoAlimente() {
        GesuchFormular gesuchFormular =
            gesuchFormularMapper.partialUpdate(createGesuchFormularUpdateDto(), createGesuchFormular());
        gesuchFormular.getEinnahmenKosten().setAlimente(null);
        gesuchFormular.getFamiliensituation().setGerichtlicheAlimentenregelung(false);

        assertThat(validator.isValid(gesuchFormular, null)).isTrue();
    }

    @Test
    void alimenteRegelungNoAlimenteViolation() {
        GesuchFormular gesuchFormular =
            gesuchFormularMapper.partialUpdate(createGesuchFormularUpdateDto(), createGesuchFormular());
        gesuchFormular.getEinnahmenKosten().setAlimente(null);
        gesuchFormular.getFamiliensituation().setGerichtlicheAlimentenregelung(true);

        assertThat(validator.isValid(gesuchFormular, null)).isFalse();
    }

    @Test
    void alimenteRegelungAlimenteNoViolation() {
        GesuchFormular gesuchFormular =
            gesuchFormularMapper.partialUpdate(createGesuchFormularUpdateDto(), createGesuchFormular());
        gesuchFormular.getEinnahmenKosten().setAlimente(1);
        gesuchFormular.getFamiliensituation().setGerichtlicheAlimentenregelung(true);

        assertThat(validator.isValid(gesuchFormular, null)).isTrue();
    }

    @Test
    void noAlimenteRegelungAlimenteViolation() {
        GesuchFormular gesuchFormular =
            gesuchFormularMapper.partialUpdate(createGesuchFormularUpdateDto(), createGesuchFormular());
        gesuchFormular.getEinnahmenKosten().setAlimente(1);
        gesuchFormular.getFamiliensituation().setGerichtlicheAlimentenregelung(false);

        assertThat(validator.isValid(gesuchFormular, TestUtil.initValidatorContext())).isFalse();
    }

    private GesuchFormular createGesuchFormular() {
        return new GesuchFormular().setTranche(new GesuchTranche().setTyp(GesuchTrancheTyp.TRANCHE));
    }

    private GesuchFormularUpdateDto createGesuchFormularUpdateDto() {
        return GesuchGenerator.createGesuch().getGesuchTrancheToWorkWith().getGesuchFormular();
    }
}
