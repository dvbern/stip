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
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import ch.dvbern.stip.api.ausbildung.type.AusbildungsstaetteNummerTyp;
import ch.dvbern.stip.api.buchhaltung.repo.BuchhaltungRepository;
import ch.dvbern.stip.api.common.type.Kanton;
import ch.dvbern.stip.api.common.type.TenantIdentifier;
import ch.dvbern.stip.api.common.util.KantonUtil;
import ch.dvbern.stip.api.config.type.StipConfig;
import ch.dvbern.stip.api.darlehen.repo.DarlehenBuchhaltungEntryRepository;
import ch.dvbern.stip.api.dokument.service.DokumentUploadService;
import ch.dvbern.stip.api.land.type.WellKnownLand;
import ch.dvbern.stip.api.plz.service.PlzService;
import ch.dvbern.stip.api.statistik.dto.FormDto;
import ch.dvbern.stip.api.statistik.dto.FormDtoBuilder;
import ch.dvbern.stip.api.statistik.dto.FormationDtoBuilder;
import ch.dvbern.stip.api.statistik.dto.HeadDtoBuilder;
import ch.dvbern.stip.api.statistik.dto.InstCodeDto;
import ch.dvbern.stip.api.statistik.dto.InstIdentificationRootDto;
import ch.dvbern.stip.api.statistik.dto.LocalPersonIdDtoBuilder;
import ch.dvbern.stip.api.statistik.dto.PersDto;
import ch.dvbern.stip.api.statistik.dto.PersDtoBuilder;
import ch.dvbern.stip.api.statistik.dto.PersonIdentificationRootDtoBuilder;
import ch.dvbern.stip.api.statistik.dto.SumDtoBuilder;
import ch.dvbern.stip.api.statistik.dto.TableDto;
import ch.dvbern.stip.api.statistik.dto.TableDtoBuilder;
import ch.dvbern.stip.api.statistik.entity.Statistik;
import ch.dvbern.stip.api.statistik.exception.StatistikGemeindeLookupException;
import ch.dvbern.stip.api.statistik.repo.StatistikRepository;
import ch.dvbern.stip.api.statistik.repo.StatistikRepository.StatistikOfYear;
import ch.dvbern.stip.api.statistik.type.StatistikBuchhaltungType;
import ch.dvbern.stip.api.statistik.util.StatistikConstants;
import ch.dvbern.stip.api.statistik.util.StatistikUtil;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.integration.gemeindelookup.domain.port.GemeindeLookupPortFactory;
import io.opentelemetry.instrumentation.annotations.WithSpan;
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
    private final TenantService tenantService;
    private final GemeindeLookupPortFactory gemeindeLookupPortFactory;
    private final DokumentUploadService dokumentUploadService;
    private final S3AsyncClient s3AsyncClient;
    private final StipConfig config;

    private final BuchhaltungRepository buchhaltungRepository;
    private final DarlehenBuchhaltungEntryRepository darlehenBuchhaltungEntryRepository;
    private final PlzService plzService;

    @Transactional
    @WithSpan
    public void createAndSave(final int year, final String triggeredBy) {
        final var startTimestamp = LocalDateTime.now();
        LOG.info("Creating and saving statistik for year {} triggered by {}", year, triggeredBy);

        final var statistik = new Statistik();

        try {
            final var outputStream = generateStatistikXml(year);

            try {
                validate(outputStream);
            } catch (SAXException e) {
                LOG.error("XML validation failed", e);
                statistik.setValid(false);
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
                config,
                StatistikConstants.STATISTIK_FILE_PATH
            );

            statistik.setObjectId(objectId);
            statistik.setFilename(fileName);
            statistik.setFilepath(StatistikConstants.STATISTIK_FILE_PATH);
            statistik.setFilesize(outputStream.size());
        } catch (StatistikGemeindeLookupException e) {
            LOG.error("XML creation failed because of Gemeinde Lookup failure", e);
            statistik.setError("XML creation failed: Gemeinde Lookup failed");
        } catch (Exception e) {
            LOG.error("XML creation failed", e);
            statistik.setError("XML creation failed");
        } finally {
            statistik.setUserTriggeredCreation(triggeredBy);
            statistik.setYear(year);
        }

        statistikRepository.persistAndFlush(statistik);

        final var endTimestamp = LocalDateTime.now();
        LOG.info(
            "Finished creating and saving statistik for year {} triggered by {} in {} ms",
            year,
            triggeredBy,
            Duration.between(startTimestamp, endTimestamp).toMillis()
        );
    }

    @WithSpan
    public void validate(ByteArrayOutputStream xmlOut) throws SAXException {
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
    @WithSpan
    public ByteArrayOutputStream generateStatistikXml(final int year) {
        return generateStatistikXml(year, statistikRepository.getStatistikValuesFor(year));
    }

    @Transactional
    @WithSpan
    public ByteArrayOutputStream generateStatistikXmlForGivenGesuchs(final int year, final List<UUID> gesuchIds) {
        final var statistikValues = statistikRepository.getStatistikValuesFor(year)
            .stream()
            .filter(g -> gesuchIds.contains(g.gesuchId()))
            .toList();
        return generateStatistikXml(year, statistikValues);
    }

    private ByteArrayOutputStream generateStatistikXml(
        final int year,
        final List<StatistikRepository.StatistikOfYear> statistikValues
    ) {
        final var tenantIdentifier = tenantService.getCurrentTenantIdentifier();
        final var kanton = KantonUtil.getByTenantIdentifier(tenantIdentifier);

        final var personMap = new HashMap<String, PersDto>();
        final var ausbildungMap = new HashMap<UUID, FormDto>();
        var ausbildungCounter = 0;
        for (var value : statistikValues) {
            if (!personMap.containsKey(value.sozialversicherungsnummer())) {
                personMap.put(
                    value.sozialversicherungsnummer(),
                    createPersonDto(value, tenantIdentifier)
                );
            }
            final var persDto = personMap.get(value.sozialversicherungsnummer());

            if (!ausbildungMap.containsKey(value.ausbildungId())) {
                ausbildungCounter++;
                final var formDto = createFormDto(value, ausbildungCounter);
                ausbildungMap.put(
                    value.ausbildungId(),
                    formDto
                );
                persDto.getForms().add(formDto);
            }
            final var formDto = ausbildungMap.get(value.ausbildungId());

            final var sumDto = SumDtoBuilder.sumDto()
                .sumId(value.sumId().intValue())
                .sumTotal(value.sumTotal())
                .sumArt(StatistikBuchhaltungType.valueOf(value.buchhaltungTyp()).getBfsCode())
                .term(StatistikUtil.getSemesterCount(value.ausbildungBegin(), value.ausbildungEnd(), year))
                .com(null)
                .build();
            formDto.getSums().add(sumDto);
        }

        final var tableDto = TableDtoBuilder.tableDto()
            .head(
                HeadDtoBuilder.headDto()
                    .version(year)
                    .canton(kanton.getBfsCode())
                    .dataDelivery(kanton.getBfsDelivery())
                    .deliveryDate(LocalDate.now().toString())
                    .build()
            )
            .persons(
                personMap.values()
                    .stream()
                    .sorted(
                        Comparator.comparing(
                            p -> p.getForms()
                                .stream()
                                .mapToInt(FormDto::getFormId)
                                .min()
                                .orElseThrow()
                        )
                    )
                    .toList()
            )
            .build();

        try {
            return generateXml(tableDto);
        } catch (JAXBException e) {
            LOG.error("Failed to generate XML for statistik for year {}", year, e);
            throw new RuntimeException("Failed to generate XML for statistik", e);
        }
    }

    private PersDto createPersonDto(StatistikOfYear value, TenantIdentifier tenantIdentifier) {
        final var isChNationality = value.nationalitaetBfs().equals(WellKnownLand.CHE.getLaendercodeBfs());
        return PersDtoBuilder.persDto()
            .personIdentificationRoot(
                PersonIdentificationRootDtoBuilder.personIdentificationRootDto()
                    .localPersonId(
                        LocalPersonIdDtoBuilder.localPersonIdDto()
                            .personIdCategory(StatistikConstants.STATISTIK_XML_PERSON_ID_CATEGORY)
                            .personId(value.sozialversicherungsnummer())
                            .build()
                    )
                    .sex(value.anrede().getBfsCode())
                    .dateOfBirth(value.geburtsdatum().toString())
                    .build()
            )
            .nationality(Integer.valueOf(value.nationalitaetBfs()))
            .residencePermitCategory(
                !isChNationality
                    ? value.niederlassungsstatus().getBfsCode()
                    : null
            )
            .place(
                value.piaAdresseLand().is(WellKnownLand.CHE) ? StatistikUtil.getGemeindeBfsNummerOrLookup(
                    value.gesuchId(),
                    value.gemeindeBfsNr(),
                    value.piaAdresse(),
                    tenantIdentifier,
                    gemeindeLookupPortFactory
                )
                    .orElseGet(
                        () -> KantonUtil.getByTenantIdentifier(tenantService.getCurrentTenantIdentifier()).getBfsCode()
                    )
                    : null
            )
            .placeHist(null)
            .country(
                value.piaAdresseLand().is(WellKnownLand.CHE) ? null
                    : Integer.parseInt(value.piaAdresseLand().getLaendercodeBfs())
            )
            .com(null)
            .forms(new LinkedList<>())
            .build();
    }

    private FormDto createFormDto(StatistikOfYear value, int ausbildungCounter) {
        return FormDtoBuilder.formDto()
            .formId(ausbildungCounter)
            .formation(
                FormationDtoBuilder.formationDto()
                    .formLevel(value.bfsKategorie())
                    .matuProf(StatistikUtil.booleanToBfsCode(value.besuchtBMS()))
                    .diploma(value.bfsStudienStufe())
                    .task(value.ausbildungspensum().getBfsCode())
                    .firstForm(StatistikUtil.booleanToBfsCode(value.isFirstAusbildung()))
                    .build()
            )
            .instIdentificationRoot(
                createInstIdentificationRoot(value)
            )
            .formPlace(
                value.isAusbildungAusland() ? Integer.parseInt(value.ausbildungLandBfs())
                    : getKanton(value.ausbildungPlz()).getBfsCode()
            )
            .com(null)
            .sums(new ArrayList<>())
            .build();
    }

    private static Kanton getKanton(final String ausbildungPlz) {
        try {
            return Kanton.valueOf(ausbildungPlz);
        } catch (Exception e) {
            throw new IllegalStateException(
                "No valid Kanton was found with given PLZ [%s]".formatted(ausbildungPlz), e
            );
        }
    }

    private InstIdentificationRootDto createInstIdentificationRoot(StatistikOfYear value) {
        final InstIdentificationRootDto instIdentificationRootDto = new InstIdentificationRootDto();

        if (StatistikUtil.HOCHSCHULSTUFEN_BFS_KATEGORIES.contains(value.bfsKategorie())) {
            var instCategory = value.ausbildungsstaetteNummerTyp().getBfsIdentification();

            if (value.ausbildungsstaetteNummerTyp().equals(AusbildungsstaetteNummerTyp.CT_NO)) {
                final var tenantIdentifier = tenantService.getCurrentTenantIdentifier();
                final var kanton = KantonUtil.getByTenantIdentifier(tenantIdentifier);
                instCategory = String.format("%s%s", instCategory, kanton.toString());
            }

            if (!value.ausbildungsstaetteNummerTyp().equals(AusbildungsstaetteNummerTyp.OHNE_NO)) {
                final var instCodeDto = InstCodeDto.builder()
                    .instCategory(instCategory)
                    .instId(value.ausbildungsstaetteNummer())
                    .build();

                instIdentificationRootDto.setInstCode(instCodeDto);
            } else {
                instIdentificationRootDto.setInstName(value.ausbildungsstaetteNameDe());
            }
        } else {
            instIdentificationRootDto.setInstName(value.ausbildungsstaetteNameDe());
        }

        return instIdentificationRootDto;
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
