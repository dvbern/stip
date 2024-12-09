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
import ch.dvbern.stip.api.common.type.StipDecision;
import ch.dvbern.stip.api.gesuch.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.lebenslauf.type.LebenslaufAusbildungsArt;
import ch.dvbern.stip.api.personinausbildung.entity.ZustaendigerKanton;
import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import ch.dvbern.stip.api.plz.service.PlzService;
import ch.dvbern.stip.api.stammdaten.service.LandService;
import ch.dvbern.stip.api.stammdaten.type.Land;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.stipdecision.decider.BernStipDecider;
import ch.dvbern.stip.stipdecision.repo.StipDecisionTextRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RequiredArgsConstructor
@QuarkusTest
@Slf4j
class BernStipDeciderTest {
    @Inject
    StipDecisionTextRepository stipDecisionTextRepository;
    private LandService landService;
    private PlzService plzService;
    private BernStipDecider decider;

    @BeforeEach
    void setUp() {
        landService = Mockito.mock(LandService.class);
        plzService = Mockito.mock(PlzService.class);
        Mockito.when(plzService.isInBern(ArgumentMatchers.any(Adresse.class))).thenReturn(true);
        Mockito.when(landService.landInEuEfta(ArgumentMatchers.any())).thenReturn(true);
        decider = new BernStipDecider(stipDecisionTextRepository, landService, plzService);
    }

    @Test
    void testGetValidDecision() {
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
        assertNotNull(decider.getTextForDecision(decision, Sprache.DEUTSCH));
        assertNotNull(decider.getTextForDecision(decision, Sprache.FRANZOESISCH));

        var event = decider.getGesuchStatusChangeEvent(decision);
        assertThat(event).isEqualTo(GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT);
        var text = decider.getTextForDecision(decision, Sprache.DEUTSCH);
        assertNotNull(text);
    }

    @Test
    void testGetDecisionAusbildungNichtAnerkannt() {
        final var gesuch = TestUtil.getGesuchForDecision(UUID.randomUUID());
        gesuch.getAusbildung().setAusbildungNichtGefunden(true);
        var decision = decider.decide(gesuch.getNewestGesuchTranche().get());
        assertThat(decision).isEqualTo(StipDecision.AUSBILDUNG_NICHT_ANERKANNT);

        var event = decider.getGesuchStatusChangeEvent(decision);
        assertThat(event).isEqualTo(GesuchStatusChangeEvent.ABKLAERUNG_DURCH_RECHSTABTEILUNG);
        var text = decider.getTextForDecision(decision, Sprache.DEUTSCH);
        assertNotNull(text);
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
        assertThrows(IllegalStateException.class, () -> decider.getTextForDecision(decision, Sprache.DEUTSCH));
    }

    @Test
    void testGetDecisionAusbildungenLaenger12Jahre() {
        final var gesuch = TestUtil.getGesuchForDecision(UUID.randomUUID());
        gesuch.getAusbildung().setAusbildungBegin(LocalDate.now().minusYears(13));

        var decision = decider.decide(gesuch.getNewestGesuchTranche().get());
        assertThat(decision).isEqualTo(StipDecision.AUSBILDUNGEN_LAENGER_12_JAHRE);

        var event = decider.getGesuchStatusChangeEvent(decision);
        assertThat(event).isEqualTo(GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN);
        var text = decider.getTextForDecision(decision, Sprache.DEUTSCH);
        assertNotNull(text);
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
        var text = decider.getTextForDecision(decision, Sprache.DEUTSCH);
        assertNotNull(text);
    }

    @Test
    void testStipendienrechtlicherWohnsitzKantonBernCheckerEvaluateFailStep1() {
        final var gesuch = TestUtil.getGesuchForDecision(UUID.randomUUID());
        Mockito.when(landService.landInEuEfta(ArgumentMatchers.any())).thenReturn(false);
        final var pia = gesuch.getNewestGesuchTranche().get().getGesuchFormular().getPersonInAusbildung();
        pia.setNationalitaet(Land.IR)
            .setNiederlassungsstatus(Niederlassungsstatus.AUFENTHALTSBEWILLIGUNG_B)
            .setEinreisedatum(LocalDate.now().minusYears(1));

        var decision = decider.decide(gesuch.getNewestGesuchTranche().get());
        assertThat(decision).isEqualTo(StipDecision.NICHT_BERECHTIGTE_PERSON);

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
        assertThat(decision).isEqualTo(StipDecision.GESUCH_VALID);

        var event = decider.getGesuchStatusChangeEvent(decision);
        assertThat(event).isEqualTo(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG);
    }

