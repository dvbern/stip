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

package ch.dvbern.stip.api.tenancy.service;

import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.config.ConfigProvider;

@ApplicationScoped
@RequiredArgsConstructor
public class TenantConfigService {
    public static final String WELCOME_MAIL_KC_URL_PATH = "kstip.%s.welcome-mail.kc-url";
    public static final String WELCOME_MAIL_KC_PATH_PATH = "kstip.%s.welcome-mail.kc-path";
    public static final String WELCOME_MAIL_KC_QUERY_PARAMETER_PATH = "kstip.%s.welcome-mail.kc-query-parameter";
    public static final String WELCOME_MAIL_KC_EMAIL_SCOPE_PATH = "kstip.%s.welcome-mail.kc-scope";
    public static final String SOZIALDIENST_SEEDING = "kstip.%s.seeding.sozialdienste";
    public static final String FRONTEND_URI = "kstip.%s.frontend-uri";

    private final TenantService tenantService;

    String getWelcomeEmailKcUrl() {
        return forCurrentTenant(WELCOME_MAIL_KC_URL_PATH);
    }

    String getWelcomeEmailKcPath() {
        return forCurrentTenant(WELCOME_MAIL_KC_PATH_PATH);
    }

    String getWelcomeEmailKcQueryParameter() {
        return forCurrentTenant(WELCOME_MAIL_KC_QUERY_PARAMETER_PATH);
    }

    String getWelcomeEmailKcScope() {
        return forCurrentTenant(WELCOME_MAIL_KC_EMAIL_SCOPE_PATH);
    }

    public String getWelcomeMailURI(final String redirectURI) {
        return getWelcomeEmailKcUrl()
        + getWelcomeEmailKcPath().replace("<TENANT>", tenantService.getCurrentTenantIdentifier())
        + getWelcomeEmailKcQueryParameter().replace("<REDIRECT_URI>", redirectURI)
        + getWelcomeEmailKcScope();
    }

    public Optional<String> getSozialdienstSeeding() {
        return forCurrentTenantOpt(SOZIALDIENST_SEEDING);
    }

    private String forCurrentTenant(String path) {
        return ConfigProvider.getConfig()
            .getValue(
                String.format(path, tenantService.getCurrentTenantIdentifier()),
                String.class
            );
    }

    private Optional<String> forCurrentTenantOpt(final String path) {
        return ConfigProvider.getConfig()
            .getOptionalValue(
                String.format(path, tenantService.getCurrentTenantIdentifier()),
                String.class
            );
    }

    public String getFrontendURI() {
        return forCurrentTenant(FRONTEND_URI);
    }
}
