package ch.dvbern.stip.api.gesuch.service;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.bildungskategorie.entity.Bildungskategorie;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.service.RequiredDokumentService;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.service.ElternMapper;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import ch.dvbern.stip.api.generator.api.model.gesuch.EinnahmenKostenUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.entities.GesuchGenerator;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuch.type.GetGesucheSBQueryType;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.lebenslauf.service.LebenslaufItemMapper;
import ch.dvbern.stip.api.notification.service.NotificationService;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.generated.dto.FamiliensituationUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import ch.dvbern.stip.generated.dto.GesuchCreateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchTrancheUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import ch.dvbern.stip.generated.dto.ValidationReportDto;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.*;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import static ch.dvbern.stip.api.generator.entities.GesuchGenerator.createGesuch;
import static ch.dvbern.stip.api.generator.entities.GesuchGenerator.initGesuchTranche;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.AUFGELOESTE_PARTNERSCHAFT;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.EINGETRAGENE_PARTNERSCHAFT;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.GESCHIEDEN_GERICHTLICH;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.KONKUBINAT;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.LEDIG;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.VERHEIRATET;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.VERWITWET;
import static ch.dvbern.stip.api.util.TestUtil.initGesuchCreateDto;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@QuarkusTest
@QuarkusTestResource(TestDatabaseEnvironment.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GesuchServiceTest {

    @Inject
    GesuchService gesuchService;

    @Inject
    GesuchTrancheMapper gesuchTrancheMapper;

    @Inject
    ElternMapper elternMapper;

    @Inject
    LebenslaufItemMapper lebenslaufItemMapper;

    @Inject
    SteuerdatenMapper steuerdatenMapper;

    @InjectMock
    GesuchRepository gesuchRepository;

    @InjectMock
    NotificationService notificationService;

    static final String TENANT_ID = "bern";

    @BeforeAll
    void setup() {
        final var requiredDokumentServiceMock = Mockito.mock(RequiredDokumentService.class);
        Mockito.when(requiredDokumentServiceMock.getSuperfluousDokumentsForGesuch(any())).thenReturn(List.of());
        Mockito.when(requiredDokumentServiceMock.getRequiredDokumentsForGesuch(any())).thenReturn(List.of());
        QuarkusMock.installMockForType(requiredDokumentServiceMock, RequiredDokumentService.class);
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
            not(0));
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
            not(0));
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
            not(0));

        GesuchTranche tranche = updateWerZahltAlimente(gesuchUpdateDto, Elternschaftsteilung.GEMEINSAM,
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
        gesuchTrancheMapper.partialUpdate(gesuchUpdateDto.getGesuchTrancheToWorkWith(), tranche);
        tranche.getGesuchFormular().getFamiliensituation().setElternVerheiratetZusammen(true);
        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        gesuchService.updateGesuch(any(), gesuchUpdateDto, TENANT_ID);

        assertThat(
            tranche.getGesuchFormular().getElterns().size(),
            Matchers.is(anzahlElternBeforeUpdate));
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
        gesuchTrancheMapper.partialUpdate(gesuchUpdateDto.getGesuchTrancheToWorkWith(), tranche);
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
        gesuchService.updateGesuch(any(), gesuchUpdateDto, TENANT_ID);
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
            ElternAbwesenheitsGrund.WEDER_NOCH);

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
            ElternAbwesenheitsGrund.WEDER_NOCH);

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
            ElternAbwesenheitsGrund.UNBEKANNT);

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
            ElternAbwesenheitsGrund.VERSTORBEN);

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
            ElternAbwesenheitsGrund.VERSTORBEN);

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
            ElternAbwesenheitsGrund.UNBEKANNT);

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
            .setAlimente(1000);
        GesuchTranche tranche = prepareGesuchTrancheWithIds(gesuchUpdateDto.getGesuchTrancheToWorkWith());
        gesuchTrancheMapper.partialUpdate(gesuchUpdateDto.getGesuchTrancheToWorkWith(), tranche);
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
        gesuchService.updateGesuch(any(), gesuchUpdateDto, TENANT_ID);

        assertThat(tranche.getGesuchFormular().getEinnahmenKosten().getAlimente(), Matchers.nullValue());
    }

    @Test
    @TestAsGesuchsteller
    void resetAlimenteIfGesetzlicheAlimenteregelungFromNullToTrue() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getEinnahmenKosten()
            .setAlimente(1000);

        GesuchTranche tranche = updateGesetzlicheAlimenteRegel(null, true, gesuchUpdateDto);

        assertThat(tranche.getGesuchFormular().getEinnahmenKosten().getAlimente(), Matchers.nullValue());
    }

    @Test
    @TestAsGesuchsteller
    void resetAlimenteIfGesetzlicheAlimenteregelungFromFalseToTrue() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getEinnahmenKosten()
            .setAlimente(1000);

        GesuchTranche tranche = updateGesetzlicheAlimenteRegel(false, true, gesuchUpdateDto);

        assertThat(tranche.getGesuchFormular().getEinnahmenKosten().getAlimente(), Matchers.nullValue());
    }

    @Test
    @TestAsGesuchsteller
    void noResetAlimenteIfGesetzlicheAlimenteregelungFromTrueToTrue() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        final var alimente = 1000;
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setAlimente(alimente);

        GesuchTranche tranche = updateGesetzlicheAlimenteRegel(true, true, gesuchUpdateDto);

        assertThat(tranche.getGesuchFormular().getEinnahmenKosten().getAlimente(), Matchers.is(alimente));
    }

    @Test
    @TestAsGesuchsteller
    void resetAlimenteIfGesetzlicheAlimenteregelungFromNullToFalse() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getEinnahmenKosten()
            .setAlimente(1000);

        GesuchTranche tranche = updateGesetzlicheAlimenteRegel(null, false, gesuchUpdateDto);

        assertThat(tranche.getGesuchFormular().getEinnahmenKosten().getAlimente(), Matchers.nullValue());
    }

    @Test
    @TestAsGesuchsteller
    void resetAlimenteIfGesetzlicheAlimenteregelungFromTrueToFalse() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getEinnahmenKosten()
            .setAlimente(1000);

        GesuchTranche tranche = updateGesetzlicheAlimenteRegel(true, false, gesuchUpdateDto);

        assertThat(tranche.getGesuchFormular().getEinnahmenKosten().getAlimente(), Matchers.nullValue());
    }

    @Test
    @TestAsGesuchsteller
    void noResetAlimenteIfGesetzlicheAlimenteregelungFromFalseToFalse() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        final var alimente = 1000;
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setAlimente(alimente);
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getFamiliensituation()
            .setGerichtlicheAlimentenregelung(false);

        GesuchTranche tranche = updateGesetzlicheAlimenteRegel(false, false, gesuchUpdateDto);

        assertThat(tranche.getGesuchFormular().getEinnahmenKosten().getAlimente(), Matchers.is(alimente));
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
        gesuchService.updateGesuch(any(), gesuchUpdateDto, TENANT_ID);

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

        GesuchTranche tranche = initTrancheFromGesuchUpdate(gesuchUpdateDto);

        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getPersonInAusbildung()
            .setWohnsitz(Wohnsitz.FAMILIE);
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getPersonInAusbildung()
            .setWohnsitzAnteilMutter(null);
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getPersonInAusbildung()
            .setWohnsitzAnteilVater(null);

        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        gesuchService.updateGesuch(any(), gesuchUpdateDto, TENANT_ID);

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

        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getPersonInAusbildung()
            .setWohnsitz(Wohnsitz.MUTTER_VATER);

        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        gesuchService.updateGesuch(any(), gesuchUpdateDto, TENANT_ID);

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
        gesuchService.updateGesuch(any(), gesuchUpdateDto, TENANT_ID);

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
        gesuchService.updateGesuch(any(), gesuchUpdateDto, TENANT_ID);

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
        gesuchService.updateGesuch(any(), gesuchUpdateDto, TENANT_ID);
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
        gesuchService.updateGesuch(any(), gesuchUpdateDto, TENANT_ID);
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
        gesuchService.updateGesuch(any(), gesuchUpdateDto, TENANT_ID);
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

        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        when(gesuchRepository.findGesucheBySvNummer(any())).thenReturn(Stream.of((Gesuch)
            new Gesuch()
                .setGesuchStatus(Gesuchstatus.EINGEREICHT)
                .setId(UUID.randomUUID())
        ));

        ValidationReportDto reportDto = gesuchService.validateGesuchEinreichen(tranche.getGesuch().getId());

        assertThat(
            reportDto.getValidationErrors().size(),
            Matchers.is(1)
        );
    }

    @Test
    @TestAsGesuchsteller
    void validateEinreichenValid() {
        EinnahmenKostenUpdateDtoSpecModel.einnahmenKostenUpdateDtoSpec.setSteuerjahr(0);
        final var gesuchUpdateDto = GesuchGenerator.createFullGesuch();
        final var famsit = new FamiliensituationUpdateDto();
        famsit.setElternVerheiratetZusammen(false);
        famsit.setGerichtlicheAlimentenregelung(false);
        famsit.setElternteilUnbekanntVerstorben(true);
        famsit.setMutterUnbekanntVerstorben(ElternAbwesenheitsGrund.VERSTORBEN);
        famsit.setVaterUnbekanntVerstorben(ElternAbwesenheitsGrund.VERSTORBEN);
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().setElterns(new ArrayList<>());
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().setFamiliensituation(famsit);
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setSteuerjahr(0);
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().setPartner(null);

        GesuchTranche tranche = initTrancheFromGesuchUpdate(GesuchGenerator.createFullGesuch());
        tranche.getGesuchFormular()
            .getAusbildung()
            .setAusbildungsgang(new Ausbildungsgang().setBildungskategorie(new Bildungskategorie()));

        tranche.getGesuchFormular().setTranche(tranche);
        tranche.getGesuch().setGesuchDokuments(
            Arrays.stream(DokumentTyp.values())
                .map(x -> new GesuchDokument().setDokumentTyp(x).setGesuch(tranche.getGesuch()))
                .toList()
        );

        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        when(gesuchRepository.findGesucheBySvNummer(any())).thenReturn(Stream.of(tranche.getGesuch()));
        tranche.getGesuchFormular().getEinnahmenKosten().setSteuerjahr(0);
        tranche.getGesuchFormular().setPartner(null);

        Set<Steuerdaten> list = new LinkedHashSet<>();
        list.add(TestUtil.prepareSteuerdaten());
        tranche.getGesuchFormular().setSteuerdaten(list);

        ValidationReportDto reportDto = gesuchService.validateGesuchEinreichen(tranche.getGesuch().getId());

        assertThat(
            reportDto.toString() + "\nEltern: " + gesuchUpdateDto.getGesuchTrancheToWorkWith()
                .getGesuchFormular()
                .getElterns()
                .size(),
            reportDto.getValidationErrors().size(),
            Matchers.is(0)
        );

        EinnahmenKostenUpdateDtoSpecModel.einnahmenKostenUpdateDtoSpec.setSteuerjahr(null);
    }

    // TODO KSTIP-1236: Enable this test
