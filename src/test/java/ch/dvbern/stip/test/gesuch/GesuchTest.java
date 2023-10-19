package ch.dvbern.stip.test.gesuch;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static ch.dvbern.stip.test.generator.entities.GesuchGenerator.initGesuch;

public class GesuchTest {

    @Test
    public void getTrancheByIdNotPresent() {
        Gesuch gesuch = initGesuch();
        MatcherAssert.assertThat(gesuch.getGesuchTrancheById(UUID.randomUUID()).isPresent(), Matchers.is(false));
    }

    @Test
    public void getTrancheByIdPresent() {
        UUID testId = UUID.randomUUID();
        Gesuch gesuch = initGesuch();
        gesuch.getGesuchTranchen().get(0).setId(testId);

        MatcherAssert.assertThat(gesuch.getGesuchTrancheById(testId).isPresent(), Matchers.is(true));
    }

    @Test
    public void getTrancheByIdPresentMultipleTranchen() {
        UUID testId = UUID.randomUUID();
        Gesuch gesuch = initGesuch();
        gesuch.getGesuchTranchen().add((GesuchTranche) new GesuchTranche().setId(testId));
        MatcherAssert.assertThat(gesuch.getGesuchTrancheById(testId).isPresent(), Matchers.is(true));
    }

    @Test
    public void getTrancheByDateNotPresent() {
        Gesuch gesuch = initGesuch();
        LocalDate ausserhalbPeriode = gesuch.getGesuchsperiode().getGueltigkeit().getGueltigBis().plusDays(1);
        MatcherAssert.assertThat(gesuch.getGesuchTrancheValidOnDate(ausserhalbPeriode).isPresent(), Matchers.is(false));
    }

    @Test
    public void getTrancheByDatePresent() {
        Gesuch gesuch = initGesuch();
        LocalDate innerhalbPeriode = gesuch.getGesuchsperiode().getGueltigkeit().getGueltigBis();
        MatcherAssert.assertThat(gesuch.getGesuchTrancheValidOnDate(innerhalbPeriode).isPresent(), Matchers.is(true));
    }
}
