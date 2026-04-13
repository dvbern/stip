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

package ch.dvbern.stip.api.swisstopoapi.service;

import java.util.UUID;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.swisstopoapi.entity.SwisstopoApiFindAddrResponse.SwisstopoApiFindAddrResponseElement;
import ch.dvbern.stip.api.swisstopoapi.entity.SwisstopoApiFindAddrResponse.SwisstopoApiFindAddrResponseElementAttributes;
import ch.dvbern.stip.api.swisstopoapi.scheduledtask.SwisstopoAddrFetchScheduledJob;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.quartz.DateBuilder;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class SwisstopoService {
    private static final String ADDR_NO_SEARCH_LAYER_DEF_KEY = "ch.swisstopo.amtliches-gebaeudeadressverzeichnis";
    private static final String ADDR_NO_SEARCH_LAYER_DEF_SEARCH_STR = "adr_number ilike '%s'";
    private static final String SWISSTOPO_ADDR_FETCH_SCHEDULED_JOB_PREFIX = "SwisstopoAddrFetchScheduledJob-";
    private static final int SWISSTOPO_ADDR_FETCH_SCHEDULED_JOB_DELAY_SECONDS = 5;

    private final GesuchService gesuchService;
    private final TenantService tenantService;
    private final Scheduler scheduler;

    @Inject
    @RestClient
    SwisstopoApiRestService swisstopoApiRestService;

    public void createFetchGemeindeDataOfGesuchScheduledTask(final Gesuch gesuch) {
        final GesuchTranche trancheToUse = gesuch.getLatestGesuchTranche();
        final Adresse adresse = trancheToUse.getGesuchFormular().getPersonInAusbildung().getAdresse();
        final JobDetail jobDetail = JobBuilder.newJob(SwisstopoAddrFetchScheduledJob.class)
            .withIdentity(SWISSTOPO_ADDR_FETCH_SCHEDULED_JOB_PREFIX + gesuch.getId().toString())
            .usingJobData("gesuchId", gesuch.getId().toString())
            .usingJobData("strasse", adresse.getStrasse())
            .usingJobData("hausnummer", adresse.getHausnummer())
            .usingJobData("plz", adresse.getPlz())
            .usingJobData("ort", adresse.getOrt())
            .usingJobData("mandantIdentifier", tenantService.getCurrentTenantIdentifier())
            .build();
        final Trigger trigger = TriggerBuilder.newTrigger()
            .withIdentity(SWISSTOPO_ADDR_FETCH_SCHEDULED_JOB_PREFIX + "trigger-" + gesuch.getId().toString())
            // This is necessary (delayed start) so we are sure this transaction is closed before the task starts.
            // Otherwise both transactions overlap
            .startAt(DateBuilder.futureDate(SWISSTOPO_ADDR_FETCH_SCHEDULED_JOB_DELAY_SECONDS, IntervalUnit.SECOND))
            .build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            LOG.error(
                String.format(
                    "Could not schedule %s",
                    SWISSTOPO_ADDR_FETCH_SCHEDULED_JOB_PREFIX
                ),
                e
            );
        }
    }

    public void getGemeindeDataOfGesuch(
        final UUID gesuchId,
        final String strasse,
        final String hausnummer,
        final String plz,
        final String ort
    ) {
        // Schnittstellen informationen:
        // https://www.swisstopo.admin.ch/dam/de/sd-web/fb0eJ1WiRYrq/Geb%C3%A4udeadressen%20Technical%20Documentation.pdf
        final var buildingNoSearchPartJson = new JSONObject();
        buildingNoSearchPartJson.put(
            ADDR_NO_SEARCH_LAYER_DEF_KEY,
            String.format(ADDR_NO_SEARCH_LAYER_DEF_SEARCH_STR, hausnummer)
        );
        try {
            final var result =
                swisstopoApiRestService.findAllMatchingBuildings(
                    strasse,
                    buildingNoSearchPartJson.toString()
                );
            final var swisstopoApiFindAddrResponseElementAttribute = result.getResults()
                .stream()
                .map(SwisstopoApiFindAddrResponseElement::getAttributes)
                .filter(
                    // Format von zip_label ist "${PLZ} ${OrtsName}"
                    swisstopoApiFindAddrResponseElementAttributes -> swisstopoApiFindAddrResponseElementAttributes
                        .getZip_label()
                        .contains(plz)
                    || swisstopoApiFindAddrResponseElementAttributes.getZip_label().contains(ort)
                )
                .findFirst()
                .orElseThrow(
                    () -> new NotFoundException(
                        String.format(
                            "Could not find Building in Swisstopo data with properties, Street: %s, No: %s",
                            strasse,
                            hausnummer
                        )
                    )
                );
            setGemeindeDataOfGesuch(gesuchId, swisstopoApiFindAddrResponseElementAttribute);
        } catch (Exception e) {
            LOG.error(
                String.format(
                    "Could not perform Building lookup in Swisstopo data with properties, Street: %s, No: %s",
                    strasse,
                    hausnummer
                ),
                e
            );
        }
    }

    @Transactional(TxType.REQUIRES_NEW)
    public void setGemeindeDataOfGesuch(
        final UUID gesuchId,
        final SwisstopoApiFindAddrResponseElementAttributes swisstopoApiFindAddrResponseElementAttribute
    ) {
        final Gesuch gesuch = gesuchService.getGesuchById(gesuchId);
        gesuch.getStatisticsdata()
            .setGemeindeBfsNr(swisstopoApiFindAddrResponseElementAttribute.getCom_fosnr());
        gesuch.getStatisticsdata()
            .setGemeindeName(swisstopoApiFindAddrResponseElementAttribute.getCom_name());
    }

}
