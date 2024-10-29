package ch.dvbern.stip.api.gesuch.service;

import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.bildungskategorie.entity.Bildungskategorie;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.service.ElternMapper;
import ch.dvbern.stip.api.generator.entities.GesuchGenerator;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.lebenslauf.service.LebenslaufItemMapper;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.generated.dto.GesuchTrancheUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static ch.dvbern.stip.api.generator.entities.GesuchGenerator.initGesuchTranche;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Slf4j
@QuarkusTest
@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
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
            .setAusbildungsgang(new Ausbildungsgang().setBildungskategorie(new Bildungskategorie()));

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
            .setAusbildungsgang(new Ausbildungsgang().setBildungskategorie(new Bildungskategorie()));

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
            .setAusbildungsgang(new Ausbildungsgang().setBildungskategorie(new Bildungskategorie()));
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
            .setAusbildungsgang(new Ausbildungsgang().setBildungskategorie(new Bildungskategorie()));
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
        gesuchFormular.setTranche(tranche);

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
