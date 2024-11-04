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

/**
 * Wir definieren unserem eigenen Exception als es ist besser zu unterscheiden die Exception die sind aus Java geworfen
 * mit die die sind bei uns im Code geworfen.
 * Spaeter kann man dieser Klasse mit properties ergänzen zu verbessern den Benutzer Feedback
 */
public class AppErrorException extends RuntimeException {

    public AppErrorException(String message) {
        super(message);
    }
}
