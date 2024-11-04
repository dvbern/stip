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

package ch.dvbern.stip.api.config.service;

import java.util.List;
import java.util.Set;

import ch.dvbern.stip.generated.dto.DeploymentConfigDto;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class ConfigService {

    @ConfigProperty(name = "kstip.environment", defaultValue = "local")
    String environment;

    @ConfigProperty(name = "quarkus.application.version")
    String version;

    @Getter
    @ConfigProperty(name = "bucket.name")
    String bucketName;

    @Getter
    @ConfigProperty(name = "kstip.allowed.mimetypes")
    Set<String> allowedMimeTypes;

    @Getter
    @ConfigProperty(name = "kstip.pre-signed-request.issuer")
    String issuer;

    @Getter
    @ConfigProperty(name = "kstip.pre-signed-request.expires-in-minutes")
    Integer expiresInMinutes;

    @Getter
    @ConfigProperty(name = "kstip.pre-signed-request.secret")
    String secret;

    @Getter
    @ConfigProperty(name = "kstip.seeding.seed-on")
    List<String> seedOnProfile;

    @Getter
    @ConfigProperty(name = "kstip.seeding.testcases.seed-on")
    List<String> seedTestcasesOnProfile;

    @Getter
    @ConfigProperty(name = "kstip.seeding.testcases.to-seed")
    List<String> testcasesToSeed;

    @Getter
    @ConfigProperty(name = "kstip.welcome-mail.kc-url")
    String welcomeEmailKcUrl;

    @Getter
    @ConfigProperty(name = "kstip.welcome-mail.kc-path")
    String welcomeEmailKcPath;

    @Getter
    @ConfigProperty(name = "kstip.welcome-mail.kc-query-parameter")
    String welcomeEmailKcQueryParameter;

    @Getter
    @ConfigProperty(name = "kstip.welcome-mail.kc-scope")
    String welcomeEmailKcScope;

    @Getter
    @ConfigProperty(name = "kstip.sap.system-id")
    Integer systemid;

    @Getter
    @ConfigProperty(name = "kstip.dmn.current-major-version")
    Integer currentDmnMajorVersion;

    @Getter
    @ConfigProperty(name = "kstip.dmn.current-minor-version")
    Integer currentDmnMinorVersion;

    @Getter
    @ConfigProperty(name = "kstip.pagination.max-allowed-page-size")
    Integer maxAllowedPageSize;

    public DeploymentConfigDto getDeploymentConfiguration() {
        return new DeploymentConfigDto()
            .version(version)
            .environment(environment)
            .allowedMimeTypes(allowedMimeTypes.stream().toList());
    }

    public String getWelcomeMailURI(String tenant, String redirectURI) {
        return welcomeEmailKcUrl
        + welcomeEmailKcPath.replace("<TENANT>", tenant)
        + welcomeEmailKcQueryParameter.replace("<REDIRECT_URI>", redirectURI)
        + welcomeEmailKcScope;
    }
}
