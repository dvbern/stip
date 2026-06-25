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

package ch.dvbern.stip.api.demo.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Stream;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.adresse.entity.AdresseBuilder;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.entity.AusbildungBuilder;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungRepository;
import ch.dvbern.stip.api.ausbildung.type.AusbildungsStatus;
import ch.dvbern.stip.api.auszahlung.entity.AuszahlungBuilder;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.entity.AbstractFamilieEntityBuilder;
import ch.dvbern.stip.api.common.entity.AbstractPersonBuilder;
import ch.dvbern.stip.api.common.exception.DemoDataApplyException;
import ch.dvbern.stip.api.common.exception.ValidationsException;
import ch.dvbern.stip.api.common.service.EntityCopyMapper;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.common.util.FileUtil;
import ch.dvbern.stip.api.common.validation.RequiredDokumentsProducer;
import ch.dvbern.stip.api.common.validation.RequiredRefDokumentsProducer;
import ch.dvbern.stip.api.config.type.StipConfig;
import ch.dvbern.stip.api.demo.entity.DemoData;
import ch.dvbern.stip.api.demo.entity.DemoPerson;
import ch.dvbern.stip.api.demo.repo.DemoDataAbschlussRepository;
import ch.dvbern.stip.api.demo.repo.DemoDataAusbildungsgangRepository;
import ch.dvbern.stip.api.demo.type.DemoDataDefaults;
import ch.dvbern.stip.api.demo.util.DemoDataAnonymizerUtil;
import ch.dvbern.stip.api.demo.util.ParseDemoDataUtil;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.entity.DokumentBuilder;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.entity.GesuchDokumentBuilder;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.type.GesuchDokumentStatus;
import ch.dvbern.stip.api.dokument.util.RequiredDokumentUtil;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKostenBuilder;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.entity.ElternBuilder;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.familiensituation.entity.FamiliensituationBuilder;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.geschwister.entity.Geschwister;
import ch.dvbern.stip.api.geschwister.entity.GeschwisterBuilder;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.service.GesuchNummerService;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchformular.repo.GesuchFormularRepository;
import ch.dvbern.stip.api.gesuchsperioden.service.GesuchsperiodenService;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.api.kind.entity.KindBuilder;
import ch.dvbern.stip.api.land.entity.Land;
import ch.dvbern.stip.api.land.repo.LandRepository;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItemBuilder;
import ch.dvbern.stip.api.partner.entity.Partner;
import ch.dvbern.stip.api.partner.entity.PartnerBuilder;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildungBuilder;
import ch.dvbern.stip.api.statusprotokoll.service.StatusprotokollService;
import ch.dvbern.stip.api.statusprotokoll.type.StatusprotokollEntryTyp;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.api.steuerdaten.entity.SteuerdatenBuilder;
import ch.dvbern.stip.api.steuererklaerung.entity.Steuererklaerung;
import ch.dvbern.stip.api.steuererklaerung.entity.SteuererklaerungBuilder;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.api.verfuegung.type.VerfuegungStatus;
import ch.dvbern.stip.api.zahlungsverbindung.entity.Zahlungsverbindung;
import ch.dvbern.stip.api.zahlungsverbindung.entity.ZahlungsverbindungBuilder;
import ch.dvbern.stip.berechnung.dto.InputUtils;
import ch.dvbern.stip.berechnung.service.BerechnungService;
import ch.dvbern.stip.berechnung.util.BerechnungUtil;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import ch.dvbern.stip.generated.dto.DemoAusbildungDto;
import ch.dvbern.stip.generated.dto.DemoDataDto;
import ch.dvbern.stip.generated.dto.DemoDataTestBerechnungResultatDto;
import ch.dvbern.stip.generated.dto.DemoDataTestBerechnungValidDto;
import ch.dvbern.stip.generated.dto.DemoDataTestBerechnungValuesDto;
import ch.dvbern.stip.generated.dto.DemoFamiliensituationDto;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.keycloak.common.util.TriFunction;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import static ch.dvbern.stip.api.common.util.DokumentDownloadConstants.GESUCH_DOKUMENT_PATH;

@RequiredArgsConstructor
@ApplicationScoped
public class GenerateDemoDataService {
    private final DemoDataAusbildungsgangRepository demoDataAusbildungsgangRepository;
    private final DemoDataAbschlussRepository demoDataAbschlussRepository;
    private final BenutzerService benutzerService;
    private final S3AsyncClient s3;
    private final EntityCopyMapper copyMapper;
    private final StipConfig config;
    private final TenantService tenantService;

    private final Instance<RequiredDokumentsProducer> requiredDokumentProducers;
    private final Instance<RequiredRefDokumentsProducer> requiredRefDokumentProducers;
    private final LandRepository landRepository;
    private final FallRepository fallRepository;
    private final AusbildungRepository ausbildungRepository;
    private final GesuchRepository gesuchRepository;
    private final GesuchFormularRepository gesuchFormularRepository;
    private final GesuchDokumentRepository gesuchDokumentRepository;
    private final GesuchTrancheRepository gesuchTrancheRepository;
    private final BerechnungService berechnungService;
    private final DokumentRepository dokumentRepository;
    private final GesuchsperiodenService gesuchsperiodenService;
    private final GesuchNummerService gesuchNummerService;
    private final Validator validator;
    private final StatusprotokollService statusprotokollService;

    private Land getLandIso2(String iso2Code) {
        try {
            return landRepository.getByIso2code(iso2Code).orElseThrow();
        } catch (Exception e) {
            throw new DemoDataApplyException("No Country with Iso Code \"%s\" found".formatted(iso2Code), e);
        }
    }

