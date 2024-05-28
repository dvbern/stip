package ch.dvbern.stip.api.plz.service;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import io.quarkus.logging.Log;

import io.quarkus.scheduler.Scheduled;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Singleton
public class PlzDataFetchService {
    @Inject
    @RestClient
    GeoCollectionService geoCollectionService;

    @ConfigProperty(name = "kstip.plzdata.featurekey")
    String geoCollectionFeatureKey;

    @Scheduled(cron = "{kstip.plzdata.cron}")
    public void fetchData() throws IOException, CsvException {
        final var geoCollection = geoCollectionService.get();
        String csvFileData = "";
        if (geoCollection != null) {
            final var uriNode = geoCollection.getFeatures().get(0).getAssets().findValue(geoCollectionFeatureKey).findValue("href");

            if (uriNode != null) {
                final var uri = URI.create(uriNode.asText());
                GeoCollectionDowloadService geoCollectionDowloadService = RestClientBuilder.newBuilder().baseUri(uri).build(GeoCollectionDowloadService.class);
                final var file = geoCollectionDowloadService.getGeoCollectionDowload();
                ZipInputStream zin = new ZipInputStream(new ByteArrayInputStream(file));
                ZipEntry ze = zin.getNextEntry();
                if (ze != null) {
                    if (ze.getName().endsWith(".csv")) {
                        int fileLen = (int) ze.getSize();
                        byte[] bytes = new byte[fileLen];

                        int bytesRead = 0;
                        while (bytesRead < fileLen) {
                            bytesRead += zin.read(bytes, bytesRead, fileLen - bytesRead);
                        }

                        csvFileData = new String(bytes, StandardCharsets.UTF_8);

                    }
                }
                zin.close();
            }
        }
        CSVReader csvReader = new CSVReaderBuilder(new StringReader(csvFileData))
            .withSkipLines(1)
            .withCSVParser(new CSVParserBuilder()
                .withSeparator(';')
                .build()
            ).build();

        final var lines = csvReader.readAll();
    }
}
