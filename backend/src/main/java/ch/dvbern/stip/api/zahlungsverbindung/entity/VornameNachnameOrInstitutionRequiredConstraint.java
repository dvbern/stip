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

package ch.dvbern.stip.api.zahlungsverbindung.entity;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_ZAHLUNGSVERBINDUNG_VORNAME_NACHNAME_OR_INSTITUTION_REQUIRED;

@Target({ ElementType.TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VornameNachnameOrInstitutionRequiredConstraintValidator.class)
@Documented
public @interface VornameNachnameOrInstitutionRequiredConstraint {
    String message() default VALIDATION_ZAHLUNGSVERBINDUNG_VORNAME_NACHNAME_OR_INSTITUTION_REQUIRED;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String property() default "";
}
