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

package ch.dvbern.stip.api.statistik.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.type.AusbildungsstaetteNummerTyp;
import ch.dvbern.stip.api.buchhaltung.entity.Buchhaltung;
import ch.dvbern.stip.api.buchhaltung.repo.BuchhaltungRepository;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.darlehen.entity.DarlehenBuchhaltungEntry;
import ch.dvbern.stip.api.darlehen.repo.DarlehenBuchhaltungEntryRepository;
import ch.dvbern.stip.api.dokument.service.DokumentUploadService;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.plz.service.PlzService;
import ch.dvbern.stip.api.statistik.dto.FormDto;
import ch.dvbern.stip.api.statistik.dto.FormationDto;
import ch.dvbern.stip.api.statistik.dto.HeadDto;
import ch.dvbern.stip.api.statistik.dto.InstCodeDto;
import ch.dvbern.stip.api.statistik.dto.InstIdentificationRootDto;
import ch.dvbern.stip.api.statistik.dto.LocalPersonIdDto;
import ch.dvbern.stip.api.statistik.dto.PersDto;
import ch.dvbern.stip.api.statistik.dto.PersonIdentificationRootDto;
import ch.dvbern.stip.api.statistik.dto.SumDto;
import ch.dvbern.stip.api.statistik.dto.TableDto;
import ch.dvbern.stip.api.statistik.entity.Statistik;
import ch.dvbern.stip.api.statistik.repo.StatistikRepository;
import ch.dvbern.stip.api.statistik.type.StatistikBuchhaltungUnion;
import ch.dvbern.stip.api.statistik.util.StatistikConstants;
import ch.dvbern.stip.api.statistik.util.StatistikUtil;
import ch.dvbern.stip.api.swisstopoapi.service.SwisstopoService;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.stipdecision.type.Kanton;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.SAXException;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class StatistikXMLService {

    private final StatistikRepository statistikRepository;
    private final BuchhaltungRepository buchhaltungRepository;
    private final DarlehenBuchhaltungEntryRepository darlehenBuchhaltungEntryRepository;
    private final TenantService tenantService;
    private final SwisstopoService swisstopoService;
    private final PlzService plzService;
    private final DokumentUploadService dokumentUploadService;
    private final S3AsyncClient s3AsyncClient;
    private final ConfigService configService;

    public void create() {
        try {
            int year = LocalDate.now().getYear();
            LOG.info("Generating test statistik XML for year {}", year);

            var xmlOutputStream = generateStatistikXml(year);

            String outputDir = "target";
            Path outputPath = Paths.get(outputDir, "statistik-test.xml");

            Files.createDirectories(outputPath.getParent());

            try (FileOutputStream fos = new FileOutputStream(outputPath.toFile())) {
                xmlOutputStream.writeTo(fos);
            }

            LOG.info("Successfully generated test statistik XML at: {}", outputPath.toAbsolutePath());

            try {
                validate(xmlOutputStream);
            } catch (SAXException e) {
                LOG.error("XML validation failed", e);
                throw e;
            }

        } catch (Exception e) {
            LOG.error("Failed to generate test statistik XML: {}", e.getMessage());
        }
    }

    public void createAndSave(final int year, final String triggeredBy) {
        final var startTimestamp = LocalDateTime.now();
        LOG.info("Creating and saving statistik for year {} triggered by {}", year, triggeredBy);

        final var outputStream = generateStatistikXml(year);

        try {
            validate(outputStream);
        } catch (SAXException e) {
            LOG.error("XML validation failed", e);
            return;
        }

        final var fileName = String.format(
            "%s%s%s",
            StatistikConstants.STATISTIK_FILE_PREFIX,
            year,
            StatistikConstants.STATISTIK_FILE_EXTENSION
        );

        final var objectId = dokumentUploadService.executeUploadDocument(
            outputStream.toByteArray(),
            fileName,
            s3AsyncClient,
            configService,
            StatistikConstants.STATISTIK_FILE_PATH
        );

        final var statistik = Statistik.builder()
            .userTriggeredCreation(triggeredBy)
            .objectId(objectId)
            .year(year)
            .filename(fileName)
            .filepath(StatistikConstants.STATISTIK_FILE_PATH)
            .filesize(outputStream.size())
            .build();

        statistikRepository.persistAndFlush(statistik);

        final var endTimestamp = LocalDateTime.now();
        LOG.info(
            "Finished creating and saving statistik for year {} triggered by {} in {} ms",
            year,
            triggeredBy,
            Duration.between(startTimestamp, endTimestamp).toMillis()
        );
    }

    public static void validate(ByteArrayOutputStream xmlOut) throws SAXException {
        SchemaFactory factory =
            SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        URL xsdUrl = StatistikXMLService.class.getResource(StatistikConstants.STATISTIK_XML_SCHEMA_PATH);
        if (xsdUrl == null) {
            throw new IllegalStateException("XSD not found: " + StatistikConstants.STATISTIK_XML_SCHEMA_PATH);
        }

        Schema schema = factory.newSchema(xsdUrl);
        Validator validator = schema.newValidator();

        try (
            ByteArrayInputStream xmlIn =
                new ByteArrayInputStream(xmlOut.toByteArray())
        ) {
            validator.validate(new StreamSource(xmlIn));
        } catch (IOException e) {
            LOG.error("Failed to validate XML input stream", e);
        }
    }

    @Transactional
    public ByteArrayOutputStream generateStatistikXml(int year) {
        List<Buchhaltung> buchhaltungs = buchhaltungRepository.findByYear(year);
        List<DarlehenBuchhaltungEntry> darlehenBuchhaltungs = darlehenBuchhaltungEntryRepository.findByYear(year);

        List<StatistikBuchhaltungUnion> statistikBuchhaltungUnions =
            StatistikUtil.combineBuchhaltungs(buchhaltungs, darlehenBuchhaltungs, year);

        TableDto tableDto = buildTableDto(statistikBuchhaltungUnions, year);

        try {
            return generateXml(tableDto);
        } catch (JAXBException e) {
            LOG.error("Failed to generate XML for statistik for year {}", year, e);
            throw new RuntimeException("Failed to generate XML for statistik", e);
        }
    }

    private TableDto buildTableDto(List<StatistikBuchhaltungUnion> buchhaltungs, int year) {
        AtomicInteger formIdCounter = new AtomicInteger(1);
        AtomicInteger sumIdCounter = new AtomicInteger(1);

        final var persons = buchhaltungs.stream()
            .map(buchhaltung -> buchhaltung.getGesuch().getAusbildung().getFall())
            .distinct()
            .map(fall -> {
                final var buchhaltungsForFall =
                    buchhaltungs.stream().filter(b -> b.getGesuch().getAusbildung().getFall().equals(fall)).toList();
                return mapToPersDto(fall, buchhaltungsForFall, year, formIdCounter, sumIdCounter);
            })
            .toList();

        final var tenantIdentifier = tenantService.getCurrentTenantIdentifier();
        final var bfsCode = StatistikUtil.getBfsCodeFromTenantIdentifier(tenantIdentifier);

        return TableDto.builder()
            .head(
                HeadDto.builder()
                    .version(year)
                    .canton(bfsCode)
                    .dataDelivery("BE_KT")
                    .deliveryDate(LocalDate.now().toString())
                    .build()
            )
            .persons(persons)
            .build();
    }

    private PersDto mapToPersDto(
        Fall fall,
        List<StatistikBuchhaltungUnion> buchhaltungs,
        int year,
        AtomicInteger formIdCounter,
        AtomicInteger sumIdCounter
    ) {
        final var gesuchTranche = StatistikUtil.getLatestGesuchTrancheFromFallByYear(fall, year);
        final var pia = gesuchTranche.getGesuchFormular().getPersonInAusbildung();

        var personId = LocalPersonIdDto.builder()
            .personIdCategory(StatistikConstants.STATISTIK_XML_PERSON_ID_CATEGORY)
            .personId(pia.getSozialversicherungsnummer())
            .build();

        var personIdentificationRoot = PersonIdentificationRootDto.builder()
            .localPersonId(personId)
            .sex(pia.getAnrede().getBfsCode())
            .dateOfBirth(pia.getGeburtsdatum().toString())
            .build();

        List<FormDto> forms = buchhaltungs.stream()
            .map(b -> b.getGesuch().getAusbildung())
            .distinct()
            .filter(ausbildung -> ausbildung.getFall().equals(fall))
            .map(ausbildung -> mapToFormDto(ausbildung, buchhaltungs, year, formIdCounter, sumIdCounter))
            .toList();

        final var persDto = PersDto.builder()
            .personIdentificationRoot(personIdentificationRoot)
            .nationality(Integer.valueOf(pia.getNationalitaet().getLaendercodeBfs()))
            .forms(forms)
            .build();

        final var bfsGemeindeCode =
            Optional.ofNullable(StatistikUtil.getBfsGemeindeNrFromGesuch(gesuchTranche, swisstopoService));

        if (bfsGemeindeCode.isEmpty()) {
            persDto.setCountry(Integer.valueOf(pia.getAdresse().getLand().getLaendercodeBfs()));
        } else {
            persDto.setPlace(bfsGemeindeCode.get());
        }

        return persDto;
    }

    private FormDto mapToFormDto(
        Ausbildung ausbildung,
        List<StatistikBuchhaltungUnion> buchhaltungs,
        int year,
        AtomicInteger formIdCounter,
        AtomicInteger sumIdCounter
    ) {
        final int ausbildungOrt;

        if (ausbildung.getIsAusbildungAusland()) {
            ausbildungOrt = Integer.parseInt(ausbildung.getLand().getLaendercodeBfs());
        } else {
            final var plz = plzService.findByPostleitzahl(ausbildung.getAusbildungsortPLZ());
            final var kanton = Kanton.valueOf(plz.getKantonskuerzel());
            ausbildungOrt = kanton.getBfsCode();
        }

        final var sums = buchhaltungs.stream()
            .filter(buchhaltung -> buchhaltung.getGesuch().getAusbildung().equals(ausbildung))
            .map(buchhaltung -> mapToSumDto(buchhaltung, sumIdCounter))
            .toList();

        return FormDto.builder()
            .formId(formIdCounter.getAndIncrement())
            .formation(mapToFormationDto(ausbildung, year))
            .instIdentificationRoot(mapToInstIdentificationRootDto(ausbildung))
            .formPlace(ausbildungOrt)
            .sums(sums)
            .build();
    }

    private FormationDto mapToFormationDto(Ausbildung ausbildung, int year) {
        final var abschluss = ausbildung.getAusbildungsgang().getAbschluss();
        final var gesuchTranche = StatistikUtil.getLatestGesuchTrancheFromFallByYear(ausbildung.getFall(), year);

        return FormationDto.builder()
            .formLevel(abschluss.getBfsKategorie())
            .matuProf(StatistikUtil.booleanToBfsCode(ausbildung.isBesuchtBMS()))
            .diploma(abschluss.getBfsStudienStufe())
            .task(ausbildung.getPensum().getBfsCode())
            .firstForm(StatistikUtil.booleanToBfsCode(StatistikUtil.isFirstAusbildung(gesuchTranche)))
            .build();
    }

    private InstIdentificationRootDto mapToInstIdentificationRootDto(Ausbildung ausbildung) {
        final var abschluss = ausbildung.getAusbildungsgang().getAbschluss();
        final var ausbildungsstaette = ausbildung.getAusbildungsgang().getAusbildungsstaette();

        final InstIdentificationRootDto instIdentificationRootDto = new InstIdentificationRootDto();

        if (List.of(8, 9, 10).contains(abschluss.getBfsKategorie())) {
            var instCategory = ausbildungsstaette.getNummerTyp().getBfsIdentification();

            if (ausbildungsstaette.getNummerTyp().equals(AusbildungsstaetteNummerTyp.CT_NO)) {
                final var tenantIdentifier = tenantService.getCurrentTenantIdentifier();
                final var kanton = StatistikUtil.getKantonFromTenantIdentifier(tenantIdentifier);
                instCategory = String.format("%s%s", instCategory, kanton.toString());
            }

            if (!ausbildungsstaette.getNummerTyp().equals(AusbildungsstaetteNummerTyp.OHNE_NO)) {
                final var instCodeDto = InstCodeDto.builder()
                    .instCategory(instCategory)
                    .instId(ausbildungsstaette.getNummer())
                    .build();

                instIdentificationRootDto.setInstCode(instCodeDto);
            } else {
                instIdentificationRootDto.setInstName(ausbildungsstaette.getNameDe());
            }
        } else {
            instIdentificationRootDto.setInstName(ausbildungsstaette.getNameDe());
        }

        return instIdentificationRootDto;
    }

    private SumDto mapToSumDto(StatistikBuchhaltungUnion buchhaltung, AtomicInteger sumIdCounter) {
        return SumDto.builder()
            .sumId(sumIdCounter.incrementAndGet())
            .sumTotal(buchhaltung.getBetrag())
            .sumArt(buchhaltung.getType().getBfsCode())
            .term(buchhaltung.getAnzahlSemester())
            .build();
    }

    private ByteArrayOutputStream generateXml(TableDto tableDto) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(TableDto.class);

        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, StatistikConstants.STATISTIK_XML_ENCODING);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        marshaller.marshal(tableDto, outputStream);

        return outputStream;
    }
}
