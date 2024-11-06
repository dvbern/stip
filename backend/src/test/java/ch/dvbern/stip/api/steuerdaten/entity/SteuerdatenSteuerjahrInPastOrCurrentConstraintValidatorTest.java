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

package ch.dvbern.stip.api.steuerdaten.entity;

import java.util.LinkedHashSet;

import ch.dvbern.stip.api.generator.entities.GesuchGenerator;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SteuerdatenSteuerjahrInPastOrCurrentConstraintValidatorTest {

    @Test
    void steuerjahrIsCurrentorPastValidationTest() {
        GesuchTranche tranche = GesuchGenerator.initGesuchTranche();
        tranche.setGesuchFormular(new GesuchFormular());
        GesuchFormular gesuchFormular = tranche.getGesuchFormular();
        gesuchFormular.setTranche(tranche);
        gesuchFormular.setSteuerdaten(new LinkedHashSet<>());
        Steuerdaten steuerdaten = new Steuerdaten();
        steuerdaten.setSteuerjahr(tranche.getGesuch().getGesuchsperiode().getGesuchsjahr().getTechnischesJahr() + 1);
        steuerdaten.setSteuerdatenTyp(SteuerdatenTyp.FAMILIE);
        gesuchFormular.getSteuerdaten().add(steuerdaten);

        final var temporalValidator = new SteuerdatenSteuerjahrInPastOrCurrentConstraintValidator();
        assertThat(temporalValidator.isValid(gesuchFormular, null)).isFalse();

        steuerdaten.setSteuerjahr(tranche.getGesuch().getGesuchsperiode().getGesuchsjahr().getTechnischesJahr() - 1);
        assertThat(temporalValidator.isValid(gesuchFormular, null)).isTrue();

    }
}
