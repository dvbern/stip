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

package ch.dvbern.stip.api.gesuch.service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.entity.Sachbearbeiter;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsJurist;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.benutzer.util.TestAsSozialdienstMitarbeiter;
import ch.dvbern.stip.api.common.authorization.AusbildungAuthorizer;
import ch.dvbern.stip.api.common.exception.ValidationsException;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.communication.mail.service.MailService;
import ch.dvbern.stip.api.dokument.entity.CustomDokumentTyp;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.dokument.service.RequiredDokumentService;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.type.GesuchDokumentStatus;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.service.ElternMapper;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import ch.dvbern.stip.api.generator.api.model.gesuch.EinnahmenKostenUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.entities.GesuchGenerator;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.util.GesuchTestUtil;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchhistory.repo.GesuchHistoryRepository;
import ch.dvbern.stip.api.gesuchstatus.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.service.GesuchTrancheMapper;
import ch.dvbern.stip.api.gesuchtranche.service.GesuchTrancheService;
import ch.dvbern.stip.api.gesuchtranche.service.GesuchTrancheValidatorService;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.gesuchtranchehistory.repo.GesuchTrancheHistoryRepository;
import ch.dvbern.stip.api.gesuchtranchehistory.service.GesuchTrancheHistoryService;
import ch.dvbern.stip.api.gesuchvalidation.service.GesuchValidatorService;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.lebenslauf.service.LebenslaufItemMapper;
import ch.dvbern.stip.api.notification.entity.Notification;
import ch.dvbern.stip.api.notification.repo.NotificationRepository;
import ch.dvbern.stip.api.notification.service.NotificationService;
import ch.dvbern.stip.api.pdf.service.VerfuegungPdfService;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
import ch.dvbern.stip.api.sap.service.SapService;
import ch.dvbern.stip.api.statusprotokoll.service.StatusprotokollService;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.api.steuererklaerung.entity.Steuererklaerung;
import ch.dvbern.stip.api.steuererklaerung.service.SteuererklaerungMapper;
import ch.dvbern.stip.api.unterschriftenblatt.service.UnterschriftenblattService;
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.api.verfuegung.entity.Verfuegung;
import ch.dvbern.stip.api.verfuegung.service.VerfuegungService;
import ch.dvbern.stip.api.zahlungsverbindung.entity.Zahlungsverbindung;
import ch.dvbern.stip.api.zuordnung.entity.Zuordnung;
import ch.dvbern.stip.api.zuordnung.service.ZuordnungService;
import ch.dvbern.stip.berechnung.service.BerechnungService;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import ch.dvbern.stip.generated.dto.FamiliensituationUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import ch.dvbern.stip.generated.dto.KommentarDto;
import ch.dvbern.stip.stipdecision.entity.StipDecisionText;
import ch.dvbern.stip.stipdecision.repo.StipDecisionTextRepository;
import ch.dvbern.stip.stipdecision.service.StipDecisionService;
import ch.dvbern.stip.stipdecision.type.StipDeciderResult;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import jakarta.inject.Inject;
import jdk.jfr.Description;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import static ch.dvbern.stip.api.common.util.Constants.VERANLAGUNGSSTATUS_DEFAULT_VALUE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_STEUERDATEN_VERANLAGUNGSSTATUS_INVALID_MESSAGE;
import static ch.dvbern.stip.api.generator.entities.GesuchGenerator.createGesuch;
import static ch.dvbern.stip.api.generator.entities.GesuchGenerator.initGesuchTranche;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.AUFGELOESTE_PARTNERSCHAFT;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.EINGETRAGENE_PARTNERSCHAFT;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.GESCHIEDEN_GERICHTLICH;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.KONKUBINAT;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.LEDIG;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.VERHEIRATET;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.VERWITWET;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
@QuarkusTestResource(TestDatabaseEnvironment.class)
class GesuchServiceTest {
    @InjectMock
    StipDecisionService decisionService;

    @Inject
    @InjectMocks
    GesuchService gesuchService;

    @Inject
    GesuchTrancheMapper gesuchTrancheMapper;

    @Inject
    ElternMapper elternMapper;

    @Inject
    LebenslaufItemMapper lebenslaufItemMapper;

    @Inject
    SteuererklaerungMapper steuererklaerungMapper;

    @InjectMock
    GesuchRepository gesuchRepository;

    @InjectMock
    GesuchHistoryRepository gesuchHistoryRepository;

    @InjectMock
    UnterschriftenblattService unterschriftenblattService;

    @Inject
    GesuchTrancheService gesuchTrancheService;

    @InjectMock
    GesuchTrancheRepository gesuchTrancheRepository;

    @InjectMock
    GesuchValidatorService gesuchValidatorService;

    @InjectMock
    BerechnungService berechnungService;

    @InjectMock
    GesuchTrancheHistoryRepository gesuchTrancheHistoryRepository;

    @InjectMock
    VerfuegungService verfuegungService;

    @InjectSpy
    MailService mailService;

    @InjectSpy
    NotificationService notificationService;

    @InjectMock
    NotificationRepository notificationRepository;

    @InjectMock
    GesuchDokumentRepository gesuchDokumentRepository;

    @InjectMock
    GesuchTrancheHistoryService gesuchTrancheHistoryService;

    @InjectMock
    FallRepository fallRepository;

    @InjectMock
    VerfuegungPdfService verfuegungPdfService;

    @InjectMock
    StipDecisionTextRepository stipDecisionTextRepository;

    @InjectMock
    StatusprotokollService statusprotokollService;

    static final String TENANT_ID = "bern";

    @BeforeAll
    static void setup() {
        final var requiredDokumentServiceMock = Mockito.mock(RequiredDokumentService.class);
        Mockito.when(requiredDokumentServiceMock.getSuperfluousDokumentsForGesuch(any())).thenReturn(List.of());
        Mockito.when(requiredDokumentServiceMock.getRequiredDokumentsForGesuchFormular(any())).thenReturn(List.of());
        QuarkusMock.installMockForType(requiredDokumentServiceMock, RequiredDokumentService.class);

        final var ausbildungAuthorizerMock = Mockito.mock(AusbildungAuthorizer.class);
        Mockito.when(ausbildungAuthorizerMock.canUpdateCheck(any())).thenReturn(false);
        QuarkusMock.installMockForType(ausbildungAuthorizerMock, AusbildungAuthorizer.class);

        final var zuordnungServiceMock = Mockito.mock(ZuordnungService.class);
        Mockito.doNothing().when(zuordnungServiceMock).updateZuordnungOnGesuch(any());
        QuarkusMock.installMockForType(zuordnungServiceMock, ZuordnungService.class);
    }

    @Test
    @TestAsGesuchsteller
    void testVerheiratetToLedigShouldResetPartner() {
        final GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        GesuchTranche tranche = updateFromZivilstandToZivilstand(gesuchUpdateDto, VERHEIRATET, LEDIG);

        assertThat(tranche.getGesuchFormular().getPartner(), Matchers.nullValue());
    }

    @Test
    @TestAsGesuchsteller
    void testLedigToEveryOtherZivilstandShouldNotResetPartner() {
        for (var zivilstand : Zivilstand.values()) {
            if (zivilstand != LEDIG) {
                final GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
                GesuchTranche tranche = updateFromZivilstandToZivilstand(gesuchUpdateDto, LEDIG, zivilstand);
                assertThat(tranche.getGesuchFormular().getPartner(), Matchers.notNullValue());
            }
        }
    }

    @Test
    @TestAsGesuchsteller
    void testVerwitwetToEveryOtherZivilstandShouldNotResetPartner() {
        for (var zivilstand : Zivilstand.values()) {
            if (zivilstand != VERWITWET) {
                final GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
                GesuchTranche tranche = updateFromZivilstandToZivilstand(gesuchUpdateDto, VERWITWET, zivilstand);
                assertThat(tranche.getGesuchFormular().getPartner(), Matchers.notNullValue());
            }
        }
    }

    @Test
    @TestAsGesuchsteller
    void testGeschiedenToEveryOtherZivilstandShouldNotResetPartner() {
        for (var zivilstand : Zivilstand.values()) {
            if (zivilstand != GESCHIEDEN_GERICHTLICH) {
                final GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
                GesuchTranche tranche =
                    updateFromZivilstandToZivilstand(gesuchUpdateDto, GESCHIEDEN_GERICHTLICH, zivilstand);
                assertThat(tranche.getGesuchFormular().getPartner(), Matchers.notNullValue());
            }
        }
    }

    @Test
    @TestAsGesuchsteller
    void testVerheiratetToKonkubinatEingetragenePartnerschaftShouldNotResetPartner() {
        for (var zivilstand : new Zivilstand[] { KONKUBINAT, EINGETRAGENE_PARTNERSCHAFT }) {
            final GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
            GesuchTranche tranche = updateFromZivilstandToZivilstand(gesuchUpdateDto, VERHEIRATET, zivilstand);
            assertThat(tranche.getGesuchFormular().getPartner(), Matchers.notNullValue());
        }
    }

    @Test
    @TestAsGesuchsteller
    void testVerheiratetToGeschiedenAufgeloestVerwitwetShouldResetPartner() {
        for (var zivilstand : new Zivilstand[] { GESCHIEDEN_GERICHTLICH, AUFGELOESTE_PARTNERSCHAFT, VERWITWET }) {
            final GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
            GesuchTranche tranche = updateFromZivilstandToZivilstand(gesuchUpdateDto, VERHEIRATET, zivilstand);
            assertThat(tranche.getGesuchFormular().getPartner(), Matchers.nullValue());
        }
    }

    @Test
    @TestAsGesuchsteller
    void testEingetragenePartnerschaftToKonkubinatVerheiratetShouldNotResetPartner() {
        for (var zivilstand : new Zivilstand[] { KONKUBINAT, VERHEIRATET }) {
            final GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
            GesuchTranche tranche =
                updateFromZivilstandToZivilstand(gesuchUpdateDto, EINGETRAGENE_PARTNERSCHAFT, zivilstand);
            assertThat(tranche.getGesuchFormular().getPartner(), Matchers.notNullValue());
        }
    }

    @Test
    @TestAsGesuchsteller
    void testEingetragenePartnerschaftToGeschiedenAufgeloestVerwitwetShouldResetPartner() {
        for (var zivilstand : new Zivilstand[] { GESCHIEDEN_GERICHTLICH, AUFGELOESTE_PARTNERSCHAFT, VERWITWET }) {
            final GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
            GesuchTranche tranche =
                updateFromZivilstandToZivilstand(gesuchUpdateDto, EINGETRAGENE_PARTNERSCHAFT, zivilstand);
            assertThat(tranche.getGesuchFormular().getPartner(), Matchers.nullValue());
        }
    }