    private Ausbildungsgang getAusbildungsgang(DemoAusbildungDto ausbildungDto) {
        try {
            return demoDataAusbildungsgangRepository.requireAusbildungsgangByDemoData(ausbildungDto);
        } catch (Exception e) {
            throw new DemoDataApplyException(
                "No Ausbildung found for Ausbildungsstätte \"%s\" and Ausbildungsgang \"%s\""
                    .formatted(ausbildungDto.getAusbildungsstaette(), ausbildungDto.getAusbildungsgang()),
                e
            );
        }
    }

    private LocalDate getGeburtsdatum(DemoData demoData, String geburtsdatum, int alter) {
        final var targetYear = demoData.getGesuchsjahr() - alter;
        return LocalDate.parse("%s.%d".formatted(geburtsdatum, targetYear), ParseDemoDataUtil.dmyFormatter);
    }

    private Zahlungsverbindung getDefaultZahlungsverbindung(DemoDataDto demoDataDto, Adresse adresse) {
        final var pia = demoDataDto.getPersonInAusbildung();
        return ZahlungsverbindungBuilder.zahlungsverbindung()
            .vorname(pia.getVorname())
            .nachname(pia.getNachname())
            .adresse(adresse)
            .iban(DemoDataDefaults.ZAHLUNGSVERBINBDUNG_IBAN)
            .institution(null)
            .build();
    }

