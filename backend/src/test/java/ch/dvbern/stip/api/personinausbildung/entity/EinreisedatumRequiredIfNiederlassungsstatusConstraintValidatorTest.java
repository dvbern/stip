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

package ch.dvbern.stip.api.personinausbildung.entity;

import java.time.LocalDate;

import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class EinreisedatumRequiredIfNiederlassungsstatusConstraintValidatorTest {
    // Removed strange test named foo

    @Test
    void einreisedatumRequiredIfAufenthaltsbewilligung() {
        final var pia = new PersonInAusbildung();
        pia.setNiederlassungsstatus(Niederlassungsstatus.AUFENTHALTSBEWILLIGUNG_B);

        final var validator = new EinreisedatumRequiredIfNiederlassungsstatusConstraintValidator();
        assertThat(validator.isValid(pia, null), is(false));

        pia.setEinreisedatum(LocalDate.now());
        assertThat(validator.isValid(pia, null), is(true));
    }

    @Test
    void einreisedatumNotRequiredIfNiederlassungsstatus() {
        final var pia = new PersonInAusbildung();
        pia.setNiederlassungsstatus(Niederlassungsstatus.NIEDERLASSUNGSBEWILLIGUNG_C);

        final var validator = new EinreisedatumRequiredIfNiederlassungsstatusConstraintValidator();
        assertThat(validator.isValid(pia, null), is(true));
    }

    @Test
    void einreisedatumNotRequiredIfFluechtling() {
        final var pia = new PersonInAusbildung();
        pia.setNiederlassungsstatus(Niederlassungsstatus.FLUECHTLING);

        final var validator = new EinreisedatumRequiredIfNiederlassungsstatusConstraintValidator();
        assertThat(validator.isValid(pia, null), is(true));
    }
}
