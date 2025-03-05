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

import java.lang.reflect.InvocationTargetException;

import ch.be.fin.sv.schemas.neskovanp._20190716.stipendienauskunftservice.BusinessFault;
import io.quarkus.security.UnauthorizedException;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.xml.ws.soap.SOAPFaultException;
import lombok.Getter;
import lombok.SneakyThrows;

@Getter
public enum NeskoSteuerdatenError {
    INVALID_TOKEN(
    SOAPFaultException.class, "Invalid access token", UnauthorizedException.class, "Invalid access token"
    ),
    SSVN_NOT_FOUND(
    BusinessFault.class, "Die angefragte Person kann nicht oder nicht eindeutig ermittelt werden",
    NotFoundException.class, "Keine Steuerdaten für die angegebene SSVN gefunden"
    ),
    STEUERJAHR_NOT_READY(
    BusinessFault.class,
    "Die Steuerdaten der angefragten Person können für das angefragte Steuerjahr nicht ermittelt werden",
    NotFoundException.class, "Keine Steuerdaten für das angegebene steuerjahr gefunden"
    ),
    STEUERJAHR_PRESENT_OR_FUTURE(
    BusinessFault.class, "Für die angefragte Person kann in NESKO-VA-NP kein Dossier ermittelt werden",
    BadRequestException.class, "Das angegebene Steuerjahr ist nicht valid"
    );

    private final Class<? extends Exception> exceptionClass;
    private final String errorStringNeedle;
    private final Class<? extends Exception> exceptionToThrow;
    private final String throwString;

    NeskoSteuerdatenError(
    Class<? extends Exception> exceptionClass, String errorStringNeedle,
    Class<? extends Exception> exceptionToThrow,
    String throwString
    ) {
        this.exceptionClass = exceptionClass;
        this.errorStringNeedle = errorStringNeedle;
        this.exceptionToThrow = exceptionToThrow;
        this.throwString = throwString;
    }

    @SneakyThrows
    public static void handleException(Exception e)
    throws NotFoundException, BadRequestException, InternalServerErrorException {
        for (NeskoSteuerdatenError error : NeskoSteuerdatenError.values()) {
            if (e.getClass().equals(error.exceptionClass) && e.getMessage().contains(error.errorStringNeedle)) {
                try {
                    throw error.exceptionToThrow.getConstructor(String.class, Throwable.class)
                        .newInstance(error.throwString, e);
                } catch (
                NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex
                ) {
                    throw new InternalServerErrorException(ex);
                }
            }
        }
    }
}
