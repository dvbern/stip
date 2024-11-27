/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.stipdecision.service;

import java.time.LocalDate;
import java.util.UUID;

import ch.dvbern.stip.api.common.type.StipDecision;
import ch.dvbern.stip.api.gesuch.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.lebenslauf.type.LebenslaufAusbildungsArt;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.stipdecision.decider.BernStipDecider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
@Slf4j
class BernStipDeciderTest {
    private final BernStipDecider decider = new BernStipDecider();

    @Test
    void testGetValidDecision() {
        // var decider = new BernStipDecider();
        final var gesuch = TestUtil.getGesuchForDecision(UUID.randomUUID());
        var decision = decider.decide(gesuch.getNewestGesuchTranche().get());
        assertThat(decision).isEqualTo(StipDecision.GESUCH_VALID);

        var event = decider.getGesuchStatusChangeEvent(decision);
        assertThat(event).isEqualTo(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG);

        var text = decider.getTextForDecision(decision, Sprache.DEUTSCH);
        assertThat(text).isEmpty();
    }

    @Test
    void testGetDecisionAbgelaufen() {
        final var gesuch = TestUtil.getGesuchForDecision(UUID.randomUUID());
        gesuch.getGesuchsperiode().setEinreichefristReduziert(LocalDate.now().minusDays(1));
        var decision = decider.decide(gesuch.getNewestGesuchTranche().get());
        assertThat(decision).isEqualTo(StipDecision.EINGABEFRIST_ABGELAUFEN);

        var event = decider.getGesuchStatusChangeEvent(decision);
        assertThat(event).isEqualTo(GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT);
    }

    @Test
    void testGetDecisionAusbildungNichtAnerkannt() {
        final var gesuch = TestUtil.getGesuchForDecision(UUID.randomUUID());
        gesuch.getAusbildung().setAusbildungNichtGefunden(true);
        var decision = decider.decide(gesuch.getNewestGesuchTranche().get());
        assertThat(decision).isEqualTo(StipDecision.AUSBILDUNG_NICHT_ANERKANNT);

        var event = decider.getGesuchStatusChangeEvent(decision);
        assertThat(event).isEqualTo(GesuchStatusChangeEvent.ABKLAERUNG_DURCH_RECHSTABTEILUNG);
    }

    @Test
    void testGetDecisionAusbildungImLebenslauf() {
        final var gesuch = TestUtil.getGesuchForDecision(UUID.randomUUID());
        gesuch.getNewestGesuchTranche()
            .get()
            .getGesuchFormular()
            .getLebenslaufItems()
            .add(
                new LebenslaufItem().setBildungsart(LebenslaufAusbildungsArt.BACHELOR_FACHHOCHSCHULE)
            );
        var decision = decider.decide(gesuch.getNewestGesuchTranche().get());
        assertThat(decision).isEqualTo(StipDecision.AUSBILDUNG_IM_LEBENSLAUF);

        var event = decider.getGesuchStatusChangeEvent(decision);
        assertThat(event).isEqualTo(GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN);
    }

    @Test
    void testGetDecisionAusbildungenLaenger12Jahre() {
        final var gesuch = TestUtil.getGesuchForDecision(UUID.randomUUID());
        gesuch.getAusbildung().setAusbildungBegin(LocalDate.now().minusYears(13));

        var decision = decider.decide(gesuch.getNewestGesuchTranche().get());
        assertThat(decision).isEqualTo(StipDecision.AUSBILDUNGEN_LAENGER_12_JAHRE);

        var event = decider.getGesuchStatusChangeEvent(decision);
        assertThat(event).isEqualTo(GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN);
    }

    @Test
    void testGetDecisionPiaAelter35Jahre() {
        final var gesuch = TestUtil.getGesuchForDecision(UUID.randomUUID());
        gesuch.getNewestGesuchTranche()
            .get()
            .getGesuchFormular()
            .getPersonInAusbildung()
            .setGeburtsdatum(LocalDate.now().minusYears(36));

        var decision = decider.decide(gesuch.getNewestGesuchTranche().get());
        assertThat(decision).isEqualTo(StipDecision.PIA_AELTER_35_JAHRE);

        var event = decider.getGesuchStatusChangeEvent(decision);
        assertThat(event).isEqualTo(GesuchStatusChangeEvent.JURISTISCHE_ABKLAERUNG);
    }

}
