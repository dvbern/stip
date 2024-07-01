package ch.dvbern.stip.api.gesuch.service;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
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
import ch.dvbern.stip.api.generator.api.GesuchTestSpecGenerator;
import ch.dvbern.stip.api.generator.entities.GesuchGenerator;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.lebenslauf.service.LebenslaufItemMapper;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.generated.dto.*;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import static ch.dvbern.stip.api.generator.entities.GesuchGenerator.createGesuch;
import static ch.dvbern.stip.api.generator.entities.GesuchGenerator.initGesuchTranche;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.*;
import static ch.dvbern.stip.api.util.TestUtil.initGesuchCreateDto;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@QuarkusTest
@QuarkusTestResource(TestDatabaseEnvironment.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GesuchUpdateDefaultValuesTest {

    @Inject
    GesuchService gesuchService;

    @Inject
    GesuchTrancheMapper gesuchTrancheMapper;

    @Inject
    ElternMapper elternMapper;

    @Inject
    LebenslaufItemMapper lebenslaufItemMapper;

    @Test
    @TestAsSachbearbeiter
    void testUpdateEinnahmeKostenVeranlagungscodeAsSB(){
        var gesuchUpdateDTO = GesuchGenerator.createFullGesuch();
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setVeranlagungsCode(99);
        var einnahmeKostenUpdateDto = gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten();

        GesuchTranche tranche = initTrancheFromGesuchUpdate(GesuchGenerator.createFullGesuch());
        tranche.getGesuchFormular()
            .getAusbildung()
            .setAusbildungsgang(new Ausbildungsgang().setBildungsart(new Bildungsart()));

        gesuchService.setAndValidateEinnahmenkostenUpdateLegality(einnahmeKostenUpdateDto,tranche);
        assertThat(einnahmeKostenUpdateDto.getVeranlagungsCode(), is(99));

        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setVeranlagungsCode(null);

        gesuchService.setAndValidateEinnahmenkostenUpdateLegality(einnahmeKostenUpdateDto,tranche);
        assertThat(einnahmeKostenUpdateDto.getVeranlagungsCode(), is(0));
    }

    @Test
    @TestAsGesuchsteller
    void testUpdateEinnahmeKostenVeranlagungscodeAsGS(){
        var gesuchUpdateDTO = GesuchGenerator.createFullGesuch();
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setVeranlagungsCode(99);
        var einnahmeKostenUpdateDto = gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten();

        GesuchTranche tranche = initTrancheFromGesuchUpdate(GesuchGenerator.createFullGesuch());
        tranche.getGesuchFormular()
            .getAusbildung()
            .setAusbildungsgang(new Ausbildungsgang().setBildungsart(new Bildungsart()));

        gesuchService.setAndValidateEinnahmenkostenUpdateLegality(einnahmeKostenUpdateDto,tranche);
        assertThat(einnahmeKostenUpdateDto.getVeranlagungsCode(), is(0));

        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setVeranlagungsCode(null);

        gesuchService.setAndValidateEinnahmenkostenUpdateLegality(einnahmeKostenUpdateDto,tranche);
        assertThat(einnahmeKostenUpdateDto.getVeranlagungsCode(), is(0));
    }

    @Test
    @TestAsSachbearbeiter
    void testUpdateGesuchEinnahmenKostenSteuerjahrAsSB(){
        var gesuchUpdateDTO = GesuchGenerator.createFullGesuch();
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setSteuerjahr(null);
        var einnahmeKostenUpdateDto = gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten();

        GesuchTranche tranche = initTrancheFromGesuchUpdate(GesuchGenerator.createFullGesuch());
        tranche.getGesuchFormular()
            .getAusbildung()
            .setAusbildungsgang(new Ausbildungsgang().setBildungsart(new Bildungsart()));
        tranche.getGesuchFormular().getEinnahmenKosten().setSteuerjahr(null);

        gesuchService.setAndValidateEinnahmenkostenUpdateLegality(einnahmeKostenUpdateDto,tranche);
        assertThat(einnahmeKostenUpdateDto.getSteuerjahr(), is(tranche.getGesuch().getGesuchsperiode().getGesuchsjahr().getTechnischesJahr() - 1));

        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setSteuerjahr(2023);

        gesuchService.setAndValidateEinnahmenkostenUpdateLegality(einnahmeKostenUpdateDto,tranche);
        assertThat(einnahmeKostenUpdateDto.getSteuerjahr(), is(2023));
    }

    @Test
    @TestAsGesuchsteller
    void testUpdateGesuchEinnahmenKostenSteuerjahrAsGS(){
        var gesuchUpdateDTO = GesuchGenerator.createFullGesuch();
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setSteuerjahr(null);
        var einnahmeKostenUpdateDto = gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten();

        GesuchTranche tranche = initTrancheFromGesuchUpdate(GesuchGenerator.createFullGesuch());
        tranche.getGesuchFormular()
            .getAusbildung()
            .setAusbildungsgang(new Ausbildungsgang().setBildungsart(new Bildungsart()));
        tranche.getGesuchFormular().getEinnahmenKosten().setSteuerjahr(null);


        gesuchService.setAndValidateEinnahmenkostenUpdateLegality(einnahmeKostenUpdateDto,tranche);
        assertThat(einnahmeKostenUpdateDto.getSteuerjahr(), is(tranche.getGesuch().getGesuchsperiode().getGesuchsjahr().getTechnischesJahr() - 1));

        // update attempt should be ignored, same default value should remain
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setSteuerjahr(2023);
        gesuchService.setAndValidateEinnahmenkostenUpdateLegality(einnahmeKostenUpdateDto,tranche);
        assertThat(einnahmeKostenUpdateDto.getSteuerjahr(), is(tranche.getGesuch().getGesuchsperiode().getGesuchsjahr().getTechnischesJahr() - 1));
    }


    private GesuchTranche initTrancheFromGesuchUpdate(GesuchUpdateDto gesuchUpdateDto) {
        GesuchTranche tranche = prepareGesuchTrancheWithIds(gesuchUpdateDto.getGesuchTrancheToWorkWith());
        return gesuchTrancheMapper.partialUpdate(gesuchUpdateDto.getGesuchTrancheToWorkWith(), tranche);
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
