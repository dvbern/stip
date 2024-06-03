package ch.dvbern.stip.api.plz.service;

import java.io.IOException;

import ch.dvbern.stip.api.tenancy.service.DataTenantResolver;
import com.opencsv.exceptions.CsvException;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.runtime.Startup;
import io.quarkus.scheduler.Scheduled;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Singleton
@Slf4j
public class PlzDataFetchScheduledTask {
    private final PlzDataFetchService plzDataFetchService;

    @Scheduled(cron = "{kstip.plzdata.cron}")
    public void run() {
        try {
            QuarkusTransaction.requiringNew().run(() -> {
                DataTenantResolver.setTenantId("none");
                LOG.info("Fetching PLZ data from scheduled task");
                try {
                    plzDataFetchService.fetchData();
                } catch (IOException | CsvException e) {
                    LOG.error(e.toString(), e);
                }
            });
        } catch (Exception e) {
            LOG.error(e.toString(), e);
        }
    }

    @Startup
    public void runAtStartup() {
        run();
    }
}
