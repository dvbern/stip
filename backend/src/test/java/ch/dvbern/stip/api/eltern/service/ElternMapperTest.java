package ch.dvbern.stip.api.eltern.service;

import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.generated.dto.AdresseDto;
import ch.dvbern.stip.generated.dto.ElternUpdateDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static ch.dvbern.stip.api.util.TestConstants.AHV_NUMMER_VALID;

class ElternMapperTest {

    ElternMapper elternMapper = new ElternMapperImpl();

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
        //DELETE ONE ADD A NEW ONE
        elternUpdateDtos.clear();
        elternUpdateDtos.add(prepareData());
        neuElternSet = elternMapper.map(elternUpdateDtos, neuElternSet);
        Assertions.assertEquals(1, neuElternSet.size());
        //DELETE ALL
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
        eltern.setErgaenzungsleistungAusbezahlt(false);
        eltern.setIdentischerZivilrechtlicherWohnsitz(false);
        eltern.setIdentischerZivilrechtlicherWohnsitzOrt("Test");
        eltern.setIdentischerZivilrechtlicherWohnsitzPLZ("1234");
        eltern.setSozialhilfebeitraegeAusbezahlt(false);
        eltern.setSozialversicherungsnummer(AHV_NUMMER_VALID);
        return eltern;
    }
}