//    @Test
//    @TestAsGesuchsteller
//    void gesuchEinreichenTest() {
//        GesuchTranche tranche = initTrancheFromGesuchUpdate(GesuchGenerator.createFullGesuch());
//        tranche.getGesuchFormular()
//            .getAusbildung()
//            .setAusbildungsgang(new Ausbildungsgang().setBildungsart(new Bildungsart()));
//        final var oldZivilstand = tranche.getGesuchFormular().getPersonInAusbildung().getZivilstand();
//        tranche.getGesuchFormular().getPersonInAusbildung().setZivilstand(LEDIG);
//
//        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
//        when(gesuchRepository.findGesucheBySvNummer(any())).thenReturn(Stream.of(tranche.getGesuch()));
//        doNothing().when(notificationService).createNotification(any(), any());
//
//        tranche.getGesuchFormular().setTranche(tranche);
//        tranche.getGesuchFormular().getEinnahmenKosten().setSteuerjahr(2022);
//        tranche.getGesuchFormular().setPartner(null);
//        tranche.getGesuch().setGesuchDokuments(
//            Arrays.stream(DokumentTyp.values())
//                .map(x -> new GesuchDokument().setDokumentTyp(x).setGesuch(tranche.getGesuch()))
//                .toList()
//        );
//
//        gesuchService.gesuchEinreichen(tranche.getGesuch().getId());
//
//        assertThat(
//            tranche.getGesuch().getGesuchStatus(),
//            Matchers.is(Gesuchstatus.BEREIT_FUER_BEARBEITUNG)
//        );
//
//        tranche.getGesuchFormular().getPersonInAusbildung().setZivilstand(oldZivilstand);
//    }
    private Steuerdaten prepareSteuerdaten() {
        Steuerdaten steuerdaten = new Steuerdaten();
        steuerdaten.setSteuerdatenTyp(SteuerdatenTyp.FAMILIE);
        steuerdaten.setEigenmietwert(0);
        steuerdaten.setVerpflegung(0);
        steuerdaten.setIsArbeitsverhaeltnisSelbstaendig(false);
        steuerdaten.setTotalEinkuenfte(0);
        steuerdaten.setFahrkosten(0);
        steuerdaten.setKinderalimente(0);
        steuerdaten.setSteuernBund(0);
        steuerdaten.setSteuernStaat(0);
        steuerdaten.setVermoegen(0);
        steuerdaten.setErgaenzungsleistungen(0);
        steuerdaten.setSteuerjahr(0);
        return  steuerdaten;
    }

    @Test
    @TestAsGesuchsteller
    void gesuchEinreichenTest() {
        GesuchTranche tranche = initTrancheFromGesuchUpdate(GesuchGenerator.createFullGesuch());
        tranche.getGesuchFormular()
            .getAusbildung()
            .setAusbildungsgang(new Ausbildungsgang().setBildungsart(new Bildungsart()));

        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        when(gesuchRepository.findGesucheBySvNummer(any())).thenReturn(Stream.of(tranche.getGesuch()));

        tranche.getGesuchFormular().setTranche(tranche);
        tranche.getGesuchFormular().getEinnahmenKosten().setSteuerjahr(2022);

        //reset zivilstand if necessary (due to generated values)
        if(tranche.getGesuchFormular().getPersonInAusbildung().getZivilstand().equals(LEDIG)
            && tranche.getGesuchFormular().getFamiliensituation().getElternVerheiratetZusammen()
            && tranche.getGesuchFormular().getFamiliensituation().getElternteilUnbekanntVerstorben() == null){
            tranche.getGesuchFormular().getPersonInAusbildung().setZivilstand(VERHEIRATET);
        }

        tranche.getGesuch().setGesuchDokuments(
            Arrays.stream(DokumentTyp.values())
                .map(x -> new GesuchDokument().setDokumentTyp(x).setGesuch(tranche.getGesuch()))
                .toList()
        );

        Set<Steuerdaten> list = new LinkedHashSet<>();
        list.add(TestUtil.prepareSteuerdaten());
        tranche.getGesuchFormular().setSteuerdaten(list);

        gesuchService.gesuchEinreichen(tranche.getGesuch().getId());

        assertThat(
            tranche.getGesuch().getGesuchStatus(),
            Matchers.is(Gesuchstatus.BEREIT_FUER_BEARBEITUNG)
        );
    }

    private SteuerdatenUpdateDto initSteuerdatenUpdateDto(SteuerdatenTyp typ) {
        SteuerdatenUpdateDto steuerdatenUpdateDto = new SteuerdatenUpdateDto();
        steuerdatenUpdateDto.setId(UUID.randomUUID());
        steuerdatenUpdateDto.setSteuerdatenTyp(typ);
        steuerdatenUpdateDto.setVeranlagungscode(5);
        steuerdatenUpdateDto.setSteuerjahr(2010);
        steuerdatenUpdateDto.setFahrkosten(0);
        steuerdatenUpdateDto.setEigenmietwert(0);
        steuerdatenUpdateDto.setErgaenzungsleistungen(0);
        steuerdatenUpdateDto.setIsArbeitsverhaeltnisSelbstaendig(false);
        steuerdatenUpdateDto.setKinderalimente(0);
        steuerdatenUpdateDto.setSteuernBund(0);
        steuerdatenUpdateDto.setSteuernStaat(0);
        steuerdatenUpdateDto.setTotalEinkuenfte(0);
        steuerdatenUpdateDto.setTotalEinkuenfte(0);
        steuerdatenUpdateDto.setVerpflegung(0);
        steuerdatenUpdateDto.setVermoegen(0);
        return steuerdatenUpdateDto;
    }

    @Test
    @TestAsGesuchsteller
    void gesuchUpdateSteuerdatenSetDefaultValuesTest_NonNullValues() {
        GesuchUpdateDto gesuchUpdateDto = createGesuch();
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().setSteuerdaten(new ArrayList<>());
        SteuerdatenUpdateDto steuerdatenUpdateDto1 = initSteuerdatenUpdateDto(SteuerdatenTyp.FAMILIE);
        steuerdatenUpdateDto1.setSteuerjahr(null);
        steuerdatenUpdateDto1.setVeranlagungscode(null);

        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getSteuerdaten().add(steuerdatenUpdateDto1);
        GesuchTranche tranche = initTrancheFromGesuchUpdate(gesuchUpdateDto);

        //update values with non-null values
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getSteuerdaten().get(0).setSteuerjahr(2010);
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getSteuerdaten().get(0).setVeranlagungscode(5);

        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        when(gesuchRepository.findGesucheBySvNummer(any())).thenReturn(Stream.of(tranche.getGesuch()));
        when(gesuchRepository.findByIdOptional(any())).thenReturn(Optional.ofNullable(tranche.getGesuch()));
        gesuchService.updateGesuch(UUID.randomUUID(), gesuchUpdateDto, TENANT_ID);

        final var steuerdatenTab = tranche.getGesuchFormular().getSteuerdaten().iterator().next();
        assertThat(steuerdatenTab.getVeranlagungsCode(), Matchers.equalTo(0));
        assertThat(
            steuerdatenTab.getSteuerjahr(),
            Matchers.equalTo(tranche.getGesuch().getGesuchsperiode().getGesuchsjahr().getTechnischesJahr() - 1)
        );
    }

    @Test
    @TestAsGesuchsteller
    @DisplayName("Steuerjahr and veranlagungscode existing in db should not be overwritten by GS")
    void gesuchUpdateSteuerdatenTest_NonNullValues() {
        //init values like they would be in the db
        GesuchCreateDtoSpec gesuchCreateDtoSpec = initGesuchCreateDto();
        var gesuchDto = gesuchService.createGesuch(new GesuchCreateDto(gesuchCreateDtoSpec.getFallId(), gesuchCreateDtoSpec.getGesuchsperiodeId()));
        GesuchUpdateDto gesuchUpdateDto = createGesuch();
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().setSteuerdaten(new ArrayList<>());
        SteuerdatenUpdateDto steuerdatenUpdateDto1 = initSteuerdatenUpdateDto(SteuerdatenTyp.FAMILIE);
        steuerdatenUpdateDto1.setSteuerjahr(2010);
        steuerdatenUpdateDto1.setVeranlagungscode(5);
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getSteuerdaten().add(steuerdatenUpdateDto1);
        GesuchTranche tranche = initTrancheFromGesuchUpdate(gesuchUpdateDto);

        //prepare an update dto and set values with non-null values
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getSteuerdaten().get(0).setSteuerjahr(0);
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getSteuerdaten().get(0).setVeranlagungscode(0);

        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        when(gesuchRepository.findGesucheBySvNummer(any())).thenReturn(Stream.of(tranche.getGesuch()));
        when(gesuchRepository.findByIdOptional(any())).thenReturn(Optional.ofNullable(tranche.getGesuch()));
        gesuchService.updateGesuch(gesuchDto.getId(), gesuchUpdateDto, TENANT_ID);

        final var steuerdatenTab = tranche.getGesuchFormular().getSteuerdaten().iterator().next();
        assertThat(steuerdatenTab.getVeranlagungsCode(), Matchers.equalTo(5));
        assertThat(
            steuerdatenTab.getSteuerjahr(),
            Matchers.equalTo(2010)
        );
    }

    @Test
    @TestAsSachbearbeiter
    @DisplayName("Steuerjahr and veranlagungscode in steuerdaten should be overwriten by SB")
    void gesuchUpdateSteuerdatenSetDefaultValuesTest_SBNonNullValues() {
        GesuchCreateDtoSpec gesuchCreateDtoSpec = initGesuchCreateDto();
        var gesuchDto = gesuchService.createGesuch(new GesuchCreateDto(gesuchCreateDtoSpec.getFallId(), gesuchCreateDtoSpec.getGesuchsperiodeId()));
        GesuchUpdateDto gesuchUpdateDto = createGesuch();
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().setSteuerdaten(new ArrayList<>());
        SteuerdatenUpdateDto steuerdatenUpdateDto1 = initSteuerdatenUpdateDto(SteuerdatenTyp.FAMILIE);
        steuerdatenUpdateDto1.setSteuerjahr(null);
        steuerdatenUpdateDto1.setVeranlagungscode(null);

        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getSteuerdaten().add(steuerdatenUpdateDto1);
        GesuchTranche tranche = initTrancheFromGesuchUpdate(gesuchUpdateDto);

        //update values with non-null values
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getSteuerdaten().get(0).setSteuerjahr(2010);
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getSteuerdaten().get(0).setVeranlagungscode(5);

        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        when(gesuchRepository.findGesucheBySvNummer(any())).thenReturn(Stream.of(tranche.getGesuch()));
        when(gesuchRepository.findByIdOptional(any())).thenReturn(Optional.ofNullable(tranche.getGesuch()));
        gesuchService.updateGesuch(gesuchDto.getId(), gesuchUpdateDto, TENANT_ID);

        final var steuerdatenTab = tranche.getGesuchFormular().getSteuerdaten().iterator().next();
        assertThat(steuerdatenTab.getVeranlagungsCode(), Matchers.equalTo(5));
        assertThat(
            steuerdatenTab.getSteuerjahr(),
            Matchers.equalTo(2010)
        );
    }

    @Test
    @TestAsGesuchsteller
    @DisplayName("Steuerjahr and veranlagungscode in steuerdaten should not be overwriten by GS")
    void gesuchUpdateSteuerdatenSetDefaultValuesTest_NullValues() {
        GesuchCreateDtoSpec gesuchCreateDtoSpec = initGesuchCreateDto();
        var gesuchDto = gesuchService.createGesuch(new GesuchCreateDto(gesuchCreateDtoSpec.getFallId(), gesuchCreateDtoSpec.getGesuchsperiodeId()));
        GesuchUpdateDto gesuchUpdateDto = createGesuch();
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().setSteuerdaten(new ArrayList<>());
        SteuerdatenUpdateDto steuerdatenUpdateDto1 = initSteuerdatenUpdateDto(SteuerdatenTyp.FAMILIE);
        steuerdatenUpdateDto1.setSteuerjahr(null);
        steuerdatenUpdateDto1.setVeranlagungscode(null);

        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getSteuerdaten().add(steuerdatenUpdateDto1);
        GesuchTranche tranche = initTrancheFromGesuchUpdate(gesuchUpdateDto);

        //set null values
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getSteuerdaten().get(0).setSteuerjahr(null);
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getSteuerdaten().get(0).setVeranlagungscode(null);

        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        when(gesuchRepository.findGesucheBySvNummer(any())).thenReturn(Stream.of(tranche.getGesuch()));
        when(gesuchRepository.findByIdOptional(any())).thenReturn(Optional.ofNullable(tranche.getGesuch()));
        gesuchService.updateGesuch(gesuchDto.getId(), gesuchUpdateDto, TENANT_ID);

        final var steuerdatenTab = tranche.getGesuchFormular().getSteuerdaten().iterator().next();
        assertThat(steuerdatenTab.getVeranlagungsCode(), Matchers.equalTo(0));
        assertThat(
            steuerdatenTab.getSteuerjahr(),
            Matchers.equalTo(tranche.getGesuch().getGesuchsperiode().getGesuchsjahr().getTechnischesJahr() - 1)
        );
    }

    @Test
    @TestAsSachbearbeiter
    @DisplayName("Correct default values should be set in steuerdaten when executed as SB")
    void gesuchUpdateSteuerdatenSetDefaultValuesTest_SBNullValues() {
        GesuchCreateDtoSpec gesuchCreateDtoSpec = initGesuchCreateDto();
        var gesuchDto = gesuchService.createGesuch(new GesuchCreateDto(gesuchCreateDtoSpec.getFallId(), gesuchCreateDtoSpec.getGesuchsperiodeId()));
        GesuchUpdateDto gesuchUpdateDto = createGesuch();
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().setSteuerdaten(new ArrayList<>());
        SteuerdatenUpdateDto steuerdatenUpdateDto1 = initSteuerdatenUpdateDto(SteuerdatenTyp.VATER);
        steuerdatenUpdateDto1.setSteuerjahr(null);
        steuerdatenUpdateDto1.setVeranlagungscode(null);

        SteuerdatenUpdateDto steuerdatenUpdateDto2 =  initSteuerdatenUpdateDto(SteuerdatenTyp.MUTTER);
        steuerdatenUpdateDto2.setSteuerjahr(null);
        steuerdatenUpdateDto2.setVeranlagungscode(null);

        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getSteuerdaten().add(steuerdatenUpdateDto1);
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getSteuerdaten().add(steuerdatenUpdateDto2);
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().setFamiliensituation(new FamiliensituationUpdateDto());
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getFamiliensituation().setElternVerheiratetZusammen(false);
        GesuchTranche tranche = initTrancheFromGesuchUpdate(gesuchUpdateDto);

        //set null values
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getSteuerdaten().get(0).setSteuerjahr(null);
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getSteuerdaten().get(0).setVeranlagungscode(null);
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getSteuerdaten().get(1).setSteuerjahr(null);
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getSteuerdaten().get(1).setVeranlagungscode(null);

        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        when(gesuchRepository.findGesucheBySvNummer(any())).thenReturn(Stream.of(tranche.getGesuch()));
        when(gesuchRepository.findByIdOptional(any())).thenReturn(Optional.ofNullable(tranche.getGesuch()));
        gesuchService.updateGesuch(gesuchDto.getId(), gesuchUpdateDto, TENANT_ID);

        final var steuerdatenTab = tranche.getGesuchFormular().getSteuerdaten().iterator().next();
        assertThat(steuerdatenTab.getVeranlagungsCode(), Matchers.equalTo(0));
        assertThat(
            steuerdatenTab.getSteuerjahr(),
            Matchers.equalTo(tranche.getGesuch().getGesuchsperiode().getGesuchsjahr().getTechnischesJahr() - 1)
        );
    }

    @Test
    @TestAsGesuchsteller
    void gesuchUpdateEinnahmenkostenDoNotSetSteuerdatenTest() {
        GesuchCreateDtoSpec gesuchCreateDtoSpec = initGesuchCreateDto();
        var gesuchDto = gesuchService.createGesuch(new GesuchCreateDto(gesuchCreateDtoSpec.getFallId(), gesuchCreateDtoSpec.getGesuchsperiodeId()));

        GesuchUpdateDto gesuchUpdateDto = createGesuch();
        GesuchTranche tranche = initTrancheFromGesuchUpdate(gesuchUpdateDto);

        tranche.getGesuchFormular().getEinnahmenKosten().setSteuerjahr(null);
        tranche.getGesuchFormular().getEinnahmenKosten().setVeranlagungsCode(null);

        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setVeranlagungsCode(5);
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setSteuerjahr(1990);

        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        when(gesuchRepository.findGesucheBySvNummer(any())).thenReturn(Stream.of(tranche.getGesuch()));
        when(gesuchRepository.findByIdOptional(any())).thenReturn(Optional.ofNullable(tranche.getGesuch()));

        gesuchService.updateGesuch(gesuchDto.getId(), gesuchUpdateDto, TENANT_ID);
        assertThat(
            gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().getSteuerjahr(),
            Matchers.equalTo(tranche.getGesuch().getGesuchsperiode().getGesuchsjahr().getTechnischesJahr() - 1)
        );
        assertThat(
            gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().getVeranlagungsCode(),
            Matchers.equalTo(0)
        );
    }

    @Test
    void pageValidation() {
        final var gesuch = new Gesuch();
        final var gesuchTranche = new GesuchTranche();
        final var gesuchFormular = new GesuchFormular();
        gesuchTranche.setGesuch(gesuch);
        gesuchFormular.setTranche(gesuchTranche);
        var reportDto = gesuchService.validatePages(gesuchFormular, gesuch.getId());
        assertThat(reportDto.getValidationErrors(), Matchers.is(empty()));

        gesuchFormular.setEinnahmenKosten(new EinnahmenKosten());
        reportDto = gesuchService.validatePages(gesuchFormular, gesuch.getId());
        var violationCount = reportDto.getValidationErrors().size();
        assertThat(reportDto.getValidationErrors(), Matchers.is(not(empty())));

        gesuchFormular.setFamiliensituation(
            new Familiensituation()
                .setElternteilUnbekanntVerstorben(true)
                .setMutterUnbekanntVerstorben(ElternAbwesenheitsGrund.VERSTORBEN)
        );
        reportDto = gesuchService.validatePages(gesuchFormular, gesuch.getId());
        assertThat(reportDto.getValidationErrors().size(), Matchers.is(greaterThan(violationCount)));
    }

    @TestAsSachbearbeiter
    @Test
    void findAlleGesucheSBShouldIgnoreGesucheWithoutPIA(){
        setupGesucheWithAndWithoutPia();
        var alleGesuche = gesuchService.findGesucheSB(GetGesucheSBQueryType.ALLE);
        assertThat(alleGesuche.stream().filter(gesuch -> gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung() == null).count(), Matchers.is(0L));
    }


    private GesuchTranche initTrancheFromGesuchUpdate(GesuchUpdateDto gesuchUpdateDto) {
        GesuchTranche tranche = prepareGesuchTrancheWithIds(gesuchUpdateDto.getGesuchTrancheToWorkWith());
        return gesuchTrancheMapper.partialUpdate(gesuchUpdateDto.getGesuchTrancheToWorkWith(), tranche);
    }

    private GesuchTranche updateGesetzlicheAlimenteRegel(
        @Nullable Boolean from,
        Boolean to,
        GesuchUpdateDto gesuchUpdateDto) {
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getFamiliensituation()
            .setGerichtlicheAlimentenregelung(from);
        GesuchTranche tranche = prepareGesuchTrancheWithIds(gesuchUpdateDto.getGesuchTrancheToWorkWith());
        tranche.getGesuchFormular().setFamiliensituation(new Familiensituation());
        tranche.getGesuchFormular().getFamiliensituation().setGerichtlicheAlimentenregelung(from);
        gesuchTrancheMapper.partialUpdate(gesuchUpdateDto.getGesuchTrancheToWorkWith(), tranche);
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
        gesuchService.updateGesuch(any(), gesuchUpdateDto, TENANT_ID);
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
        ElternAbwesenheitsGrund grundVater) {
        GesuchTranche tranche = prepareGesuchTrancheWithIds(gesuchUpdateDto.getGesuchTrancheToWorkWith());
        gesuchTrancheMapper.partialUpdate(gesuchUpdateDto.getGesuchTrancheToWorkWith(), tranche);

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
        gesuchService.updateGesuch(any(), gesuchUpdateDto, TENANT_ID);
        return tranche;
    }

    private void setupGesucheWithAndWithoutPia(){
        Gesuch gesuchWithoutPia = GesuchGenerator.initGesuch();
        gesuchWithoutPia.getNewestGesuchTranche().get().setGesuchFormular(new GesuchFormular());
        gesuchWithoutPia.getNewestGesuchTranche().get().getGesuchFormular().setPersonInAusbildung(null);

        Gesuch gesuchWithPia = GesuchGenerator.initGesuch();
        gesuchWithPia.getNewestGesuchTranche().get().setGesuchFormular(new GesuchFormular());
        gesuchWithPia.getNewestGesuchTranche().get().getGesuchFormular().setPersonInAusbildung(new PersonInAusbildung());
        when(gesuchRepository.findAlle()).thenReturn(Stream.of(gesuchWithoutPia, gesuchWithPia));
    }

    private GesuchTranche updateWerZahltAlimente(
        GesuchUpdateDto gesuchUpdateDto,
        Elternschaftsteilung from,
        Elternschaftsteilung to) {
        GesuchTranche tranche = prepareGesuchTrancheWithIds(gesuchUpdateDto.getGesuchTrancheToWorkWith());
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getFamiliensituation()
            .setGerichtlicheAlimentenregelung(true);
        gesuchUpdateDto.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getFamiliensituation()
            .setWerZahltAlimente(from);
        gesuchTrancheMapper.partialUpdate(gesuchUpdateDto.getGesuchTrancheToWorkWith(), tranche);

        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getFamiliensituation().setWerZahltAlimente(to);
        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        gesuchService.updateGesuch(any(), gesuchUpdateDto, TENANT_ID);
        return tranche;
    }

    private GesuchTranche updateFromZivilstandToZivilstand(
        GesuchUpdateDto gesuchUpdateDto,
        Zivilstand from,
        Zivilstand to) {
        GesuchTranche tranche = prepareGesuchTrancheWithIds(gesuchUpdateDto.getGesuchTrancheToWorkWith());
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().setZivilstand(from);
        gesuchTrancheMapper.partialUpdate(gesuchUpdateDto.getGesuchTrancheToWorkWith(), tranche);
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().setZivilstand(to);
        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        gesuchService.updateGesuch(any(), gesuchUpdateDto, TENANT_ID);
        return tranche;
    }

    private GesuchTranche prepareGesuchTrancheWithIds(GesuchTrancheUpdateDto trancheUpdate) {
        GesuchTranche tranche = initGesuchTranche();
        GesuchFormular gesuchFormular = new GesuchFormular();

        trancheUpdate.getGesuchFormular().getElterns().forEach(elternUpdateDto -> {
            elternUpdateDto.setId(UUID.randomUUID());
            gesuchFormular.getElterns().add(elternMapper.partialUpdate(elternUpdateDto, new Eltern()));
        });

        trancheUpdate.getGesuchFormular().getLebenslaufItems().forEach(item -> {
            item.setId(UUID.randomUUID());
            gesuchFormular.getLebenslaufItems().add(lebenslaufItemMapper.partialUpdate(item, new LebenslaufItem()));
        });

        if(trancheUpdate.getGesuchFormular().getSteuerdaten() != null){
            trancheUpdate.getGesuchFormular().getSteuerdaten().forEach(item ->{
                item.setId(UUID.randomUUID());
                gesuchFormular.getSteuerdaten().add(steuerdatenMapper.partialUpdate(item, new Steuerdaten()));
            });
        }
        gesuchFormular.setFamiliensituation(new Familiensituation());

        return tranche.setGesuchFormular(gesuchFormular);
    }
}
