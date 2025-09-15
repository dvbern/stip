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

public class ExceptionConstants {
    public static final String NESKO_INVALID_TOKEN = "nesko.invalid.token";
    public static final String NESKO_SSVN_NOT_FOUND = "nesko.ssvn.not.found";
    public static final String NESKO_STEUERJAHR_NOT_READY = "nesko.steuerjahr.not.ready";
    public static final String NESKO_STEUERJAHR_PRESENT_OR_FUTURE = "nesko.steuerjahr.present.or.future";
    public static final String NESKO_STEUERJAHR_PAST_NOT_FOUND = "nesko.steuerjahr.past.not.found";
    public static final String NESKO_STEUERJAHR_NOT_PARSABLE = "nesko.steuerjahr.not.parsable";
    public static final String NESKO_SERVICE_UNAVAILABLE = "nesko.service.unavailable";
}
