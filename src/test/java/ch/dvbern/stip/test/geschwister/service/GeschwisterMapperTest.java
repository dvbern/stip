package ch.dvbern.stip.test.geschwister.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import ch.dvbern.stip.api.common.type.Ausbildungssituation;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.geschwister.entity.Geschwister;
import ch.dvbern.stip.api.geschwister.service.GeschwisterMapper;
import ch.dvbern.stip.api.geschwister.service.GeschwisterMapperImpl;
import ch.dvbern.stip.generated.dto.GeschwisterUpdateDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GeschwisterMapperTest {
	GeschwisterMapper geschwisterMapper = new GeschwisterMapperImpl();

	@Test
	void testGeschwisterMapperMapAddDelete() {
		Set<Geschwister> geschwisterSet = new LinkedHashSet<>();
		GeschwisterUpdateDto geschwisterUpdateDto = prepareData();
		List<GeschwisterUpdateDto> geschwisterUpdateDtos = new ArrayList<>();
		geschwisterUpdateDtos.add(geschwisterUpdateDto);
		Set<Geschwister> neuGeschwisterSet = geschwisterMapper.map(geschwisterUpdateDtos, geschwisterSet);
		Assertions.assertEquals(1, neuGeschwisterSet.size());
		geschwisterUpdateDtos = new ArrayList<>();
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
