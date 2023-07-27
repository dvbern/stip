package ch.dvbern.stip.test.kind.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import ch.dvbern.stip.api.common.type.Ausbildungssituation;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.api.kind.service.KindMapper;
import ch.dvbern.stip.api.kind.service.KindMapperImpl;
import ch.dvbern.stip.generated.dto.KindUpdateDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class KindMapperTest {

	KindMapper kindMapper = new KindMapperImpl();

	@Test
	void testKindMapperMapAddDelete() {
		Set<Kind> kindSet = new LinkedHashSet<>();
		KindUpdateDto kindUpdateDto = prepareData();
		List<KindUpdateDto> kindUpdateDtos = new ArrayList<>();
		kindUpdateDtos.add(kindUpdateDto);
		Set<Kind> neuKindSet = kindMapper.map(kindUpdateDtos, kindSet);
		Assertions.assertEquals(1, neuKindSet.size());
		kindUpdateDtos = new ArrayList<>();
		neuKindSet = kindMapper.map(kindUpdateDtos, neuKindSet);
		Assertions.assertEquals(0, neuKindSet.size());
	}

	private KindUpdateDto prepareData() {
		var kind = new KindUpdateDto();
		kind.setNachname("Kindnachname");
		kind.setVorname("Kindvorname");
		kind.setGeburtsdatum(LocalDate.of(2011, 1, 1));
		kind.setAusbildungssituation(Ausbildungssituation.IN_AUSBILDUNG);
		kind.setWohnsitz(Wohnsitz.MUTTER_VATER);
		kind.setWohnsitzAnteilMutter(new BigDecimal(40));
		kind.setWohnsitzAnteilVater(new BigDecimal(60));
		return kind;
	}
}
