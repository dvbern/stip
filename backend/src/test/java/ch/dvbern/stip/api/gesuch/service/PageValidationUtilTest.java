package ch.dvbern.stip.api.gesuch.service;

import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PageValidationUtilTest {
    @Test
    void getGroupsFromGesuchFormular() {
        final var gesuchFormular = new GesuchFormular();

        var groups = PageValidationUtil.getGroupsFromGesuchFormular(gesuchFormular);
        assertThat(groups, is(empty()));

        gesuchFormular.setEinnahmenKosten(new EinnahmenKosten());

        groups = PageValidationUtil.getGroupsFromGesuchFormular(gesuchFormular);
        assertThat(groups.size(), is(1));
    }
}
