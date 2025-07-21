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
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.AenderungZurueckweisenHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.FehlendeDokumenteEinreichenHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.FehlendeDokumenteHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.GesuchFehlendeDokumenteNichtEingereichtHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.GesuchZurueckweisenHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.KomplettEingereichtHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.NegativeVerfuegungHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.StipendienAnspruchHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.VersandbereitHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.VersendetHandler;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

class GesuchStatusConfigProducerTest {
    @Test
    void allGesuchstatusInConfig() {
        final var config = new GesuchStatusConfigProducer(
            Mockito.mock(GesuchFehlendeDokumenteNichtEingereichtHandler.class),
            Mockito.mock(GesuchZurueckweisenHandler.class),
            Mockito.mock(KomplettEingereichtHandler.class),
            Mockito.mock(FehlendeDokumenteEinreichenHandler.class),
            Mockito.mock(FehlendeDokumenteHandler.class),
            Mockito.mock(VersandbereitHandler.class),
            Mockito.mock(VersendetHandler.class),
            Mockito.mock(NegativeVerfuegungHandler.class),
            Mockito.mock(AenderungZurueckweisenHandler.class),
            Mockito.mock(AenderungFehlendeDokumenteNichtEingereichtHandler.class),
            Mockito.mock(StipendienAnspruchHandler.class)
        ).createStateMachineConfig();

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
