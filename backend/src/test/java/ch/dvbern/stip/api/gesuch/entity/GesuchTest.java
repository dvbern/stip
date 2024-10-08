package ch.dvbern.stip.api.gesuch.entity;

import java.time.LocalDate;
import java.util.UUID;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static ch.dvbern.stip.api.generator.entities.GesuchGenerator.initGesuch;

class GesuchTest {

    @Test
    void getTrancheByIdNotPresent() {
        Gesuch gesuch = initGesuch();
        MatcherAssert.assertThat(gesuch.getGesuchTrancheById(UUID.randomUUID()).isPresent(), Matchers.is(false));
    }

    @Test
    void getTrancheByIdPresent() {
        UUID testId = UUID.randomUUID();
        Gesuch gesuch = initGesuch();
        gesuch.getGesuchTranchen().get(0).setId(testId);

        MatcherAssert.assertThat(gesuch.getGesuchTrancheById(testId).isPresent(), Matchers.is(true));
    }

    @Test
    void getTrancheByIdPresentMultipleTranchen() {
        UUID testId = UUID.randomUUID();
        Gesuch gesuch = initGesuch();
        gesuch.getGesuchTranchen().add((GesuchTranche) new GesuchTranche().setId(testId));
        MatcherAssert.assertThat(gesuch.getGesuchTrancheById(testId).isPresent(), Matchers.is(true));
    }

    @Test
    void getTrancheByDateNotPresent() {
        var gesuch = initGesuch();
        LocalDate ausserhalbPeriode = gesuch.getGesuchsperiode().getGesuchsperiodeStopp().plusDays(1);
        MatcherAssert.assertThat(gesuch.getAllTranchenValidOnDate(ausserhalbPeriode).isPresent(), Matchers.is(false));
    }

    @Test
    void getTrancheByDatePresent() {
        Gesuch gesuch = initGesuch();
        LocalDate innerhalbPeriode = gesuch.getGesuchsperiode().getGesuchsperiodeStopp();
        MatcherAssert.assertThat(gesuch.getAllTranchenValidOnDate(innerhalbPeriode).isPresent(), Matchers.is(true));
    }
}
