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

package ch.dvbern.stip.api.nesko.service;

import java.math.BigDecimal;

import ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice.EffSatzType;
import ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice.GetSteuerdatenResponse;
import ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice.MannFrauEffSatzType;
import ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice.SteuerdatenType;
import ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice.VeranlagungsStatusType;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NeskoSteuerdatenMapperTest {
    GetSteuerdatenResponse getSteuerdatenResponse;
    Steuerdaten steuerdaten;

    @BeforeEach
    void setUp() {
        getSteuerdatenResponse = new GetSteuerdatenResponse();
        getSteuerdatenResponse.setSteuerdaten(new SteuerdatenType());
        var steuerbaresVermoegenKanton = new EffSatzType();
        steuerbaresVermoegenKanton.setEffektiv(BigDecimal.ZERO);
        getSteuerdatenResponse.getSteuerdaten().setSteuerbaresVermoegenKanton(steuerbaresVermoegenKanton);
        getSteuerdatenResponse.getSteuerdaten().setFahrkosten(new MannFrauEffSatzType());
        getSteuerdatenResponse.getSteuerdaten().setKostenAuswaertigeVerpflegung(new MannFrauEffSatzType());
        getSteuerdatenResponse.getSteuerdaten().setStatusVeranlagung(VeranlagungsStatusType.DEFINITIV_BEARBEITET);
        steuerdaten = new Steuerdaten();
        steuerdaten.setSteuerdatenTyp(SteuerdatenTyp.FAMILIE);
    }

    @Test
    void shouldTakeMaximumValueOfEinkommenTest() {
        var effSatzType = new EffSatzType();
        final var value1 = 1000L;
        final var value2 = 2000L;
        effSatzType.setEffektiv(BigDecimal.valueOf(value1));
        effSatzType.setSatzbestimmend(BigDecimal.valueOf(value2));
        getSteuerdatenResponse.getSteuerdaten().setTotalEinkuenfte(effSatzType);

        assertEquals(
            NeskoSteuerdatenMapper.updateFromNeskoSteuerdaten(steuerdaten, getSteuerdatenResponse)
                .getTotalEinkuenfte()
                .longValue(),
            value2
        );
    }
}
