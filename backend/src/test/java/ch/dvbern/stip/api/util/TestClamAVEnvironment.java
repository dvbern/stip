package ch.dvbern.stip.api.util;

import java.util.HashMap;
import java.util.Map;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class TestClamAVEnvironment implements QuarkusTestResourceLifecycleManager {
    private final DockerImageName clamAvImageName = DockerImageName.parse("docker-registry.dvbern.ch/dockerhub/clamav/clamav");
    private final GenericContainer<?> clamAv = new GenericContainer<>(clamAvImageName);

    @Override
    public Map<String, String> start() {
        clamAv.withExposedPorts(3310).start();

        Map<String, String> systemProps = new HashMap<>();
        systemProps.put("quarkus.antivirus.clamav.host", clamAv.getHost());
        systemProps.put("quarkus.antivirus.clamav.port", String.valueOf(clamAv.getMappedPort(3310)));
        systemProps.put("quarkus.antivirus.clamav.enabled", "true");
        systemProps.put("quarkus.antivirus.clamav.health.enabled", "true");
        return systemProps;
    }

    @Override
    public void stop() {
        clamAv.stop();
    }
}
