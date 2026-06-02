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

package ch.dvbern.stip.integration.steuerdaten.domain.exception;

public class SteuerdatenPortExceptionConstants {
    public static final String INVALID_TOKEN = "steuerdaten.port.invalid.token";
    public static final String SVN_NOT_FOUND = "steuerdaten.port.svn.not.found";
    public static final String STEUERJAHR_NOT_READY = "steuerdaten.port.steuerjahr.not.ready";
    public static final String STEUERJAHR_PRESENT_OR_FUTURE = "steuerdaten.port.steuerjahr.present.or.future";
    public static final String STEUERJAHR_PAST_NOT_FOUND = "steuerdaten.port.steuerjahr.past.not.found";
    public static final String STEUERJAHR_NOT_PARSABLE = "steuerdaten.port.steuerjahr.not.parsable";
    public static final String SERVICE_UNAVAILABLE = "steuerdaten.port.service.unavailable";
}
