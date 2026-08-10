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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.adresse.entity.AdresseBuilder;
import ch.dvbern.stip.api.auszahlung.entity.AuszahlungBuilder;
import ch.dvbern.stip.api.benutzer.entity.BenutzerBuilder;
import ch.dvbern.stip.api.benutzer.entity.Sachbearbeiter;
import ch.dvbern.stip.api.benutzer.repo.SachbearbeiterRepository;
import ch.dvbern.stip.api.benutzer.service.RolleService;
import ch.dvbern.stip.api.benutzer.type.BenutzerStatus;
import ch.dvbern.stip.api.benutzereinstellungen.entity.BenutzereinstellungenBuilder;
import ch.dvbern.stip.api.buchhaltung.entity.Buchhaltung;
import ch.dvbern.stip.api.buchhaltung.type.BuchhaltungType;
import ch.dvbern.stip.api.buchhaltung.type.SapStatus;
import ch.dvbern.stip.api.common.type.Kanton;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.communication.mail.service.MailAlreadySentCheckerService;
import ch.dvbern.stip.api.demo.entity.DemoData;
import ch.dvbern.stip.api.demo.repo.DemoDataRepository;
import ch.dvbern.stip.api.demo.type.DemoDataDefaults;
import ch.dvbern.stip.api.demo.util.DemoDataAnonymizerUtil;
import ch.dvbern.stip.api.demo.util.ParseDemoDataUtil;
import ch.dvbern.stip.api.dokument.service.DokumentDownloadService;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.fall.entity.FallBuilder;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.StatisticsdataBuilder;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchformular.service.GesuchFormularService;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.land.repo.LandRepository;
import ch.dvbern.stip.api.land.type.WellKnownLand;
import ch.dvbern.stip.api.sap.entity.SapDelivery;
import ch.dvbern.stip.api.statistik.service.StatistikXMLService;
import ch.dvbern.stip.api.zahlungsverbindung.entity.ZahlungsverbindungBuilder;
import ch.dvbern.stip.api.zuordnung.entity.Zuordnung;
import ch.dvbern.stip.api.zuordnung.type.ZuordnungType;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.TransactionManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
@ApplicationScoped
public class GenerateMultipleDemoDataService {
    private final GenerateDemoDataService generateDemoDataService;
    private final DemoDataRepository demoDataRepository;
    private final GesuchRepository gesuchRepository;
    private final LandRepository landRepository;
    private final RolleService rolleService;
    private final TransactionManager transactionManager;
    private final DokumentDownloadService dokumentDownloadService;
    private final MailAlreadySentCheckerService mailAlreadySentCheckerService;
    private final SachbearbeiterRepository sachbearbeiterRepository;
    private final StatistikXMLService statistikXMLService;
    private final GesuchFormularService gesuchFormularService;

    @Transactional
    @SneakyThrows
    @WithSpan
    public void generateAllGesucheAsVerfuegt(final boolean ethereal, final String fallPrefix) {
        mailAlreadySentCheckerService.sentStandardNotification();
        final var allDemoData = demoDataRepository.findAll()
            .stream()
            .filter(d -> d.getTyp() == GesuchTrancheTyp.TRANCHE)
            .toList();
        final var admin = sachbearbeiterRepository
            .findByRolle(OidcConstants.ROLE_SACHBEARBEITER_ADMIN)
            .findFirst()
            .orElseThrow(NotFoundException::new);

        if (ethereal) {
            transactionManager.setRollbackOnly();
        }
        final var count = allDemoData.size();
        var gesuchs = new ArrayList<Gesuch>();
        var demoDataIterator = allDemoData.iterator();
        for (var i = 0; i < count; i++) {
            if (!demoDataIterator.hasNext()) {
                demoDataIterator = allDemoData.iterator();
            }
            final var demoData = demoDataIterator.next();
            gesuchs.add(createGesuch(demoData, admin, fallPrefix));
        }

        gesuchRepository.persist(gesuchs);
    }

