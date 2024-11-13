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

package ch.dvbern.stip.api.plz.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.plz.entity.GeoCollectionItem;
import ch.dvbern.stip.api.plz.entity.Plz;
import ch.dvbern.stip.api.plz.repo.PlzRepository;
import ch.dvbern.stip.api.scheduledtask.entity.Scheduledtask;
import ch.dvbern.stip.api.scheduledtask.repo.ScheduledtaskRepository;
import ch.dvbern.stip.api.scheduledtask.type.ScheduledtaskType;
import com.fasterxml.jackson.databind.JsonNode;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@RequiredArgsConstructor
@ApplicationScoped
public class PlzDataFetchService {
    private final PlzRepository plzRepository;
    private final ScheduledtaskRepository scheduledtaskRepository;

    @Inject
    @RestClient
    GeoCollectionService geoCollectionService;

    @ConfigProperty(name = "kstip.plzdata.featurekey")
    String geoCollectionFeatureKey;

    @ConfigProperty(name = "kstip.plzdata.hashkey")
    String geoCollectionHashKey;

    public void fetchData() throws IOException, CsvException {
        final GeoCollectionItem geoCollection = geoCollectionService.get();

        if (geoCollection == null) {
            return;
        }

        final JsonNode geoCollectionAssetJSON = geoCollection
            .getFeatures()
            .get(0)
            .getAssets();

        if (isNewDataAvailable(geoCollectionAssetJSON)) {
            final JsonNode uriNode = geoCollectionAssetJSON
                .findValue(geoCollectionFeatureKey)
                .findValue("href");
            if (uriNode != null) {
                loadNewData(URI.create(uriNode.asText()));
            }
        }
        reportScheduledTaskExecution(geoCollectionAssetJSON);
    }

    @Transactional
    public void reportScheduledTaskExecution(final JsonNode payload) {
        final var scheduledtask = new Scheduledtask();
        scheduledtask.setType(ScheduledtaskType.PLZ_DATA_FETCH.name());
        scheduledtask.setLastExecution(LocalDateTime.now());
        scheduledtask.setPayload(payload);
        scheduledtaskRepository.persistAndFlush(scheduledtask);
    }

    @Transactional
    public boolean isNewDataAvailable(final JsonNode currentGeoCollectionAssetJSON) {
        final Optional<Scheduledtask> latestScheduledTask = scheduledtaskRepository
            .findLatestWithType(ScheduledtaskType.PLZ_DATA_FETCH.name());

        boolean newDataAvailable = true;
        if (latestScheduledTask.isPresent()) {
            final String lastHash = latestScheduledTask
                .get()
                .getPayload()
                .findValue(geoCollectionHashKey)
                .asText();

            final String currentHash = currentGeoCollectionAssetJSON
                .findValue(geoCollectionHashKey)
                .asText();
            if (lastHash.equals(currentHash)) {
                newDataAvailable = false;
            }
        }
        return newDataAvailable;
    }

    private void loadNewData(final URI uri) throws IOException, CsvException {
        final String csvFileData = loadCsvFileData(uri);
        final Set<List<String>> plzHashSet = new HashSet<>();
        try (
            final CSVReader csvReader = new CSVReaderBuilder(new StringReader(csvFileData))
                .withSkipLines(1)
                .withCSVParser(
                    new CSVParserBuilder()
                        .withSeparator(';')
                        .build()
                )
                .build()
        ) {
            csvReader.readAll()
                .forEach(
                    plzLine -> plzHashSet.add(Arrays.asList(plzLine[0], plzLine[1], plzLine[5]))
                );
        }

        final List<Plz> plzList = new ArrayList<>(plzHashSet.size());
        plzHashSet.forEach(
            plzLineElement -> plzList.add(
                new Plz()
                    .setOrt(plzLineElement.get(0))
                    .setPlz(plzLineElement.get(1))
                    .setKantonskuerzel(plzLineElement.get(2))
            )
        );

        storePlzData(plzList);
    }

    private String loadCsvFileData(final URI uri) throws IOException {
        try (
            final var resource = this.getClass().getClassLoader().getResourceAsStream("ortschaften_plz.csv");
            final var reader = new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8));
        ) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    @Transactional
    public void storePlzData(final List<Plz> plzList) {
        plzRepository.deleteAll();
        plzRepository.persist(plzList.stream());
        plzRepository.flush();
    }
}
