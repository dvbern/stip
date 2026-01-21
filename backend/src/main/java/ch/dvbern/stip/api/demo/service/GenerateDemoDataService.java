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
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.adresse.entity.AdresseBuilder;
import ch.dvbern.stip.api.ausbildung.entity.AusbildungBuilder;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungRepository;
import ch.dvbern.stip.api.ausbildung.type.AusbildungsStatus;
import ch.dvbern.stip.api.auszahlung.entity.AuszahlungBuilder;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.entity.AbstractFamilieEntityBuilder;
import ch.dvbern.stip.api.common.entity.AbstractPersonBuilder;
import ch.dvbern.stip.api.common.service.EntityCopyMapper;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.common.util.FileUtil;
import ch.dvbern.stip.api.common.validation.RequiredDocumentsProducer;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.demo.entity.DemoPerson;
import ch.dvbern.stip.api.demo.repo.DemoDataAbschlussRepository;
import ch.dvbern.stip.api.demo.repo.DemoDataAusbildungsgangRepository;
import ch.dvbern.stip.api.demo.type.DemoDataDefaults;
import ch.dvbern.stip.api.demo.type.DemoDataValueMap;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.entity.DokumentBuilder;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.entity.GesuchDokumentBuilder;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.type.GesuchDokumentStatus;
import ch.dvbern.stip.api.dokument.util.RequiredDokumentUtil;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKostenBuilder;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.entity.ElternBuilder;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.familiensituation.entity.FamiliensituationBuilder;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.geschwister.entity.Geschwister;
import ch.dvbern.stip.api.geschwister.entity.GeschwisterBuilder;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.api.gesuchformular.repo.GesuchFormularRepository;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.api.kind.entity.KindBuilder;
import ch.dvbern.stip.api.land.entity.Land;
import ch.dvbern.stip.api.land.repo.LandRepository;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItemBuilder;
import ch.dvbern.stip.api.partner.entity.Partner;
import ch.dvbern.stip.api.partner.entity.PartnerBuilder;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildungBuilder;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.api.steuerdaten.entity.SteuerdatenBuilder;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.api.steuererklaerung.entity.Steuererklaerung;
import ch.dvbern.stip.api.steuererklaerung.entity.SteuererklaerungBuilder;
import ch.dvbern.stip.api.zahlungsverbindung.entity.Zahlungsverbindung;
import ch.dvbern.stip.api.zahlungsverbindung.entity.ZahlungsverbindungBuilder;
import ch.dvbern.stip.generated.dto.DemoDataDto;
import ch.dvbern.stip.generated.dto.DemoFamiliensituationDto;
import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.transaction.Transactional;
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
    private final GesuchService gesuchService;
    private final S3AsyncClient s3;
    private final EntityCopyMapper copyMapper;
    private final ConfigService configService;

    private final Instance<RequiredDocumentsProducer> requiredDocumentProducers;
    private final LandRepository landRepository;
    private final FallRepository fallRepository;
    private final AusbildungRepository ausbildungRepository;
    private final GesuchRepository gesuchRepository;
    private final GesuchFormularRepository gesuchFormularRepository;
    private final GesuchDokumentRepository gesuchDokumentRepository;
    private final GesuchTrancheRepository gesuchTrancheRepository;
    private final DokumentRepository dokumentRepository;

    private Land getLand(String landName) {
        return getLandIso2(DemoDataValueMap.KNOWN_LANDS.get(landName));
    }

    private Land getLandIso2(String iso2Code) {
        return landRepository.getByIso2code(iso2Code).orElseThrow(IllegalStateException::new);
    }

    private Zahlungsverbindung getDefaultZahlungsverbindung(DemoDataDto demoDataDto, Adresse adresse) {
        final var pia = demoDataDto.getPersonInAusbildung();
        return ZahlungsverbindungBuilder.zahlungsverbindung()
            .vorname(pia.getVorname())
            .nachname(pia.getNachname())
            .adresse(adresse)
            .iban(DemoDataDefaults.ZAHLUNGSVERBINBDUNG_IBAN)
            .build();
    }

    private void anonymizeZahlungsverbindung(Gesuch gesuch) {
        final var zahlungsverbindung = gesuch.getAusbildung().getFall().getAuszahlung().getZahlungsverbindung();
        final var gesuchSuffix = getLastGesuchNummerPart(gesuch);
        zahlungsverbindung.setVorname("Vorname-%s".formatted(gesuchSuffix));
        zahlungsverbindung.setNachname("Nachname-%s".formatted(gesuchSuffix));
        anonymizeAdresse(gesuch, zahlungsverbindung.getAdresse());
    }

    private void anonymizePersonInAusbildung(Gesuch gesuch) {
        final var personInAusbildung = gesuch.getLatestGesuchTranche().getGesuchFormular().getPersonInAusbildung();
        final var gesuchSuffix = getLastGesuchNummerPart(gesuch);
        personInAusbildung.setVorname("Vorname-%s".formatted(gesuchSuffix));
        personInAusbildung.setNachname("Nachname-%s".formatted(gesuchSuffix));
        anonymizeAdresse(gesuch, personInAusbildung.getAdresse());
    }

    private void anonymizeAdresse(Gesuch gesuch, Adresse adresse) {
        final var gesuchSuffix = getLastGesuchNummerPart(gesuch);
        adresse.setStrasse("%s %s".formatted(adresse.getStrasse(), gesuchSuffix));
    }

    private void anonymizeGesuch(Gesuch gesuch) {
        anonymizeZahlungsverbindung(gesuch);
        anonymizePersonInAusbildung(gesuch);
    }

    private String getLastGesuchNummerPart(Gesuch gesuch) {
        final var gesuchNummer = gesuch.getGesuchNummer();
        return gesuchNummer.substring(gesuchNummer.lastIndexOf('.') + 1);
    }

    private SteuerdatenTyp getSteuerdatenTyp(DemoDataDto demoDataDto, ElternTyp type) {
        if (demoDataDto.getFamiliensituation().getElternVerheiratetZusammen()) {
            return SteuerdatenTyp.FAMILIE;
        }
        return switch (type) {
            case VATER -> SteuerdatenTyp.VATER;
            case MUTTER -> SteuerdatenTyp.MUTTER;
        };
    }

    @Transactional
    public UUID createEingereicht(DemoDataDto demoDataDto) {
        final var piaDto = demoDataDto.getPersonInAusbildung();
        final var piaAdresse = AdresseBuilder.adresse()
            .land(getLand(piaDto.getLand()))
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
        final var gesuchstellerId = benutzerService.getCurrentBenutzer().getId();
        final var fall = fallRepository.findFallForGsOptional(gesuchstellerId).orElseThrow();
        fall.setAuszahlung(auszahlung);

        final var ausbildungDto = demoDataDto.getAusbildung();
        final var ausbildung = AusbildungBuilder.ausbildung()
            .fall(fall)
            .gesuchs(new ArrayList<>())
            .ausbildungsgang(
                !ausbildungDto.getAusbildungNichtGefunden()
                    ? demoDataAusbildungsgangRepository.requireAusbildungsgangByDemoData(demoDataDto)
                    : null
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
            .build();
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
                .nationalitaet(getLand(piaDto.getNationalitaet()))
                .heimatort(piaDto.getHeimatort())
                .heimatortPLZ(piaDto.getHeimatortPLZ())
                .niederlassungsstatus(piaDto.getNiederlassungsstatus())
                .einreisedatum(piaDto.getEinreisedatum())
                .zivilstand(piaDto.getZivilstand())
                .sozialhilfebeitraege(piaDto.getSozialhilfebeitraege())
                .vormundschaft(piaDto.getVormundschaft())
                .korrespondenzSprache(DemoDataDefaults.SPRACHE)
                .zustaendigeKESB(piaDto.getZustaendigeKESB()),
            AbstractFamilieEntityBuilder.abstractFamilieEntity()
                .wohnsitz(piaDto.getWohnsitz())
                .wohnsitzAnteilMutter(
                    piaDto.getWohnsitz() == Wohnsitz.MUTTER_VATER ? BigDecimal.valueOf(piaDto.getWohnsitzAnteilMutter())
                        : null
                )
                .wohnsitzAnteilVater(
                    piaDto.getWohnsitz() == Wohnsitz.MUTTER_VATER ? BigDecimal.valueOf(piaDto.getWohnsitzAnteilVater())
                        : null
                ),
            AbstractPersonBuilder.abstractPerson()
                .nachname(piaDto.getNachname())
                .vorname(piaDto.getNachname())
                .geburtsdatum(piaDto.getGeburtsdatum())
        );
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
        Partner partner = null;
        final var demoPartnerDto = demoDataDto.getPartner();
        if (Objects.nonNull(demoPartnerDto)) {
            partner = DemoPerson.createPartner(
                PartnerBuilder.partner()
                    .adresse(
                        AdresseBuilder.adresse()
                            .land(getLand(demoPartnerDto.getLand()))
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
                    .geburtsdatum(demoPartnerDto.getGeburtsdatum())
            );
        }
        final Set<Kind> kinds = new HashSet<>();
        for (var kindDto : demoDataDto.getKinder()) {
            final var kind = DemoPerson.createKind(
                KindBuilder.kind()
                    .ausbildungssituation(kindDto.getAusbildungssituation())
                    .unterhaltsbeitraege(
                        DemoDataDefaults.defaultByKindsIfNull(kindDto.getUnterhaltsbeitraege(), demoDataDto)
                    )
                    .wohnsitzAnteilPia(kindDto.getWohnsitzAnteilPia())
                    .kinderUndAusbildungszulagen(kindDto.getKinderUndAusbildungszulagen())
                    .renten(kindDto.getRenten())
                    .andereEinnahmen(kindDto.getAndereEinnahmen()),
                AbstractPersonBuilder.abstractPerson()
                    .nachname(kindDto.getNachname())
                    .vorname(kindDto.getVorname())
                    .geburtsdatum(kindDto.getGeburtsdatum())
            );
            kinds.add(kind);
        }
        final var ekDto = demoDataDto.getEinnahmenKosten();
        final var einnahmenKosten = EinnahmenKostenBuilder.einnahmenKosten()
            .nettoerwerbseinkommen(ekDto.getNettoerwerbseinkommen())
            .fahrkosten(ekDto.getFahrkosten())
            .wohnkosten(ekDto.getWohnkosten())
            .wgWohnend(DemoDataDefaults.EK_WG_WOHNEND)
            .wgAnzahlPersonen(null)
            .alternativeWohnformWohnend(DemoDataDefaults.EK_ALTERNATIVE_WOHNFORM_WOHNEND)
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
            .veranlagungsStatus(null)
            .steuerjahr(DemoDataDefaults.getSteuerjahr(ausbildungDto.getAusbildungBeginn()))
            .vermoegen(ekDto.getVermoegen())
            .einnahmenBGSA(ekDto.getEinnahmenBGSA())
            .taggelderAHVIV(ekDto.getTaggelderAHVIV())
            .andereEinnahmen(ekDto.getAndereEinnahmen())
            .arbeitspensumProzent(DemoDataDefaults.EK_ARBEITSPENSUM)
            .build();
        final var ekPartnerDtoOpt = Optional.ofNullable(demoDataDto.getEinnahmenKostenPartner());
        final var einnahmenKostenPartner = ekPartnerDtoOpt.map(
            ekPartnerDto -> EinnahmenKostenBuilder.einnahmenKosten()
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
                .veranlagungsStatus(null)
                .steuerjahr(DemoDataDefaults.getSteuerjahr(ausbildungDto.getAusbildungBeginn()))
                .vermoegen(Objects.requireNonNullElse(ekPartnerDto.getVermoegen(), DemoDataDefaults.EK_VERMOEGEN))
                .einnahmenBGSA(ekPartnerDto.getEinnahmenBGSA())
                .taggelderAHVIV(ekPartnerDto.getTaggelderAHVIV())
                .andereEinnahmen(ekPartnerDto.getAndereEinnahmen())
                .arbeitspensumProzent(DemoDataDefaults.EK_ARBEITSPENSUM)
                .build()
        );
        final var famsitDto = demoDataDto.getFamiliensituation();
        final var familiensituation = FamiliensituationBuilder.familiensituation()
            .elternVerheiratetZusammen(famsitDto.getElternVerheiratetZusammen())
            .elternteilUnbekanntVerstorben(famsitDto.getElternteilUnbekanntVerstorben())
            .gerichtlicheAlimentenregelung(famsitDto.getGerichtlicheAlimentenregelung())
            .mutterUnbekanntVerstorben(getAbwesenheitsGrund(famsitDto, ElternTyp.MUTTER))
            .mutterUnbekanntGrund(famsitDto.getMutterUnbekanntGrund())
            .mutterWiederverheiratet(
                firstSetValueOrNull(
                    famsitDto.getMutterWiederverheiratetAlimente(),
                    famsitDto.getMutterWiederverheiratetUnbekannt(),
                    famsitDto.getMutterWiederverheiratetUngewiss()
                )
            )
            .vaterUnbekanntVerstorben(getAbwesenheitsGrund(famsitDto, ElternTyp.VATER))
            .vaterUnbekanntGrund(famsitDto.getVaterUnbekanntGrund())
            .vaterWiederverheiratet(
                firstSetValueOrNull(
                    famsitDto.getVaterWiederverheiratetAlimente(),
                    famsitDto.getVaterWiederverheiratetUnbekannt(),
                    famsitDto.getVaterWiederverheiratetUngewiss()
                )
            )
            .werZahltAlimente(famsitDto.getWerZahltAlimente())
            .build();
        final List<Eltern> elterns = new ArrayList<>();
        for (var elternDto : demoDataDto.getElterns()) {
            elterns.add(
                DemoPerson.createEltern(
                    ElternBuilder.eltern()
                        .adresse(
                            AdresseBuilder.adresse()
                                .land(getLand(elternDto.getLand()))
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
                        .wohnkosten(elternDto.getWohnkosten()),
                    AbstractPersonBuilder.abstractPerson()
                        .nachname(elternDto.getNachname())
                        .vorname(elternDto.getVorname())
                        .geburtsdatum(elternDto.getGeburtsdatum())
                )
            );
        }
        final List<Steuererklaerung> steuererklaerungs = new ArrayList<>();
        for (var steuererklaerungDto : demoDataDto.getSteuererklaerung()) {
            steuererklaerungs.add(
                SteuererklaerungBuilder.steuererklaerung()
                    .steuerdatenTyp(getSteuerdatenTyp(demoDataDto, steuererklaerungDto.getType()))
                    .steuererklaerungInBern(steuererklaerungDto.getSteuererklaerungInBern())
                    .ergaenzungsleistungen(steuererklaerungDto.getErgaenzungsleistungen())
                    .unterhaltsbeitraege(steuererklaerungDto.getUnterhaltsbeitraege())
                    .renten(steuererklaerungDto.getRenten())
                    .einnahmenBGSA(steuererklaerungDto.getEinnahmenBGSA())
                    .andereEinnahmen(steuererklaerungDto.getAndereEinnahmen())
                    .build()
            );
        }
        final List<Steuerdaten> steuerdatens = new ArrayList<>();
        for (var steuerdatenDto : demoDataDto.getSteuerdaten()) {
            steuerdatens.add(
                SteuerdatenBuilder.steuerdaten()
                    .steuerdatenTyp(getSteuerdatenTyp(demoDataDto, steuerdatenDto.getType()))
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
        final List<Geschwister> geschwisters = new ArrayList<>();
        for (var geschwisterDto : demoDataDto.getGeschwister()) {
            geschwisters.add(
                DemoPerson.createGeschwister(
                    GeschwisterBuilder.geschwister()
                        .ausbildungssituation(geschwisterDto.getAusbildungssituation()),
                    AbstractFamilieEntityBuilder.abstractFamilieEntity()
                        .wohnsitz(geschwisterDto.getWohnsitzBei())
                        .wohnsitzAnteilMutter(BigDecimal.valueOf(geschwisterDto.getWohnsitzAnteilMutter()))
                        .wohnsitzAnteilVater(BigDecimal.valueOf(geschwisterDto.getWohnsitzAnteilVater())),
                    AbstractPersonBuilder.abstractPerson()
                        .nachname(geschwisterDto.getNachname())
                        .vorname(geschwisterDto.getVorname())
                        .geburtsdatum(geschwisterDto.getGeburtsdatum())
                )
            );
        }

        fallRepository.persist(fall);
        ausbildungRepository.persist(ausbildung);
        final var createdGesuch = gesuchService.createGesuchForAusbildung(
            new GesuchCreateDto().ausbildungId(ausbildung.getId())
        );
        if (Objects.nonNull(createdGesuch.getRight())) {
            throw new IllegalStateException("Unable to create the gesuch");
        }
        final var gesuch = gesuchRepository.requireById(createdGesuch.getLeft().getId());
        final var gesuchFormular = gesuch
            .getLatestGesuchTranche()
            .getGesuchFormular();
        gesuchFormular
            .setPersonInAusbildung(pia)
            .setPartner(partner)
            .setEinnahmenKosten(einnahmenKosten)
            .setEinnahmenKostenPartner(einnahmenKostenPartner.orElse(null))
            .setFamiliensituation(familiensituation);
        gesuchFormular.getLebenslaufItems().addAll(lebenslaufItems);
        gesuchFormular.getKinds().addAll(kinds);
        gesuchFormular.getElterns().addAll(elterns);
        gesuchFormular.getSteuererklaerung().addAll(steuererklaerungs);
        gesuchFormular.getSteuerdaten().addAll(steuerdatens);
        gesuchFormular.getGeschwisters().addAll(geschwisters);

        gesuchFormularRepository.persist(gesuchFormular);
        gesuchRepository.persistAndFlush(gesuch);
        anonymizeGesuch(gesuch);
        gesuchRepository.persistAndFlush(gesuch);

        return gesuch.getId();
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

    public void createDemoDokumentsForAllRequired(Gesuch gesuch) {
        final var gesuchTranche = gesuch.getLatestGesuchTranche();
        final var requiredDocuments = RequiredDokumentUtil.getRequiredDokumentTypesForGesuch(
            gesuch.getLatestGesuchTranche().getGesuchFormular(),
            requiredDocumentProducers
        );

        final var gesuchDokuments = requiredDocuments.stream()
            .map(dokumentTyp -> createDemoGesuchDokumentWithoutUpload(dokumentTyp, gesuchTranche))
            .toList();
        final var allDokuments =
            gesuchDokuments.stream().flatMap(gesuchDokument -> gesuchDokument.getDokumente().stream()).toList();

        createS3EntriesForDokumente(allDokuments);
        gesuchTranche.getGesuchDokuments().addAll(gesuchDokuments);
        dokumentRepository.persist(allDokuments);
        gesuchDokumentRepository.persist(gesuchDokuments);
        gesuchTrancheRepository.persist(gesuchTranche);
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

    private void createS3EntriesForDokumente(List<Dokument> dokuments) {
        final var pngBody = AsyncRequestBody.fromBytes(Base64.decodeBase64(configService.getSmallestPng()));
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
            .bucket(configService.getBucketName())
            .key(objectId)
            .contentType("image/png")
            .build();
    }

    private CopyObjectRequest buildCopyRequest(final String sourceObjectId, final String objectId) {
        return CopyObjectRequest.builder()
            .sourceBucket(configService.getBucketName())
            .destinationBucket(configService.getBucketName())
            .sourceKey(sourceObjectId)
            .destinationKey(objectId)
            .build();
    }
}
