package ch.dvbern.stip.api.bildungskategorie.entity;

import ch.dvbern.stip.api.bildungskategorie.type.Bildungsstufe;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class BildungskategorieTest {
    @Test
    void testBildungsstufeSekundaer() {
        //Arrange
        Bildungskategorie bk = new Bildungskategorie();

        //Assert
        bk.setBfs(6);
        MatcherAssert.assertThat(bk.getBildungsstufe(), Matchers.is(Bildungsstufe.SEKUNDAR_2));
    }

    @Test
    void testBildungsstufeTertiaer() {
        //Arrange
        Bildungskategorie bk = new Bildungskategorie();

        //Assert
        bk.setBfs(7);
        MatcherAssert.assertThat(bk.getBildungsstufe(), Matchers.is(Bildungsstufe.TERTIAER));
    }


}
