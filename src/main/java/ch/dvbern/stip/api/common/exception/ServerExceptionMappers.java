package ch.dvbern.stip.api.common.exception;

import ch.dvbern.stip.generated.dto.ValidationReportDto;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

public class ServerExceptionMappers {

    @ServerExceptionMapper
    public RestResponse<ValidationReportDto> mapException(ValidationsException validationsException) {
        return RestResponse.status(RestResponse.Status.BAD_REQUEST, ValidationsExceptionMapper.toDto(validationsException));
    }

    @ServerExceptionMapper
    public RestResponse<ValidationReportDto> mapException(CustomValidationsException validationsException) {
        return RestResponse.status(RestResponse.Status.BAD_REQUEST, CustomValidationsExceptionMapper.toDto(validationsException));
    }
}
