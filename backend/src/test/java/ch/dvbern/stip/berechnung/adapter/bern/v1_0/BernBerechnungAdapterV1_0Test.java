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

package ch.dvbern.stip.berechnung.adapter.bern.v1_0;

import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.AusbildungUnterbruchAntrag;
import ch.dvbern.stip.api.ausbildung.type.AusbildungUnterbruchAntragStatus;
import ch.dvbern.stip.api.ausbildung.type.Bildungskategorie;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.berechnung.adapter.bern.v1_0.service.BernBerechnungAdapterV1_0;
import ch.dvbern.stip.generated.dto.BerechnungsStammdatenDto;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;

public class BernBerechnungAdapterV1_0Test {
    final BernBerechnungAdapterV1_0 adapterV1 = new BernBerechnungAdapterV1_0(
        (gesuchsperiode, anzahlMonate) -> new BerechnungsStammdatenDto()
    );

    @Test
    void getBerechnungsresultatTestNoKuerzung() {
        final Gesuch gesuch = TestUtil.getGesuchForBerechnung(UUID.randomUUID());

        var berechnungsresultatDto = adapterV1.getBerechnungsresultat(gesuch);

        assertThat(berechnungsresultatDto.getBerechnungVorKuerzungUndTeilung(), is(10458));
        assertThat(berechnungsresultatDto.getBerechnungStipendium(), is(10458));
    }

    @Test
    void getBerechnungsresultatTestAllKuerzung() {
        final Gesuch gesuch = TestUtil.getGesuchForBerechnung(UUID.randomUUID());
        gesuch.getAusbildung()
            .getAusbildungsgang()
            .getAbschluss()
            .setBildungskategorie(Bildungskategorie.TERTIAERSTUFE_A);
        gesuch.getAusbildung().setAusbildungBegin(gesuch.getAusbildung().getAusbildungBegin().minusYears(3));
        gesuch.setEinreichedatum(gesuch.getEinreichedatum().plusMonths(4));

        gesuch.getAusbildung()
            .getAusbildungUnterbruchAntrags()
            .add(
                new AusbildungUnterbruchAntrag().setGesuch(gesuch)
                    .setStatus(AusbildungUnterbruchAntragStatus.AKZEPTIERT)
                    .setMonateOhneAnspruch(3)
            );

        var berechnungsresultatDto = adapterV1.getBerechnungsresultat(gesuch);

        assertThat(berechnungsresultatDto.getBerechnungVorKuerzungUndTeilung(), is(10458));
        assertThat(berechnungsresultatDto.getMonateMitDarlehen(), is(12));
        assertThat(berechnungsresultatDto.getUngekuerztStipendien(), is(6972));
        assertThat(berechnungsresultatDto.getUngekuerztDarlehen(), is(3500));
        assertThat(berechnungsresultatDto.getAnzahlMonateEinreichefrist(), notNullValue());
        assertThat(
            berechnungsresultatDto.getTotalNachKuerzungNachEinreichefrist(),
            lessThan(berechnungsresultatDto.getUngekuerztDarlehen())
        );
        assertThat(berechnungsresultatDto.getAnzahlMonateUnterbruch(), is(3));
        assertThat(
            berechnungsresultatDto.getTotalNachKuerzungUnterbruch(),
            lessThan(berechnungsresultatDto.getTotalNachKuerzungNachEinreichefrist())
        );
        assertThat(
            berechnungsresultatDto.getBerechnungVorTeilungDarlehen(),
            equalTo(berechnungsresultatDto.getTotalNachKuerzungUnterbruch())
        );
        assertThat(
            berechnungsresultatDto.getBerechnungStipendium(),
            lessThan(berechnungsresultatDto.getBerechnungVorTeilungDarlehen())
        );
        assertThat(
            berechnungsresultatDto.getBerechnungDarlehen(),
            lessThan(berechnungsresultatDto.getBerechnungStipendium())
        );
    }
}
