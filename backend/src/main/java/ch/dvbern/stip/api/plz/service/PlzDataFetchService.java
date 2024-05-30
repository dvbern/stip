package ch.dvbern.stip.api.plz.service;

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
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
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

        if (geoCollection != null) {
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
    }

    @Transactional
    public void reportScheduledTaskExecution(JsonNode payload) {
        Scheduledtask scheduledtask = new Scheduledtask();
        scheduledtask.setType(ScheduledtaskType.PLZ_DATA_FETCH.name());
        scheduledtask.setTimestamp(LocalDateTime.now());
        scheduledtask.setPayload(payload);
        scheduledtaskRepository.persistAndFlush(scheduledtask);
    }

    @Transactional
    public boolean isNewDataAvailable(JsonNode currentGeoCollectionAssetJSON) {
        final Optional<Scheduledtask> lastScheduledTaskOptional = scheduledtaskRepository
            .findLastWithType(ScheduledtaskType.PLZ_DATA_FETCH.name());

        boolean newDataAvailable = true;
        if (lastScheduledTaskOptional.isPresent()) {
            final String lastHash = lastScheduledTaskOptional
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

    private void loadNewData(URI uri) throws IOException, CsvException {
        final String csvFileData = loadCsvFileData(uri);
        CSVReader csvReader = new CSVReaderBuilder(new StringReader(csvFileData))
            .withSkipLines(1)
            .withCSVParser(new CSVParserBuilder()
                .withSeparator(';')
                .build()
            ).build();

        Set<List<String>> plzHashSet = new HashSet<>();
        csvReader.readAll().forEach(plzLine ->
            plzHashSet.add(Arrays.asList(plzLine[0], plzLine[1], plzLine[5]))
        );
        Log.warn("hslen: "+ plzHashSet.size());

        List<Plz> plzList = new ArrayList<>(plzHashSet.size());
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

    private String loadCsvFileData(URI uri) throws IOException {
        GeoCollectionDowloadService geoCollectionDowloadService = RestClientBuilder.newBuilder()
            .baseUri(uri)
            .build(GeoCollectionDowloadService.class);
        final byte[] file = geoCollectionDowloadService.getGeoCollectionDowload();

        ZipInputStream zin = new ZipInputStream(new ByteArrayInputStream(file));
        ZipEntry ze = zin.getNextEntry();
        String csvFileData = "";
        if (ze != null) {
            if (ze.getName().endsWith(".csv")) {
                final int fileLen = (int) ze.getSize();
                final byte[] bytes = new byte[fileLen];

                int bytesRead = 0;
                while (bytesRead < fileLen) {
                    bytesRead += zin.read(bytes, bytesRead, fileLen - bytesRead);
                }

                csvFileData = new String(bytes, StandardCharsets.UTF_8);
            }
        }
        zin.close();
        return csvFileData;
    }

    @Transactional
    public void storePlzData(List<Plz> plzList) {
        plzRepository.deleteAll();
        plzRepository.persist(plzList.stream());
        plzRepository.flush();
    }
}