    @WithSpan
    public Gesuch createEinreichableGesuch(DemoData demoData, Fall fall) {
        // <editor-fold desc="Prepare..." defaultstate="collapsed">
        final var demoDataDto = demoData.parseDemoDataDto();
        final var piaDto = demoDataDto.getPersonInAusbildung();
        final var piaAdresse = AdresseBuilder.adresse()
            .land(getLandIso2(piaDto.getLand()))
            .coAdresse(piaDto.getCoAdresse())
            .strasse(piaDto.getStrasse())
            .hausnummer(piaDto.getHausnummer())
            .plz(piaDto.getPlz())
            .ort(piaDto.getOrt())
            .build();
        final var auszahlung = AuszahlungBuilder.auszahlung()
            .zahlungsverbindung(getDefaultZahlungsverbindung(demoDataDto, copyMapper.createCopy(piaAdresse)))
            .auszahlungAnSozialdienst(false)
            .sapBusinessPartnerId(null)
            .buchhaltung(null)
            .build();
        fall.setAuszahlung(auszahlung);
        final var ausbildungDto = demoDataDto.getAusbildung();
        // </editor-fold>

        // <editor-fold desc="Ausbildung..." defaultstate="collapsed">
        // Reuse an existing Ausbildung if it exists and is not completed, otherwise create a new one
        final var ausbildung = fall.getAusbildungs()
            .stream()
            .filter(a -> !a.getStatus().isCompleted())
            .max(Comparator.comparing(Ausbildung::getAusbildungBegin))
            .orElseGet(
                () -> AusbildungBuilder.ausbildung()
                    .fall(fall)
                    .gesuchs(new ArrayList<>())
                    .ausbildungsgang(
                        !ausbildungDto.getAusbildungNichtGefunden() ? getAusbildungsgang(ausbildungDto) : null
                    )
                    .besuchtBMS(DemoDataDefaults.AUSBILDUNG_BESUCHT_BMS)
                    .alternativeAusbildungsgang(
                        ausbildungDto.getAusbildungNichtGefunden() ? ausbildungDto.getAusbildungsgang() : null
                    )
                    .alternativeAusbildungsstaette(
                        ausbildungDto.getAusbildungNichtGefunden() ? ausbildungDto.getAusbildungsstaette() : null
                    )
                    .fachrichtungBerufsbezeichnung(ausbildungDto.getBerufsbezeichnungFachrichtung())
                    .ausbildungNichtGefunden(ausbildungDto.getAusbildungNichtGefunden())
                    .ausbildungBegin(ausbildungDto.getAusbildungBeginn())
                    .ausbildungEnd(ausbildungDto.getAusbildungEnd())
                    .pensum(Objects.requireNonNullElse(ausbildungDto.getPensum(), DemoDataDefaults.AUSBILDUNG_PENSUM))
                    .ausbildungsort(ausbildungDto.getOrt())
                    .ausbildungsortPLZ(ausbildungDto.getPlz())
                    .isAusbildungAusland(ausbildungDto.getIsAusbildungAusland())
                    .land(null)
                    .status(AusbildungsStatus.AKTIV)
                    .ausbildungUnterbruchAntrags(List.of())
                    .build()
            );
        // </editor-fold>

        // <editor-fold desc="PiA..." defaultstate="collapsed">
        final var pia = DemoPerson.createPersonInAusbildung(
            PersonInAusbildungBuilder.personInAusbildung()
                .adresse(piaAdresse)
                .sozialversicherungsnummer(DemoDataDefaults.generateSVN())
                .anrede(piaDto.getAnrede())
                .identischerZivilrechtlicherWohnsitz(piaDto.getIdentischerZivilrechtlicherWohnsitz())
                .identischerZivilrechtlicherWohnsitzOrt(
                    !piaDto.getIdentischerZivilrechtlicherWohnsitz()
                        ? piaDto.getIdentischerZivilrechtlicherWohnsitzOrt()
                        : null
                )
                .identischerZivilrechtlicherWohnsitzPLZ(
                    !piaDto.getIdentischerZivilrechtlicherWohnsitz()
                        ? piaDto.getIdentischerZivilrechtlicherWohnsitzPLZ()
                        : null
                )
                .email(piaDto.getEmail())
                .telefonnummer(piaDto.getTelefonnummer())
                .nationalitaet(getLandIso2(piaDto.getNationalitaet()))
                .heimatort(piaDto.getHeimatort())
                .heimatortPLZ(piaDto.getHeimatortPLZ())
                .niederlassungsstatus(piaDto.getNiederlassungsstatus())
                .einreisedatum(piaDto.getEinreisedatum())
                .zivilstand(piaDto.getZivilstand())
                .sozialhilfebeitraege(piaDto.getSozialhilfebeitraege())
                .vormundschaft(
                    Objects.requireNonNullElse(piaDto.getVormundschaft(), DemoDataDefaults.PIA_VORMUNDSCHAFT)
                )
                .korrespondenzSprache(DemoDataDefaults.SPRACHE)
                .zustaendigeKESB(piaDto.getZustaendigeKESB()),
            AbstractFamilieEntityBuilder.abstractFamilieEntity()
                .wohnsitz(piaDto.getWohnsitz())
                .wohnsitzAnteilMutter(
                    piaDto.getWohnsitz() == Wohnsitz.MUTTER_VATER
                        ? DemoDataDefaults.bigDecimalNullable(piaDto.getWohnsitzAnteilMutter())
                        : null
                )
                .wohnsitzAnteilVater(
                    piaDto.getWohnsitz() == Wohnsitz.MUTTER_VATER
                        ? DemoDataDefaults.bigDecimalNullable(piaDto.getWohnsitzAnteilVater())
                        : null
                ),
            AbstractPersonBuilder.abstractPerson()
                .nachname(piaDto.getNachname())
                .vorname(piaDto.getNachname())
                .geburtsdatum(getGeburtsdatum(demoData, piaDto.getGeburtsdatum(), piaDto.getAlter()))
        );
        // </editor-fold>

        // <editor-fold desc="Lebenslauf Items..." defaultstate="collapsed">
        final Set<LebenslaufItem> lebenslaufItems = new HashSet<>();
        for (var lebenslaufItemDto : demoDataDto.getLebenslauf().getAusbildung()) {
            final var lebenslauf = LebenslaufItemBuilder.lebenslaufItem()
                .abschluss(demoDataAbschlussRepository.requireByAbschlussName(lebenslaufItemDto.getAbschluss()))
                .von(lebenslaufItemDto.getVon())
                .bis(lebenslaufItemDto.getBis())
                .taetigkeitsart(null)
                .taetigkeitsBeschreibung(null)
                .fachrichtungBerufsbezeichnung(lebenslaufItemDto.getBerufsbezeichnungFachrichtung())
                .ausbildungAbgeschlossen(lebenslaufItemDto.getAusbildungAbgeschlossen())
                .wohnsitz(
                    Objects.requireNonNullElse(
                        lebenslaufItemDto.getWohnsitz(),
                        DemoDataDefaults.LEBENSLAUF_WOHNSITZ_KANTON
                    )
                )
                .build();
            lebenslaufItems.add(lebenslauf);
        }
        for (var lebenslaufItemDto : demoDataDto.getLebenslauf().getTaetigkeiten()) {
            final var lebenslauf = LebenslaufItemBuilder.lebenslaufItem()
                .abschluss(null)
                .von(lebenslaufItemDto.getVon())
                .bis(lebenslaufItemDto.getBis())
                .taetigkeitsart(lebenslaufItemDto.getTaetigkeitsart())
                .taetigkeitsBeschreibung(lebenslaufItemDto.getTaetigkeitsBeschreibung())
                .fachrichtungBerufsbezeichnung(null)
                .ausbildungAbgeschlossen(false)
                .wohnsitz(
                    Objects.requireNonNullElse(
                        lebenslaufItemDto.getWohnsitz(),
                        DemoDataDefaults.LEBENSLAUF_WOHNSITZ_KANTON
                    )
                )
                .build();
            lebenslaufItems.add(lebenslauf);
        }
        // </editor-fold>

        // <editor-fold desc="Partner..." defaultstate="collapsed">
        Partner partner = null;
        final var demoPartnerDto = demoDataDto.getPartner();
        if (Objects.nonNull(demoPartnerDto)) {
            partner = DemoPerson.createPartner(
                PartnerBuilder.partner()
                    .adresse(
                        AdresseBuilder.adresse()
                            .land(getLandIso2(demoPartnerDto.getLand()))
                            .coAdresse(demoPartnerDto.getCoAdresse())
                            .strasse(demoPartnerDto.getStrasse())
                            .hausnummer(demoPartnerDto.getHausnummer())
                            .plz(demoPartnerDto.getPlz())
                            .ort(demoPartnerDto.getOrt())
                            .build()
                    )
                    .sozialversicherungsnummer(DemoDataDefaults.generateSVN())
                    .inAusbildung(demoPartnerDto.getInAusbildung())
                    .ausbildungspensum(demoPartnerDto.getPensum()),
                AbstractPersonBuilder.abstractPerson()
                    .nachname(demoPartnerDto.getNachname())
                    .vorname(demoPartnerDto.getVorname())
                    .geburtsdatum(
                        getGeburtsdatum(demoData, demoPartnerDto.getGeburtsdatum(), demoPartnerDto.getAlter())
                    )
            );
        }
        // </editor-fold>

        // <editor-fold desc="Kinds..." defaultstate="collapsed">
        final Set<Kind> kinds = new HashSet<>();
        for (var kindDto : demoDataDto.getKinder()) {
            final var kind = DemoPerson.createKind(
                KindBuilder.kind()
                    .ausbildungssituation(kindDto.getAusbildungssituation())
                    .unterhaltsbeitraege(
                        DemoDataDefaults.defaultByKindsIfNull(kindDto.getUnterhaltsbeitraege(), demoDataDto)
                    )
                    .entryId(UUID.randomUUID())
                    .wohnsitzAnteilPia(kindDto.getWohnsitzAnteilPia())
                    .ergaenzungsleistungen(kindDto.getErgaenzungsleistungen())
                    .kinderUndAusbildungszulagen(kindDto.getKinderUndAusbildungszulagen())
                    .renten(kindDto.getRenten())
                    .andereEinnahmen(kindDto.getAndereEinnahmen()),
                AbstractPersonBuilder.abstractPerson()
                    .nachname(kindDto.getNachname())
                    .vorname(kindDto.getVorname())
                    .geburtsdatum(getGeburtsdatum(demoData, kindDto.getGeburtsdatum(), kindDto.getAlter()))
            );
            kinds.add(kind);
        }
        // </editor-fold>

        // <editor-fold desc="Einnahmen & Kosten..." defaultstate="collapsed">
        final var ekDto = demoDataDto.getEinnahmenKosten();
        final var einnahmenKosten = EinnahmenKostenBuilder.einnahmenKosten()
            .nettoerwerbseinkommen(ekDto.getNettoerwerbseinkommen())
            .fahrkosten(ekDto.getFahrkosten())
            .wohnkosten(ekDto.getWohnkosten())
            .wgWohnend(ekDto.getWgWohnend())
            .wgAnzahlPersonen(ekDto.getWgWohnend() ? DemoDataDefaults.EK_WG_ANZAHL_PERSONEN : null)
            .alternativeWohnformWohnend(ekDto.getAlternativeWohnformWohnend())
            .unterhaltsbeitraege(ekDto.getUnterhaltsbeitraege())
            .zulagen(DemoDataDefaults.defaultByKindsIfNull(ekDto.getZulagen(), demoDataDto))
            .renten(ekDto.getRenten())
            .eoLeistungen(ekDto.getEoLeistungen())
            .ergaenzungsleistungen(ekDto.getErgaenzungsleistungen())
            .beitraege(ekDto.getBeitraege())
            .ausbildungskosten(ekDto.getAusbildungskosten())
            .auswaertigeMittagessenProWoche(ekDto.getAuswaertigeMittagessenProWoche())
            .verpflegungskosten(null)
            .betreuungskostenKinder(
                DemoDataDefaults.defaultByKindsIfNull(ekDto.getBetreuungskostenKinder(), demoDataDto)
            )
            .veranlagungsStatus(DemoDataDefaults.STEUERDATEN_VERANLAGUNGSSTATUS)
            .steuerjahr(DemoDataDefaults.getSteuerjahr(ausbildungDto.getAusbildungBeginn()))
            .steuern(ekDto.getSteuernKantonGemeinde())
            .vermoegen(ekDto.getVermoegen())
            .einnahmenBGSA(ekDto.getEinnahmenBGSA())
            .taggelderAHVIV(ekDto.getTaggelderAHVIV())
            .andereEinnahmen(ekDto.getAndereEinnahmen())
            .arbeitspensumProzent(
                Objects.requireNonNullElse(ekDto.getNettoerwerbseinkommen(), 0) > 0 ? DemoDataDefaults.EK_ARBEITSPENSUM
                    : null
            )
            .build();
        EinnahmenKosten einnahmenKostenPartner = null;
        if (Objects.nonNull(demoPartnerDto)) {
            var ekPartnerDto = demoDataDto.getEinnahmenKostenPartner();
            einnahmenKostenPartner = EinnahmenKostenBuilder.einnahmenKosten()
                .nettoerwerbseinkommen(
                    Objects.requireNonNullElse(ekPartnerDto.getNettoerwerbseinkommen(), DemoDataDefaults.EK_EINKOMMEN)
                )
                .fahrkosten(Objects.requireNonNullElse(ekPartnerDto.getFahrkosten(), DemoDataDefaults.EK_FAHRKOSTEN))
                .wohnkosten(null)
                .wgWohnend(null)
                .wgAnzahlPersonen(null)
                .alternativeWohnformWohnend(null)
                .unterhaltsbeitraege(ekPartnerDto.getUnterhaltsbeitraege())
                .zulagen(DemoDataDefaults.defaultByKindsIfNull(ekPartnerDto.getZulagen(), demoDataDto))
                .renten(ekPartnerDto.getRenten())
                .eoLeistungen(ekPartnerDto.getEoLeistungen())
                .ergaenzungsleistungen(ekPartnerDto.getErgaenzungsleistungen())
                .beitraege(ekPartnerDto.getBeitraege())
                .ausbildungskosten(null)
                .auswaertigeMittagessenProWoche(ekPartnerDto.getAuswaertigeMittagessenProWoche())
                .verpflegungskosten(
                    Objects.requireNonNullElse(
                        ekPartnerDto.getVerpflegungskosten(),
                        DemoDataDefaults.EK_VERPFLEGUNGSKOSTEN
                    )
                )
                .betreuungskostenKinder(
                    DemoDataDefaults.defaultByKindsIfNull(ekDto.getBetreuungskostenKinder(), demoDataDto)
                )
                .veranlagungsStatus(DemoDataDefaults.STEUERDATEN_VERANLAGUNGSSTATUS)
                .steuerjahr(DemoDataDefaults.getSteuerjahr(ausbildungDto.getAusbildungBeginn()))
                .steuern(ekPartnerDto.getSteuernKantonGemeinde())
                .vermoegen(Objects.requireNonNullElse(ekPartnerDto.getVermoegen(), DemoDataDefaults.EK_VERMOEGEN))
                .einnahmenBGSA(ekPartnerDto.getEinnahmenBGSA())
                .taggelderAHVIV(ekPartnerDto.getTaggelderAHVIV())
                .andereEinnahmen(ekPartnerDto.getAndereEinnahmen())
                .arbeitspensumProzent(
                    Objects.requireNonNullElse(ekPartnerDto.getNettoerwerbseinkommen(), 0) > 0
                        ? DemoDataDefaults.EK_ARBEITSPENSUM
                        : null
                )
                .build();
        }
        // </editor-fold>

        // <editor-fold desc="Familiensituation..." defaultstate="collapsed">
        final var famsitDto = demoDataDto.getFamiliensituation();
        final var familiensituation = FamiliensituationBuilder.familiensituation()
            .elternVerheiratetZusammen(famsitDto.getElternVerheiratetZusammen())
            .elternteilUnbekanntVerstorben(famsitDto.getElternteilUnbekanntVerstorben())
            .gerichtlicheAlimentenregelung(famsitDto.getGerichtlicheAlimentenregelung())
            .mutterUnbekanntVerstorben(getAbwesenheitsGrund(famsitDto, ElternTyp.MUTTER))
            .mutterUnbekanntGrund(famsitDto.getMutterUnbekanntGrund())
            .vaterUnbekanntVerstorben(getAbwesenheitsGrund(famsitDto, ElternTyp.VATER))
            .vaterUnbekanntGrund(famsitDto.getVaterUnbekanntGrund())
            .werZahltAlimente(famsitDto.getWerZahltAlimente())
            .build();
        // </editor-fold>

        // <editor-fold desc="Elterns..." defaultstate="collapsed">
        final List<Eltern> elterns = new ArrayList<>();
        for (var elternDto : demoDataDto.getElterns()) {
            elterns.add(
                DemoPerson.createEltern(
                    ElternBuilder.eltern()
                        .adresse(
                            AdresseBuilder.adresse()
                                .land(getLandIso2(elternDto.getLand()))
                                .coAdresse(elternDto.getCoAdresse())
                                .strasse(elternDto.getStrasse())
                                .hausnummer(elternDto.getHausnummer())
                                .plz(elternDto.getPlz())
                                .ort(elternDto.getOrt())
                                .build()
                        )
                        .sozialversicherungsnummer(elternDto.getSozialversicherungsnummer())
                        .elternTyp(elternDto.getType())
                        .telefonnummer(elternDto.getTelefonnummer())
                        .ausweisbFluechtling(elternDto.getAusweisbFluechtling())
                        .identischerZivilrechtlicherWohnsitz(elternDto.getIdentischerZivilrechtlicherWohnsitz())
                        .identischerZivilrechtlicherWohnsitzOrt(elternDto.getIdentischerZivilrechtlicherWohnsitzOrt())
                        .identischerZivilrechtlicherWohnsitzPLZ(elternDto.getIdentischerZivilrechtlicherWohnsitzPLZ())
                        .sozialhilfebeitraege(elternDto.getSozialhilfebeitraege())
                        .wohnkosten(elternDto.getWohnkosten())
                        .wiederverheiratet(elternDto.getWiederverheiratet()),
                    AbstractPersonBuilder.abstractPerson()
                        .nachname(elternDto.getNachname())
                        .vorname(elternDto.getVorname())
                        .geburtsdatum(getGeburtsdatum(demoData, elternDto.getGeburtsdatum(), elternDto.getAlter()))
                )
            );
        }
        // </editor-fold>

        // <editor-fold desc="Steuererklaerungs..." defaultstate="collapsed">
        final List<Steuererklaerung> steuererklaerungs = new ArrayList<>();
        for (var steuererklaerungDto : demoDataDto.getSteuererklaerung()) {
            steuererklaerungs.add(
                SteuererklaerungBuilder.steuererklaerung()
                    .steuerdatenTyp(steuererklaerungDto.getType())
                    .steuererklaerungInBern(steuererklaerungDto.getSteuererklaerungInBern())
                    .ergaenzungsleistungen(steuererklaerungDto.getErgaenzungsleistungen())
                    .unterhaltsbeitraege(steuererklaerungDto.getUnterhaltsbeitraege())
                    .renten(steuererklaerungDto.getRenten())
                    .einnahmenBGSA(steuererklaerungDto.getEinnahmenBGSA())
                    .andereEinnahmen(steuererklaerungDto.getAndereEinnahmen())
                    .build()
            );
        }
        // </editor-fold>

        // <editor-fold desc="Steuerdatens..." defaultstate="collapsed">
        final List<Steuerdaten> steuerdatens = new ArrayList<>();
        for (var steuerdatenDto : demoDataDto.getSteuerdaten()) {
            steuerdatens.add(
                SteuerdatenBuilder.steuerdaten()
                    .steuerdatenTyp(steuerdatenDto.getType())
                    .totalEinkuenfte(steuerdatenDto.getTotalEinkuenfte())
                    .eigenmietwert(
                        Objects.requireNonNullElse(
                            steuerdatenDto.getEigenmietwert(),
                            DemoDataDefaults.STEUERDATEN_EIGENMIETWERT
                        )
                    )
                    .isArbeitsverhaeltnisSelbstaendig(steuerdatenDto.getIsArbeitsverhaeltnisSelbstaendig())
                    .saeule3a(steuerdatenDto.getSaeule3a())
                    .saeule2(steuerdatenDto.getSaeule2())
                    .vermoegen(
                        Objects
                            .requireNonNullElse(steuerdatenDto.getVermoegen(), DemoDataDefaults.STEUERDATEN_VERMOEGEN)
                    )
                    .steuernKantonGemeinde(
                        Objects.requireNonNullElse(
                            steuerdatenDto.getSteuernKantonGemeinde(),
                            DemoDataDefaults.STEUERDATEN_STEUERN_KANTON_GEMEINDE
                        )
                    )
                    .steuernBund(
                        Objects.requireNonNullElse(
                            steuerdatenDto.getSteuernBund(),
                            DemoDataDefaults.STEUERDATEN_STEUERN_BUND
                        )
                    )
                    .fahrkosten(steuerdatenDto.getFahrkosten())
                    .fahrkostenPartner(steuerdatenDto.getFahrkostenPartner())
                    .verpflegung(steuerdatenDto.getVerpflegung())
                    .verpflegungPartner(steuerdatenDto.getVerpflegungPartner())
                    .steuerjahr(
                        Objects.requireNonNullElse(
                            steuerdatenDto.getSteuerjahr(),
                            DemoDataDefaults.getSteuerjahr(ausbildungDto.getAusbildungBeginn())
                        )
                    )
                    .veranlagungsStatus(
                        Objects.requireNonNullElse(
                            steuerdatenDto.getVeranlagungsStatus(),
                            DemoDataDefaults.STEUERDATEN_VERANLAGUNGSSTATUS
                        )
                    )
                    .build()
            );
        }
        // </editor-fold>

        // <editor-fold desc="Geschwisters..." defaultstate="collapsed">
        final List<Geschwister> geschwisters = new ArrayList<>();
        for (var geschwisterDto : demoDataDto.getGeschwister()) {
            geschwisters.add(
                DemoPerson.createGeschwister(
                    GeschwisterBuilder.geschwister()
                        .ausbildungssituation(geschwisterDto.getAusbildungssituation())
                        .entryId(UUID.randomUUID())
                        .hidden(false),
                    AbstractFamilieEntityBuilder.abstractFamilieEntity()
                        .wohnsitz(geschwisterDto.getWohnsitzBei())
                        .wohnsitzAnteilMutter(
                            DemoDataDefaults.bigDecimalNullable(geschwisterDto.getWohnsitzAnteilMutter())
                        )
                        .wohnsitzAnteilVater(
                            DemoDataDefaults.bigDecimalNullable(geschwisterDto.getWohnsitzAnteilVater())
                        ),
                    AbstractPersonBuilder.abstractPerson()
                        .nachname(geschwisterDto.getNachname())
                        .vorname(geschwisterDto.getVorname())
                        .geburtsdatum(
                            getGeburtsdatum(demoData, geschwisterDto.getGeburtsdatum(), geschwisterDto.getAlter())
                        )
                )
            );
        }
        // </editor-fold>

        final Gesuch gesuch = new Gesuch();
        gesuch.setAusbildung(ausbildung);

        final var gesuchsperiode = gesuchsperiodenService.getGesuchsperiodeForAusbildung(
            ausbildung
        ).getLeft();

        var ausbildungsstart = gesuch
            .getAusbildung()
            .getAusbildungBegin()
            .withYear(gesuchsperiode.getGesuchsperiodeStart().getYear());
        if (ausbildungsstart.isAfter(gesuchsperiode.getGesuchsperiodeStopp())) {
            ausbildungsstart = ausbildungsstart.minusYears(1);
        }

        var tranche = new GesuchTranche()
            .setGueltigkeit(new DateRange(ausbildungsstart, ausbildungsstart.plusYears(1).minusDays(1)))
            .setGesuch(gesuch)
            .setGesuchFormular(new GesuchFormular())
            .setTyp(GesuchTrancheTyp.TRANCHE);

        tranche.getGesuchFormular().setTranche(tranche);

        gesuch.getGesuchTranchen().add(tranche);
        gesuch.setGesuchsperiode(gesuchsperiode);
        gesuch.setGesuchNummer(gesuchNummerService.createGesuchNummer(gesuch.getGesuchsperiode().getId()));

        final var gesuchFormular = gesuch
            .getLatestGesuchTranche()
            .getGesuchFormular();
        gesuchFormular
            .setPersonInAusbildung(pia)
            .setPartner(partner)
            .setEinnahmenKosten(einnahmenKosten)
            .setEinnahmenKostenPartner(einnahmenKostenPartner)
            .setFamiliensituation(familiensituation);
        gesuchFormular.getLebenslaufItems().addAll(lebenslaufItems);
        gesuchFormular.getKinds().addAll(kinds);
        gesuchFormular.getElterns().addAll(elterns);
        gesuchFormular.getSteuererklaerung().addAll(steuererklaerungs);
        gesuchFormular.getSteuerdaten().addAll(steuerdatens);
        gesuchFormular.getGeschwisters().addAll(geschwisters);
        return gesuch;
    }

