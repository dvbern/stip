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

package ch.dvbern.stip.api.notification.service;

import java.util.Map;

import ch.dvbern.stip.api.common.type.Anrede;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import lombok.experimental.UtilityClass;

@UtilityClass
public class NotificationTemplateUtils {
    private static final Map<Sprache, String> ANREDE_FRAU_MAP = Map.of(
        Sprache.FRANZOESISCH,
        "Chère Madame",
        Sprache.DEUTSCH,
        "Sehr geehrte Frau"
    );
    private static final Map<Sprache, String> ANREDE_HERR_MAP = Map.of(
        Sprache.FRANZOESISCH,
        "Cher Monsieur",
        Sprache.DEUTSCH,
        "Sehr geehrter Herr"
    );

    public String getAnredeText(Anrede anrede, Sprache korrespondenzSprache) {
        switch (anrede) {
            case FRAU:
                return ANREDE_FRAU_MAP.get(korrespondenzSprache);
            default:
                return ANREDE_HERR_MAP.get(korrespondenzSprache);
        }
    }
}
