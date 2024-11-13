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

import ch.dvbern.stip.api.partner.entity.Partner;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
import ch.dvbern.stip.api.util.TestUtil;
import jakarta.validation.constraints.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class PartnerNullRequiredWhenAllinstehendConstraintValidatorTest {
    PartnerNullRequiredWhenAlleinstehendConstraintValidator validator =
        new PartnerNullRequiredWhenAlleinstehendConstraintValidator();

    @NotNull
    private static GesuchFormular preapreGesuchFormularWithZivilstand(
        Zivilstand zivilstand,
        @Nullable Partner partner
    ) {
        GesuchFormular gesuchFormular = new GesuchFormular()
            .setPartner(partner)
            .setPersonInAusbildung(new PersonInAusbildung().setZivilstand(zivilstand));
        return gesuchFormular;
    }

    @Test
    void personInAusbildungLedigGeschiedenAufgeloestOrVerwittwetAndPartnerNotNullShouldNotBeValid() {
        Zivilstand.getZivilstandsNoPartnerschaft().forEach(zivilstand -> {
            GesuchFormular gesuchFormular = preapreGesuchFormularWithZivilstand(zivilstand, new Partner());
            assertThat(validator.isValid(gesuchFormular, null), is(false));
        });
    }

    @Test
    void personInAusbildungLedigGeschiedenAufgeloestOrVerwittwetAndPartnerNullShouldBeValid() {
        Zivilstand.getZivilstandsNoPartnerschaft().forEach(zivilstand -> {
            GesuchFormular gesuchFormular = preapreGesuchFormularWithZivilstand(zivilstand, null);
            assertThat(validator.isValid(gesuchFormular, null), is(true));
        });
    }

    @Test
    void personInAusbildungVerheiratetKonkubinatPartnerschaftAndPartnerNullShouldNotBeValid() {
        Zivilstand.getZivilstandsWithPartnerschaft().forEach(zivilstand -> {
            GesuchFormular gesuchFormular = preapreGesuchFormularWithZivilstand(zivilstand, null);
            assertThat(validator.isValid(gesuchFormular, TestUtil.initValidatorContext()), is(false));
        });
    }

    @Test
    void personInAusbildungVerheiratetKonkubinatPartnerschaftAndPartnerNotNullShouldBeValid() {
        Zivilstand.getZivilstandsWithPartnerschaft().forEach(zivilstand -> {
            GesuchFormular gesuchFormular = preapreGesuchFormularWithZivilstand(zivilstand, new Partner());
            assertThat(validator.isValid(gesuchFormular, null), is(true));
        });
    }

}
