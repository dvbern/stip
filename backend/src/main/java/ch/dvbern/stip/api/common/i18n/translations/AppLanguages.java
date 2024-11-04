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

package ch.dvbern.stip.api.common.i18n.translations;

import java.util.Arrays;
import java.util.Locale;

import ch.dvbern.oss.commons.i18nl10n.AppLanguage;
import com.ibm.icu.util.ULocale;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Keep in Sync with config "quarkus.locales"!!!
 */
@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public enum AppLanguages implements AppLanguage {
    DE(ULocale.forLanguageTag("de-CH")),
    FR(ULocale.forLanguageTag("fr-CH"));

    public static final AppLanguage DEFAULT = AppLanguages.DE;

    private final ULocale locale;

    public static AppLanguages fromLocale(Locale locale) {
        return Arrays.stream(AppLanguages.values())
            .filter(appLanguage -> appLanguage.javaLocale().getLanguage().equals(locale.getLanguage()))
            .findFirst()
            .orElseGet(() -> (AppLanguages) AppLanguages.DEFAULT);
    }
}
