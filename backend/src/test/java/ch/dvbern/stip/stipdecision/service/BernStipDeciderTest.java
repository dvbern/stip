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
import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.generator.entities.service.LandGenerator;
import ch.dvbern.stip.api.gesuchstatus.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.lebenslauf.type.LebenslaufAusbildungsArt;
import ch.dvbern.stip.api.personinausbildung.entity.ZustaendigerKanton;
import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import ch.dvbern.stip.api.plz.service.PlzService;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.stipdecision.decider.BernStipDecider;
import ch.dvbern.stip.stipdecision.type.StipDeciderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
@Slf4j
class BernStipDeciderTest {
    private PlzService plzService;
    private BernStipDecider decider;

    @BeforeEach
    void setUp() {
        plzService = Mockito.mock(PlzService.class);
        Mockito.when(plzService.isInBern(ArgumentMatchers.any(Adresse.class))).thenReturn(true);
        decider = new BernStipDecider(plzService);
    }

    @Test
    void testGetValidDecision() {
        final var gesuch = TestUtil.getGesuchForDecision(UUID.randomUUID());
        var decision = decider.decide(gesuch.getNewestGesuchTranche().get());
        assertThat(decision).isEqualTo(StipDeciderResult.GESUCH_VALID);

        var event = decider.getGesuchStatusChangeEvent(decision);
        assertThat(event).isEqualTo(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG);
    }

    @Test
    void testGetDecisionAbgelaufen() {
        final var gesuch = TestUtil.getGesuchForDecision(UUID.randomUUID());
        gesuch.getGesuchsperiode().setEinreichefristReduziert(LocalDate.now().minusDays(1));
        var decision = decider.decide(gesuch.getNewestGesuchTranche().get());
        assertThat(decision).isEqualTo(StipDeciderResult.NEGATIVVERFUEGUNG_NICHTEINTRETENSVERFUEGUNG);

        var event = decider.getGesuchStatusChangeEvent(decision);
        assertThat(event).isEqualTo(GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT);
    }

    @Test
    void testGetDecisionAusbildungNichtAnerkannt() {
        final var gesuch = TestUtil.getGesuchForDecision(UUID.randomUUID());
        gesuch.getAusbildung().setAusbildungNichtGefunden(true);
        var decision = decider.decide(gesuch.getNewestGesuchTranche().get());
        assertThat(decision).isEqualTo(StipDeciderResult.ANSPRUCH_MANUELL_PRUEFEN_AUSBILDUNG_NICHT_ANERKANNT);

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
        assertThat(decision).isEqualTo(StipDeciderResult.ANSPRUCH_MANUELL_PRUEFEN_ZWEITAUSBILDUNG);

        var event = decider.getGesuchStatusChangeEvent(decision);
        assertThat(event).isEqualTo(GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN);
    }

    @Test
    void testGetDecisionAusbildungenLaenger12Jahre() {
        final var gesuch = TestUtil.getGesuchForDecision(UUID.randomUUID());
        gesuch.getAusbildung().setAusbildungBegin(LocalDate.now().minusYears(13));

        var decision = decider.decide(gesuch.getNewestGesuchTranche().get());
        assertThat(decision).isEqualTo(StipDeciderResult.ANSPRUCH_MANUELL_PRUEFEN_AUSBILDUNGSDAUER);

        var event = decider.getGesuchStatusChangeEvent(decision);
        assertThat(event).isEqualTo(GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN);
    }

    @Test
    void testGetDecisionPiaAelter35JahreBeforeBeginOfAusbildung() {
        final var gesuch = TestUtil.getGesuchForDecision(UUID.randomUUID());
        gesuch.getNewestGesuchTranche()
            .get()
            .getGesuchFormular()
            .getPersonInAusbildung()
            .setGeburtsdatum(gesuch.getAusbildung().getAusbildungBegin().minusDays(1).minusYears(36));

        var decision = decider.decide(gesuch.getNewestGesuchTranche().get());
        assertThat(decision).isEqualTo(StipDeciderResult.ANSPRUCH_MANUELL_PRUEFEN_ALTER_PIA);

        var event = decider.getGesuchStatusChangeEvent(decision);
        assertThat(event).isEqualTo(GesuchStatusChangeEvent.JURISTISCHE_ABKLAERUNG);
    }

