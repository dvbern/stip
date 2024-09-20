package ch.dvbern.stip.api.common.service.seeding;

import java.util.Comparator;

import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class SeedingExecutor {
    private final Instance<Seeder> seeders;

    @Startup
    public void execute() {
        LOG.info("SeedingExecutor starting execution");
        seeders.stream().sorted(Comparator.comparing(Seeder::getPriority).reversed()).forEach(Seeder::seed);
        LOG.info("SeedingExecutor finished execution");
    }
}
