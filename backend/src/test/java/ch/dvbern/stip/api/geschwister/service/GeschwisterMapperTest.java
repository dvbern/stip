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

package ch.dvbern.stip.api.geschwister.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.common.type.Ausbildungssituation;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.geschwister.entity.Geschwister;
import ch.dvbern.stip.generated.dto.GeschwisterUpdateDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GeschwisterMapperTest {
    GeschwisterMapper geschwisterMapper = new GeschwisterMapperImpl();

    @Test
    void testGeschwisterMapperMapAddDelete() {
        Set<Geschwister> geschwisterSet = new LinkedHashSet<>();
        GeschwisterUpdateDto geschwisterUpdateDto = prepareData();
        List<GeschwisterUpdateDto> geschwisterUpdateDtos = new ArrayList<>();
        // ADD
        geschwisterUpdateDtos.add(geschwisterUpdateDto);
        Set<Geschwister> neuGeschwisterSet = geschwisterMapper.map(geschwisterUpdateDtos, geschwisterSet);
        Assertions.assertEquals(1, neuGeschwisterSet.size());
        // UPDATE
        geschwisterUpdateDto.setId(UUID.randomUUID());
        geschwisterUpdateDtos.clear();
        geschwisterUpdateDtos.add(geschwisterUpdateDto);
        neuGeschwisterSet.stream().forEach(geschwister -> geschwister.setId(geschwisterUpdateDto.getId()));
        neuGeschwisterSet = geschwisterMapper.map(geschwisterUpdateDtos, neuGeschwisterSet);
        Assertions.assertEquals(geschwisterUpdateDto.getId(), neuGeschwisterSet.stream().findFirst().get().getId());
        // DELETE ONE ADD A NEW ONE
        geschwisterUpdateDtos.clear();
        geschwisterUpdateDtos.add(prepareData());
        neuGeschwisterSet = geschwisterMapper.map(geschwisterUpdateDtos, neuGeschwisterSet);
        Assertions.assertEquals(1, neuGeschwisterSet.size());
        // DELETE ALL
        geschwisterUpdateDtos.clear();
        neuGeschwisterSet = geschwisterMapper.map(geschwisterUpdateDtos, neuGeschwisterSet);
        Assertions.assertEquals(0, neuGeschwisterSet.size());
    }

    private GeschwisterUpdateDto prepareData() {
        var geschwister = new GeschwisterUpdateDto();
        geschwister.setNachname("Geschwisternachname");
        geschwister.setVorname("Geschwistervorname");
        geschwister.setGeburtsdatum(LocalDate.of(2001, 1, 1));
        geschwister.setAusbildungssituation(Ausbildungssituation.IN_AUSBILDUNG);
        geschwister.setWohnsitz(Wohnsitz.MUTTER_VATER);
        geschwister.setWohnsitzAnteilMutter(new BigDecimal(40));
        geschwister.setWohnsitzAnteilVater(new BigDecimal(60));
        return geschwister;
    }
}