    @Test
    void testGetDecisionPiaExact35JahreAtBeginOfAusbildung() {
        final var gesuch = TestUtil.getGesuchForDecision(UUID.randomUUID());
        gesuch.getNewestGesuchTranche()
            .get()
            .getGesuchFormular()
            .getPersonInAusbildung()
            .setGeburtsdatum(gesuch.getAusbildung().getAusbildungBegin().minusYears(35));

        var decision = decider.decide(gesuch.getNewestGesuchTranche().get());
        assertThat(decision).isEqualTo(StipDeciderResult.GESUCH_VALID);

        var event = decider.getGesuchStatusChangeEvent(decision);
        assertThat(event).isEqualTo(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG);
    }

    @Test
    void testStipendienrechtlicherWohnsitzKantonBernCheckerEvaluateFailStep1() {
        final var gesuch = TestUtil.getGesuchForDecision(UUID.randomUUID());
        final var pia = gesuch.getNewestGesuchTranche().get().getGesuchFormular().getPersonInAusbildung();
        pia.setNationalitaet(LandGenerator.initIran())
            .setNiederlassungsstatus(Niederlassungsstatus.AUFENTHALTSBEWILLIGUNG_B)
            .setEinreisedatum(LocalDate.now().minusYears(1));

        var decision = decider.decide(gesuch.getNewestGesuchTranche().get());
        assertThat(decision).isEqualTo(StipDeciderResult.NEGATIVVERFUEGUNG_NICHT_BERECHTIGTE_PERSON);

        var event = decider.getGesuchStatusChangeEvent(decision);
        assertThat(event).isEqualTo(GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT);
    }

    @Test
    void testStipendienrechtlicherWohnsitzKantonBernCheckFluechtlingBern() {
        final var gesuch = TestUtil.getGesuchForDecision(UUID.randomUUID());
        final var pia = gesuch.getNewestGesuchTranche().get().getGesuchFormular().getPersonInAusbildung();
        pia.setNiederlassungsstatus(Niederlassungsstatus.FLUECHTLING)
            .setZustaendigerKanton(ZustaendigerKanton.BERN);
        gesuch.getNewestGesuchTranche().get().getGesuchFormular().setElterns(Set.of());

        var decision = decider.decide(gesuch.getNewestGesuchTranche().get());
        assertThat(decision).isEqualTo(StipDeciderResult.GESUCH_VALID);

        var event = decider.getGesuchStatusChangeEvent(decision);
        assertThat(event).isEqualTo(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG);
    }

    @Test
    void testStipendienrechtlicherWohnsitzKantonBernCheckEuEftaBern() {
        final var gesuch = TestUtil.getGesuchForDecision(UUID.randomUUID());
        final var pia = gesuch.getNewestGesuchTranche().get().getGesuchFormular().getPersonInAusbildung();
        pia.setNationalitaet(LandGenerator.initIran());
        pia.setAdresse(new Adresse().setLand(LandGenerator.initSwitzerland()));
        pia.setNiederlassungsstatus(Niederlassungsstatus.NIEDERLASSUNGSBEWILLIGUNG_C);
        gesuch.getNewestGesuchTranche().get().getGesuchFormular().setElterns(Set.of());

        var decision = decider.decide(gesuch.getNewestGesuchTranche().get());
        assertThat(decision).isEqualTo(StipDeciderResult.GESUCH_VALID);

        var event = decider.getGesuchStatusChangeEvent(decision);
        assertThat(event).isEqualTo(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG);
    }

