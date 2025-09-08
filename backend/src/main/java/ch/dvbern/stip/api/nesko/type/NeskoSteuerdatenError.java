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

package ch.dvbern.stip.api.nesko.type;

import java.util.function.BiFunction;

import ch.dvbern.stip.api.common.exception.ExceptionConstants;
import ch.dvbern.stip.api.common.exception.NeskoInternalException;
import ch.dvbern.stip.api.common.exception.NeskoNotFoundException;
import ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice.BusinessFault;
import io.quarkus.logging.Log;
import jakarta.xml.ws.WebServiceException;
import jakarta.xml.ws.soap.SOAPFaultException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Getter
@RequiredArgsConstructor
public enum NeskoSteuerdatenError {
    INVALID_TOKEN(
    SOAPFaultException.class, "Invalid access token",
    (e, faultCode) -> new NeskoInternalException(ExceptionConstants.NESKO_INVALID_TOKEN, faultCode)
    ),
    SSVN_NOT_FOUND(
    BusinessFault.class, "Die angefragte Person kann nicht oder nicht eindeutig ermittelt werden",
    (e, faultCode) -> new NeskoNotFoundException(ExceptionConstants.NESKO_SSVN_NOT_FOUND, faultCode)
    ),
    STEUERJAHR_NOT_READY(
    BusinessFault.class,
    "Die Steuerdaten der angefragten Person können für das angefragte Steuerjahr nicht ermittelt werden",
    (e, faultCode) -> new NeskoNotFoundException(ExceptionConstants.NESKO_STEUERJAHR_NOT_READY, faultCode)
    ),
    STEUERJAHR_PRESENT_OR_FUTURE(
    BusinessFault.class, "Für die angefragte Person kann in NESKO-VA-NP kein Dossier ermittelt werden",
    (e, faultCode) -> new NeskoNotFoundException(ExceptionConstants.NESKO_STEUERJAHR_PRESENT_OR_FUTURE, faultCode)
    ),
    STEUERJAHR_PAST_NOT_FOUND(
    BusinessFault.class, "Für die angefragte Person kann im Register keine Haushaltstruktur ermittelt werden",
    (e, faultCode) -> new NeskoNotFoundException(ExceptionConstants.NESKO_STEUERJAHR_PAST_NOT_FOUND, faultCode)
    ),
    STEUERJAHR_NOT_PARSEABLE(
    SOAPFaultException.class, "is not facet-valid with respect to pattern '[2-9][0-9][0-9][0-9]'",
    (e, faultCode) -> new NeskoNotFoundException(ExceptionConstants.NESKO_STEUERJAHR_NOT_PARSABLE, faultCode)
    ),
    NESKO_SERVICE_UNAVAILABLE(
    WebServiceException.class, "",
    (e, faultCode) -> new NeskoInternalException(ExceptionConstants.NESKO_SERVICE_UNAVAILABLE, faultCode)
    ),
    ;

    private final Class<? extends Exception> exceptionClass;
    private final String errorStringNeedle;
    private final BiFunction<Exception, String, Exception> exceptionSupplier;

    @SneakyThrows
    public static void handleException(Exception e) {
        for (NeskoSteuerdatenError error : NeskoSteuerdatenError.values()) {
            if (e.getClass().equals(error.exceptionClass) && e.getMessage().contains(error.errorStringNeedle)) {
                throw error.exceptionSupplier.apply(e, ((BusinessFault) e).getFaultInfo().getErrorCode());
            }
        }
        Log.error(e.getMessage(), e);
    }
}
