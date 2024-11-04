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

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import ch.dvbern.oss.commons.i18nl10n.AppLanguage;
import ch.dvbern.oss.commons.i18nl10n.HttpAcceptLanguageHeaderParser;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.ext.Provider;
import lombok.Getter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Provider
@PreMatching
@RequestScoped
public class HttpAcceptLanguageRequestFilter implements ContainerRequestFilter {
    private static final String ACCEPT_LANGUAGE = "Accept-Language";

    @Getter
    private Optional<AppLanguage> appLanguage = Optional.empty();

    private final Function<@Nullable String, AppLanguage> headerParser;

    public HttpAcceptLanguageRequestFilter() {
        var parser = HttpAcceptLanguageHeaderParser.forAppLanguages(
            Set.of(AppLanguages.values()),
            AppLanguages.DEFAULT
        );
        this.headerParser = parser::parse;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        var acceptLanguageHeader = requestContext.getHeaderString(ACCEPT_LANGUAGE);

        appLanguage = Optional.of(headerParser.apply(acceptLanguageHeader));
    }
}