    private Gesuch createGesuch(final DemoData demoData, final Sachbearbeiter admin, final String fallPrefix) {
        final var fall = createFakeFall(UUID.randomUUID().toString().substring(0, 8), fallPrefix);
        final var gesuch = generateDemoDataService.createEinreichableGesuch(demoData, fall);
        DemoDataAnonymizerUtil.anonymizeGesuch(demoData, gesuch);
        final var zuordnung = new Zuordnung()
            .setZuordnungType(ZuordnungType.AUTOMATIC)
            .setFall(gesuch.getAusbildung().getFall())
            .setSachbearbeiter(admin);

        gesuch.setRemainderPaymentExecuted(true);
        gesuch.getAusbildung().getFall().setSachbearbeiterZuordnung(zuordnung);
        gesuch.setEinreichedatum(LocalDate.parse(demoData.getGesuchseingang(), ParseDemoDataUtil.dmyFormatter));
        gesuch.setGesuchStatus(Gesuchstatus.STIPENDIENANSPRUCH);
        gesuch.setStatisticsdata(
            StatisticsdataBuilder.statisticsdata()
                .gesuch(gesuch)
                .gemeindeBfsNr(Kanton.BE.getBfsCode())
                .gemeindeName("Bern")
                .build()
        );
        createBuchhaltung(demoData, gesuch);

        final var preValidation =
            gesuchFormularService.validatePagesSb(gesuch.getLatestGesuchTranche().getGesuchFormular());
        if (!preValidation.getValidationErrors().isEmpty()) {
            throw new IllegalStateException(
                "Tescase %s is invalid: %s".formatted(
                    demoData.getTestFall(),
                    preValidation.getValidationErrors()
                        .stream()
                        .map(v -> "\"%s\" %s".formatted(v.getPropertyPath(), v.getMessage()))
                        .collect(Collectors.joining(", "))
                )
            );
        }

        return gesuch;
    }

    public void createBuchhaltung(final DemoData demoData, final Gesuch gesuch) {
        final var demoDataDto = demoData.parseDemoDataDto();
        final var buchhaltung = new Buchhaltung()
            .setBuchhaltungType(BuchhaltungType.AUSZAHLUNG_INITIAL)
            .setBetrag(demoDataDto.getBerechnungValues().getStipendien())
            .setSaldo(demoDataDto.getBerechnungValues().getStipendien())
            .setStipendium(demoDataDto.getBerechnungValues().getStipendien())
            .setComment("Testcase")
            .setGesuch(gesuch)
            .setFall(gesuch.getAusbildung().getFall());
        final var sapDelivery = new SapDelivery()
            .setSapStatus(SapStatus.SUCCESS)
            .setBuchhaltung(buchhaltung);
        buchhaltung.getSapDeliverys().add(sapDelivery);
        gesuch.getAusbildung().getFall().getBuchhaltungs().add(buchhaltung);
    }

    @Transactional
    public Multi<Buffer> generateStatistikXmlWithAllTestcases() {
        final var year = LocalDate.now().getYear();
        generateAllGesucheAsVerfuegt(true, DemoDataDefaults.MASS_GESUCH_STATISTIK_FALL_PREFIX);
        final var statistikXML = statistikXMLService.generateStatistikXmlForGivenGesuchs(
            year,
            demoDataRepository.getMassGeneratedGesuche(DemoDataDefaults.MASS_GESUCH_STATISTIK_FALL_PREFIX)
        );

        return dokumentDownloadService.getWrapedDokument(
            "Testcase_Statistik_XML_%d.xml".formatted(year),
            statistikXML
        );
    }

    private Fall createFakeFall(final String identifier, final String fallPrefix) {
        final var benutzereinstellungen = BenutzereinstellungenBuilder.benutzereinstellungen()
            .digitaleKommunikation(true)
            .build();
        final var fakeGesuchsteller = BenutzerBuilder.benutzer()
            .nachname("Statistik %s".formatted(identifier))
            .vorname("Test %s".formatted(identifier))
            .nutzungsbedingungenAkzeptiert(true)
            .benutzerStatus(BenutzerStatus.AKTIV)
            .rollen(rolleService.mapOrCreateRoles(Set.of(OidcConstants.ROLE_GESUCHSTELLER)))
            .benutzereinstellungen(benutzereinstellungen)
            .build();

        final var fakeAuszahlung = AuszahlungBuilder.auszahlung()
            .zahlungsverbindung(
                ZahlungsverbindungBuilder.zahlungsverbindung()
                    .vorname("test")
                    .nachname("test")
                    .adresse(
                        AdresseBuilder.adresse()
                            .land(landRepository.getByBfsCode(WellKnownLand.CHE.getLaendercodeBfs()).orElseThrow())
                            .coAdresse(null)
                            .strasse("statistik")
                            .hausnummer("1")
                            .plz("3011")
                            .ort("Bern")
                            .build()
                    )
                    .iban(DemoDataDefaults.ZAHLUNGSVERBINBDUNG_IBAN)
                    .institution(null)
                    .build()
            )
            .auszahlungAnSozialdienst(false)
            .sapBusinessPartnerId(null)
            .buchhaltung(null)
            .build();

        return FallBuilder.fall()
            .gesuchsteller(fakeGesuchsteller)
            .ausbildungs(new HashSet<>())
            .buchhaltungs(new ArrayList<>())
            .freiwilligDarlehens(new ArrayList<>())
            .historicalDelegierungs(new HashSet<>())
            .auszahlung(fakeAuszahlung)
            .fallNummer("%s%s".formatted(fallPrefix, identifier))
            .build();
    }
}
