package ch.dvbern.stip.test.eltern.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.service.ElternMapper;
import ch.dvbern.stip.api.eltern.service.ElternMapperImpl;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.generated.dto.AdresseDto;
import ch.dvbern.stip.generated.dto.ElternUpdateDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ElternMapperTest {

	ElternMapper elternMapper = new ElternMapperImpl();

	@Test
	void testElternMapperMapAddDelete() {
		Set<Eltern> elternSet = new LinkedHashSet<>();
		ElternUpdateDto elternUpdateDto = prepareData();
		List<ElternUpdateDto> elternUpdateDtos = new ArrayList<>();
		elternUpdateDtos.add(elternUpdateDto);
		Set<Eltern> neuElternSet = elternMapper.map(elternUpdateDtos, elternSet);
		Assertions.assertEquals(1, neuElternSet.size());
		elternUpdateDtos = new ArrayList<>();
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
		eltern.setSozialversicherungsnummer("756.0000.0000.05");
		return eltern;
	}
}
