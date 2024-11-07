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

package ch.dvbern.stip.berechnung.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.io.Resource;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.event.DMNRuntimeEventListener;
import org.kie.dmn.core.internal.utils.DMNRuntimeBuilder;
import org.kie.dmn.core.internal.utils.DynamicDMNContextBuilder;
import org.kie.internal.io.ResourceFactory;

@ApplicationScoped
@Slf4j
public class DMNService {
    private static final String DMN_BASE_DIR = "dmn";
    private static final String DMN_EXTENSION = ".dmn";

    public DMNResult evaluateModel(final List<Resource> models, final Map<String, Object> context) {
        return evaluateModel(models, context, null);
    }

    public DMNResult evaluateModel(
        final List<Resource> models,
        final Map<String, Object> context,
        final DMNRuntimeEventListener listener
    ) {
        final var runtime = DMNRuntimeBuilder
            .fromDefaults()
            .buildConfiguration()
            .fromResources(models)
            .getOrElseThrow(RuntimeException::new);

        if (listener != null) {
            runtime.addListener(listener);
        }

        final var model = runtime.getModels().get(runtime.getModels().size() - 1);
        final var dynamicContext = new DynamicDMNContextBuilder(runtime.newContext(), model)
            .populateContextWith(context);

        return runtime.evaluateAll(model, dynamicContext);
    }

    public List<Resource> loadModelsForTenantAndVersionByName(
        final String tenantId,
        final String version,
        final String modelName
    ) {
        // String concatenation with "/" is the correct way, since the Java Resource API requires "/" as separator
        final var modelsDirectoryPath = DMN_BASE_DIR + "/" + tenantId + "/" + version;
        final var modelFilePath = modelsDirectoryPath + "/" + modelName + DMN_EXTENSION;
        // When the app is packaged in a jar file the dirs/files are no longer dirs/files so we have to read them as a
        // stream and listing files gets complicated.
        // Since we depend on a specific model being present we just statically construct the path and load it that way.
        final var modelFileStream =
            Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(modelFilePath));

        final var modelResource = ResourceFactory.newInputStreamResource(modelFileStream);
        return List.of(modelResource);
    }
}
