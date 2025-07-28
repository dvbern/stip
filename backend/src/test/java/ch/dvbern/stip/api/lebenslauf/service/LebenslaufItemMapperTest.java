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

package ch.dvbern.stip.api.lebenslauf.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.Abschluss;
import ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie;
import ch.dvbern.stip.api.ausbildung.type.Bildungskategorie;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.lebenslauf.type.LebenslaufAusbildungsArt;
import ch.dvbern.stip.api.lebenslauf.type.WohnsitzKanton;
import ch.dvbern.stip.generated.dto.LebenslaufItemUpdateDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LebenslaufItemMapperTest {

    LebenslaufItemMapper lebenslaufItemMapper = new LebenslaufItemMapperImpl();

    @Test
    void testLebenslaufMapperMapAddDelete() {
        Set<LebenslaufItem> lebenslaufItemSet = new LinkedHashSet<>();
        LebenslaufItemUpdateDto lebenslaufItemUpdateDto = prepareData();
        List<LebenslaufItemUpdateDto> lebenslaufItemUpdateDtos = new ArrayList<>();
        // ADD
        lebenslaufItemUpdateDtos.add(lebenslaufItemUpdateDto);
        Set<LebenslaufItem> neulebenslaufItemSet =
            lebenslaufItemMapper.map(lebenslaufItemUpdateDtos, lebenslaufItemSet);
        Assertions.assertEquals(1, neulebenslaufItemSet.size());
        // UPDATE
        lebenslaufItemUpdateDto.setId(UUID.randomUUID());
        lebenslaufItemUpdateDtos.clear();
        lebenslaufItemUpdateDtos.add(lebenslaufItemUpdateDto);
        neulebenslaufItemSet.stream().forEach(lebenslaufItem -> lebenslaufItem.setId(lebenslaufItemUpdateDto.getId()));
        neulebenslaufItemSet = lebenslaufItemMapper.map(lebenslaufItemUpdateDtos, neulebenslaufItemSet);
        Assertions.assertEquals(
            lebenslaufItemUpdateDto.getId(),
            neulebenslaufItemSet.stream().findFirst().get().getId()
        );
        // DELETE ONE ADD A NEW ONE
        lebenslaufItemUpdateDtos.clear();
        lebenslaufItemUpdateDtos.add(prepareData());
        neulebenslaufItemSet = lebenslaufItemMapper.map(lebenslaufItemUpdateDtos, neulebenslaufItemSet);
        Assertions.assertEquals(1, neulebenslaufItemSet.size());
        // DELETE ALL
        lebenslaufItemUpdateDtos.clear();
        neulebenslaufItemSet = lebenslaufItemMapper.map(lebenslaufItemUpdateDtos, neulebenslaufItemSet);
        Assertions.assertEquals(0, neulebenslaufItemSet.size());
    }

    private LebenslaufItemUpdateDto prepareData() {
        var lebenslaufItem = new LebenslaufItemUpdateDto();
        lebenslaufItem.setBis("02.2022");
        lebenslaufItem.setVon("01.2022");
        lebenslaufItem.setWohnsitz(WohnsitzKanton.BE);
        return lebenslaufItem;
    }
}
