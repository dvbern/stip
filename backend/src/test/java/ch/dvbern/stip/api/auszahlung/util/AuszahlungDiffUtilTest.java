package ch.dvbern.stip.api.auszahlung.util;

import java.util.UUID;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.generated.dto.AdresseDto;
import ch.dvbern.stip.generated.dto.AuszahlungUpdateDto;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class AuszahlungDiffUtilTest {
    @Test
    void hasAdresseChanged() {
        final var knownId = UUID.randomUUID();
        final var original = new Auszahlung()
            .setAdresse((Adresse) new Adresse().setId(knownId));

        final var updateAdresse = new AdresseDto();
        updateAdresse.setId(knownId);

        final var updateAuszahlung = new AuszahlungUpdateDto();
        updateAuszahlung.setAdresse(updateAdresse);

        assertThat(AuszahlungDiffUtil.hasAdresseChanged(updateAuszahlung, original), is(false));

        updateAdresse.setId(UUID.randomUUID());
        assertThat(AuszahlungDiffUtil.hasAdresseChanged(updateAuszahlung, original), is(true));
    }
}
