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

package ch.dvbern.stip.api.common.util;

import java.util.Locale;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LocaleUtil {
    public Sprache getKorrespondenzSprache(Gesuch gesuch) {
        return gesuch.getLatestGesuchTranche()
            .getGesuchFormular()
            .getPersonInAusbildung()
            .getKorrespondenzSprache();
    }

    public Sprache getKorrespondenzSprache(GesuchTranche gesuchTranche) {
        return gesuchTranche
            .getGesuchFormular()
            .getPersonInAusbildung()
            .getKorrespondenzSprache();
    }

    public Locale getLocale(Gesuch gesuch) {
        return getKorrespondenzSprache(gesuch).getLocale();
    }

    public Locale getLocale(GesuchTranche gesuchTranche) {
        return getKorrespondenzSprache(gesuchTranche).getLocale();
    }
}
