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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ch.dvbern.stip.api.gesuchformular.type.LandGueltigFor;
import jakarta.validation.Constraint;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_GESUCH_FORMULAR_LAND_UNGUELTIG;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LandMustBeGueltigConstraintValidator.class)
@Documented
@Repeatable(LandMustBeGueltigConstraint.List.class)
public @interface LandMustBeGueltigConstraint {
    String message() default VALIDATION_GESUCH_FORMULAR_LAND_UNGUELTIG;

    Class<?>[] groups() default {};

    LandGueltigFor landGueltigFor();

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        LandMustBeGueltigConstraint[] value();
    }
}
