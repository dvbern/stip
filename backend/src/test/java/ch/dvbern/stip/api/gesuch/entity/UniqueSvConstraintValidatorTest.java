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

import ch.dvbern.stip.api.generator.entities.GesuchGenerator;
import ch.dvbern.stip.api.gesuch.service.GesuchFormularMapper;
import ch.dvbern.stip.api.partner.entity.Partner;
import ch.dvbern.stip.api.util.TestConstants;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
class UniqueSvConstraintValidatorTest {
    UniqueSvNumberConstraintValidator validator =
        new UniqueSvNumberConstraintValidator();

    GesuchFormular gesuchFormular = new GesuchFormular();

    @Inject
    GesuchFormularMapper gesuchFormularMapper;

    @BeforeEach
    void setUp() {
        gesuchFormular = gesuchFormularMapper.partialUpdate(
            GesuchGenerator.createGesuch()
                .getGesuchTrancheToWorkWith()
                .getGesuchFormular(),
            gesuchFormular
        );
    }

    @Test
    void onlyPersonInAusbildung() {
        gesuchFormular.setPartner(null)
            .setElterns(null);

        assertThat(gesuchFormular.getPersonInAusbildung(), notNullValue());
        assertThat(validator.isValid(gesuchFormular, null), is(true));
    }

    @Test
    void personInAusbildungAndPartnerSameSvNumber() {
        gesuchFormular.setPartner(new Partner());
        gesuchFormular.getPartner()
            .setSozialversicherungsnummer(gesuchFormular.getPersonInAusbildung().getSozialversicherungsnummer());

        assertThat(validator.isValid(gesuchFormular, null), is(false));
    }

    @Test
    void personInAusbildungAndElternSameSvNumber() {
        gesuchFormular.getElterns()
            .stream()
            .toList()
            .get(0)
            .setSozialversicherungsnummer(gesuchFormular.getPersonInAusbildung().getSozialversicherungsnummer());

        assertThat(validator.isValid(gesuchFormular, null), is(false));
    }

    @Test
    void elternSameSvNumber() {
        gesuchFormular.getElterns()
            .forEach(eltern -> eltern.setSozialversicherungsnummer(TestConstants.AHV_NUMMER_VALID));
        assertThat(validator.isValid(gesuchFormular, null), is(false));
    }

    @Test
    void formularAllPersonUniqueAhvNumber() {
        gesuchFormular.setPartner(new Partner());
        assertThat(gesuchFormular.getPersonInAusbildung(), notNullValue());
        assertThat(gesuchFormular.getPartner(), notNullValue());
        assertThat(gesuchFormular.getElterns(), notNullValue());
        assertThat(validator.isValid(gesuchFormular, null), is(true));
    }

}