    @Transactional
    public UUID createAndPersistEinreichableGesuch(DemoData demoData) {
        final var gesuchstellerId = benutzerService.getCurrentBenutzer().getId();
        final var fall = fallRepository.findFallForGsOptional(gesuchstellerId).orElseThrow();

        final var gesuch = createEinreichableGesuch(demoData, fall);

        fallRepository.persist(fall);
        ausbildungRepository.persist(gesuch.getAusbildung());

        Set<ConstraintViolation<Ausbildung>> violations = validator.validate(gesuch.getAusbildung());
        if (!violations.isEmpty()) {
            throw new ValidationsException(ValidationsException.ENTITY_NOT_VALID_MESSAGE, violations);
        }

        gesuchRepository.persistAndFlush(gesuch);
        statusprotokollService.createStatusprotokoll(
            Gesuchstatus.IN_BEARBEITUNG_GS.toString(),
            null,
            StatusprotokollEntryTyp.GESUCH,
            null,
            gesuch
        );

        final var gesuchFormular = gesuch
            .getLatestGesuchTranche()
            .getGesuchFormular();

        gesuchFormularRepository.persist(gesuchFormular);
        gesuchRepository.persistAndFlush(gesuch);
        DemoDataAnonymizerUtil.anonymizeGesuch(demoData, gesuch);
        gesuchRepository.persistAndFlush(gesuch);

        return gesuch.getId();
    }

