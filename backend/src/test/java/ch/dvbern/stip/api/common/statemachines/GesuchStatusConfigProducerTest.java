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

import ch.dvbern.stip.api.common.statemachines.gesuch.GesuchStatusConfigProducer;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.AenderungFehlendeDokumenteNichtEingereichtHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.AenderungFehlendeDokumenteZurueckweisenHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.AenderungZurueckweisenHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.BereitFuerBearbeitungHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.DatenschutzbriefDruckbereitHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.FehlendeDokumenteEinreichenHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.FehlendeDokumenteHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.GesuchFehlendeDokumenteNichtEingereichtHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.GesuchZurueckweisenHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.JuristischeAbklaerungDurchPruefungHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.KomplettEingereichtHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.NegativeVerfuegungHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.StipendienAnspruchHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.VerfuegtHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.VerfuegungDruckbereitHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.VerfuegungVersendetHandler;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.statusprotokoll.service.StatusprotokollService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

@Execution(ExecutionMode.CONCURRENT)
class GesuchStatusConfigProducerTest {
    @Test
    void allGesuchstatusInConfig() {
        final var config = new GesuchStatusConfigProducer(
            Mockito.mock(GesuchFehlendeDokumenteNichtEingereichtHandler.class),
            Mockito.mock(GesuchZurueckweisenHandler.class),
            Mockito.mock(KomplettEingereichtHandler.class),
            Mockito.mock(DatenschutzbriefDruckbereitHandler.class),
            Mockito.mock(FehlendeDokumenteEinreichenHandler.class),
            Mockito.mock(FehlendeDokumenteHandler.class),
            Mockito.mock(VerfuegungDruckbereitHandler.class),
            Mockito.mock(VerfuegungVersendetHandler.class),
            Mockito.mock(NegativeVerfuegungHandler.class),
            Mockito.mock(AenderungZurueckweisenHandler.class),
            Mockito.mock(AenderungFehlendeDokumenteNichtEingereichtHandler.class),
            Mockito.mock(StipendienAnspruchHandler.class),
            Mockito.mock(JuristischeAbklaerungDurchPruefungHandler.class),
            Mockito.mock(StatusprotokollService.class),
            Mockito.mock(BereitFuerBearbeitungHandler.class),
            Mockito.mock(VerfuegtHandler.class),
            Mockito.mock(AenderungFehlendeDokumenteZurueckweisenHandler.class)
        )
            .createStateMachineConfig();

        for (final var status : Gesuchstatus.values()) {
            final var representation = config.getRepresentation(status);
            assertThat(
                String.format("Gesuchstatus '%s' must be represented by the state machine", status),
                representation,
                is(not(nullValue()))
            );
        }
    }
}
