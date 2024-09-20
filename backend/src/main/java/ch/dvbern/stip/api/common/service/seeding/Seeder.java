package ch.dvbern.stip.api.common.service.seeding;

import java.util.Collections;
import java.util.List;

import ch.dvbern.stip.api.tenancy.service.DataTenantResolver;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.runtime.configuration.ConfigUtils;

public abstract class Seeder {
    public static final int DEFAULT_PRIORITY = 1_000;

    protected void seed() {
        final var tenant = getTenant();
        if (!Collections.disjoint(ConfigUtils.getProfiles(), getProfiles())) {
            QuarkusTransaction.requiringNew().run(() -> {
                DataTenantResolver.setTenantId(tenant);
                doSeed();
            });
        }
    }

    /**
     * Returns the priority in which this Seeder should execute. Higher number means executes earlier
     */
    public int getPriority() {
        return DEFAULT_PRIORITY;
    }

    protected abstract void doSeed();

    /**
     * Returns a list of profiles on which to run seeding.
     */
    protected abstract List<String> getProfiles();

    /**
     * Returns the tenant for which the seeding is to be ran
     */
    protected String getTenant() {
        return "bern";
    }
}