    public DemoDataTestBerechnungResultatDto getBerechnungResultatDto(Gesuch gesuch, DemoData demoData) {
        final var initialEinreicheDatum = gesuch.getEinreichedatum();
        // Temporarily set the einreichedatum to the gesuchseingang date from demoData and reset it afterwards
        if (Objects.isNull(initialEinreicheDatum)) {
            gesuch.setEinreichedatum(LocalDate.parse(demoData.getGesuchseingang(), ParseDemoDataUtil.dmyFormatter));
        }

        final var berechnungResultatSoll = demoData.parseDemoDataDto().getBerechnungValues();
        var berechnungsResultat = new BerechnungsresultatDto();
        var berechnungsResultatIst = new DemoDataTestBerechnungValuesDto();
        final var tenantConfig = tenantService.getConfigForCurrentTenant();
        String message = null;
        VerfuegungStatus statusIst;
        try {
            berechnungsResultat = berechnungService.getBerechnungsresultatFromGesuch(
                gesuch,
                tenantConfig.berechnung().currentMajorVersion(),
                tenantConfig.berechnung().currentMinorVersion()
            );
            statusIst = InputUtils.sumNullables(
                berechnungsResultat.getBerechnungStipendium(),
                berechnungsResultat.getBerechnungDarlehen()
            ) > 0 ? VerfuegungStatus.ANSPRUCH : VerfuegungStatus.KEIN_ANSPRUCH;
            var stipendien = berechnungsResultat.getBerechnungStipendium();
            var darlehen = berechnungsResultat.getBerechnungDarlehen();
            if (demoData.getAnzahlMonate() != 12) {
                if (Objects.nonNull(stipendien)) {
                    stipendien = BigDecimal.valueOf(stipendien)
                        .divide(BigDecimal.valueOf(12), 2, RoundingMode.UP)
                        .multiply(BigDecimal.valueOf(demoData.getAnzahlMonate()))
                        .intValue();
                }
                if (Objects.nonNull(darlehen)) {
                    darlehen = BigDecimal.valueOf(darlehen)
                        .divide(BigDecimal.valueOf(12), 2, RoundingMode.UP)
                        .multiply(BigDecimal.valueOf(demoData.getAnzahlMonate()))
                        .intValue();
                }
            }
            berechnungsResultatIst = new DemoDataTestBerechnungValuesDto()
                .status(statusIst)
                .ungekuerztStipendien(berechnungsResultat.getUngekuerztStipendien())
                .ungekuerztDarlehen(berechnungsResultat.getUngekuerztDarlehen())
                .stipendien(stipendien)
                .darlehen(darlehen);
        } catch (Exception e) {
            message = e.getMessage();
        } finally {
            if (Objects.isNull(initialEinreicheDatum)) {
                gesuch.setEinreichedatum(null);
            }
        }

        return new DemoDataTestBerechnungResultatDto()
            .demoDataId(demoData.getId())
            .testFall(demoData.getTestFall())
            .valid(
                new DemoDataTestBerechnungValidDto()
                    // Status is not possible to compare at the moment because of manual negative Verfügung, etc.
                    .status(true)
                    .ungekuerztStipendien(
                        BerechnungUtil.nullableCompare(
                            berechnungResultatSoll.getUngekuerztStipendien(),
                            berechnungsResultatIst.getUngekuerztStipendien(),
                            0
                        )
                    )
                    .ungekuerztDarlehen(
                        BerechnungUtil.nullableCompare(
                            berechnungResultatSoll.getUngekuerztDarlehen(),
                            berechnungsResultatIst.getUngekuerztDarlehen(),
                            0
                        )
                    )
                    .stipendien(
                        BerechnungUtil.nullableCompare(
                            berechnungResultatSoll.getStipendien(),
                            berechnungsResultatIst.getStipendien(),
                            0
                        )
                    )
                    .darlehen(
                        BerechnungUtil.nullableCompare(
                            berechnungResultatSoll.getDarlehen(),
                            berechnungsResultatIst.getDarlehen(),
                            0
                        )
                    )
            )
            .soll(berechnungResultatSoll)
            .ist(berechnungsResultatIst)
            .message(message);
    }

