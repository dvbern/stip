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
import ch.dvbern.stip.api.auszahlung.entity.Zahlungsverbindung;
import ch.dvbern.stip.generated.dto.AdresseDto;
import ch.dvbern.stip.generated.dto.AuszahlungUpdateDto;
import ch.dvbern.stip.generated.dto.ZahlungsverbindungDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

class AuszahlungMapperTest {
    @Test
    void resetDependentDataUpdatesAdresseTest() {
        final var targetAdresseId = UUID.randomUUID();
        var zahlungsverbindung = new Zahlungsverbindung();
        zahlungsverbindung.setAdresse((Adresse) new Adresse().setId(targetAdresseId));
        var target = new Auszahlung();
        target.setZahlungsverbindung(zahlungsverbindung);

        final var updateAdresse = new AdresseDto();
        updateAdresse.setId(targetAdresseId);

        var updateZahlungsverbindung = new ZahlungsverbindungDto();
        updateZahlungsverbindung.setAdresse(updateAdresse);
        var updateAuszahlung = new AuszahlungUpdateDto();
        updateAuszahlung.setZahlungsverbindung(updateZahlungsverbindung);

        final var updateAdresseId = UUID.randomUUID();
        final var repo = Mockito.mock(AdresseRepository.class);
        Mockito.when(repo.requireById(targetAdresseId)).thenReturn(target.getZahlungsverbindung().getAdresse());
        Mockito.when(repo.requireById(updateAdresseId)).thenReturn((Adresse) new Adresse().setId(updateAdresseId));

        final var mapper = new AuszahlungMapperImpl();
        mapper.adresseRepository = repo;

        mapper.resetDependentDataBeforeUpdate(updateAuszahlung, target);
        assertThat(target.getZahlungsverbindung().getAdresse().getId(), is(targetAdresseId));

        updateAdresse.setId(updateAdresseId);

        mapper.resetDependentDataBeforeUpdate(updateAuszahlung, target);
        assertThat(target.getZahlungsverbindung().getAdresse().getId(), is(updateAdresseId));

        updateAdresse.setId(null);

        mapper.resetDependentDataBeforeUpdate(updateAuszahlung, target);
        assertThat(target.getZahlungsverbindung().getAdresse(), is(nullValue()));
    }
}