    @Test
    @TestAsGesuchsteller
    void testkonkubinatToVerheiratetEingetragenePartnerschaftShouldNotResetPartner() {
        for (var zivilstand : new Zivilstand[] { VERHEIRATET, EINGETRAGENE_PARTNERSCHAFT }) {
            final GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
            GesuchTranche tranche = updateFromZivilstandToZivilstand(gesuchUpdateDto, KONKUBINAT, zivilstand);
            assertThat(tranche.getGesuchFormular().getPartner(), Matchers.notNullValue());
        }
    }

    @Test
    @TestAsGesuchsteller
    void testKonkubinatToGeschiedenAufgeloestVerwitwetShouldResetPartner() {
        for (var zivilstand : new Zivilstand[] { GESCHIEDEN_GERICHTLICH, AUFGELOESTE_PARTNERSCHAFT, VERWITWET }) {
            final GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
            GesuchTranche tranche = updateFromZivilstandToZivilstand(gesuchUpdateDto, KONKUBINAT, zivilstand);
            assertThat(tranche.getGesuchFormular().getPartner(), Matchers.nullValue());
        }
    }

    @Test
    @TestAsGesuchsteller
    void testAufgeloestePartnerschaftToEveryOtherZivilstandShouldNotResetPartner() {
        for (var zivilstand : Zivilstand.values()) {
            if (zivilstand != AUFGELOESTE_PARTNERSCHAFT) {
                final GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
                GesuchTranche tranche =
                    updateFromZivilstandToZivilstand(gesuchUpdateDto, AUFGELOESTE_PARTNERSCHAFT, zivilstand);
                assertThat(tranche.getGesuchFormular().getPartner(), Matchers.notNullValue());
            }
        }
    }

