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

package ch.dvbern.stip.api.darlehen.entity;

import java.time.LocalDate;
import java.util.Objects;

import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DarlehenConstraintValidator
    implements ConstraintValidator<DarlehenValidationConstraint, GesuchFormular> {
    private String property = "";

    @Override
    public void initialize(DarlehenValidationConstraint constraintAnnotation) {
        property = constraintAnnotation.property();
    }

    private static boolean isVolljaehrig(LocalDate geburtsdatum) {
        if (geburtsdatum == null) {
            return false;
        }
        LocalDate volljaehrigCompareDate = LocalDate.now().minusYears(18);
        return geburtsdatum.isBefore(volljaehrigCompareDate) || geburtsdatum.isEqual(volljaehrigCompareDate);
    }

    private boolean isAtLeastOneReasonSelected(Darlehen darlehen) {
        return Objects.nonNull(darlehen.getGrundZweitausbildung()) && darlehen.getGrundZweitausbildung()
        || Objects.nonNull(darlehen.getGrundAusbildungZwoelfJahre()) && darlehen.getGrundAusbildungZwoelfJahre()
        || Objects.nonNull(darlehen.getGrundHoheGebuehren()) && darlehen.getGrundHoheGebuehren()
        || Objects.nonNull(darlehen.getGrundNichtBerechtigt()) && darlehen.getGrundNichtBerechtigt();
    }

    @Override
    public boolean isValid(
        GesuchFormular gesuchFormular,
        ConstraintValidatorContext constraintValidatorContext
    ) {
        final var pia = gesuchFormular.getPersonInAusbildung();
        if (
            Objects.nonNull(gesuchFormular.getDarlehen())
            && gesuchFormular.getDarlehen().getWillDarlehen()
        ) {
            final var darlehen = gesuchFormular.getDarlehen();

            return isVolljaehrig(pia.getGeburtsdatum())
            && isAtLeastOneReasonSelected(darlehen)
            && Objects.nonNull(darlehen.getGrundZweitausbildung())
            && Objects.nonNull(darlehen.getGrundAnschaffungenFuerAusbildung())
            && Objects.nonNull(darlehen.getGrundNichtBerechtigt())
            && Objects.nonNull(darlehen.getGrundHoheGebuehren())
            && Objects.nonNull(darlehen.getGrundAusbildungZwoelfJahre())
            && Objects.nonNull(darlehen.getBetragDarlehen())
            && Objects.nonNull(darlehen.getAnzahlBetreibungen())
            && Objects.nonNull(darlehen.getSchulden())
            && Objects.nonNull(darlehen.getBetragBezogenKanton());
        }
        return true;
    }
}
