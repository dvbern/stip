package ch.dvbern.stip.api.common.statemachines;

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