    private ElternAbwesenheitsGrund getAbwesenheitsGrund(DemoFamiliensituationDto dto, ElternTyp elternTyp) {
        TriFunction<Boolean, Boolean, Boolean, ElternAbwesenheitsGrund> getGrund =
            (verstorben, unbekannt, wedernoch) -> Boolean.TRUE.equals(verstorben) ? ElternAbwesenheitsGrund.VERSTORBEN
                : Boolean.TRUE.equals(unbekannt) ? ElternAbwesenheitsGrund.UNBEKANNT
                : Boolean.TRUE.equals(wedernoch) ? ElternAbwesenheitsGrund.WEDER_NOCH
                : null;
        return switch (elternTyp) {
            case ElternTyp.MUTTER -> getGrund
                .apply(dto.getMutterVerstorben(), dto.getMutterUnbekannt(), dto.getMutterKeineOptionen());
            case ElternTyp.VATER -> getGrund
                .apply(dto.getVaterVerstorben(), dto.getVaterUnbekannt(), dto.getVaterKeineOptionen());
        };
    }

    @SafeVarargs
    private <T> T firstSetValueOrNull(T... value) {
        return Arrays.stream(value).filter(Objects::nonNull).findFirst().orElse(null);
    }

    public void createDemoDokumentsForAllRequired(UUID gesuchTrancheId) {
        createDemoDokumentsForAllRequired(gesuchTrancheRepository.requireById(gesuchTrancheId));
    }

