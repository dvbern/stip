package ch.dvbern.stip.api.common.i18n.translations;

import ch.dvbern.oss.commons.i18nl10n.AppLanguage;
import com.ibm.icu.util.ULocale;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.Locale;

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
