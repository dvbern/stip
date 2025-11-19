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

package ch.dvbern.stip.api.eltern.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.adresse.service.MockAdresseMapperImpl;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.generated.dto.AdresseDto;
import ch.dvbern.stip.generated.dto.ElternUpdateDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static ch.dvbern.stip.api.util.TestConstants.AHV_NUMMER_VALID;

@Execution(ExecutionMode.CONCURRENT)
class ElternMapperTest {

    ElternMapper elternMapper = new ElternMapperImpl(new MockAdresseMapperImpl());

    @Test
    void testElternMapperMapAddDelete() {
        Set<Eltern> elternSet = new LinkedHashSet<>();
        ElternUpdateDto elternUpdateDto = prepareData();
        List<ElternUpdateDto> elternUpdateDtos = new ArrayList<>();
        elternUpdateDtos.add(elternUpdateDto);
        Set<Eltern> neuElternSet = elternMapper.map(elternUpdateDtos, elternSet);
        Assertions.assertEquals(1, neuElternSet.size());
        // UPDATE
        elternUpdateDto.setId(UUID.randomUUID());
        elternUpdateDtos.clear();
        elternUpdateDtos.add(elternUpdateDto);
        neuElternSet.stream().forEach(eltern -> eltern.setId(elternUpdateDto.getId()));
        neuElternSet = elternMapper.map(elternUpdateDtos, neuElternSet);
        Assertions.assertEquals(elternUpdateDto.getId(), neuElternSet.stream().findFirst().get().getId());
        // DELETE ONE ADD A NEW ONE
        elternUpdateDtos.clear();
        elternUpdateDtos.add(prepareData());
        neuElternSet = elternMapper.map(elternUpdateDtos, neuElternSet);
        Assertions.assertEquals(1, neuElternSet.size());
        // DELETE ALL
        elternUpdateDtos.clear();
        neuElternSet = elternMapper.map(elternUpdateDtos, neuElternSet);
        Assertions.assertEquals(0, neuElternSet.size());
    }

    private ElternUpdateDto prepareData() {
        var eltern = new ElternUpdateDto();
        eltern.setNachname("Elternnachname");
        eltern.setVorname("Elternvorname");
        eltern.setGeburtsdatum(LocalDate.of(2001, 1, 1));
        eltern.setTelefonnummer("");
        eltern.setAusweisbFluechtling(false);
        eltern.setAdresse(new AdresseDto());
        eltern.setElternTyp(ElternTyp.VATER);
        eltern.setIdentischerZivilrechtlicherWohnsitz(false);
        eltern.setIdentischerZivilrechtlicherWohnsitzOrt("Test");
        eltern.setIdentischerZivilrechtlicherWohnsitzPLZ("1234");
        eltern.setSozialversicherungsnummer(AHV_NUMMER_VALID);
        return eltern;
    }
}
