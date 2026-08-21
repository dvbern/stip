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

import java.net.URL;
import java.util.Map;

import ch.dvbern.oss.commons.i18nl10n.AppLanguage;
import ch.dvbern.oss.commons.i18nl10n.I18nMessage;
import ch.dvbern.oss.commons.i18nl10n.Translator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.icu.text.MessageFormat;
import lombok.SneakyThrows;

public class JsonTranslatorStrategy implements Translator {
    public record TranslationFiles(URL de, URL fr) {
    }

    private final AppLanguage appLanguage;
    private final Map<String, String> translations;

    private JsonTranslatorStrategy(AppLanguage appLanguage) {
        this.appLanguage = appLanguage;
        translations = parseTranslationJson();
    }

    private String getTranslationFile() {
        return "/contract/translations/contract.%s.json".formatted(
            appLanguage.locale().getLanguage()
        );
    }

    @Override
    public String translate(I18nMessage message) {
        final var translationKey = message.key().value();
        if (!translations.containsKey(translationKey)) {
            throw new IllegalStateException(
                "Translation key [%s] not found at \"%s\" with lang [%s]".formatted(
                    translationKey,
                    getTranslationFile(),
                    appLanguage.javaLocale().getLanguage()
                )
            );
        }
        return new MessageFormat(
            translations.get(translationKey),
            appLanguage.locale()
        ).format(message.args());
    }

    public static JsonTranslatorStrategy create(AppLanguage appLanguage) {
        return new JsonTranslatorStrategy(appLanguage);
    }

    @SneakyThrows
    private Map<String, String> parseTranslationJson() {
        final var inputStream = getClass().getResourceAsStream(getTranslationFile());
        final var typeReference = new TypeReference<Map<String, String>>() {};
        final var objectMapper = new ObjectMapper();

        return objectMapper.readValue(inputStream, typeReference);
    }
}
