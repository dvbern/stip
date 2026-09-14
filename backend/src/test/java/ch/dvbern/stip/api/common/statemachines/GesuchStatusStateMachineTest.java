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
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.AenderungAkzeptierenHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.AenderungFehlendeDokumenteNichtEingereichtHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.AenderungFehlendeDokumenteZurueckweisenHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.AenderungZurueckweisenHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.AusbildungUnterbruchAkzeptierenHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.BeschwerdeErfolgreichAkzeptierenHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.DatenschutzbriefDruckbereitHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.FehlendeDokumenteEinreichenHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.FehlendeDokumenteHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.GesuchFehlendeDokumenteNichtEingereichtHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.GesuchZurueckweisenHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.JuristischeAbklaerungDurchPruefungHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.KeinStipendienAnspruchHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.KomplettEingereichtHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.NegativVerfuegtHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.SbInitialisiertAenderungHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.StipendienAnspruchHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.VerfuegtHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.VerfuegungDruckbereitHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.VerfuegungVersendetHandler;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchstatus.service.GesuchStatusChangeEventTrigger;
import ch.dvbern.stip.api.gesuchstatus.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.statusprotokoll.service.StatusprotokollService;
import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Execution(ExecutionMode.CONCURRENT)
class GesuchStatusStateMachineTest {
    private GesuchFehlendeDokumenteNichtEingereichtHandler gesuchFehlendeDokumenteNichtEingereichtHandlerMock;
    private GesuchZurueckweisenHandler gesuchZurueckweisenHandlerMock;
    private KomplettEingereichtHandler komplettEingereichtHandlerMock;
    private DatenschutzbriefDruckbereitHandler datenschutzbriefDruckbereitHandlerMock;
    private FehlendeDokumenteEinreichenHandler fehlendeDokumenteEinreichenHandlerMock;
    private FehlendeDokumenteHandler fehlendeDokumenteHandlerMock;
    private VerfuegungDruckbereitHandler verfuegungDruckbereitHandlerMock;
    private VerfuegungVersendetHandler verfuegungVersendetHandlerMock;
    private AenderungZurueckweisenHandler aenderungZurueckweisenHandlerMock;
    private AenderungFehlendeDokumenteNichtEingereichtHandler aenderungFehlendeDokumenteNichtEingereichtHandlerMock;
    private AenderungFehlendeDokumenteZurueckweisenHandler aenderungFehlendeDokumenteZurueckweisenHandlerMock;
    private StipendienAnspruchHandler stipendienAnspruchHandlerMock;
    private KeinStipendienAnspruchHandler keinStipendienAnspruchHandlerMock;
    private JuristischeAbklaerungDurchPruefungHandler juristischeAbklaerungDurchPruefungHandlerMock;
    private StatusprotokollService statusprotokollService;
    private VerfuegtHandler verfuegtHandlerMock;
    private NegativVerfuegtHandler negativVerfuegtHandlerMock;
    private AenderungAkzeptierenHandler aenderungAkzeptierenHandler;
    private AusbildungUnterbruchAkzeptierenHandler ausbildungUnterbruchAkzeptierenHandler;
    private BeschwerdeErfolgreichAkzeptierenHandler beschwerdeErfolgreichAkzeptierenHandler;
    private SbInitialisiertAenderungHandler sbInitialisiertAenderungHandler;
    private StateMachineConfig<Gesuchstatus, GesuchStatusChangeEvent> config;

