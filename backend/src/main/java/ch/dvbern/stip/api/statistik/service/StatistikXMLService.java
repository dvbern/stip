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
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import ch.dvbern.stip.api.ausbildung.type.AusbildungsstaetteNummerTyp;
import ch.dvbern.stip.api.common.type.Kanton;
import ch.dvbern.stip.api.common.type.TenantIdentifier;
import ch.dvbern.stip.api.common.util.KantonUtil;
import ch.dvbern.stip.api.config.type.StipConfig;
import ch.dvbern.stip.api.dokument.service.DokumentUploadService;
import ch.dvbern.stip.api.land.type.WellKnownLand;
import ch.dvbern.stip.api.statistik.dto.FormDto;
import ch.dvbern.stip.api.statistik.dto.FormDtoBuilder;
import ch.dvbern.stip.api.statistik.dto.FormationDtoBuilder;
import ch.dvbern.stip.api.statistik.dto.HeadDtoBuilder;
import ch.dvbern.stip.api.statistik.dto.InstCodeDtoBuilder;
import ch.dvbern.stip.api.statistik.dto.InstIdentificationRootDto;
import ch.dvbern.stip.api.statistik.dto.InstIdentificationRootDtoBuilder;
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

    public void createAndSave(final int year, final String triggeredBy) {
        final var startTimestamp = LocalDateTime.now();
        LOG.info("Creating and saving statistik for year {} triggered by {}", year, triggeredBy);

        final var statistik = new Statistik();

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

        statistik.setUserTriggeredCreation(triggeredBy);
        statistik.setObjectId(objectId);
        statistik.setYear(year);
        statistik.setFilename(fileName);
        statistik.setFilepath(StatistikConstants.STATISTIK_FILE_PATH);
        statistik.setFilesize(outputStream.size());

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

        final var tenantIdentifier = tenantService.getCurrentTenantIdentifier();
        final var kanton = KantonUtil.getByTenantIdentifier(tenantIdentifier);

        final var personMap = new HashMap<String, PersDto>();
        final var ausbildungMap = new HashMap<UUID, FormDto>();
        var ausbildungCounter = 0;
        for (var value : statistikRepository.getStatistikValuesFor(year)) {
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
            .persons(personMap.values().stream().toList())
            .build();

        try {
            return generateXml(tableDto);
        } catch (JAXBException e) {
            LOG.error("Failed to generate XML for statistik for year {}", year, e);
            throw new RuntimeException("Failed to generate XML for statistik", e);
        }
    }

    private PersDto createPersonDto(StatistikOfYear value, TenantIdentifier tenantIdentifier) {
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
            .residencePermitCategoryType(
                Objects.nonNull(value.niederlassungsstatus())
                    ? String.valueOf(value.niederlassungsstatus().getBfsCode())
                    : null
            )
            .place(
                value.piaAdresseLand().is(WellKnownLand.CHE) ? StatistikUtil.getGemeindeBfsNummer(
                    value.gesuchId(),
                    value.piaAdresse(),
                    tenantIdentifier,
                    gemeindeLookupPortFactory
                )
                    .orElseThrow(StatistikGemeindeLookupException::new)
                    : null
            )
            .placeHist(null)
            .country(
                value.piaAdresseLand().is(WellKnownLand.CHE) ? null
                    : Integer.parseInt(value.piaAdresseLand().getLaendercodeBfs())
            )
            .com(null)
            .forms(new ArrayList<>())
            .build();
    }

    private static FormDto createFormDto(StatistikOfYear value, int ausbildungCounter) {
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
                    : Kanton.valueOf(value.ausbildungKanton()).getBfsCode()
            )
            .com(null)
            .sums(new ArrayList<>())
            .build();
    }

    private static InstIdentificationRootDto createInstIdentificationRoot(StatistikOfYear value) {
        final var instIdentificationRootDto = InstIdentificationRootDtoBuilder.instIdentificationRootDto()
            .instName(
                StatistikUtil.HOCHSCHULSTUFEN_BFS_KATEGORIES.contains(value.bfsKategorie()) ? null
                    : value.ausbildungsstaetteNameDe()
            );
        return instIdentificationRootDto
            .instCode(
                value.ausbildungsstaetteNummerTyp() == AusbildungsstaetteNummerTyp.OHNE_NO ? null
                    : InstCodeDtoBuilder.instCodeDto()
                        .instCategory(value.ausbildungsstaetteNummerTyp().getBfsIdentification())
                        .instId(value.ausbildungsstaetteNummer())
                        .build()
            )
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
