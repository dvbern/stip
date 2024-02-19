package ch.dvbern.stip.test.util;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class TestDatabaseEnvironment implements QuarkusTestResourceLifecycleManager {


    private final JdbcDatabaseContainer postgres =
            new PostgreSQLContainer<>(DockerImageName.parse("docker-registry.dvbern.ch/dockerhub/library/postgres:15.3")
                    .asCompatibleSubstituteFor("postgres"));

    @Override
    public Map<String, String> start() {
        String dbName = "stip";
        String dbUser = "stip";
        String dbPassword = UUID.randomUUID().toString();

        postgres
                .withDatabaseName(dbName)
                .withUsername(dbUser)
                .withPassword(dbPassword)
                .withEnv("TZ", "Europe/Zurich")
                .start();

        Map<String, String> systemProps = new HashMap<>();
        systemProps.put("quarkus.datasource.jdbc.url", postgres.getJdbcUrl());
        systemProps.put("quarkus.datasource.username", dbUser);
        systemProps.put("quarkus.datasource.password", dbPassword);

        return systemProps;
    }

    @Override
    public void stop() {
        postgres.stop();
    }
}