    @BeforeEach
    public void createStateMachineConfig() {
        gesuchFehlendeDokumenteNichtEingereichtHandlerMock =
            Mockito.mock(GesuchFehlendeDokumenteNichtEingereichtHandler.class);
        gesuchZurueckweisenHandlerMock = Mockito.mock(GesuchZurueckweisenHandler.class);
        komplettEingereichtHandlerMock = Mockito.mock(KomplettEingereichtHandler.class);
        datenschutzbriefDruckbereitHandlerMock = Mockito.mock(DatenschutzbriefDruckbereitHandler.class);
        fehlendeDokumenteEinreichenHandlerMock = Mockito.mock(FehlendeDokumenteEinreichenHandler.class);
        fehlendeDokumenteHandlerMock = Mockito.mock(FehlendeDokumenteHandler.class);
        verfuegungDruckbereitHandlerMock = Mockito.mock(VerfuegungDruckbereitHandler.class);
        verfuegungVersendetHandlerMock = Mockito.mock(VerfuegungVersendetHandler.class);
        aenderungZurueckweisenHandlerMock = Mockito.mock(AenderungZurueckweisenHandler.class);
        aenderungFehlendeDokumenteNichtEingereichtHandlerMock =
            Mockito.mock(AenderungFehlendeDokumenteNichtEingereichtHandler.class);
        stipendienAnspruchHandlerMock = Mockito.mock(StipendienAnspruchHandler.class);
        keinStipendienAnspruchHandlerMock = Mockito.mock(KeinStipendienAnspruchHandler.class);
        juristischeAbklaerungDurchPruefungHandlerMock = Mockito.mock(JuristischeAbklaerungDurchPruefungHandler.class);
        statusprotokollService = Mockito.mock(StatusprotokollService.class);
        aenderungFehlendeDokumenteZurueckweisenHandlerMock =
            Mockito.mock(AenderungFehlendeDokumenteZurueckweisenHandler.class);
        verfuegtHandlerMock = Mockito.mock(VerfuegtHandler.class);
        negativVerfuegtHandlerMock = Mockito.mock(NegativVerfuegtHandler.class);
        aenderungAkzeptierenHandler = Mockito.mock(AenderungAkzeptierenHandler.class);
        ausbildungUnterbruchAkzeptierenHandler = Mockito.mock(AusbildungUnterbruchAkzeptierenHandler.class);
        beschwerdeErfolgreichAkzeptierenHandler = Mockito.mock(BeschwerdeErfolgreichAkzeptierenHandler.class);
        sbInitialisiertAenderungHandler = Mockito.mock(SbInitialisiertAenderungHandler.class);

        config = new GesuchStatusConfigProducer(
            gesuchFehlendeDokumenteNichtEingereichtHandlerMock,
            gesuchZurueckweisenHandlerMock,
            komplettEingereichtHandlerMock,
            datenschutzbriefDruckbereitHandlerMock,
            fehlendeDokumenteEinreichenHandlerMock,
            fehlendeDokumenteHandlerMock,
            verfuegungDruckbereitHandlerMock,
            verfuegungVersendetHandlerMock,
            aenderungZurueckweisenHandlerMock,
            aenderungFehlendeDokumenteNichtEingereichtHandlerMock,
            stipendienAnspruchHandlerMock,
            keinStipendienAnspruchHandlerMock,
            juristischeAbklaerungDurchPruefungHandlerMock,
            statusprotokollService,
            aenderungFehlendeDokumenteZurueckweisenHandlerMock,
            verfuegtHandlerMock,
            negativVerfuegtHandlerMock,
            aenderungAkzeptierenHandler,
            ausbildungUnterbruchAkzeptierenHandler,
            beschwerdeErfolgreichAkzeptierenHandler,
            sbInitialisiertAenderungHandler
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

        assertThrows(AppErrorException.class, () -> sm.fire(trigger, null, null));
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
        sm.fire(GesuchStatusChangeEventTrigger.createTrigger(GesuchStatusChangeEvent.EINGEREICHT), gesuch, null);

        verify(komplettEingereichtHandlerMock).handle(Mockito.any(), Mockito.any());

        final var handlerList = Arrays.asList(
            gesuchFehlendeDokumenteNichtEingereichtHandlerMock,
            gesuchZurueckweisenHandlerMock,
            fehlendeDokumenteEinreichenHandlerMock,
            fehlendeDokumenteHandlerMock,
            verfuegungDruckbereitHandlerMock,
            verfuegungVersendetHandlerMock,
            aenderungZurueckweisenHandlerMock,
            aenderungFehlendeDokumenteNichtEingereichtHandlerMock,
            stipendienAnspruchHandlerMock
        );
        handlerList.forEach(handler -> {
            verify(handler, times(0)).handle(Mockito.any(), Mockito.any());
        });
    }
}
