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

import java.util.Objects;

import ch.dvbern.stip.generated.dto.DemoDataErrorDto;

public class DemoDataExceptionMapper {
    private DemoDataExceptionMapper() {}

    public static DemoDataErrorDto toDto(DemoDataImportException exception) {
        DemoDataErrorDto demoDataErrorDto = new DemoDataErrorDto();
        if (exception == null) {
            return demoDataErrorDto;
        }

        demoDataErrorDto.setInternalMessage(exception.getMessage());
        final var errorClass = Objects.requireNonNullElse(exception.getCause(), exception);
        demoDataErrorDto.setErrorClass(errorClass.getClass().getName());

        if (Objects.nonNull(errorClass.getCause())) {
            demoDataErrorDto.setCause(exception.getCause().getMessage());
        }

        return demoDataErrorDto;
    }

    public static DemoDataErrorDto toDto(DemoDataApplyException exception) {
        DemoDataErrorDto demoDataErrorDto = new DemoDataErrorDto();
        if (exception == null) {
            return demoDataErrorDto;
        }

        demoDataErrorDto.setInternalMessage(exception.getMessage());
        final var errorClass = Objects.requireNonNullElse(exception.getCause(), exception);
        demoDataErrorDto.setErrorClass(errorClass.getClass().getName());

        if (Objects.nonNull(exception.getCause())) {
            demoDataErrorDto.setCause(exception.getCause().getMessage());
        }

        demoDataErrorDto.setValidationErrors(exception.validationErrors);

        return demoDataErrorDto;
    }
}
