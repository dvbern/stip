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

import ch.dvbern.stip.generated.dto.DemoDataErrorDto;
import ch.dvbern.stip.generated.dto.NeskoErrorDto;
import ch.dvbern.stip.generated.dto.ValidationReportDto;
import jakarta.ws.rs.BadRequestException;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

public class ServerExceptionMappers {

    @ServerExceptionMapper
    public RestResponse<ValidationReportDto> mapException(ValidationsException validationsException) {
        return RestResponse.status(
            RestResponse.Status.BAD_REQUEST,
            ValidationsExceptionMapper.toDto(validationsException)
        );
    }

    @ServerExceptionMapper
    public RestResponse<ValidationReportDto> mapException(CustomValidationsException validationsException) {
        return RestResponse.status(
            RestResponse.Status.BAD_REQUEST,
            ValidationsExceptionMapper.toDto(validationsException)
        );
    }

    @ServerExceptionMapper
    public RestResponse<NeskoErrorDto> mapException(NeskoNotFoundException e) {
        return RestResponse.status(
            RestResponse.Status.NOT_FOUND,
            NeskoExceptionMapper.toDto(e)
        );
    }

    @ServerExceptionMapper
    public RestResponse<NeskoErrorDto> mapException(NeskoInternalException e) {
        return RestResponse.status(
            RestResponse.Status.BAD_GATEWAY,
            NeskoExceptionMapper.toDto(e)
        );
    }

    @ServerExceptionMapper
    public RestResponse<DemoDataErrorDto> mapException(DemoDataImportException e) {
        return RestResponse.status(
            RestResponse.Status.BAD_GATEWAY,
            new DemoDataErrorDto(e.getMessage(), e.getCause().getClass().getTypeName(), null)
        );
    }

    @ServerExceptionMapper
    public RestResponse<DemoDataErrorDto> mapException(DemoDataApplyException e) {
        return RestResponse.status(
            RestResponse.Status.BAD_GATEWAY,
            new DemoDataErrorDto(e.getMessage(), e.getClass().getTypeName(), e.validationErrors)
        );
    }

    @ServerExceptionMapper
    public RestResponse<String> mapException(BadRequestException e) {
        return RestResponse.status(RestResponse.Status.BAD_REQUEST, e.getMessage());
    }
}
