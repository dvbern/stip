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

package ch.dvbern.stip.api.auszahlung.util;

import java.util.UUID;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.auszahlung.entity.Zahlungsverbindung;
import ch.dvbern.stip.generated.dto.AdresseDto;
import ch.dvbern.stip.generated.dto.AuszahlungUpdateDto;
import ch.dvbern.stip.generated.dto.ZahlungsverbindungDto;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class ZahlungsverbindungDiffUtilTest {
    @Test
    void hasAdresseChanged() {
        final var knownId = UUID.randomUUID();
        final var zahlungsverbindung = new Zahlungsverbindung().setAdresse((Adresse) new Adresse().setId(knownId));
        final var original = new Auszahlung()
            .setZahlungsverbindung(zahlungsverbindung);

        final var updateAdresse = new AdresseDto();
        updateAdresse.setId(knownId);

        var updateZahlungsverbindung = new ZahlungsverbindungDto();
        updateZahlungsverbindung.setAdresse(updateAdresse);
        var updateAuszahlung = new AuszahlungUpdateDto();
        updateAuszahlung.setZahlungsverbindung(updateZahlungsverbindung);

        assertThat(
            ZahlungsverbindungDiffUtil.hasAdresseChanged(updateZahlungsverbindung, original.getZahlungsverbindung()),
            is(false)
        );

        updateAdresse.setId(UUID.randomUUID());
        assertThat(
            ZahlungsverbindungDiffUtil.hasAdresseChanged(updateZahlungsverbindung, original.getZahlungsverbindung()),
            is(true)
        );
    }
}
