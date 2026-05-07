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

package ch.dvbern.stip.api.config;

import java.util.Map;
import java.util.Set;

import ch.dvbern.stip.api.common.type.TenantIdentifier;
import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@StaticInitSafe
@ConfigMapping(prefix = "kstip")
public interface StipConfig {

    @WithDefault("0.1-SNAPSHOT")
    String version();

    @WithDefault("local")
    String environment();

    @WithDefault("bern")
    String defaultTenant();

    OIDC oidc();

    S3 s3();

    Upload upload();

    Pagination pagination();

    Demo demo();

    PreSignedRequest preSignedRequest();

    Seeding seeding();

    Map<String, SchedulerConfig> scheduler();

    PlzData plzData();

    Map<TenantIdentifier, TenantConfig> tenant();

    interface OIDC {
        String url();

        String frontendUrl();

        @WithDefault("stip-api")
        String clientId();
    }

    interface S3 {
        String bucketName();
    }

    interface Upload {
        @WithDefault("image/tiff,image/jpeg,image/png,application/pdf")
        Set<String> allowedMimetypes();

        @WithDefault("application/x-tika-ooxml,application/zip")
        Set<String> allowedTestcaseMimetypes();
    }

    interface Pagination {
        @WithDefault("50")
        int maxAllowedPageSize();
    }

    interface Demo {
        @WithDefault("iVBORw0KGgoAAAANSUhEUgAAAAEAAAABAQAAAAA3bvkkAAAACklEQVR4AWNgAAAAAgABc3UBGAAAAABJRU5ErkJggg==")
        String smallestPng();
    }

    interface PreSignedRequest {
        @WithDefault("https://stip.kibon.ch")
        String issuer();

        @WithDefault("1")
        int expiresInMinutes();

        String secret();
    }

    interface Seeding {
        @WithDefault("dev")
        Set<String> seedOnProfile();
    }

    interface PlzData {
        @WithDefault("ortschaftenverzeichnis_plz_2056.csv.zip")
        String featureKey();

        @WithDefault("checksum:multihash")
        String hashKey();
    }
}
