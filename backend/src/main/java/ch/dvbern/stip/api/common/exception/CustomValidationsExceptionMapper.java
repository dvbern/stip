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

package ch.dvbern.stip.api.common.exception;

import java.util.List;

import ch.dvbern.stip.generated.dto.ValidationErrorDto;
import ch.dvbern.stip.generated.dto.ValidationReportDto;

public final class CustomValidationsExceptionMapper {

    private CustomValidationsExceptionMapper() {}

    public static ValidationReportDto toDto(CustomValidationsException validationsException) {
        ValidationReportDto validationsReportDto = new ValidationReportDto();
        if (validationsException == null) {
            return validationsReportDto;
        }

        ValidationErrorDto validationErrorDto = new ValidationErrorDto();
        validationErrorDto.setMessage(validationsException.getConstraintViolation().getMessage());
        validationErrorDto.setMessageTemplate(validationsException.getConstraintViolation().getMessageTemplate());
        validationErrorDto.setPropertyPath(validationsException.getConstraintViolation().getPropertyPath());
        validationsReportDto.setValidationErrors(List.of(validationErrorDto));

        return validationsReportDto;
    }
}
