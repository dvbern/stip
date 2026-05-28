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

package ch.dvbern.stip.integration.plzfetch.adapter.swisstopo.service;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import ch.dvbern.stip.api.config.type.StipConfig;
import ch.dvbern.stip.api.scheduledtask.entity.Scheduledtask;
import ch.dvbern.stip.api.scheduledtask.repo.ScheduledtaskRepository;
import ch.dvbern.stip.api.scheduledtask.type.ScheduledtaskType;
import ch.dvbern.stip.integration.plzfetch.adapter.swisstopo.type.SwisstopoPlzDiscoveryResponse;
import ch.dvbern.stip.integration.plzfetch.domain.model.PlzFetchAdapterType;
import ch.dvbern.stip.integration.plzfetch.domain.model.PlzFetchData;
import ch.dvbern.stip.integration.plzfetch.domain.port.PlzFetchPort;
import ch.dvbern.stip.integration.plzfetch.domain.qualifier.PlzFetchQualifier;
import com.fasterxml.jackson.databind.JsonNode;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderHeaderAwareBuilder;
import com.opencsv.exceptions.CsvException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Slf4j
@RequestScoped
@PlzFetchQualifier(PlzFetchAdapterType.SWISSTOPO)
public class SwisstopoPlzFetchAdapter implements PlzFetchPort {

    private final SwisstopoPlzDiscoveryService plzDiscoveryService;
    private final ScheduledtaskRepository scheduledtaskRepository;
    private final StipConfig config;

    public SwisstopoPlzFetchAdapter(
    @RestClient SwisstopoPlzDiscoveryService plzDiscoveryService, ScheduledtaskRepository scheduledtaskRepository,
    StipConfig config
    ) {
        this.plzDiscoveryService = plzDiscoveryService;
        this.scheduledtaskRepository = scheduledtaskRepository;
        this.config = config;
    }

    @Override
    public Optional<Set<PlzFetchData>> fetchData() throws IOException, CsvException {
        Optional<Set<PlzFetchData>> result = Optional.empty();

        final SwisstopoPlzDiscoveryResponse plzDiscovery = plzDiscoveryService.get();

        if (plzDiscovery == null) {
            LOG.error("SwisstopoPlzDiscoveryService returned null, cannot fetch PLZ data");
            return Optional.empty();
        }

        final var plzDiscoveryJson = plzDiscovery.features().get(0).assets();

        if (isNewDataAvailable(plzDiscoveryJson)) {
            final var uriNode = plzDiscoveryJson.findValue(config.plzData().featureKey()).findValue("href");

            if (uriNode != null) {
                result = loadNewData(URI.create(uriNode.asText()));
            }
        }

        reportScheduledTaskExecution(plzDiscoveryJson);

        return result;
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
    public boolean isNewDataAvailable(final JsonNode plzDiscoveryJson) {
        final Optional<Scheduledtask> latestScheduledTask = scheduledtaskRepository
            .findLatestWithType(ScheduledtaskType.PLZ_DATA_FETCH.name());

        boolean newDataAvailable = true;
        if (latestScheduledTask.isPresent()) {
            final String lastHash = latestScheduledTask
                .get()
                .getPayload()
                .findValue(config.plzData().hashKey())
                .asText();

            final String currentHash = plzDiscoveryJson
                .findValue(config.plzData().hashKey())
                .asText();
            if (lastHash.equals(currentHash)) {
                newDataAvailable = false;
            }
        }
        return newDataAvailable;
    }

    private Optional<Set<PlzFetchData>> loadNewData(final URI uri) throws IOException, CsvException {
        final String csvFileData = loadCsvFileData(uri);
        if (csvFileData == null) {
            LOG.error("Failed to load PLZ CSV");
            return Optional.empty();
        }

        final var csvParser = new CSVParserBuilder()
            .withSeparator(';')
            .build();

        try (
            final var reader =
                new CSVReaderHeaderAwareBuilder(new StringReader(csvFileData))
                    .withCSVParser(csvParser)
                    .build()
        ) {
            Set<PlzFetchData> plzList = new HashSet<>();
            Map<String, String> rowMap;
            while ((rowMap = reader.readMap()) != null) {
                final var plzFetchData = PlzFetchData.builder()
                    .plz(rowMap.get("PLZ4"))
                    .ort(rowMap.get("\uFEFFOrtschaftsname"))
                    .kantonskuerzel(rowMap.get("Kantonskürzel"))
                    .build();
                plzList.add(plzFetchData);
            }

            final var exception = new IllegalStateException(
                "Importing of PLZ data failed to find known '3005 Bern BE', check CSV manually if the format changed"
            );

            final var bernPlz = plzList.stream()
                .filter(plz -> plz.plz().equals("3005"))
                .findFirst()
                .orElseThrow(() -> exception);

            if (!bernPlz.ort().equals("Bern") || !bernPlz.kantonskuerzel().equalsIgnoreCase("be")) {
                throw exception;
            }

            return Optional.of(plzList);
        }
    }

    private String loadCsvFileData(final URI uri) throws IOException {
        final var downloadService = RestClientBuilder.newBuilder()
            .baseUri(uri)
            .build(SwisstopoPlzDownloadService.class);

        final var file = downloadService.getPlzDownload();

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
}