    @Test
    void testStipendienrechtlicherWohnsitzKantonSchweizerElternlos() {
        final var gesuch = TestUtil.getGesuchForDecision(UUID.randomUUID());
        final var pia = gesuch.getNewestGesuchTranche().get().getGesuchFormular().getPersonInAusbildung();
        pia.setNationalitaet(LandGenerator.initSwitzerland());
        gesuch.getNewestGesuchTranche()
            .get()
            .getGesuchFormular()
            .setElterns(Set.of());

        var decision = decider.decide(gesuch.getNewestGesuchTranche().get());
        assertThat(decision)
            .isEqualTo(StipDeciderResult.ANSPRUCH_MANUELL_PRUEFEN_STIPENDIENRECHTLICHER_WOHNSITZ_HEIMATORT_NICHT_BERN);

        var event = decider.getGesuchStatusChangeEvent(decision);
        assertThat(event).isEqualTo(GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN);
    }

    @Test
    void testStipendienrechtlicherWohnsitzKantonOneElternBern() {
        final var gesuch = TestUtil.getGesuchForDecision(UUID.randomUUID());
        final Adresse adresseBern = new Adresse().setLand(LandGenerator.initSwitzerland());
        final Adresse adresseNotBern = new Adresse().setLand(LandGenerator.initGermany());
        Mockito.when(plzService.isInBern(adresseBern)).thenReturn(true);
        Mockito.when(plzService.isInBern(adresseNotBern)).thenReturn(false);
        final var pia = gesuch.getNewestGesuchTranche().get().getGesuchFormular().getPersonInAusbildung();
        pia.setNationalitaet(LandGenerator.initSwitzerland());
        pia.setAdresse(new Adresse().setLand(LandGenerator.initSwitzerland()));
        gesuch.getNewestGesuchTranche()
            .get()
            .getGesuchFormular()
            .getElterns()
            .stream()
            .toList()
            .get(0)
            .setAdresse(adresseBern);
        gesuch.getNewestGesuchTranche()
            .get()
            .getGesuchFormular()
            .getElterns()
            .stream()
            .toList()
            .get(1)
            .setAdresse(adresseNotBern);

        var decision = decider.decide(gesuch.getNewestGesuchTranche().get());
        assertThat(decision).isEqualTo(
            StipDeciderResult.ANSPRUCH_MANUELL_PRUEFEN_STIPENDIENRECHTLICHER_WOHNSITZ_WOHNSITZ_ELTERN_NICHT_BERN
        );

        var event = decider.getGesuchStatusChangeEvent(decision);
        assertThat(event).isEqualTo(GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN);
    }

    @Test
    void testStipendienrechtlicherWohnsitzKantonNoElternBern() {
        final var gesuch = TestUtil.getGesuchForDecision(UUID.randomUUID());
        final Adresse adresseNotBern1 = new Adresse().setLand(LandGenerator.initSwitzerland());
        final Adresse adresseNotBern2 = new Adresse().setLand(LandGenerator.initGermany());
        Mockito.when(plzService.isInBern(adresseNotBern1)).thenReturn(false);
        Mockito.when(plzService.isInBern(adresseNotBern2)).thenReturn(false);
        final var pia = gesuch.getNewestGesuchTranche().get().getGesuchFormular().getPersonInAusbildung();
        pia.setNationalitaet(LandGenerator.initSwitzerland());
        gesuch.getNewestGesuchTranche()
            .get()
            .getGesuchFormular()
            .getElterns()
            .stream()
            .toList()
            .get(0)
            .setAdresse(adresseNotBern1);
        gesuch.getNewestGesuchTranche()
            .get()
            .getGesuchFormular()
            .getElterns()
            .stream()
            .toList()
            .get(1)
            .setAdresse(adresseNotBern2);

        var decision = decider.decide(gesuch.getNewestGesuchTranche().get());
        assertThat(decision)
            .isEqualTo(StipDeciderResult.NEGATIVVERFUEGUNG_STIPENDIENRECHTLICHER_WOHNSITZ_WOHNSITZ_ELTERN_NICHT_BERN);

        var event = decider.getGesuchStatusChangeEvent(decision);
        assertThat(event).isEqualTo(GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT);
    }

