package ch.dvbern.stip.berechnung.service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ch.dvbern.stip.api.common.exception.AppErrorException;
import jakarta.enterprise.context.ApplicationScoped;
import org.kie.api.io.Resource;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.event.DMNRuntimeEventListener;
import org.kie.dmn.core.internal.utils.DMNRuntimeBuilder;
import org.kie.dmn.core.internal.utils.DynamicDMNContextBuilder;
import org.kie.internal.io.ResourceFactory;

@ApplicationScoped
public class DMNService {
    private static final String DMN_BASE_DIR = "dmn";
    private static final String DMN_EXTENSION = ".dmn";

    public DMNResult evaluateModel(final List<Resource> models, final Map<String, Object> context) {
        return evaluateModel(models, context, null);
    }

    public DMNResult evaluateModel(final List<Resource> models, final Map<String, Object> context, DMNRuntimeEventListener listener) {
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

    public List<Resource> loadModelsForTenantAndVersion(final String tenantId, final String version) {
        // String concatenation with "/" is the correct way, since the Java Resource API requires "/" as separator
        final var modelsDirectoryPath = DMN_BASE_DIR + "/" + tenantId + "/" + version;
        final var modelsDirectory = new File(
            Objects.requireNonNull(getClass().getClassLoader().getResource(modelsDirectoryPath)).getFile()
        );

        if (modelsDirectory.isDirectory()) {
            final var models = modelsDirectory.listFiles(path -> path.getName().toLowerCase().endsWith(DMN_EXTENSION));
            if (models != null && models.length > 0) {
                return Arrays.stream(models).map(ResourceFactory::newFileResource).toList();
            }
        }

        throw new AppErrorException("DMN model(s) not available");
    }

    public List<Resource> loadModelsForTenantAndVersionByName(final String tenantId, final String version, final String modelName) {
        // String concatenation with "/" is the correct way, since the Java Resource API requires "/" as separator
        final var modelsDirectoryPath = DMN_BASE_DIR + "/" + tenantId + "/" + version;
        final var modelsDirectory = new File(
            Objects.requireNonNull(getClass().getClassLoader().getResource(modelsDirectoryPath)).getFile()
        );

        if (modelsDirectory.isDirectory()) {
            final var models = modelsDirectory.listFiles(
                path -> path.getName().toLowerCase().endsWith(DMN_EXTENSION) &&
                        path.getName().contains(modelName)
            );

            if (models != null && models.length > 0) {
                return Arrays.stream(models).map(ResourceFactory::newFileResource).toList();
            }
        }

        throw new AppErrorException("DMN model(s) not available");
    }
}
