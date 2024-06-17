package ch.dvbern.stip.api.plz.service;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@QuarkusTest
class PlzServiceTest {
    @Inject PlzService plzService;
    @Test
    void isPLZInCantonBernTest(){

        final String plzInBern1="3011";
        final String plzInBern2="1657";

        final String plzNotInBern="8032";
        final String plzOutsideSwitzerland="200000";
        final String invalidPlz="test20";

        final String exInBern="2830";//Vellerat(JU),since1996

        List<String> plzsInBE = plzService.getAllPlzByKantonsKuerzel("be").stream().map(plzDto -> plzDto.getPlz()).collect(Collectors.toUnmodifiableList());
        assertNotNull(plzsInBE);
        assertThat(plzsInBE.contains(plzInBern1)).isTrue();
        assertThat(plzsInBE.contains(plzInBern2)).isTrue();

        assertThat(plzsInBE.contains(plzOutsideSwitzerland)).isFalse();
        assertThat(plzsInBE.contains(plzNotInBern)).isFalse();
        assertThat(plzsInBE.contains(exInBern)).isFalse();
        assertThat(plzsInBE.contains(invalidPlz)).isFalse();
        assertThat(plzsInBE.contains("")).isFalse();
    }

}
