package ch.dvbern.stip.api.gesuch.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.bildungsart.entity.Bildungsart;
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
import ch.dvbern.stip.api.generator.entities.GesuchGenerator;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuch.util.GesuchFormularCalculationUtil;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.lebenslauf.service.LebenslaufItemMapper;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.generated.dto.FamiliensituationUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import ch.dvbern.stip.generated.dto.ValidationReportDto;
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

import static ch.dvbern.stip.api.generator.entities.GesuchGenerator.initGesuchTranche;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.AUFGELOESTE_PARTNERSCHAFT;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.EINGETRAGENE_PARTNERSCHAFT;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.GESCHIEDEN_GERICHTLICH;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.KONKUBINAT;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.LEDIG;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.VERHEIRATET;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.VERWITWET;
import static com.ibm.icu.impl.Assert.fail;
import static io.smallrye.common.constraint.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNotNull;
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

    @InjectMock
    GesuchRepository gesuchRepository;

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
        tranche.getGesuch().setGesuchStatus(Gesuchstatus.KOMPLETT_EINGEREICHT);

        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        when(gesuchRepository.findGesucheBySvNummer(any())).thenReturn(Stream.of((Gesuch)
            new Gesuch()
                .setGesuchStatus(Gesuchstatus.KOMPLETT_EINGEREICHT)
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
        final var gesuchUpdateDto = GesuchGenerator.createFullGesuch();
        final var famsit = new FamiliensituationUpdateDto();
        famsit.setElternVerheiratetZusammen(false);
        famsit.setGerichtlicheAlimentenregelung(false);
        famsit.setElternteilUnbekanntVerstorben(true);
        famsit.setMutterUnbekanntVerstorben(ElternAbwesenheitsGrund.VERSTORBEN);
        famsit.setVaterUnbekanntVerstorben(ElternAbwesenheitsGrund.VERSTORBEN);
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().setElterns(new ArrayList<>());
        gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().setFamiliensituation(famsit);

        GesuchTranche tranche = initTrancheFromGesuchUpdate(GesuchGenerator.createFullGesuch());
        tranche.getGesuchFormular()
            .getAusbildung()
            .setAusbildungsgang(new Ausbildungsgang().setBildungsart(new Bildungsart()));

        tranche.getGesuchFormular().setTranche(tranche);
        tranche.getGesuch().setGesuchDokuments(
            Arrays.stream(DokumentTyp.values())
                .map(x -> new GesuchDokument().setDokumentTyp(x).setGesuch(tranche.getGesuch()))
                .toList()
        );

        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        when(gesuchRepository.findGesucheBySvNummer(any())).thenReturn(Stream.of(tranche.getGesuch()));

        ValidationReportDto reportDto = gesuchService.validateGesuchEinreichen(tranche.getGesuch().getId());

        assertThat(
            reportDto.toString() + "\nEltern: " + gesuchUpdateDto.getGesuchTrancheToWorkWith()
                .getGesuchFormular()
                .getElterns()
                .size(),
            reportDto.getValidationErrors().size(),
            Matchers.is(0)
        );
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
        tranche.getGesuch().setGesuchDokuments(
            Arrays.stream(DokumentTyp.values())
                .map(x -> new GesuchDokument().setDokumentTyp(x).setGesuch(tranche.getGesuch()))
                .toList()
        );

        gesuchService.gesuchEinreichen(tranche.getGesuch().getId());

        assertThat(
            tranche.getGesuch().getGesuchStatus(),
            Matchers.is(Gesuchstatus.BEREIT_FUER_BEARBEITUNG)
        );
    }

    @Test
    @TestAsGesuchsteller
    void gesuchEinreichenSetCorrectVermoegenTest() {
        GesuchTranche tranche = initTrancheFromGesuchUpdate(GesuchGenerator.createFullGesuch());
        assertTrue(tranche.getGesuch().getGesuchsperiode().getGesuchsjahr().getTechnischesJahr() != null );

        //GS is older or equal than 18 years old
        tranche.getGesuchFormular().getPersonInAusbildung().setGeburtsdatum(LocalDate.now().minusYears(20));
        tranche.getGesuchFormular().setTranche(tranche);
        assertTrue(GesuchFormularCalculationUtil.wasGSOlderThan18(tranche.getGesuchFormular()));


        tranche.getGesuchFormular()
            .getAusbildung()
            .setAusbildungsgang(new Ausbildungsgang().setBildungsart(new Bildungsart()));

        //GS is younger than 18 years
        tranche.getGesuchFormular().getPersonInAusbildung().setGeburtsdatum(LocalDate.now().minusYears(1));

        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        when(gesuchRepository.findGesucheBySvNummer(any())).thenReturn(Stream.of(tranche.getGesuch()));

        tranche.getGesuchFormular().setTranche(tranche);
        tranche.getGesuch().setGesuchDokuments(
            Arrays.stream(DokumentTyp.values())
                .map(x -> new GesuchDokument().setDokumentTyp(x).setGesuch(tranche.getGesuch()))
                .toList()
        );



        gesuchService.gesuchEinreichen(tranche.getGesuch().getId());

        assertThat(
            tranche.getGesuch().getGesuchStatus(),
            Matchers.is(Gesuchstatus.BEREIT_FUER_BEARBEITUNG)
        );

        Gesuch eingereicht = gesuchRepository.findById(tranche.getGesuch().getId());
        assertThat(eingereicht.getGesuchTrancheById(tranche.getId()).get().getGesuchFormular().getEinnahmenKosten().getVermoegen(), is(null));

    }

    @Test
    @TestAsGesuchsteller
    void gesuchUpdateSetCorrectVermoegenTest() {
        GesuchUpdateDto gesuchUpdateDto = GesuchGenerator.createGesuch();
        assertThat(
            gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getElterns().size(),
            not(0));
        GesuchTranche tranche =
            updateWerZahltAlimente(gesuchUpdateDto, Elternschaftsteilung.MUTTER, Elternschaftsteilung.GEMEINSAM);
        assertThat(tranche.getGesuchFormular().getElterns().size(), Matchers.is(0));
        fail("not implemented");
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

        return tranche.setGesuchFormular(gesuchFormular);
    }
}