    @Test
    @TestAsGesuchsteller
    void resetElternDataIfChangeFromMutterToGemeinsam() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        assertThat(
            gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getElterns().size(),
            not(0)
        );
        GesuchTranche tranche =
            updateWerZahltAlimente(gesuchUpdateDto, Elternschaftsteilung.MUTTER, Elternschaftsteilung.GEMEINSAM);
        assertThat(tranche.getGesuchFormular().getElterns().size(), Matchers.is(0));
    }

    @Test
    @TestAsGesuchsteller
    void resetElternDataIfChangeFromVaterToGemeinsam() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        assertThat(
            gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getElterns().size(),
            not(0)
        );
        GesuchTranche tranche =
            updateWerZahltAlimente(gesuchUpdateDto, Elternschaftsteilung.VATER, Elternschaftsteilung.GEMEINSAM);
        assertThat(tranche.getGesuchFormular().getElterns().size(), Matchers.is(0));
    }

    @Test
    @TestAsGesuchsteller
    void noResetElternDataIfNoChangeGemeinsam() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        assertThat(
            gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getElterns().size(),
            not(0)
        );

        GesuchTranche tranche = updateWerZahltAlimente(
            gesuchUpdateDto,
            Elternschaftsteilung.GEMEINSAM,
            Elternschaftsteilung.GEMEINSAM
        );
        assertThat(
            tranche.getGesuchFormular().getElterns().size(),
            Matchers.is(0)
        );
    }

    @Test
    @TestAsGesuchsteller
    void noResetIfElternStayZusammen() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getFamiliensituation()
            .setElternVerheiratetZusammen(true);
        var anzahlElternBeforeUpdate =
            gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getElterns().size();
        GesuchTranche tranche = prepareGesuchTrancheWithIds(gesuchUpdateDto.getGesuchTrancheToWorkWith());
        gesuchTrancheMapper.partialUpdate(gesuchUpdateDto.getGesuchTrancheToWorkWith(), tranche, true);
        tranche.getGesuchFormular().getFamiliensituation().setElternVerheiratetZusammen(true);
        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        gesuchService.updateGesuch(any(), gesuchUpdateDto, true);

        assertThat(
            tranche.getGesuchFormular().getElterns().size(),
            Matchers.is(anzahlElternBeforeUpdate)
        );
    }

    @Test
    @TestAsGesuchsteller
    void noResetIfNotUnbekanntOrVerstorben() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getFamiliensituation()
            .setMutterUnbekanntVerstorben(ElternAbwesenheitsGrund.WEDER_NOCH);
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getFamiliensituation()
            .setVaterUnbekanntVerstorben(ElternAbwesenheitsGrund.WEDER_NOCH);

        GesuchTranche tranche = prepareGesuchTrancheWithIds(gesuchUpdateDto.getGesuchTrancheToWorkWith());
        gesuchTrancheMapper.partialUpdate(gesuchUpdateDto.getGesuchTrancheToWorkWith(), tranche, true);
        tranche.getGesuchFormular().getFamiliensituation().setElternVerheiratetZusammen(true);
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getFamiliensituation()
            .setElternVerheiratetZusammen(false);
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getFamiliensituation()
            .setElternteilUnbekanntVerstorben(false);

        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        gesuchService.updateGesuch(any(), gesuchUpdateDto, true);
        assertThat(hasMutter(tranche.getGesuchFormular().getElterns()), Matchers.is(true));
        assertThat(hasVater(tranche.getGesuchFormular().getElterns()), Matchers.is(true));
    }

    @Test
    @TestAsGesuchsteller
    void resetMutterIfUnbekannt() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getFamiliensituation()
            .setElternVerheiratetZusammen(true);

        GesuchTranche tranche = updateElternteilUnbekanntVerstorben(
            gesuchUpdateDto,
            ElternAbwesenheitsGrund.UNBEKANNT,
            ElternAbwesenheitsGrund.WEDER_NOCH
        );

        assertThat(hasMutter(tranche.getGesuchFormular().getElterns()), Matchers.is(false));
        assertThat(hasVater(tranche.getGesuchFormular().getElterns()), Matchers.is(true));
    }

    @Test
    @TestAsGesuchsteller
    void resetMutterIfVerstorben() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getFamiliensituation()
            .setElternVerheiratetZusammen(true);

        GesuchTranche tranche = updateElternteilUnbekanntVerstorben(
            gesuchUpdateDto,
            ElternAbwesenheitsGrund.VERSTORBEN,
            ElternAbwesenheitsGrund.WEDER_NOCH
        );

        assertThat(hasMutter(tranche.getGesuchFormular().getElterns()), Matchers.is(false));
        assertThat(hasVater(tranche.getGesuchFormular().getElterns()), Matchers.is(true));
    }

    @Test
    @TestAsGesuchsteller
    void resetVaterIfUnbekannt() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getFamiliensituation()
            .setElternVerheiratetZusammen(true);

        GesuchTranche tranche = updateElternteilUnbekanntVerstorben(
            gesuchUpdateDto,
            ElternAbwesenheitsGrund.WEDER_NOCH,
            ElternAbwesenheitsGrund.UNBEKANNT
        );

        assertThat(hasMutter(tranche.getGesuchFormular().getElterns()), Matchers.is(true));
        assertThat(hasVater(tranche.getGesuchFormular().getElterns()), Matchers.is(false));
    }

    @Test
    @TestAsGesuchsteller
    void resetVaterIfVerstorben() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getFamiliensituation()
            .setElternVerheiratetZusammen(true);

        GesuchTranche tranche = updateElternteilUnbekanntVerstorben(
            gesuchUpdateDto,
            ElternAbwesenheitsGrund.WEDER_NOCH,
            ElternAbwesenheitsGrund.VERSTORBEN
        );

        assertThat(hasMutter(tranche.getGesuchFormular().getElterns()), Matchers.is(true));
        assertThat(hasVater(tranche.getGesuchFormular().getElterns()), Matchers.is(false));
    }

    @Test
    @TestAsGesuchsteller
    void resetBothIfVerstorben() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getFamiliensituation()
            .setElternVerheiratetZusammen(true);

        GesuchTranche tranche = updateElternteilUnbekanntVerstorben(
            gesuchUpdateDto,
            ElternAbwesenheitsGrund.VERSTORBEN,
            ElternAbwesenheitsGrund.VERSTORBEN
        );

        assertThat(hasMutter(tranche.getGesuchFormular().getElterns()), Matchers.is(false));
        assertThat(hasVater(tranche.getGesuchFormular().getElterns()), Matchers.is(false));
    }

    @Test
    @TestAsGesuchsteller
    void resetBothIfUnbekannt() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getFamiliensituation()
            .setElternVerheiratetZusammen(true);

        GesuchTranche tranche = updateElternteilUnbekanntVerstorben(
            gesuchUpdateDto,
            ElternAbwesenheitsGrund.UNBEKANNT,
            ElternAbwesenheitsGrund.UNBEKANNT
        );

        assertThat(hasMutter(tranche.getGesuchFormular().getElterns()), Matchers.is(false));
        assertThat(hasVater(tranche.getGesuchFormular().getElterns()), Matchers.is(false));
    }

    @Test
    @TestAsGesuchsteller
    void resetAlimenteIfGesetzlicheAlimenteregelungFromNoFamsitToTrue() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();

        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().setFamiliensituation(null);
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getEinnahmenKosten()
            .setUnterhaltsbeitraege(1000);
        GesuchTranche tranche = prepareGesuchTrancheWithIds(gesuchUpdateDto.getGesuchTrancheToWorkWith());
        gesuchTrancheMapper.partialUpdate(gesuchUpdateDto.getGesuchTrancheToWorkWith(), tranche, true);
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .setFamiliensituation(new FamiliensituationUpdateDto());
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getFamiliensituation()
            .setElternVerheiratetZusammen(false);
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getFamiliensituation()
            .setGerichtlicheAlimentenregelung(true);
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getFamiliensituation()
            .setWerZahltAlimente(Elternschaftsteilung.GEMEINSAM);

        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        gesuchService.updateGesuch(any(), gesuchUpdateDto, true);

        assertThat(tranche.getGesuchFormular().getEinnahmenKosten().getUnterhaltsbeitraege(), Matchers.nullValue());
    }

    @Test
    @TestAsGesuchsteller
    void resetAlimenteIfGesetzlicheAlimenteregelungFromNullToTrue() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getEinnahmenKosten()
            .setUnterhaltsbeitraege(1000);

        GesuchTranche tranche = updateGesetzlicheAlimenteRegel(null, true, gesuchUpdateDto);

        assertThat(tranche.getGesuchFormular().getEinnahmenKosten().getUnterhaltsbeitraege(), Matchers.nullValue());
    }

    @Test
    @TestAsGesuchsteller
    void resetAlimenteIfGesetzlicheAlimenteregelungFromFalseToTrue() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getEinnahmenKosten()
            .setUnterhaltsbeitraege(1000);

        GesuchTranche tranche = updateGesetzlicheAlimenteRegel(false, true, gesuchUpdateDto);

        assertThat(tranche.getGesuchFormular().getEinnahmenKosten().getUnterhaltsbeitraege(), Matchers.nullValue());
    }

    @Test
    @TestAsGesuchsteller
    void noResetAlimenteIfGesetzlicheAlimenteregelungFromTrueToTrue() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        final var alimente = 1000;
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getEinnahmenKosten()
            .setUnterhaltsbeitraege(alimente);

        GesuchTranche tranche = updateGesetzlicheAlimenteRegel(true, true, gesuchUpdateDto);

        assertThat(tranche.getGesuchFormular().getEinnahmenKosten().getUnterhaltsbeitraege(), Matchers.is(alimente));
    }

    @Test
    @TestAsGesuchsteller
    void resetAlimenteIfGesetzlicheAlimenteregelungFromNullToFalse() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getEinnahmenKosten()
            .setUnterhaltsbeitraege(1000);

        GesuchTranche tranche = updateGesetzlicheAlimenteRegel(null, false, gesuchUpdateDto);

        assertThat(tranche.getGesuchFormular().getEinnahmenKosten().getUnterhaltsbeitraege(), Matchers.nullValue());
    }

    @Test
    @TestAsGesuchsteller
    void resetAlimenteIfGesetzlicheAlimenteregelungFromTrueToFalse() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getEinnahmenKosten()
            .setUnterhaltsbeitraege(1000);

        GesuchTranche tranche = updateGesetzlicheAlimenteRegel(true, false, gesuchUpdateDto);

        assertThat(tranche.getGesuchFormular().getEinnahmenKosten().getUnterhaltsbeitraege(), Matchers.nullValue());
    }

    @Test
    @TestAsGesuchsteller
    void noResetAlimenteIfGesetzlicheAlimenteregelungFromFalseToFalse() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        final var alimente = 1000;
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getEinnahmenKosten()
            .setUnterhaltsbeitraege(alimente);
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getFamiliensituation()
            .setGerichtlicheAlimentenregelung(false);

        GesuchTranche tranche = updateGesetzlicheAlimenteRegel(false, false, gesuchUpdateDto);

        assertThat(tranche.getGesuchFormular().getEinnahmenKosten().getUnterhaltsbeitraege(), Matchers.is(alimente));
    }

    @Test
    @TestAsGesuchsteller
    void noResetAuswertigesMittagessenIfPersonIsAusbildungIsNull() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().setPersonInAusbildung(null);
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getEinnahmenKosten()
            .setAuswaertigeMittagessenProWoche(1);

        GesuchTranche tranche = initTrancheFromGesuchUpdate(gesuchUpdateDto);
        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        gesuchService.updateGesuch(any(), gesuchUpdateDto, true);

        assertThat(
            tranche.getGesuchFormular().getEinnahmenKosten().getAuswaertigeMittagessenProWoche(),
            Matchers.is(1)
        );
    }

    @Test
    @TestAsGesuchsteller
    void noResetAuswertigesMittagessenIfWohnsitzFamilie() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getEinnahmenKosten()
            .setAuswaertigeMittagessenProWoche(1);
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getEinnahmenKosten()
            .setWgWohnend(false);
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getEinnahmenKosten()
            .setAlternativeWohnformWohnend(false);

        GesuchTranche tranche = initTrancheFromGesuchUpdate(gesuchUpdateDto);

        final var pia = gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getPersonInAusbildung();

        pia.setWohnsitz(Wohnsitz.FAMILIE);
        pia.setWohnsitzAnteilMutter(null);
        pia.setWohnsitzAnteilVater(null);

        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getEinnahmenKosten()
            .setAuswaertigeMittagessenProWoche(1);

        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        gesuchService.updateGesuch(any(), gesuchUpdateDto, true);

        assertThat(
            tranche.getGesuchFormular().getEinnahmenKosten().getAuswaertigeMittagessenProWoche(),
            Matchers.is(1)
        );
    }

    @Test
    @TestAsGesuchsteller
    void noResetAuswertigesMittagessenIfWohnsitzMutterVater() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getEinnahmenKosten()
            .setAuswaertigeMittagessenProWoche(1);

        GesuchTranche tranche = initTrancheFromGesuchUpdate(gesuchUpdateDto);

        final var pia = gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getPersonInAusbildung();

        pia.setWohnsitz(Wohnsitz.MUTTER_VATER);
        pia.setWohnsitzAnteilVater(BigDecimal.valueOf(50));
        pia.setWohnsitzAnteilMutter(BigDecimal.valueOf(50));

        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getEinnahmenKosten()
            .setAuswaertigeMittagessenProWoche(1);
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getEinnahmenKosten()
            .setWgWohnend(false);
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getEinnahmenKosten()
            .setAlternativeWohnformWohnend(false);
        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        gesuchService.updateGesuch(any(), gesuchUpdateDto, true);

        assertThat(
            tranche.getGesuchFormular().getEinnahmenKosten().getAuswaertigeMittagessenProWoche(),
            Matchers.is(1)
        );
    }

    @Test
    @TestAsGesuchsteller
    void resetAuswertigesMittagessenIfWohnsitzEigenerHaushalt() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getEinnahmenKosten()
            .setAuswaertigeMittagessenProWoche(1);

        GesuchTranche tranche = initTrancheFromGesuchUpdate(gesuchUpdateDto);

        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getPersonInAusbildung()
            .setWohnsitz(Wohnsitz.EIGENER_HAUSHALT);
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getPersonInAusbildung()
            .setWohnsitzAnteilMutter(null);
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getPersonInAusbildung()
            .setWohnsitzAnteilVater(null);

        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        gesuchService.updateGesuch(any(), gesuchUpdateDto, true);

        assertThat(
            tranche.getGesuchFormular().getEinnahmenKosten().getAuswaertigeMittagessenProWoche(),
            Matchers.nullValue()
        );
    }

    @Test
    @TestAsGesuchsteller
    void einnahmenKostenNullResetAuswertigesMittagessen() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().setEinnahmenKosten(null);

        GesuchTranche tranche = initTrancheFromGesuchUpdate(gesuchUpdateDto);
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getPersonInAusbildung()
            .setWohnsitz(Wohnsitz.EIGENER_HAUSHALT);
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getPersonInAusbildung()
            .setWohnsitzAnteilMutter(null);
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getPersonInAusbildung()
            .setWohnsitzAnteilVater(null);

        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        gesuchService.updateGesuch(any(), gesuchUpdateDto, true);

        assertThat(
            tranche.getGesuchFormular().getEinnahmenKosten(),
            Matchers.nullValue()
        );
    }

    @Test
    @TestAsGesuchsteller
    void noResetLebenslaufIfGebrutsdatumNotChanged() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        GesuchTranche tranche = initTrancheFromGesuchUpdate(gesuchUpdateDto);

        assertThat(
            tranche.getGesuchFormular().getLebenslaufItems().size(),
            Matchers.is(1)
        );

        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        gesuchService.updateGesuch(any(), gesuchUpdateDto, true);
        assertThat(
            tranche.getGesuchFormular().getLebenslaufItems().size(),
            Matchers.is(1)
        );
    }

    @Test
    @TestAsGesuchsteller
    void resetLebenslaufIfGebrutsdatumChanged() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        GesuchTranche tranche = initTrancheFromGesuchUpdate(gesuchUpdateDto);

        assertThat(
            tranche.getGesuchFormular().getLebenslaufItems().size(),
            Matchers.is(1)
        );

        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getPersonInAusbildung()
            .setGeburtsdatum(LocalDate.of(2000, 10, 11));
        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        gesuchService.updateGesuch(any(), gesuchUpdateDto, true);
        assertThat(
            tranche.getGesuchFormular().getLebenslaufItems().size(),
            Matchers.is(0)
        );
    }

    @Test
    @TestAsGesuchsteller
    void noResetLebenslaufIfNoUpdateOfPersonInAusbildung() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        GesuchTranche tranche = initTrancheFromGesuchUpdate(gesuchUpdateDto);

        assertThat(
            tranche.getGesuchFormular().getLebenslaufItems().size(),
            Matchers.is(1)
        );

        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().setPersonInAusbildung(null);
        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        gesuchService.updateGesuch(any(), gesuchUpdateDto, true);
        assertThat(
            tranche.getGesuchFormular().getLebenslaufItems().size(),
            Matchers.is(1)
        );
    }

    @Test
    @TestAsGesuchsteller
    void validateEinreichenInvalid() {
        GesuchTranche tranche = initTrancheFromGesuchUpdate(GesuchGenerator.createGesuch());
        tranche.getGesuch().setGesuchStatus(Gesuchstatus.EINGEREICHT);

        when(gesuchTrancheRepository.requireById(any())).thenReturn(tranche);
        when(gesuchRepository.findGesucheBySvNummer(any())).thenReturn(
            Stream.of(
                (Gesuch) new Gesuch()
                    .setGesuchStatus(Gesuchstatus.EINGEREICHT)
                    .setId(UUID.randomUUID())
            )
        );
        when(gesuchTrancheHistoryService.getLatestTranche(any())).thenReturn(tranche);

        final var reportDto = gesuchTrancheService.einreichenValidierenSB(tranche.getId());

        assertThat(
            reportDto.getValidationErrors().size(),
            Matchers.is(1)
        );
    }

    @Test
    @TestAsGesuchsteller
    void validateEinreichenInvalidWithoutAuszahlung() {
        EinnahmenKostenUpdateDtoSpecModel.einnahmenKostenUpdateDtoSpec().setSteuerjahr(0);
        final var gesuchUpdateDto = GesuchGenerator.createFullGesuch();
        final var famsit = new FamiliensituationUpdateDto();
        famsit.setElternVerheiratetZusammen(false);
        famsit.setGerichtlicheAlimentenregelung(false);
        famsit.setElternteilUnbekanntVerstorben(true);
        famsit.setMutterUnbekanntVerstorben(ElternAbwesenheitsGrund.VERSTORBEN);
        famsit.setVaterUnbekanntVerstorben(ElternAbwesenheitsGrund.VERSTORBEN);

        GesuchTranche tranche = initTrancheFromGesuchUpdate(GesuchGenerator.createFullGesuch());
        tranche.getGesuch().setGesuchNummer("TEST.20XX.213981");
        tranche.getGesuchFormular()
            .getAusbildung()
            .setAusbildungsgang(new Ausbildungsgang());

        tranche.getGesuchFormular().setTranche(tranche);
        tranche.setGesuchDokuments(
            Arrays.stream(DokumentTyp.values())
                .map(x -> {
                    final var gesuchDokument = new GesuchDokument().setDokumentTyp(x).setGesuchTranche(tranche);
                    gesuchDokument.addDokument(new Dokument());
                    return gesuchDokument;
                })
                .toList()
        );

        when(gesuchTrancheRepository.requireById(any())).thenReturn(tranche);
        when(gesuchRepository.findGesucheBySvNummer(any())).thenReturn(Stream.of(tranche.getGesuch()));
        when(gesuchTrancheHistoryService.getCurrentOrHistoricalTrancheForGS(any())).thenReturn(tranche);
        tranche.getGesuchFormular().getEinnahmenKosten().setSteuerjahr(0);
        tranche.setTyp(GesuchTrancheTyp.TRANCHE);

        Set<Steuerdaten> list = new LinkedHashSet<>();
        list.add(TestUtil.prepareSteuerdaten());
        tranche.getGesuchFormular().setSteuerdaten(list);

        final var reportDto = gesuchTrancheService.einreichenValidierenGS(tranche.getId());
        assertThat(
            reportDto.getValidationErrors().stream().anyMatch(error -> error.getPropertyPath().equals("auszahlung")),
            is(true)
        );
    }

    @Test
    @TestAsSachbearbeiter
    void validateEinreichenValid() {
        EinnahmenKostenUpdateDtoSpecModel.einnahmenKostenUpdateDtoSpec().setSteuerjahr(0);
        final var gesuchUpdateDto = GesuchGenerator.createFullGesuch();
        final var famsit = new FamiliensituationUpdateDto();
        famsit.setElternVerheiratetZusammen(false);
        famsit.setGerichtlicheAlimentenregelung(false);
        famsit.setElternteilUnbekanntVerstorben(true);
        famsit.setMutterUnbekanntVerstorben(ElternAbwesenheitsGrund.VERSTORBEN);
        famsit.setVaterUnbekanntVerstorben(ElternAbwesenheitsGrund.VERSTORBEN);

        GesuchTranche tranche = initTrancheFromGesuchUpdate(GesuchGenerator.createFullGesuch());
        tranche.getGesuch().setGesuchNummer("TEST.20XX.213981");
        tranche.getGesuchFormular()
            .getAusbildung()
            .setAusbildungsgang(new Ausbildungsgang());

        tranche.getGesuchFormular().setTranche(tranche);
        tranche.setGesuchDokuments(
            Arrays.stream(DokumentTyp.values())
                .map(x -> {
                    final var gesuchDokument = new GesuchDokument().setDokumentTyp(x)
                        .setGesuchTranche(tranche)
                        .setStatus(GesuchDokumentStatus.AKZEPTIERT);
                    gesuchDokument.addDokument(new Dokument());
                    return gesuchDokument;
                })
                .toList()
        );

        when(gesuchTrancheRepository.requireById(any())).thenReturn(tranche);
        when(gesuchRepository.findGesucheBySvNummer(any())).thenReturn(Stream.of(tranche.getGesuch()));
        when(gesuchTrancheHistoryService.getLatestTranche(any())).thenReturn(tranche);
        tranche.getGesuchFormular().getEinnahmenKosten().setSteuerjahr(0);
        tranche.setTyp(GesuchTrancheTyp.TRANCHE);

        Set<Steuerdaten> list = new LinkedHashSet<>();
        list.add(TestUtil.prepareSteuerdaten());
        tranche.getGesuchFormular().setSteuerdaten(list);
        tranche.getGesuchFormular().getSteuerdaten().stream().toList().get(0).setVeranlagungsStatus("test");

        var zahlungsverbindung = new Zahlungsverbindung();
        zahlungsverbindung.setIban(TestConstants.IBAN_CH_NUMMER_VALID);
        zahlungsverbindung.setAdresse(tranche.getGesuchFormular().getPersonInAusbildung().getAdresse());
        zahlungsverbindung.setNachname(tranche.getGesuchFormular().getPersonInAusbildung().getNachname());
        zahlungsverbindung.setVorname(tranche.getGesuchFormular().getPersonInAusbildung().getVorname());

        var auszahlung = new Auszahlung();
        auszahlung.setAuszahlungAnSozialdienst(false);
        auszahlung.setZahlungsverbindung(zahlungsverbindung);
        tranche.getGesuch().getAusbildung().getFall().setAuszahlung(auszahlung);

        final var reportDto = gesuchTrancheService.einreichenValidierenSB(tranche.getId());

        assertThat(
            reportDto.toString() + "\nEltern: " + gesuchUpdateDto.getGesuchTrancheToWorkWith()
                .getGesuchFormular()
                .getElterns()
                .size(),
            reportDto.getValidationErrors().size(),
            Matchers.is(0)
        );
    }

    // TODO KSTIP-1236: Enable this test
    // @Test
    // @TestAsGesuchsteller
    // void gesuchEinreichenTest() {
    // GesuchTranche tranche = initTrancheFromGesuchUpdate(GesuchGenerator.createFullGesuch());
    // tranche.getGesuchFormular()
    // .getAusbildung()
    // .setAusbildungsgang(new Ausbildungsgang().setBildungsart(new Bildungsart()));
    // final var oldZivilstand = tranche.getGesuchFormular().getPersonInAusbildung().getZivilstand();
    // tranche.getGesuchFormular().getPersonInAusbildung().setZivilstand(LEDIG);
    //
    // when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
    // when(gesuchRepository.findGesucheBySvNummer(any())).thenReturn(Stream.of(tranche.getGesuch()));
    // doNothing().when(notificationService).createNotification(any(), any());
    //
    // tranche.getGesuchFormular().setTranche(tranche);
    // tranche.getGesuchFormular().getEinnahmenKosten().setSteuerjahr(2022);
    // tranche.getGesuchFormular().setPartner(null);
    // tranche.getGesuch().setGesuchDokuments(
    // Arrays.stream(DokumentTyp.values())
    // .map(x -> new GesuchDokument().setDokumentTyp(x).setGesuch(tranche.getGesuch()))
    // .toList()
    // );
    //
    // gesuchService.gesuchEinreichen(tranche.getGesuch().getId());
    //
    // assertThat(
    // tranche.getGesuch().getGesuchStatus(),
    // Matchers.is(Gesuchstatus.BEREIT_FUER_BEARBEITUNG)
    // );
    //
    // tranche.getGesuchFormular().getPersonInAusbildung().setZivilstand(oldZivilstand);
    // }

    @Test
    @TestAsGesuchsteller
    void gesuchUpdateEinnahmenkostenDoNotSetSteuerdatenTest() {
        GesuchUpdateDto gesuchUpdateDto = createGesuch();
        GesuchTranche tranche = initTrancheFromGesuchUpdate(gesuchUpdateDto);

        tranche.getGesuchFormular().getEinnahmenKosten().setSteuerjahr(null);
        tranche.getGesuchFormular().getEinnahmenKosten().setVeranlagungsStatus(null);

        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setSteuerjahr(1990);
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getEinnahmenKosten()
            .setVeranlagungsStatus("test");

        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        when(gesuchRepository.findGesucheBySvNummer(any())).thenReturn(Stream.of(tranche.getGesuch()));
        when(gesuchRepository.findByIdOptional(any())).thenReturn(Optional.ofNullable(tranche.getGesuch()));

        gesuchService.updateGesuch(UUID.randomUUID(), gesuchUpdateDto, true);
        assertThat(
            gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().getSteuerjahr(),
            Matchers.equalTo(tranche.getGesuch().getGesuchsperiode().getGesuchsjahr().getTechnischesJahr() - 1)
        );
        assertThat(
            gesuchUpdateDto.getGesuchTrancheToWorkWith()
                .getGesuchFormular()
                .getEinnahmenKosten()
                .getVeranlagungsStatus(),
            Matchers.is(VERANLAGUNGSSTATUS_DEFAULT_VALUE)
        );
    }

    @TestAsSachbearbeiter
    @Test
    @Description("It should be possible to change Gesuchstatus from JURISTISCHE_ABKLAERUNG to BEREIT_FUER_BEARBEITUNG")
    void changeGesuchstatus_from_JuristischeAbklaerung_to_BereitFuerBearbeitungTest() {
        Gesuch gesuch = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.JURISTISCHE_ABKLAERUNG);
        when(gesuchRepository.requireById(any())).thenReturn(gesuch);

        assertDoesNotThrow(() -> gesuchService.gesuchStatusToBereitFuerBearbeitung(gesuch.getId()));
        assertEquals(
            Gesuchstatus.BEREIT_FUER_BEARBEITUNG,
            gesuchRepository.requireById(gesuch.getId()).getGesuchStatus()
        );
    }

    @Test
    void changeGesuchstatusCheckUnterschriftenblattToVersandbereit() {
        final var gesuch = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.VERFUEGT);
        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        when(unterschriftenblattService.requiredUnterschriftenblaetterExistOrWasAlreadyVerfuegtOnceBefore(any()))
            .thenReturn(true);

        when(berechnungService.getBerechnungsresultatFromGesuch(gesuch, 1, 0))
            .thenReturn(new BerechnungsresultatDto().berechnungTotal(0).year(Year.now().getValue()));

        assertDoesNotThrow(() -> gesuchService.gesuchStatusCheckUnterschriftenblatt(gesuch.getId()));
        assertEquals(
            Gesuchstatus.VERFUEGUNG_DRUCKBEREIT,
            gesuchRepository.requireById(gesuch.getId()).getGesuchStatus()
        );
    }

    @Test
    void changeGesuchstatusCheckUnterschriftenblattToWartenAufUnterschriftenblatt() {
        final var gesuch = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.VERFUEGT);
        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        when(unterschriftenblattService.requiredUnterschriftenblaetterExistOrWasAlreadyVerfuegtOnceBefore(any()))
            .thenReturn(false);

        assertDoesNotThrow(() -> gesuchService.gesuchStatusCheckUnterschriftenblatt(gesuch.getId()));
        assertEquals(
            Gesuchstatus.WARTEN_AUF_UNTERSCHRIFTENBLATT,
            gesuchRepository.requireById(gesuch.getId()).getGesuchStatus()
        );
    }

    @TestAsSachbearbeiter
    @Test
    void changeGesuchstatusFromVersendetToKeinStipendienanspruch() {
        final var gesuch = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.VERFUEGUNG_VERSENDET);
        gesuch.getVerfuegungs().add((Verfuegung) new Verfuegung().setTimestampErstellt(LocalDateTime.now()));
        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        when(berechnungService.getBerechnungsresultatFromGesuch(gesuch, 1, 0))
            .thenReturn(new BerechnungsresultatDto().berechnungTotal(0).year(Year.now().getValue()));
        assertDoesNotThrow(() -> gesuchService.gesuchStatusToStipendienanspruch(gesuch.getId()));
        assertEquals(
            Gesuchstatus.KEIN_STIPENDIENANSPRUCH,
            gesuchRepository.requireById(gesuch.getId()).getGesuchStatus()
        );
    }

    @TestAsSachbearbeiter
    @Test
    void changeGesuchstatusFromVersendetToStipendienanspruch() {
        final var gesuchOrig = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.VERFUEGUNG_VERSENDET);
        gesuchOrig.getVerfuegungs().add((Verfuegung) new Verfuegung().setTimestampErstellt(LocalDateTime.now()));
        final var gesuch = Mockito.spy(gesuchOrig);
        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        when(berechnungService.getBerechnungsresultatFromGesuch(gesuch, 1, 0))
            .thenReturn(new BerechnungsresultatDto().berechnungTotal(1).year(Year.now().getValue()));
        QuarkusMock.installMockForType(Mockito.mock(SapService.class), SapService.class);
        assertDoesNotThrow(() -> gesuchService.gesuchStatusToStipendienanspruch(gesuch.getId()));
        assertEquals(
            Gesuchstatus.STIPENDIENANSPRUCH,
            gesuchRepository.requireById(gesuch.getId()).getGesuchStatus()
        );
    }

    @TestAsSachbearbeiter
    @Test
    @Description("It should be possible to change Gesuchstatus from IN_FREIGABE to VERFUEGT")
    void changeGesuchstatus_from_InFreigabe_to_VerfuegtTest() {
        Gesuch gesuchOrig = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.IN_FREIGABE);
        var gesuch = Mockito.spy(gesuchOrig);
        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        doNothing().when(gesuchValidatorService).validateGesuchForTransition(any(), any());

        when(berechnungService.getBerechnungsresultatFromGesuch(gesuch, 1, 0))
            .thenReturn(new BerechnungsresultatDto().berechnungTotal(0).year(Year.now().getValue()));

        var verfuegung = new Verfuegung();
        verfuegung.setTimestampErstellt(LocalDateTime.now());
        verfuegung.setGesuch(gesuch);
        gesuch.getVerfuegungs().add(verfuegung);
        when(verfuegungPdfService.createVerfuegungOhneAnspruchPdf(any(), any()))
            .thenReturn(new ByteArrayOutputStream());
        when(stipDecisionTextRepository.requireById(any())).thenReturn(new StipDecisionText());

        assertDoesNotThrow(() -> gesuchService.gesuchStatusToVerfuegt(gesuch.getId()));
        assertEquals(
            Gesuchstatus.VERFUEGT,
            gesuchRepository.requireById(gesuch.getId()).getGesuchStatus()
        );
    }

    @TestAsSachbearbeiter
    @Test
    @Description("It should be possible to change Gesuchstatus from IN_FREIGABE to BEREIT_FUER_BEARBEITUNG")
    void changeGesuchstatus_from_InFreigabe_to_BereitFuerBearbeitungTest() {
        Gesuch gesuch = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.IN_FREIGABE);
        when(gesuchRepository.requireById(any())).thenReturn(gesuch);

        assertDoesNotThrow(() -> gesuchService.gesuchStatusToBereitFuerBearbeitung(gesuch.getId()));
        assertEquals(
            Gesuchstatus.BEREIT_FUER_BEARBEITUNG,
            gesuchRepository.requireById(gesuch.getId()).getGesuchStatus()
        );
    }

    @TestAsSachbearbeiter
    @Test
    @Description("GS should receive an email and a notifcation if documents are missing")
    void documentsMissingSentMessagesTest() {
        // arrange
        Zuordnung zuordnung = new Zuordnung();
        zuordnung.setSachbearbeiter(
            (Sachbearbeiter) new Sachbearbeiter()
                .setFunktionDe("")
                .setFunktionFr("")
                .setTelefonnummer("")
                .setEmail("")
                .setVorname("test")
                .setNachname("test")
        );
        Fall fall = new Fall();
        fall.setSachbearbeiterZuordnung(zuordnung);
        Gesuch gesuch = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.IN_BEARBEITUNG_SB);
        gesuch.getAusbildung().setFall(fall);

        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        Mockito.doNothing().when(notificationRepository).persistAndFlush(any(Notification.class));
        Mockito.doNothing().when(mailService).sendStandardNotificationEmail(any(), any(), any(), any());

        // act
        gesuchService.gesuchFehlendeDokumenteUebermitteln(gesuch.getId());

        // assert
        Mockito.verify(notificationService).createMissingDocumentNotification(any());

        // TODO KSTIP-1652: Deduplicate mail sending
        Mockito.verify(mailService, Mockito.atMost(2)).sendStandardNotificationEmail(any(), any(), any(), any());
    }

    @TestAsSachbearbeiter
    @Test
    @Description("gesuchFehlendeDokumenteUebermitteln should also handle custom documents in state AUSSTEHEND")
    void gesuchFehlendeDokumenteEinreichenAlsoHandlesGenericMissingDocuments() {
        // arrange
        Zuordnung zuordnung = new Zuordnung();
        zuordnung.setSachbearbeiter(
            (Sachbearbeiter) new Sachbearbeiter()
                .setFunktionDe("")
                .setFunktionFr("")
                .setTelefonnummer("")
                .setEmail("")
                .setVorname("test")
                .setNachname("test")
        );

        /*
         * Setup:
         * a gesuch in state IN_BEARBEITUNG_SB
         * a customGesuchDokumentTyp
         * one gesuchDokument in state ABGELEHNT
         * one cusotm gesuchDokument in state AUSSTEHEND
         *
         * Expected:
         * method call should not fail, no exception thrown
         * these 2 gesuchDokuments should appear in state AUSSTEHEND (to the GS)
         */

        Fall fall = new Fall();
        fall.setSachbearbeiterZuordnung(zuordnung);
        Gesuch gesuch = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.IN_BEARBEITUNG_SB);

        // ad a "normal" gesuch document
        GesuchDokument gesuchDokument = new GesuchDokument();
        gesuchDokument.setGesuchTranche(gesuch.getNewestGesuchTranche().orElseThrow());
        gesuchDokument.setDokumentTyp(DokumentTyp.values()[0]);
        // gesuchDokument.setDokumente(List.of(new Dokument()));
        gesuchDokument.setId(UUID.randomUUID());
        gesuchDokument.setStatus(GesuchDokumentStatus.ABGELEHNT);

        // add custom document
        CustomDokumentTyp customDokument = new CustomDokumentTyp();
        customDokument.setId(UUID.randomUUID());
        customDokument.setType("test");
        customDokument.setDescription("test");
        GesuchDokument customGesuchDokument = new GesuchDokument();
        customGesuchDokument.setId(UUID.randomUUID());
        customGesuchDokument.setStatus(GesuchDokumentStatus.AUSSTEHEND);
        customGesuchDokument.setCustomDokumentTyp(customDokument);
        customGesuchDokument.setGesuchTranche(gesuch.getNewestGesuchTranche().orElseThrow());

        // add gesuchdokumente to gesuch
        gesuch.getNewestGesuchTranche().orElseThrow().getGesuchDokuments().add(customGesuchDokument);
        gesuch.getNewestGesuchTranche().orElseThrow().getGesuchDokuments().add(gesuchDokument);

        gesuch.getAusbildung().setFall(fall);

        when(gesuchDokumentRepository.getAllForGesuchInStatus(gesuch, GesuchDokumentStatus.ABGELEHNT))
            .thenReturn(Stream.of(gesuchDokument));
        when(gesuchDokumentRepository.getAllForGesuchInStatus(gesuch, GesuchDokumentStatus.AUSSTEHEND))
            .thenReturn(Stream.of(customGesuchDokument));

        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        when(gesuchTrancheRepository.requireById(any())).thenReturn(gesuch.getGesuchTranchen().get(0));
        Mockito.doNothing().when(notificationRepository).persistAndFlush(any(Notification.class));
        Mockito.doNothing().when(mailService).sendStandardNotificationEmail(any(), any(), any(), any());

        assertDoesNotThrow(() -> gesuchService.gesuchFehlendeDokumenteUebermitteln(gesuch.getId()));
    }

    @Description("gesuchFehlendeDokumenteUebermitteln should also handle custom documents in state AUSSTEHEND")
    @Test
    @TestAsSachbearbeiter
    void gesuchFehlendeDokumenteEinreichenAlsoHandlesGenericMissingDocumentsAllOthersAccepted() {
        // arrange
        Zuordnung zuordnung = new Zuordnung();
        zuordnung.setSachbearbeiter(
            (Sachbearbeiter) new Sachbearbeiter()
                .setFunktionDe("")
                .setFunktionFr("")
                .setTelefonnummer("")
                .setEmail("")
                .setVorname("test")
                .setNachname("test")
        );

        /*
         * Setup:
         * a gesuch in state IN_BEARBEITUNG_SB
         * a customGesuchDokumentTyp
         * one gesuchDokument in state AKZEPTIERT
         * one cusotm gesuchDokument in state AUSSTEHEND
         *
         * Expected:
         * method call should not fail, no exception thrown
         */

        Fall fall = new Fall();
        fall.setSachbearbeiterZuordnung(zuordnung);
        Gesuch gesuch = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.IN_BEARBEITUNG_SB);

        // ad a "normal" gesuch document
        GesuchDokument gesuchDokument = new GesuchDokument();
        gesuchDokument.setGesuchTranche(gesuch.getNewestGesuchTranche().orElseThrow());
        gesuchDokument.setDokumentTyp(DokumentTyp.values()[0]);
        // gesuchDokument.setDokumente(List.of(new Dokument()));
        gesuchDokument.setId(UUID.randomUUID());
        gesuchDokument.setStatus(GesuchDokumentStatus.AKZEPTIERT);

        // add custom document
        CustomDokumentTyp customDokument = new CustomDokumentTyp();
        customDokument.setId(UUID.randomUUID());
        customDokument.setType("test");
        customDokument.setDescription("test");
        GesuchDokument customGesuchDokument = new GesuchDokument();
        customGesuchDokument.setId(UUID.randomUUID());
        customGesuchDokument.setStatus(GesuchDokumentStatus.AUSSTEHEND);
        customGesuchDokument.setCustomDokumentTyp(customDokument);
        customGesuchDokument.setGesuchTranche(gesuch.getNewestGesuchTranche().orElseThrow());

        // add gesuchdokumente to gesuch
        gesuch.getNewestGesuchTranche().orElseThrow().getGesuchDokuments().add(customGesuchDokument);
        gesuch.getNewestGesuchTranche().orElseThrow().getGesuchDokuments().add(gesuchDokument);

        gesuch.getAusbildung().setFall(fall);

        when(gesuchDokumentRepository.getAllForGesuchInStatus(gesuch, GesuchDokumentStatus.AKZEPTIERT))
            .thenReturn(Stream.of(gesuchDokument));
        when(gesuchDokumentRepository.getAllForGesuchInStatus(gesuch, GesuchDokumentStatus.AUSSTEHEND))
            .thenReturn(Stream.of(customGesuchDokument));

        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        when(gesuchTrancheRepository.requireById(any())).thenReturn(gesuch.getGesuchTranchen().get(0));
        Mockito.doNothing().when(notificationRepository).persistAndFlush(any(Notification.class));
        Mockito.doNothing().when(mailService).sendStandardNotificationEmail(any(), any(), any(), any());

        assertDoesNotThrow(() -> gesuchService.gesuchFehlendeDokumenteUebermitteln(gesuch.getId()));
    }

    @TestAsGesuchsteller
    @Test
    @Description("Try to einreichen a gesuch in state FEHLENDE_DOKUMENTE with missing documents")
    void gesuchFehlendeDokumenteEinreichenFail() {
        // arrange
        Zuordnung zuordnung = new Zuordnung();
        zuordnung.setSachbearbeiter(
            (Sachbearbeiter) new Sachbearbeiter()
                .setFunktionDe("")
                .setFunktionFr("")
                .setTelefonnummer("")
                .setEmail("")
                .setVorname("test")
                .setNachname("test")
        );
        Fall fall = new Fall();
        fall.setSachbearbeiterZuordnung(zuordnung);
        Gesuch gesuch = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.IN_BEARBEITUNG_SB);
        gesuch.getAusbildung().setFall(fall);
        gesuch.getAusbildung().setAusbildungsgang(null);

        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        when(gesuchTrancheRepository.requireById(any())).thenReturn(gesuch.getGesuchTranchen().get(0));
        Mockito.doNothing().when(notificationRepository).persistAndFlush(any(Notification.class));
        Mockito.doNothing().when(mailService).sendStandardNotificationEmail(any(), any(), any(), any());
        gesuchService.gesuchFehlendeDokumenteUebermitteln(gesuch.getId());

        // act/assert
        try {
            gesuchService.gesuchFehlendeDokumenteEinreichen(gesuch.getGesuchTranchen().get(0).getId());
        } catch (ValidationsException e) {
            assertThat(e.toString(), Matchers.containsString("Dokument wurde nicht hochgeladen"));
        }
        // assertThrows(
        // ValidationsException.class,
        // () -> gesuchService.gesuchFehlendeDokumenteEinreichen(gesuch.getGesuchTranchen().get(0).getId())
        // );
    }

    @TestAsGesuchsteller
    @Test
    @Description("Try to einreichen a gesuch in state FEHLENDE_DOKUMENTE with missing documents")
    void gesuchFehlendeDokumenteEinreichen() {
        // arrange
        Zuordnung zuordnung = new Zuordnung();
        zuordnung.setSachbearbeiter(
            (Sachbearbeiter) new Sachbearbeiter()
                .setFunktionDe("")
                .setFunktionFr("")
                .setTelefonnummer("")
                .setEmail("")
                .setVorname("test")
                .setNachname("test")
        );
        Fall fall = new Fall();
        fall.setSachbearbeiterZuordnung(zuordnung);
        Gesuch gesuch = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.IN_BEARBEITUNG_SB);
        gesuch.getAusbildung().setFall(fall);

        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        when(gesuchTrancheRepository.requireById(any())).thenReturn(gesuch.getGesuchTranchen().get(0));
        Mockito.doNothing().when(notificationRepository).persistAndFlush(any(Notification.class));
        Mockito.doNothing().when(mailService).sendStandardNotificationEmail(any(), any(), any(), any());
        var gesuchTrancheValidatorServiceMock = Mockito.mock(GesuchTrancheValidatorService.class);

        Mockito.doNothing().when(gesuchTrancheValidatorServiceMock).validateGesuchTrancheForEinreichen(any());
        QuarkusMock.installMockForType(gesuchTrancheValidatorServiceMock, GesuchTrancheValidatorService.class);

        gesuchService.gesuchFehlendeDokumenteUebermitteln(gesuch.getId());
        assertThat(gesuch.getGesuchStatus(), is(Gesuchstatus.FEHLENDE_DOKUMENTE));
        gesuchService.gesuchFehlendeDokumenteEinreichen(gesuch.getGesuchTranchen().get(0).getId());
        assertThat(gesuch.getGesuchStatus(), is(Gesuchstatus.BEREIT_FUER_BEARBEITUNG));
    }

    @TestAsGesuchsteller
    @Test
    @Description("getGesuchGS should contain possible changes / current Gesuch when in status FEHLENDE_DOKUMENTE")
    void getGesuchGSShouldReturnActualGesuchWhenInStatusFehlendeDokumente() {
        // arrange
        Zuordnung zuordnung = new Zuordnung();
        zuordnung.setSachbearbeiter(
            (Sachbearbeiter) new Sachbearbeiter()
                .setFunktionDe("")
                .setFunktionFr("")
                .setTelefonnummer("")
                .setEmail("")
                .setVorname("test")
                .setNachname("test")
        );
        Fall fall = new Fall();
        fall.setSachbearbeiterZuordnung(zuordnung);
        Gesuch gesuch = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.FEHLENDE_DOKUMENTE);
        gesuch.getAusbildung().setFall(fall);

        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        when(gesuchTrancheRepository.requireById(any())).thenReturn(gesuch.getGesuchTranchen().get(0));
        when(gesuchTrancheRepository.findByIdOptional(any()))
            .thenReturn(Optional.of(gesuch.getGesuchTranchen().get(0)));
        when(gesuchTrancheHistoryService.getLatestTranche(any())).thenReturn(gesuch.getGesuchTranchen().get(0));
        when(gesuchTrancheHistoryService.getCurrentOrHistoricalTrancheForGS(any()))
            .thenReturn(gesuch.getGesuchTranchen().get(0));
        // act
        final var gesuchGS = gesuchService.getGesuchGS(gesuch.getGesuchTranchen().get(0).getId());

        // assert that gesuchHistory is NOT queried, but the actual gesuch is returned
        assertThat(gesuchGS.getGesuchStatus(), is(gesuch.getGesuchStatus()));
    }

    @TestAsGesuchsteller
    @Test
    @Description("getGesuchGS should return Gesuch in state EINGEREICHT when in status IN_FREIGABE")
    void getGesuchGSShouldNOTReturnActualGesuchWhenInStatusInFreigabe() {
        // arrange
        Zuordnung zuordnung = new Zuordnung();
        zuordnung.setSachbearbeiter(
            (Sachbearbeiter) new Sachbearbeiter()
                .setFunktionDe("")
                .setFunktionFr("")
                .setTelefonnummer("")
                .setEmail("")
                .setVorname("test")
                .setNachname("test")
        );
        Fall fall = new Fall();
        fall.setSachbearbeiterZuordnung(zuordnung);
        Gesuch gesuch = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.IN_FREIGABE);
        gesuch.getAusbildung().setFall(fall);
        gesuch.setEinreichedatum(LocalDate.now());

        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        when(gesuchTrancheRepository.requireById(any())).thenReturn(gesuch.getGesuchTranchen().get(0));
        when(gesuchTrancheRepository.findByIdOptional(any()))
            .thenReturn(Optional.of(gesuch.getGesuchTranchen().get(0)));
        when(gesuchTrancheHistoryService.getLatestTranche(any())).thenReturn(gesuch.getGesuchTranchen().get(0));
        final var gesuchToReturn = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.EINGEREICHT);
        when(gesuchTrancheHistoryRepository.getLatestWhereGesuchStatusChangedToEingereicht(any(), any()))
            .thenReturn(gesuchToReturn.getNewestGesuchTranche());
        when(gesuchHistoryRepository.getLatestWhereStatusChangedTo(any(), any()))
            .thenReturn(Optional.of(gesuchToReturn));
        when(gesuchTrancheHistoryService.getLatestTranche(any()))
            .thenReturn(gesuchToReturn.getGesuchTranchen().get(0));
        when(gesuchTrancheHistoryService.getCurrentOrHistoricalTrancheForGS(any()))
            .thenReturn(gesuchToReturn.getGesuchTranchen().get(0));

        final var gesuchGS = gesuchService.getGesuchGS(gesuch.getGesuchTranchen().get(0).getId());
        // assert that gesuchHistory IS queried AND the gesuch in state EINGEREICHT is returned
        assertThat(gesuchGS.getGesuchStatus(), is(Gesuchstatus.EINGEREICHT));
    }

    @TestAsGesuchsteller
    @Test
    @Description("getGesuchGS should contain possible changes / current Gesuch when in status VERFUEGT")
    void getGesuchGSShouldReturnActualGesuchWhenInStatusStipendienanspruch() {
        // arrange
        Zuordnung zuordnung = new Zuordnung();
        zuordnung.setSachbearbeiter(
            (Sachbearbeiter) new Sachbearbeiter()
                .setFunktionDe("")
                .setFunktionFr("")
                .setTelefonnummer("")
                .setEmail("")
                .setVorname("test")
                .setNachname("test")
        );
        Fall fall = new Fall();
        fall.setSachbearbeiterZuordnung(zuordnung);
        Gesuch gesuch = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.STIPENDIENANSPRUCH);
        gesuch.getAusbildung().setFall(fall);
        gesuch.setEinreichedatum(LocalDate.now());

        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        when(gesuchTrancheRepository.requireById(any())).thenReturn(gesuch.getGesuchTranchen().get(0));
        when(gesuchTrancheRepository.findByIdOptional(any()))
            .thenReturn(Optional.of(gesuch.getGesuchTranchen().get(0)));
        when(gesuchTrancheHistoryService.getLatestTranche(any())).thenReturn(gesuch.getGesuchTranchen().get(0));
        when(gesuchTrancheHistoryService.getCurrentOrHistoricalTrancheForGS(any()))
            .thenReturn(gesuch.getGesuchTranchen().get(0));
        when(gesuchHistoryRepository.getStatusHistory(any())).thenReturn(
            List.of(
                GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.IN_BEARBEITUNG_GS),
                GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.EINGEREICHT),
                GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.IN_BEARBEITUNG_SB),
                GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.FEHLENDE_DOKUMENTE),
                GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.EINGEREICHT),
                GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.IN_BEARBEITUNG_SB)
            )
        );
        final var gesuchToReturn = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.EINGEREICHT);
        when(gesuchHistoryRepository.getLatestWhereStatusChangedTo(any(), any()))
            .thenReturn(Optional.of(gesuchToReturn));

        final var gesuchGS = gesuchService.getGesuchGS(gesuch.getGesuchTranchen().get(0).getId());
        // assert that gesuchHistory is NOT queried, but the actual gesuch is returned
        assertThat(gesuchGS.getGesuchStatus(), is(gesuch.getGesuchStatus()));
    }

    /*
     * Gesuch is in state IN_BEARBEITUNG_SB.
     * GS receives Tranche of state when Gesuch was in state EINGEREICHT.
     * the changes made to the tranche by SB (previous step) should not be visible
     * changes is empty
     * current gesuchtranche : state of eingereicht
     */
    @Test
    @Description("GS should receive data of gesuch in state eingereicht when in bearbeitung by SB")
    void checkGSReceivesTrancheInStateEingereicht() {
        // arrange
        Zuordnung zuordnung = new Zuordnung();
        zuordnung.setSachbearbeiter(
            (Sachbearbeiter) new Sachbearbeiter()
                .setFunktionDe("")
                .setFunktionFr("")
                .setTelefonnummer("")
                .setEmail("")
                .setVorname("test")
                .setNachname("test")
        );
        Fall fall = new Fall();
        fall.setSachbearbeiterZuordnung(zuordnung);

        final int initialWohnkostenValue = 10;
        final int editedWohnkostenValue = 77;

        Gesuch eingereichtesGesuch = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.EINGEREICHT);
        eingereichtesGesuch.getGesuchTranchen()
            .get(0)
            .getGesuchFormular()
            .setEinnahmenKosten(new EinnahmenKosten());
        eingereichtesGesuch.getGesuchTranchen()
            .get(0)
            .getGesuchFormular()
            .getEinnahmenKosten()
            .setWohnkosten(initialWohnkostenValue);
        eingereichtesGesuch.getAusbildung().setFall(fall);

        Gesuch gesuchInBearbeitungSB = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.IN_BEARBEITUNG_SB);
        gesuchInBearbeitungSB.getGesuchTranchen()
            .get(0)
            .getGesuchFormular()
            .setEinnahmenKosten(new EinnahmenKosten());
        gesuchInBearbeitungSB.getGesuchTranchen()
            .get(0)
            .getGesuchFormular()
            .getEinnahmenKosten()
            .setWohnkosten(editedWohnkostenValue);
        gesuchInBearbeitungSB.getAusbildung().setFall(fall);
        gesuchInBearbeitungSB.setEinreichedatum(LocalDate.now());

        when(gesuchRepository.requireById(any())).thenReturn(gesuchInBearbeitungSB);
        when(gesuchTrancheRepository.requireById(any())).thenReturn(gesuchInBearbeitungSB.getGesuchTranchen().get(0));
        when(gesuchTrancheRepository.findByIdOptional(any()))
            .thenReturn(Optional.of(gesuchInBearbeitungSB.getGesuchTranchen().get(0)));
        when(gesuchTrancheHistoryService.getLatestTranche(any()))
            .thenReturn(eingereichtesGesuch.getGesuchTranchen().get(0));
        when(gesuchTrancheHistoryService.getCurrentOrHistoricalTrancheForGS(any()))
            .thenReturn(eingereichtesGesuch.getGesuchTranchen().get(0));
        when(gesuchHistoryRepository.getStatusHistory(any())).thenReturn(
            List.of(
                GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.IN_BEARBEITUNG_GS),
                eingereichtesGesuch,
                gesuchInBearbeitungSB
            )
        );
        when(gesuchTrancheHistoryRepository.getLatestWhereGesuchStatusChangedToEingereicht(any(), any()))
            .thenReturn(Optional.ofNullable(eingereichtesGesuch.getGesuchTranchen().get(0)));
        when(gesuchHistoryRepository.getLatestWhereStatusChangedTo(any(), any()))
            .thenReturn(Optional.of(eingereichtesGesuch));
        var gesuchGS = gesuchService
            .getGesuchGS(gesuchInBearbeitungSB.getGesuchTranchen().get(0).getId());
        assertThat(gesuchGS.getGesuchStatus(), is(eingereichtesGesuch.getGesuchStatus()));
        assertThat(
            gesuchGS.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().getWohnkosten(),
            is(initialWohnkostenValue)
        );

        // also test for other related states
        gesuchInBearbeitungSB.setGesuchStatus(Gesuchstatus.BEREIT_FUER_BEARBEITUNG);
        gesuchGS = gesuchService
            .getGesuchGS(gesuchInBearbeitungSB.getGesuchTranchen().get(0).getId());
        assertThat(gesuchGS.getGesuchStatus(), is(eingereichtesGesuch.getGesuchStatus()));
        assertThat(
            gesuchGS.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().getWohnkosten(),
            is(initialWohnkostenValue)
        );
    }

    /*
     * Gesuch gets rejected by SB
     * data of GesuchFormular should be reset entirely to the state of EINGEREICHT
     * the gesuch shoudl be in state IN_BEARBEITUNG_GS
     */
    @Test
    @Description("The whole gesuch should should be reset to snapshot of EINGEREICHT when rejected by SB")
    void checkGesuchIsResetedAfterRejectionTest() {
        // arrange
        Zuordnung zuordnung = new Zuordnung();
        zuordnung.setSachbearbeiter(
            (Sachbearbeiter) new Sachbearbeiter()
                .setFunktionDe("")
                .setFunktionFr("")
                .setTelefonnummer("")
                .setEmail("")
                .setVorname("test")
                .setNachname("test")
        );
        Fall fall = new Fall();
        fall.setSachbearbeiterZuordnung(zuordnung);

        final int initialWohnkostenValue = 10;
        final int editedWohnkostenValue = 77;

        Gesuch eingereichtesGesuch = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.EINGEREICHT);
        eingereichtesGesuch.getGesuchTranchen()
            .get(0)
            .getGesuchFormular()
            .setEinnahmenKosten(new EinnahmenKosten());
        eingereichtesGesuch.getGesuchTranchen()
            .get(0)
            .getGesuchFormular()
            .getEinnahmenKosten()
            .setWohnkosten(initialWohnkostenValue);
        eingereichtesGesuch.getAusbildung().setFall(fall);

        Gesuch gesuchInBearbeitungSB = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.IN_BEARBEITUNG_SB);
        final var gesuchInBearbeitungSpy = Mockito.spy(gesuchInBearbeitungSB);
        gesuchInBearbeitungSpy.getGesuchTranchen()
            .get(0)
            .getGesuchFormular()
            .setEinnahmenKosten(new EinnahmenKosten());
        gesuchInBearbeitungSpy.getGesuchTranchen()
            .get(0)
            .getGesuchFormular()
            .getEinnahmenKosten()
            .setWohnkosten(editedWohnkostenValue);
        gesuchInBearbeitungSpy.getAusbildung().setFall(fall);

        doReturn(gesuchInBearbeitungSpy.getGesuchTranchen().get(0)).when(gesuchInBearbeitungSpy)
            .getLatestGesuchTranche();

        when(gesuchRepository.requireById(any())).thenReturn(gesuchInBearbeitungSpy);
        when(gesuchHistoryRepository.getStatusHistory(any())).thenReturn(
            List.of(
                GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.IN_BEARBEITUNG_GS),
                eingereichtesGesuch,
                gesuchInBearbeitungSpy
            )
        );
        when(gesuchHistoryRepository.getLatestWhereStatusChangedTo(any(), any()))
            .thenReturn(Optional.of(eingereichtesGesuch));

        // gesuch gets rejected
        gesuchInBearbeitungSpy.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_SB);
        when(gesuchTrancheRepository.requireById(any())).thenReturn(gesuchInBearbeitungSB.getGesuchTranchen().get(0));
        when(gesuchTrancheService.getGesuchTrancheOrHistorical(any()))
            .thenReturn(gesuchInBearbeitungSB.getGesuchTranchen().get(0));
        gesuchService.gesuchZurueckweisen(gesuchInBearbeitungSpy.getId(), new KommentarDto("test"));
        final var gesuchSB = gesuchService
            .getGesuchSB(gesuchInBearbeitungSpy.getId(), gesuchInBearbeitungSpy.getGesuchTranchen().get(0).getId());
        assertThat(gesuchSB.getGesuchStatus(), is(Gesuchstatus.IN_BEARBEITUNG_GS));
        assertThat(
            gesuchSB.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().getWohnkosten(),
            is(initialWohnkostenValue)
        );
    }

    @TestAsGesuchsteller
    @Test
    @Description("Check that gesuch is moved to state IN_BEARBEITUNG_GS when the frist is done")
    void checkForFehlendeDokumenteOnAllGesucheTest() {
        // arrange
        Zuordnung zuordnung = new Zuordnung();
        zuordnung.setSachbearbeiter(
            (Sachbearbeiter) new Sachbearbeiter()
                .setFunktionDe("")
                .setFunktionFr("")
                .setTelefonnummer("")
                .setEmail("")
                .setVorname("test")
                .setNachname("test")
        );
        Fall fall = new Fall();
        fall.setSachbearbeiterZuordnung(zuordnung);
        Gesuch gesuch = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.IN_BEARBEITUNG_SB);
        gesuch.getAusbildung().setFall(fall);

        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        when(gesuchRepository.getAllFehlendeDokumente()).thenReturn(List.of(gesuch));
        when(gesuchTrancheRepository.requireById(any())).thenReturn(gesuch.getGesuchTranchen().get(0));
        Mockito.doNothing().when(notificationRepository).persistAndFlush(any(Notification.class));
        Mockito.doNothing().when(mailService).sendStandardNotificationEmail(any(), any(), any(), any());
        var gesuchTrancheValidatorServiceMock = Mockito.mock(GesuchTrancheValidatorService.class);
        Mockito.doNothing().when(gesuchTrancheValidatorServiceMock).validateGesuchTrancheForEinreichen(any());
        QuarkusMock.installMockForType(gesuchTrancheValidatorServiceMock, GesuchTrancheValidatorService.class);

        gesuchService.gesuchFehlendeDokumenteUebermitteln(gesuch.getId());
        gesuch.setNachfristDokumente(LocalDate.now().minusDays(1));
        when(
            gesuchHistoryRepository
                .getLatestWhereStatusChangedTo(any(), ArgumentMatchers.eq(Gesuchstatus.FEHLENDE_DOKUMENTE))
        ).thenReturn(
            Optional.of(gesuch)
        );

        when(gesuchHistoryRepository.getLatestWhereStatusChangedTo(any(), any()))
            .thenReturn(Optional.of(gesuch));

        gesuchService.checkForFehlendeDokumenteOnAllGesuche();
        assertThat(gesuch.getGesuchStatus(), is(Gesuchstatus.IN_BEARBEITUNG_GS));
        assertNull(gesuch.getEinreichedatum());
    }

    @TestAsGesuchsteller
    @Test
    @Description("Check that verfuegt gesuch is moved to state BEREIT_FUER_BEARBEITUNG when the frist is done")
    void checkForFehlendeDokumenteOnAllVerfuegtGesucheTest() {
        // arrange
        Zuordnung zuordnung = new Zuordnung();
        zuordnung.setSachbearbeiter(
            (Sachbearbeiter) new Sachbearbeiter()
                .setFunktionDe("")
                .setFunktionFr("")
                .setTelefonnummer("")
                .setEmail("")
                .setVorname("test")
                .setNachname("test")
        );
        Fall fall = new Fall();
        fall.setSachbearbeiterZuordnung(zuordnung);
        Gesuch gesuch = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.FEHLENDE_DOKUMENTE);
        gesuch.setVerfuegt(true);
        gesuch.getAusbildung().setFall(fall);

        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        when(gesuchRepository.getAllFehlendeDokumente()).thenReturn(List.of(gesuch));
        when(gesuchTrancheRepository.requireById(any())).thenReturn(gesuch.getGesuchTranchen().get(0));
        var gesuchTrancheValidatorServiceMock = Mockito.mock(GesuchTrancheValidatorService.class);
        Mockito.doNothing().when(gesuchTrancheValidatorServiceMock).validateGesuchTrancheForEinreichen(any());
        QuarkusMock.installMockForType(gesuchTrancheValidatorServiceMock, GesuchTrancheValidatorService.class);

        gesuch.setNachfristDokumente(LocalDate.now().minusDays(1));

        gesuchService.checkForFehlendeDokumenteOnAllGesuche();
        assertThat(gesuch.getGesuchStatus(), is(Gesuchstatus.BEREIT_FUER_BEARBEITUNG));
        assertNull(gesuch.getEinreichedatum());
    }

    @Test
    @TestAsJurist
    void gesuchShouldNotBeEingereichtAgainWhenAusbildungUpdatedByJurist() {
        Gesuch gesuch = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.ABKLAERUNG_DURCH_RECHSTABTEILUNG);

        when(decisionService.decide(any())).thenReturn(StipDeciderResult.GESUCH_VALID);
        when(decisionService.getGesuchStatusChangeEvent(any()))
            .thenReturn(GesuchStatusChangeEvent.DATENSCHUTZBRIEF_DRUCKBEREIT);
        gesuch.setEinreichedatum(LocalDate.now());
        when(gesuchRepository.requireById(any())).thenReturn(gesuch);

        gesuchService.setGesuchStatusToAnspruchPruefen(gesuch.getId());
        gesuchService.stipendienAnspruchPruefen(gesuch.getId());
        verify(mailService, never()).sendStandardNotificationEmail(any(), any(), any(), any());
        verify(notificationRepository, never()).persistAndFlush(any(Notification.class));
    }

    @Test
    @TestAsSozialdienstMitarbeiter
    void getFallDashboardAsSozialdienstMitarbeiterTest() {
        // arrange
        Zuordnung zuordnung = new Zuordnung();
        zuordnung.setSachbearbeiter(
            (Sachbearbeiter) new Sachbearbeiter()
                .setFunktionDe("")
                .setFunktionFr("")
                .setTelefonnummer("")
                .setEmail("")
                .setVorname("test")
                .setNachname("test")
        );
        Fall fall = new Fall();
        fall.setGesuchsteller(new Benutzer());
        fall.setSachbearbeiterZuordnung(zuordnung);
        Gesuch gesuch = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.IN_BEARBEITUNG_SB);
        gesuch.getAusbildung().setFall(fall);

        when(fallRepository.requireById(any())).thenReturn(fall);
        final var result = gesuchService.getSozialdienstMitarbeiterFallDashboardItemDtos(UUID.randomUUID());
        assertNotNull(result);
    }

    @TestAsSachbearbeiter
    @Test
    void bearbeitungAbschliessen_shouldNotBePossible_without_veranlagungsStatus() {
        // arrange
        var gesuch = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.IN_BEARBEITUNG_SB);
        var gesuchFormular = gesuch.getGesuchTranchen()
            .get(0)
            .getGesuchFormular();
        gesuch.getVerfuegungs().add((Verfuegung) new Verfuegung().setTimestampErstellt(LocalDateTime.now()));
        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        when(gesuchTrancheRepository.requireById(any())).thenReturn(gesuch.getGesuchTranchen().get(0));
        when(gesuchTrancheHistoryService.getLatestTranche(any())).thenReturn(gesuch.getGesuchTranchen().get(0));
        when(berechnungService.getBerechnungsresultatFromGesuch(gesuch, 1, 0))
            .thenReturn(new BerechnungsresultatDto().berechnungTotal(0).year(Year.now().getValue()));

        gesuchFormular
            .getFamiliensituation()
            .setMutterUnbekanntVerstorben(ElternAbwesenheitsGrund.WEDER_NOCH);
        gesuchFormular.getSteuererklaerung()
            .add(new Steuererklaerung().setSteuerdatenTyp(SteuerdatenTyp.MUTTER).setSteuererklaerungInBern(true));
        gesuchFormular.getSteuerdaten().add(new Steuerdaten().setSteuerdatenTyp(SteuerdatenTyp.MUTTER));

        // act
        var validationReport = gesuchTrancheService.einreichenValidierenSB(UUID.randomUUID());
        var validationErrors = validationReport.getValidationErrors()
            .stream()
            .map(validationErrorDto -> validationErrorDto.getMessageTemplate())
            .toList();

        // assert
        assertThat(
            validationErrors,
            hasItem(
                VALIDATION_STEUERDATEN_VERANLAGUNGSSTATUS_INVALID_MESSAGE
            )
        );
    }

    private GesuchTranche initTrancheFromGesuchUpdate(GesuchUpdateDto gesuchUpdateDto) {
        GesuchTranche tranche = prepareGesuchTrancheWithIds(gesuchUpdateDto.getGesuchTrancheToWorkWith());
        return gesuchTrancheMapper.partialUpdate(gesuchUpdateDto.getGesuchTrancheToWorkWith(), tranche, true);
    }

    private GesuchTranche updateGesetzlicheAlimenteRegel(
        @Nullable Boolean from,
        Boolean to,
        GesuchUpdateDto gesuchUpdateDto
    ) {
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getFamiliensituation()
            .setGerichtlicheAlimentenregelung(from);
        GesuchTranche tranche = prepareGesuchTrancheWithIds(gesuchUpdateDto.getGesuchTrancheToWorkWith());
        tranche.getGesuchFormular().setFamiliensituation(new Familiensituation());
        tranche.getGesuchFormular().getFamiliensituation().setGerichtlicheAlimentenregelung(from);
        gesuchTrancheMapper.partialUpdate(gesuchUpdateDto.getGesuchTrancheToWorkWith(), tranche, true);
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getFamiliensituation()
            .setGerichtlicheAlimentenregelung(to);
        if (to) {
            gesuchUpdateDto.getGesuchTrancheToWorkWith()
                .getGesuchFormular()
                .getFamiliensituation()
                .setWerZahltAlimente(Elternschaftsteilung.GEMEINSAM);
        }

        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        gesuchService.updateGesuch(any(), gesuchUpdateDto, true);
        return tranche;
    }

    private boolean hasMutter(Set<Eltern> elterns) {
        return getElternFromElternsByElternTyp(elterns, ElternTyp.MUTTER).isPresent();
    }

    private boolean hasVater(Set<Eltern> elterns) {
        return getElternFromElternsByElternTyp(elterns, ElternTyp.VATER).isPresent();
    }

    private Optional<Eltern> getElternFromElternsByElternTyp(Set<Eltern> elterns, ElternTyp elternTyp) {
        return elterns.stream()
            .filter(eltern -> eltern.getElternTyp() == elternTyp)
            .findFirst();
    }

    private GesuchTranche updateElternteilUnbekanntVerstorben(
        GesuchUpdateDto gesuchUpdateDto,
        ElternAbwesenheitsGrund grundMutter,
        ElternAbwesenheitsGrund grundVater
    ) {
        GesuchTranche tranche = prepareGesuchTrancheWithIds(gesuchUpdateDto.getGesuchTrancheToWorkWith());
        gesuchTrancheMapper.partialUpdate(gesuchUpdateDto.getGesuchTrancheToWorkWith(), tranche, true);

        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getFamiliensituation()
            .setElternVerheiratetZusammen(false);
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getFamiliensituation()
            .setElternteilUnbekanntVerstorben(true);
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getFamiliensituation()
            .setMutterUnbekanntVerstorben(grundMutter);
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getFamiliensituation()
            .setVaterUnbekanntVerstorben(grundVater);

        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        gesuchService.updateGesuch(any(), gesuchUpdateDto, true);
        return tranche;
    }

    private GesuchTranche updateWerZahltAlimente(
        GesuchUpdateDto gesuchUpdateDto,
        Elternschaftsteilung from,
        Elternschaftsteilung to
    ) {
        GesuchTranche tranche = prepareGesuchTrancheWithIds(gesuchUpdateDto.getGesuchTrancheToWorkWith());
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getFamiliensituation()
            .setGerichtlicheAlimentenregelung(true);
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getFamiliensituation()
            .setWerZahltAlimente(from);
        gesuchTrancheMapper.partialUpdate(gesuchUpdateDto.getGesuchTrancheToWorkWith(), tranche, true);

        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getFamiliensituation()
            .setWerZahltAlimente(to);
        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        gesuchService.updateGesuch(any(), gesuchUpdateDto, true);
        return tranche;
    }

    private GesuchTranche updateFromZivilstandToZivilstand(
        GesuchUpdateDto gesuchUpdateDto,
        Zivilstand from,
        Zivilstand to
    ) {
        GesuchTranche tranche = prepareGesuchTrancheWithIds(gesuchUpdateDto.getGesuchTrancheToWorkWith());
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().setZivilstand(from);
        gesuchTrancheMapper.partialUpdate(gesuchUpdateDto.getGesuchTrancheToWorkWith(), tranche, true);
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().setZivilstand(to);
        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        gesuchService.updateGesuch(any(), gesuchUpdateDto, true);
        return tranche;
    }

    private GesuchTranche prepareGesuchTrancheWithIds(GesuchTrancheUpdateDto trancheUpdate) {
        GesuchTranche tranche = initGesuchTranche();
        GesuchFormular gesuchFormular = new GesuchFormular();
        gesuchFormular.setTranche(tranche);
        tranche.setTyp(GesuchTrancheTyp.TRANCHE);

        trancheUpdate.getGesuchFormular().getElterns().forEach(elternUpdateDto -> {
            elternUpdateDto.setId(UUID.randomUUID());
            gesuchFormular.getElterns().add(elternMapper.partialUpdate(elternUpdateDto, new Eltern()));
        });

        trancheUpdate.getGesuchFormular().getLebenslaufItems().forEach(item -> {
            item.setId(UUID.randomUUID());
            gesuchFormular.getLebenslaufItems().add(lebenslaufItemMapper.partialUpdate(item, new LebenslaufItem()));
        });

        if (trancheUpdate.getGesuchFormular().getSteuererklaerung() != null) {
            trancheUpdate.getGesuchFormular().getSteuererklaerung().forEach(item -> {
                gesuchFormular.getSteuererklaerung()
                    .add(steuererklaerungMapper.partialUpdate(item, new Steuererklaerung()));
            });
        }

        return tranche.setGesuchFormular(gesuchFormular);
    }
}