    @Test
    void testDecisionANSPRUCH_MANUELL_PRUEFEN_STIPENDIENRECHTLICHER_WOHNSITZ_FINANZIELL_UNABHAENGIG() {
        final var gesuch = TestUtil.getGesuchForDecision(UUID.randomUUID());
        gesuch.getNewestGesuchTranche()
            .get()
            .getGesuchFormular()
            .getLebenslaufItems()
            .add(
                new LebenslaufItem().setBildungsart(LebenslaufAusbildungsArt.MASTER).setAusbildungAbgeschlossen(true)
            );
        var decision = decider.decide(gesuch.getNewestGesuchTranche().get());
        assertThat(decision).isEqualTo(
            StipDeciderResult.ANSPRUCH_MANUELL_PRUEFEN_STIPENDIENRECHTLICHER_WOHNSITZ_FINANZIELL_UNABHAENGIG
        );

        var event = decider.getGesuchStatusChangeEvent(decision);
        assertThat(event).isEqualTo(GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN);
    }

    @Test
    void testDecisionANSPRUCH_MANUELL_PRUEFEN_STIPENDIENRECHTLICHER_WOHNSITZ_KESB_NICHT_BERN() {
        final var gesuch = TestUtil.getGesuchForDecision(UUID.randomUUID());
        gesuch.getNewestGesuchTranche()
            .get()
            .getGesuchFormular()
            .getPersonInAusbildung()
            .setVormundschaft(true);
        var decision = decider.decide(gesuch.getNewestGesuchTranche().get());
        assertThat(decision)
            .isEqualTo(StipDeciderResult.ANSPRUCH_MANUELL_PRUEFEN_STIPENDIENRECHTLICHER_WOHNSITZ_KESB_NICHT_BERN);

        var event = decider.getGesuchStatusChangeEvent(decision);
        assertThat(event).isEqualTo(GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN);
    }

    @Test
    void testDecisionNEGATIVVERFUEGUNG_STIPENDIENRECHTLICHER_WOHNSITZ_FLUECHTLING_NICHT_BERN() {
        final var gesuch = TestUtil.getGesuchForDecision(UUID.randomUUID());
        gesuch.getNewestGesuchTranche().get().getGesuchFormular().setElterns(Set.of());
        final var pia = gesuch.getNewestGesuchTranche().get().getGesuchFormular().getPersonInAusbildung();
        pia.setNiederlassungsstatus(Niederlassungsstatus.FLUECHTLING);
        pia.setZustaendigerKanton(ZustaendigerKanton.ANDERER_KANTON);
        var decision = decider.decide(gesuch.getNewestGesuchTranche().get());
        assertThat(decision)
            .isEqualTo(StipDeciderResult.NEGATIVVERFUEGUNG_STIPENDIENRECHTLICHER_WOHNSITZ_FLUECHTLING_NICHT_BERN);

        var event = decider.getGesuchStatusChangeEvent(decision);
        assertThat(event).isEqualTo(GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT);
    }

    @Test
    void testDecisionNEGATIVVERFUEGUNG_STIPENDIENRECHTLICHER_WOHNSITZ_WOHNSITZ_PIA_NICHT_BERN() {
        final var gesuch = TestUtil.getGesuchForDecision(UUID.randomUUID());
        Mockito.when(plzService.isInBern(ArgumentMatchers.any(Adresse.class))).thenReturn(false);
        gesuch.getNewestGesuchTranche().get().getGesuchFormular().setElterns(Set.of());
        final var pia = gesuch.getNewestGesuchTranche().get().getGesuchFormular().getPersonInAusbildung();
        pia.setNationalitaet(LandGenerator.initIran())
            .setNiederlassungsstatus(Niederlassungsstatus.NIEDERLASSUNGSBEWILLIGUNG_C)
            .setEinreisedatum(LocalDate.now().minusYears(1));
        var decision = decider.decide(gesuch.getNewestGesuchTranche().get());
        assertThat(decision)
            .isEqualTo(StipDeciderResult.NEGATIVVERFUEGUNG_STIPENDIENRECHTLICHER_WOHNSITZ_WOHNSITZ_PIA_NICHT_BERN);

        var event = decider.getGesuchStatusChangeEvent(decision);
        assertThat(event).isEqualTo(GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT);
    }
}
