package ch.dvbern.stip.api.common.service;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.plz.service.PlzOrtService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@QuarkusTest
class PlzOrtServiceTest {
    @Inject
    PlzOrtService plzOrtService;
    @Test
    void isPLZInCantonBernTest(){

        final String plzInBern1="3011";
        final String plzInBern2="1657";

        final String plzNotInBern="8032";
        final String plzOutsideSwitzerland="200000";
        final String invalidPlz="test20";

        final String exInBern="2830";//Vellerat(JU),since1996

        assertThat(plzOrtService.isInBern(new Adresse().setPlz(plzInBern1))).isTrue();
        assertThat(plzOrtService.isInBern(new Adresse().setPlz(plzInBern2))).isTrue();
        assertThat(plzOrtService.isInBern(new Adresse().setPlz(exInBern))).isFalse();
        assertThat(plzOrtService.isInBern(new Adresse().setPlz(plzNotInBern))).isFalse();

        assertThat(plzOrtService.isInBern(new Adresse().setPlz(plzOutsideSwitzerland))).isFalse();
        assertThat(plzOrtService.isInBern(new Adresse().setPlz(invalidPlz))).isFalse();
        assertThat(plzOrtService.isInBern(new Adresse().setPlz(""))).isFalse();
        assertThat(plzOrtService.isInBern(new Adresse().setPlz(null))).isFalse();
    }
}
