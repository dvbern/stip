package ch.dvbern.stip.api.common.service.seeding;

import java.util.List;

import ch.dvbern.stip.api.tenancy.service.DataTenantResolver;
import io.quarkus.narayana.jta.QuarkusTransaction;
import org.checkerframework.dataflow.qual.Pure;

public abstract class Seeder {
    protected void seed() {
        final var tenant = getTenant();
        // TODO KSTIP-1004
//        if (!Collections.disjoint(ConfigUtils.getProfiles(), getProfiles())) {
            QuarkusTransaction.requiringNew().run(() -> {
                DataTenantResolver.setTenantId(tenant);
                doSeed();
            });
//        }
    }

    public abstract void startup();

    protected abstract void doSeed();

    /**
     * Returns a list of profiles on which to run seeding.
     * */
    @Pure
    protected List<String> getProfiles() {
        return List.of("dev");
    }

    /**
     * Returns the tenant for which the seeding is to be ran
     * */
    protected String getTenant() {
        return "bern";
    }
}
