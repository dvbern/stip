package ch.dvbern.stip.api.steuerdaten.entity;

import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.generator.entities.GesuchGenerator;
import ch.dvbern.stip.api.gesuch.entity.EinnahmenKostenSteuerjahrInPastOrCurrentConstraintValidator;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.LinkedHashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SteuerdatenSteuerjahrInPastOrCurrentConstraintValidatorTest {

    @Test
    void steuerjahrIsCurrentorPastValidationTest(){
        GesuchTranche tranche = GesuchGenerator.initGesuchTranche();
        tranche.setGesuchFormular(new GesuchFormular());
        GesuchFormular gesuchFormular = tranche.getGesuchFormular();
        gesuchFormular.setTranche(tranche);
        gesuchFormular.setSteuerdaten(new LinkedHashSet<>());
        Steuerdaten steuerdaten = new Steuerdaten();
        steuerdaten.setSteuerjahr(tranche.getGesuch().getGesuchsperiode().getGesuchsjahr().getTechnischesJahr() + 1);
        gesuchFormular.getSteuerdaten().add(steuerdaten);


        final var temporalValidator = new SteuerdatenSteuerjahrInPastOrCurrentConstraintValidator();
        assertThat(temporalValidator.isValid(gesuchFormular, null)).isFalse();

        steuerdaten.setSteuerjahr(tranche.getGesuch().getGesuchsperiode().getGesuchsjahr().getTechnischesJahr() -1 );
        assertThat(temporalValidator.isValid(gesuchFormular, null)).isTrue();

    }
}
