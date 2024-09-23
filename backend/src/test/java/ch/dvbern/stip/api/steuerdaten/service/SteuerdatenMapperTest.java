package ch.dvbern.stip.api.steuerdaten.service;

import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.generated.dto.SteuerdatenUpdateDto;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

class SteuerdatenMapperTest {
    @Test
    void resetSaeulenBecauseArbeitsverhaletnissChangedTest() {
        // Arrange
        final var source = new SteuerdatenUpdateDto();
        source.setIsArbeitsverhaeltnisSelbstaendig(true);
        source.setSaeule2(10);
        source.setSaeule3a(10);

        final var target = new Steuerdaten();
        final var mapper = createMapper();

        // Init target
        mapper.partialUpdate(source, target);

        // Rearrange
        source.setIsArbeitsverhaeltnisSelbstaendig(false);

        // Act
        mapper.partialUpdate(source, target);

        // Assert
        assertThat(target.getSaeule2(), is(nullValue()));
        assertThat(target.getSaeule3a(), is(nullValue()));
    }

    SteuerdatenMapper createMapper() {
        return new SteuerdatenMapperImpl();
    }
}
