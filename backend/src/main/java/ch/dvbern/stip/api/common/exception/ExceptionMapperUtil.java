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

import ch.dvbern.stip.api.common.validation.CustomConstraintViolation;
import ch.dvbern.stip.generated.dto.ValidationMessageDto;
import jakarta.validation.ConstraintViolation;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMapperUtil {
    public ValidationMessageDto toMessageDto(
        final String message,
        final String messageTemplate,
        final String propertyPath
    ) {
        var messageDto = new ValidationMessageDto();
        messageDto.setMessage(message);
        messageDto.setMessageTemplate(messageTemplate);
        messageDto.setPropertyPath(propertyPath);
        return messageDto;
    }

    public ValidationMessageDto toMessageDto(ConstraintViolation<?> constraintViolation) {
        return toMessageDto(
            constraintViolation.getMessage(),
            constraintViolation.getMessageTemplate(),
            constraintViolation.getPropertyPath().toString()
        );
    }

    public ValidationMessageDto toMessageDto(CustomConstraintViolation additionalConstraintViolation) {
        return toMessageDto(
            additionalConstraintViolation.getMessage(),
            additionalConstraintViolation.getMessageTemplate(),
            additionalConstraintViolation.getPropertyPath()
        );
    }

    public ValidationMessageDto toMessageDto(CustomValidationsException validationsException) {
        return toMessageDto(validationsException.getConstraintViolation());
    }
}
