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

import ch.dvbern.stip.api.datenschutzbrief.entity.Datenschutzbrief;
import ch.dvbern.stip.api.util.TestUtil;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MaxTwoDatenschutzbriefePerGesuchConstraintValidatorTest {
    private Gesuch gesuch;

    @Test
    public void validateEmptyDatenschutzbriefsIsValid() {
        gesuch = TestUtil.getFullGesuch();
        assertTrue(new MaxTwoDatenschutzbriefePerGesuchConstraintValidator().isValid(gesuch, null));
    }

    @Test
    public void validateDatenschutzbriefsMaxAmountIsValid() {
        gesuch = TestUtil.getFullGesuch();
        gesuch.getDatenschutzbriefs().add(new Datenschutzbrief());
        gesuch.getDatenschutzbriefs().add(new Datenschutzbrief());
        assertTrue(new MaxTwoDatenschutzbriefePerGesuchConstraintValidator().isValid(gesuch, null));
    }

    @Test
    public void validateDatenschutzbriefsMaxAmountPlusOneIsNotValid() {
        gesuch = TestUtil.getFullGesuch();
        gesuch.getDatenschutzbriefs().add(new Datenschutzbrief());
        gesuch.getDatenschutzbriefs().add(new Datenschutzbrief());
        gesuch.getDatenschutzbriefs().add(new Datenschutzbrief());
        assertFalse(new MaxTwoDatenschutzbriefePerGesuchConstraintValidator().isValid(gesuch, null));
    }
}
