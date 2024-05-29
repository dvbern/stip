package ch.dvbern.stip.api.plz.service;

import ch.dvbern.stip.api.tenancy.service.DataTenantResolver;
import com.opencsv.exceptions.CsvException;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

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
                try {
                    plzDataFetchService.fetchData();
                } catch (IOException | CsvException e) {
                    LOG.error(e.toString(), e);
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            LOG.error(e.toString(), e);
        }
    }
}