    public List<Dokument> createDemoDokumentsForAllRequired(GesuchTranche gesuchTranche) {
        return createDemoDokumentsForAllRequired(gesuchTranche, this::createS3EntriesForDokumente);
    }

    public List<Dokument> createDemoDokumentsForAllRequired(
        GesuchTranche gesuchTranche,
        Consumer<List<Dokument>> handleS3
    ) {
        final var requiredDokuments = RequiredDokumentUtil.getRequiredDokumentTypesForGesuch(
            gesuchTranche.getGesuchFormular(),
            requiredDokumentProducers,
            true
        );
        final var requiredListDocuments = RequiredDokumentUtil.getRequiredListDokumentRefsForGesuch(
            gesuchTranche.getGesuchFormular(),
            requiredRefDokumentProducers,
            true
        );

        final var gesuchDokuments = Stream.concat(
            requiredDokuments.stream()
                .map(dokumentTyp -> createDemoGesuchDokumentWithoutUpload(dokumentTyp, gesuchTranche)),
            requiredListDocuments.stream()
                .map(pair -> createDemoGesuchDokumentWithoutUpload(pair.getLeft(), gesuchTranche, pair.getRight()))
        ).toList();
        final var allDokuments =
            gesuchDokuments.stream().flatMap(gesuchDokument -> gesuchDokument.getDokumente().stream()).toList();

        handleS3.accept(allDokuments);

        gesuchTranche.getGesuchDokuments().addAll(gesuchDokuments);
        dokumentRepository.persist(allDokuments);
        gesuchDokumentRepository.persist(gesuchDokuments);
        gesuchTrancheRepository.persist(gesuchTranche);

        return allDokuments;
    }

