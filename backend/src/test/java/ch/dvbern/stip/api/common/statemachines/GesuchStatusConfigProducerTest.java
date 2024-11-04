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

import ch.dvbern.stip.api.common.statemachines.gesuchstatus.GesuchStatusConfigProducer;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

class GesuchStatusConfigProducerTest {
    @Test
    void allGesuchstatusInConfig() {
        final var config = new GesuchStatusConfigProducer(null).createStateMachineConfig();

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
