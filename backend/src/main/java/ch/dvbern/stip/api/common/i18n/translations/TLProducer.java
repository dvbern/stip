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

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;
import java.util.concurrent.ConcurrentHashMap;

import ch.dvbern.oss.commons.i18nl10n.AppLanguage;
import ch.dvbern.oss.commons.i18nl10n.DefaultTranslatorStrategy;
import ch.dvbern.oss.commons.i18nl10n.Translator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

import static java.lang.Thread.currentThread;
import static java.util.Objects.requireNonNull;

@ApplicationScoped
public class TLProducer {
    private static final String BUNDLE_BASE_NAME = "translations";

    private final ConcurrentHashMap<AppLanguage, ResourceBundle> bundleMap = new ConcurrentHashMap<>();
    private final AppLanguageProvider appLanguageProvider;
    private final String baseName;

    @Inject
    public TLProducer(
    AppLanguageProvider appLanguageProvider
    ) {
        this(
            requireNonNull(appLanguageProvider),
            BUNDLE_BASE_NAME
        );
    }

    TLProducer(
    AppLanguageProvider appLanguageProvider,
    String baseName
    ) {
        this.appLanguageProvider = requireNonNull(appLanguageProvider);
        this.baseName = baseName;
    }

    public static TLProducer defaultBundle() {
        return new TLProducer(() -> AppLanguages.DEFAULT, BUNDLE_BASE_NAME);
    }

    // should not need the @RequestScoped annotation!
    // but since the FromRequestAppLanguageProvider is @RequestScoped
    @Produces
    @RequestScoped
    public TL produceTL() {
        var appLanguage = appLanguageProvider.current();

        return forAppLanguage(appLanguage);
    }

    public TL forAppLanguage(AppLanguage appLanguage) {
        Translator translator = DefaultTranslatorStrategy.createDefault(
            appLanguage,
            this::loadBundle
        );

        return translator::translate;
    }

    ResourceBundle loadBundle(AppLanguage appLanguage) {
        return bundleMap.computeIfAbsent(appLanguage, this::initBundle);
    }

    ResourceBundle initBundle(AppLanguage appLanguage) {
        return ResourceBundle.getBundle(
            baseName,
            appLanguage.javaLocale(),
            requireNonNull(currentThread().getContextClassLoader()),
            new FallbackToBaseBundleControl()
        );
    }

    private static class FallbackToBaseBundleControl extends Control {
        @Override
        public Locale getFallbackLocale(String baseName, Locale locale) {
            if (locale.equals(Locale.ROOT)) {
                throw new IllegalStateException(
                    ("No fallback resource bundle found for baseName: %s. The default bundle-file must be named: "
                    + "%s.properties without any locale suffix")
                        .formatted(baseName, baseName)
                );
            }

            return Locale.ROOT;
        }
    }
}
