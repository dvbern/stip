package ch.dvbern.stip.api.util;

import java.util.HashMap;
import java.util.Map;

import io.quarkus.logging.Log;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class TestDatabaseEnvironment implements QuarkusTestResourceLifecycleManager {

    private final JdbcDatabaseContainer postgres =
        new PostgreSQLContainer<>(DockerImageName.parse("docker-registry.dvbern.ch/dockerhub/library/postgres:15.3")
            .asCompatibleSubstituteFor("postgres"));

    @Override
    public Map<String, String> start() {
        String dbName = "stip";
        String dbUser = "stip";
        String dbPassword = "stip";

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
        Log.info("TestDatabaseEnvironment start \n"
            + " - url: '" + postgres.getJdbcUrl() + "'\n"
            + " - user: '" + dbUser + "'\n"
            + " - password: '" + dbPassword + "'"
        );

        return systemProps;
    }

    @Override
    public void stop() {
        postgres.stop();
    }
}
