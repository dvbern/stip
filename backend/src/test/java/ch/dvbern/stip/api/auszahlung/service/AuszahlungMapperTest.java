package ch.dvbern.stip.api.auszahlung.service;

import java.util.UUID;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.auszahlung.repo.AdresseRepository;
import ch.dvbern.stip.generated.dto.AdresseDto;
import ch.dvbern.stip.generated.dto.AuszahlungUpdateDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

class AuszahlungMapperTest {
    @Test
    void resetDependentDataUpdatesAdresseTest() {
        final var targetAdresseId = UUID.randomUUID();
        final var target = new Auszahlung()
            .setAdresse(
                (Adresse) new Adresse().setId(targetAdresseId)
            );

        final var updateAdresse = new AdresseDto();
        updateAdresse.setId(targetAdresseId);

        final var updateAuszahlung = new AuszahlungUpdateDto();
        updateAuszahlung.setAdresse(updateAdresse);

        final var updateAdresseId = UUID.randomUUID();
        final var repo = Mockito.mock(AdresseRepository.class);
        Mockito.when(repo.requireById(targetAdresseId)).thenReturn(target.getAdresse());
        Mockito.when(repo.requireById(updateAdresseId)).thenReturn((Adresse) new Adresse().setId(updateAdresseId));

        final var mapper = new AuszahlungMapperImpl();
        mapper.adresseRepository = repo;

        mapper.resetDependentDataBeforeUpdate(updateAuszahlung, target);
        assertThat(target.getAdresse().getId(), is(targetAdresseId));

        updateAdresse.setId(updateAdresseId);

        mapper.resetDependentDataBeforeUpdate(updateAuszahlung, target);
        assertThat(target.getAdresse().getId(), is(updateAdresseId));

        updateAdresse.setId(null);

        mapper.resetDependentDataBeforeUpdate(updateAuszahlung, target);
        assertThat(target.getAdresse(), is(nullValue()));
    }
}
