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

package ch.dvbern.stip.api.kind.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.common.type.Ausbildungssituation;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.generated.dto.KindUpdateDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class KindMapperTest {

    KindMapper kindMapper = new KindMapperImpl();

    @Test
    void testKindMapperMapAddDelete() {
        Set<Kind> kindSet = new LinkedHashSet<>();
        KindUpdateDto kindUpdateDto = prepareData();
        // ADD
        List<KindUpdateDto> kindUpdateDtos = new ArrayList<>();
        kindUpdateDtos.add(kindUpdateDto);
        Set<Kind> neuKindSet = kindMapper.map(kindUpdateDtos, kindSet);
        Assertions.assertEquals(1, neuKindSet.size());
        // UPDATE
        kindUpdateDto.setId(UUID.randomUUID());
        kindUpdateDtos.clear();
        kindUpdateDtos.add(kindUpdateDto);
        neuKindSet.stream().forEach(kind -> kind.setId(kindUpdateDto.getId()));
        neuKindSet = kindMapper.map(kindUpdateDtos, neuKindSet);
        Assertions.assertEquals(kindUpdateDto.getId(), neuKindSet.stream().findFirst().get().getId());
        // DELETE ONE ADD A NEW ONE
        kindUpdateDtos.clear();
        kindUpdateDtos.add(prepareData());
        neuKindSet = kindMapper.map(kindUpdateDtos, neuKindSet);
        Assertions.assertEquals(1, neuKindSet.size());
        // DELETE ALL
        kindUpdateDtos.clear();
        neuKindSet = kindMapper.map(kindUpdateDtos, neuKindSet);
        Assertions.assertEquals(0, neuKindSet.size());
    }

    private KindUpdateDto prepareData() {
        var kind = new KindUpdateDto();
        kind.setNachname("Kindnachname");
        kind.setVorname("Kindvorname");
        kind.setGeburtsdatum(LocalDate.of(2011, 1, 1));
        kind.setAusbildungssituation(Ausbildungssituation.IN_AUSBILDUNG);
        kind.setWohnsitzAnteilPia(60);
        return kind;
    }
}