    @Test
    void testStipendienrechtlicherWohnsitzKantonBernCheckEuEftaBern() {
        final var gesuch = TestUtil.getGesuchForDecision(UUID.randomUUID());
        Mockito.when(landService.landInEuEfta(ArgumentMatchers.any())).thenReturn(false);
        final var pia = gesuch.getNewestGesuchTranche().get().getGesuchFormular().getPersonInAusbildung();
        pia.setNationalitaet(Land.IR);
        pia.setAdresse(new Adresse().setLand(Land.CH));
        pia.setNiederlassungsstatus(Niederlassungsstatus.NIEDERLASSUNGSBEWILLIGUNG_C);
        gesuch.getNewestGesuchTranche().get().getGesuchFormular().setElterns(Set.of());

        var decision = decider.decide(gesuch.getNewestGesuchTranche().get());
        assertThat(decision).isEqualTo(StipDecision.GESUCH_VALID);

        var event = decider.getGesuchStatusChangeEvent(decision);
        assertThat(event).isEqualTo(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG);
    }

    @Test
    void testStipendienrechtlicherWohnsitzKantonSchweizerElternlos() {
        final var gesuch = TestUtil.getGesuchForDecision(UUID.randomUUID());
        Mockito.when(landService.landInEuEfta(ArgumentMatchers.any())).thenReturn(true);
        final var pia = gesuch.getNewestGesuchTranche().get().getGesuchFormular().getPersonInAusbildung();
        pia.setNationalitaet(Land.CH);
        gesuch.getNewestGesuchTranche()
            .get()
            .getGesuchFormular()
            .setElterns(Set.of());

        var decision = decider.decide(gesuch.getNewestGesuchTranche().get());
        assertThat(decision).isEqualTo(StipDecision.ANSPRUCH_MANUELL_PRUEFEN);

        var event = decider.getGesuchStatusChangeEvent(decision);
        assertThat(event).isEqualTo(GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN);
    }

    @Test
    void testStipendienrechtlicherWohnsitzKantonOneElternBern() {
        final var gesuch = TestUtil.getGesuchForDecision(UUID.randomUUID());
        Mockito.when(landService.landInEuEfta(ArgumentMatchers.any())).thenReturn(true);
        final Adresse adresseBern = new Adresse();
        final Adresse adresseNotBern = new Adresse().setLand(Land.DE);
        Mockito.when(plzService.isInBern(adresseBern)).thenReturn(true);
        Mockito.when(plzService.isInBern(adresseNotBern)).thenReturn(false);
        final var pia = gesuch.getNewestGesuchTranche().get().getGesuchFormular().getPersonInAusbildung();
        pia.setNationalitaet(Land.CH);
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
        assertThat(decision).isEqualTo(StipDecision.ANSPRUCH_MANUELL_PRUEFEN);

        var event = decider.getGesuchStatusChangeEvent(decision);
        assertThat(event).isEqualTo(GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN);
    }

    @Test
    void testStipendienrechtlicherWohnsitzKantonNoElternBern() {
        final var gesuch = TestUtil.getGesuchForDecision(UUID.randomUUID());
        Mockito.when(landService.landInEuEfta(ArgumentMatchers.any())).thenReturn(true);
        final Adresse adresseNotBern1 = new Adresse().setLand(Land.CH);
        final Adresse adresseNotBern2 = new Adresse().setLand(Land.DE);
        Mockito.when(plzService.isInBern(adresseNotBern1)).thenReturn(false);
        Mockito.when(plzService.isInBern(adresseNotBern2)).thenReturn(false);
        final var pia = gesuch.getNewestGesuchTranche().get().getGesuchFormular().getPersonInAusbildung();
        pia.setNationalitaet(Land.CH);
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
        assertThat(decision).isEqualTo(StipDecision.NICHT_BERECHTIGTE_PERSON);

        var event = decider.getGesuchStatusChangeEvent(decision);
        assertThat(event).isEqualTo(GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT);
    }
}
