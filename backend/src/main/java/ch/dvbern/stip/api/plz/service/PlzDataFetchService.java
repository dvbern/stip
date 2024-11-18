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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Slf4j
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
        if (csvFileData == null) {
            LOG.error("Failed to load PLZ CSV");
            return;
        }

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
        final var downloadService = RestClientBuilder.newBuilder()
            .baseUri(uri)
            .build(GeoCollectionDowloadService.class);

        final var file = downloadService.getGeoCollectionDowload();

        // Buffer the input, so we can reset it, since we have to read it twice
        // https://bugs.java.com/bugdatabase/view_bug.do?bug_id=4079029
        try (final var bufferedInput = new BufferedInputStream(new ByteArrayInputStream(file))) {
            bufferedInput.mark(Integer.MAX_VALUE);

            final var csvSize = findCsvSize(bufferedInput);
            if (csvSize == -1) {
                LOG.error("Could not determine size of CSV");
                return null;
            }

            bufferedInput.reset();
            final var zipStream = new ZipInputStream(bufferedInput);

            ZipEntry zipEntry;
            while ((zipEntry = zipStream.getNextEntry()) != null) {
                if (zipEntry.getName().endsWith(".csv")) {
                    final var bytes = new byte[csvSize];

                    int bytesRead = 0;
                    while (bytesRead < csvSize) {
                        bytesRead += zipStream.read(bytes, bytesRead, csvSize - bytesRead);
                    }

                    return new String(bytes, StandardCharsets.UTF_8);
                }
            }
        }

        LOG.error("No CSV file found in downloaded zip");
        return null;
    }

    private int findCsvSize(final BufferedInputStream inStream) throws IOException {
        // No try-with because it would close the underlying stream
        final var zipStream = new ZipInputStream(inStream);
        ZipEntry zipEntry;
        while ((zipEntry = zipStream.getNextEntry()) != null) {
            if (zipEntry.getName().endsWith(".csv")) {
                if (zipEntry.getSize() < 0) {
                    zipStream.getNextEntry();
                }

                return (int) zipEntry.getSize();
            }
        }

        return -1;
    }

    @Transactional
    public void storePlzData(final List<Plz> plzList) {
        plzRepository.deleteAll();
        plzRepository.persist(plzList.stream());
        plzRepository.flush();
    }
}