    private GesuchDokument createDemoGesuchDokumentWithoutUpload(
        DokumentTyp dokumentTyp,
        GesuchTranche gesuchTranche,
        UUID entryId
    ) {
        return createDemoGesuchDokumentWithoutUpload(dokumentTyp, gesuchTranche).setEntryId(entryId);
    }

    private GesuchDokument createDemoGesuchDokumentWithoutUpload(DokumentTyp dokumentTyp, GesuchTranche gesuchTranche) {
        final var dokument = DokumentBuilder.dokument()
            .filename("demodata.png")
            .filepath(GESUCH_DOKUMENT_PATH)
            .filesize("1")
            .objectId(null)
            .build();
        final var gesuchDokument = GesuchDokumentBuilder.gesuchDokument()
            .gesuchTranche(gesuchTranche)
            .dokumentTyp(dokumentTyp)
            .customDokumentTyp(null)
            .status(GesuchDokumentStatus.AUSSTEHEND)
            .dokumente(List.of(dokument))
            .gesuchDokumentKommentare(new ArrayList<>())
            .build();
        dokument.setGesuchDokument(gesuchDokument);

        return gesuchDokument;
    }

    public void createS3EntriesForDokumente(List<Dokument> dokuments) {
        final var pngBody = AsyncRequestBody.fromBytes(Base64.decodeBase64(config.demo().smallestPng()));
        String firstDokumentKey = null;
        CompletableFuture<String> dokumentUploadOrCopyRequest = CompletableFuture.supplyAsync(() -> "pending");
        for (Dokument dokument : dokuments) {
            final var objectId = FileUtil.generateUUIDWithFileExtension(dokument.getFilename());
            final var key = dokument.getFilepath() + objectId;
            if (Objects.isNull(firstDokumentKey)) {
                dokumentUploadOrCopyRequest
                    .thenCombineAsync(s3.putObject(buildPutRequest(key), pngBody), (a, b) -> "ok");
                firstDokumentKey = key;
            } else {
                dokumentUploadOrCopyRequest
                    .thenCombineAsync(s3.copyObject(buildCopyRequest(firstDokumentKey, key)), (a, b) -> "ok");
            }
            Uni.createFrom().completionStage(dokumentUploadOrCopyRequest).await().atMost(Duration.ofSeconds(30));
            dokument.setObjectId(objectId);
        }
    }

    private PutObjectRequest buildPutRequest(final String objectId) {
        return PutObjectRequest.builder()
            .bucket(config.s3().bucketName())
            .key(objectId)
            .contentType("image/png")
            .build();
    }

    private CopyObjectRequest buildCopyRequest(final String sourceObjectId, final String objectId) {
        return CopyObjectRequest.builder()
            .sourceBucket(config.s3().bucketName())
            .destinationBucket(config.s3().bucketName())
            .sourceKey(sourceObjectId)
            .destinationKey(objectId)
            .build();
    }
}
