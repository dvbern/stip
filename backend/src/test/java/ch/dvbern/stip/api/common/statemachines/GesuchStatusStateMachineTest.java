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

package ch.dvbern.stip.api.common.statemachines;

import java.time.LocalDateTime;
import java.util.Arrays;

import ch.dvbern.stip.api.common.exception.AppErrorException;
import ch.dvbern.stip.api.common.statemachines.gesuch.GesuchStatusConfigProducer;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.AenderungFehlendeDokumenteNichtEingereichtHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.AenderungZurueckweisenHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.AnspruchPruefenStatusHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.FehlendeDokumenteEinreichenHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.FehlendeDokumenteHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.GesuchFehlendeDokumenteNichtEingereichtHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.GesuchZurueckweisenHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.KomplettEingereichtHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.NegativeVerfuegungHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.StipendienAnspruchHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.VersandbereitHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.VersendetHandler;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchstatus.service.GesuchStatusChangeEventTrigger;
import ch.dvbern.stip.api.gesuchstatus.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class GesuchStatusStateMachineTest {
    private GesuchFehlendeDokumenteNichtEingereichtHandler gesuchFehlendeDokumenteNichtEingereichtHandlerSpy;
    private GesuchZurueckweisenHandler gesuchZurueckweisenHandlerSpy;
    private KomplettEingereichtHandler komplettEingereichtHandlerSpy;
    private FehlendeDokumenteEinreichenHandler fehlendeDokumenteEinreichenHandlerSpy;
    private FehlendeDokumenteHandler fehlendeDokumenteHandlerSpy;
    private VersandbereitHandler versandbereitHandlerSpy;
    private VersendetHandler versendetHandlerSpy;
    private NegativeVerfuegungHandler negativeVerfuegungHandlerSpy;
    private AenderungZurueckweisenHandler aenderungZurueckweisenHandlerSpy;
    private AenderungFehlendeDokumenteNichtEingereichtHandler aenderungFehlendeDokumenteNichtEingereichtHandlerSpy;
    private StipendienAnspruchHandler stipendienAnspruchHandlerSpy;
    private AnspruchPruefenStatusHandler anspruchPruefenStatusHandlerSpy;
    private StateMachineConfig<Gesuchstatus, GesuchStatusChangeEvent> config;

    @BeforeEach
    public void createStateMachineConfig() {
        gesuchFehlendeDokumenteNichtEingereichtHandlerSpy =
            Mockito.mock(GesuchFehlendeDokumenteNichtEingereichtHandler.class);
        gesuchZurueckweisenHandlerSpy = Mockito.mock(GesuchZurueckweisenHandler.class);
        komplettEingereichtHandlerSpy = Mockito.mock(KomplettEingereichtHandler.class);
        fehlendeDokumenteEinreichenHandlerSpy = Mockito.mock(FehlendeDokumenteEinreichenHandler.class);
        fehlendeDokumenteHandlerSpy = Mockito.mock(FehlendeDokumenteHandler.class);
        versandbereitHandlerSpy = Mockito.mock(VersandbereitHandler.class);
        versendetHandlerSpy = Mockito.mock(VersendetHandler.class);
        negativeVerfuegungHandlerSpy = Mockito.mock(NegativeVerfuegungHandler.class);
        aenderungZurueckweisenHandlerSpy = Mockito.mock(AenderungZurueckweisenHandler.class);
        aenderungFehlendeDokumenteNichtEingereichtHandlerSpy =
            Mockito.mock(AenderungFehlendeDokumenteNichtEingereichtHandler.class);
        stipendienAnspruchHandlerSpy = Mockito.mock(StipendienAnspruchHandler.class);
        anspruchPruefenStatusHandlerSpy = Mockito.mock(AnspruchPruefenStatusHandler.class);

        config = new GesuchStatusConfigProducer(
            gesuchFehlendeDokumenteNichtEingereichtHandlerSpy,
            gesuchZurueckweisenHandlerSpy,
            komplettEingereichtHandlerSpy,
            fehlendeDokumenteEinreichenHandlerSpy,
            fehlendeDokumenteHandlerSpy,
            versandbereitHandlerSpy,
            versendetHandlerSpy,
            negativeVerfuegungHandlerSpy,
            aenderungZurueckweisenHandlerSpy,
            aenderungFehlendeDokumenteNichtEingereichtHandlerSpy,
            stipendienAnspruchHandlerSpy,
            anspruchPruefenStatusHandlerSpy
        ).createStateMachineConfig();
    }

    @Test
    void failsWithoutGesuchAsParameter() {
        final var gesuch = new Gesuch().setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_GS);
        final var sm = new StateMachine<>(
            gesuch.getGesuchStatus(),
            gesuch::getGesuchStatus,
            s -> gesuch.setGesuchStatus(s)
                .setGesuchStatusAenderungDatum(LocalDateTime.now()),
            config
        );

        final var trigger = GesuchStatusChangeEventTrigger.createTrigger(GesuchStatusChangeEvent.EINGEREICHT);

        assertThrows(AppErrorException.class, () -> sm.fire(trigger, null));
    }

    @Test
    void transitionCallsCorrectHandler() {
        final var gesuch = new Gesuch().setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_GS);
        final var sm = new StateMachine<>(
            gesuch.getGesuchStatus(),
            gesuch::getGesuchStatus,
            s -> gesuch.setGesuchStatus(s)
                .setGesuchStatusAenderungDatum(LocalDateTime.now()),
            config
        );
        sm.fire(GesuchStatusChangeEventTrigger.createTrigger(GesuchStatusChangeEvent.EINGEREICHT), gesuch);

        verify(komplettEingereichtHandlerSpy).handle(Mockito.any());

        final var handlerList = Arrays.asList(
            gesuchFehlendeDokumenteNichtEingereichtHandlerSpy,
            gesuchZurueckweisenHandlerSpy,
            fehlendeDokumenteEinreichenHandlerSpy,
            fehlendeDokumenteHandlerSpy,
            versandbereitHandlerSpy,
            versendetHandlerSpy,
            negativeVerfuegungHandlerSpy,
            aenderungZurueckweisenHandlerSpy,
            aenderungFehlendeDokumenteNichtEingereichtHandlerSpy,
            stipendienAnspruchHandlerSpy
        );
        handlerList.forEach(handler -> {
            verify(handler, times(0)).handle(Mockito.any());
        });
    }
}
