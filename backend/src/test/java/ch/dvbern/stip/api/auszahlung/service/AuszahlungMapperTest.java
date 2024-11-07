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

package ch.dvbern.stip.api.auszahlung.service;

import java.util.UUID;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.adresse.repo.AdresseRepository;
import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
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
