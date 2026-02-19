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

package ch.dvbern.stip.api.verfuegung.util;

import java.util.UUID;
import java.util.stream.IntStream;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.buchhaltung.entity.Buchhaltung;
import ch.dvbern.stip.api.buchhaltung.service.BuchhaltungService;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.verfuegung.entity.Verfuegung;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class VerfuegungUtilTest {

    private Verfuegung createGesuchWithAmountVerfuegungen(int amountVerfuegung) {
        final var fall = new Fall();
        fall.setId(UUID.randomUUID());
        final var gesuch = new Gesuch().setAusbildung(new Ausbildung().setFall(fall));
        gesuch.setVerfuegungs(
            IntStream.range(0, amountVerfuegung)
                .mapToObj(
                    (ignored) -> new Verfuegung()
                        .setGesuch(gesuch)
                )
                .toList()
        );
        return gesuch.getVerfuegungs().getLast();
    }

    @ParameterizedTest
    @CsvSource(
        {
            "1,false",
            "2,true",
            "22,true",
        }
    )
    void checkIsAenderung(int amountVerfuegung, boolean expected) {
        final var isAenderung = VerfuegungUtil.isAenderung(createGesuchWithAmountVerfuegungen(amountVerfuegung));

        assertEquals(expected, isAenderung);
    }

    @ParameterizedTest
    @CsvSource(
        {
            "1,-500,true", // First verfuegung
            "1,500,false",
            "2,-500,false",
            "2,500,false",
        }
    )
    void checkIsRueckforderung(int amountVerfuegung, int saldo, boolean expected) {
        final var buchhaltungsServiceMock = Mockito.mock(BuchhaltungService.class);
        when(buchhaltungsServiceMock.getLatestBuchhaltungEntry(any())).thenReturn(new Buchhaltung().setSaldo(saldo));
        final var isRueckforderung = VerfuegungUtil
            .isRueckforderung(createGesuchWithAmountVerfuegungen(amountVerfuegung), buchhaltungsServiceMock);

        assertEquals(expected, isRueckforderung);
    }
}
