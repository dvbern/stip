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

import java.util.List;

import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LebenslaufAusbildungUeberschneidenConstraintValidator
    implements ConstraintValidator<LebenslaufAusbildungUeberschneidenConstraint, GesuchFormular> {
    private String property = "";

    @Override
    public void initialize(LebenslaufAusbildungUeberschneidenConstraint constraintAnnotation) {
        property = constraintAnnotation.property();
    }

    private static boolean isOverlapping(LebenslaufItem a, LebenslaufItem b) {
        return !a.getBis().isBefore(b.getVon()) && !b.getBis().isBefore(a.getVon());
    }

    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext constraintValidatorContext) {
        List<LebenslaufItem> lebenslaufItemList = gesuchFormular.getLebenslaufItems()
            .stream()
            .filter(LebenslaufItem::isAusbildung)
            .toList();

        int n = lebenslaufItemList.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (isOverlapping(lebenslaufItemList.get(i), lebenslaufItemList.get(j))) {
                    return GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
                }
            }
        }
        return true;
    }
}
