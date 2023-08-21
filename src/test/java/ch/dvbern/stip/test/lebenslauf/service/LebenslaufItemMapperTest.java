package ch.dvbern.stip.test.lebenslauf.service;

import ch.dvbern.stip.api.common.service.DateMapperImpl;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.lebenslauf.service.LebenslaufItemMapper;
import ch.dvbern.stip.api.lebenslauf.service.LebenslaufItemMapperImpl;
import ch.dvbern.stip.api.common.type.Bildungsart;
import ch.dvbern.stip.api.lebenslauf.type.WohnsitzKanton;
import ch.dvbern.stip.generated.dto.LebenslaufItemUpdateDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

class LebenslaufItemMapperTest {

    LebenslaufItemMapper lebenslaufItemMapper = new LebenslaufItemMapperImpl(new DateMapperImpl());

	@Test
	void testGeschwisterMapperMapAddDelete() {
		Set<LebenslaufItem> lebenslaufItemSet = new LinkedHashSet<>();
		LebenslaufItemUpdateDto lebenslaufItemUpdateDto = prepareData();
		List<LebenslaufItemUpdateDto> lebenslaufItemUpdateDtos = new ArrayList<>();
		lebenslaufItemUpdateDtos.add(lebenslaufItemUpdateDto);
		Set<LebenslaufItem> neulebenslaufItemSet = lebenslaufItemMapper.map(lebenslaufItemUpdateDtos, lebenslaufItemSet);
		Assertions.assertEquals(1, neulebenslaufItemSet.size());
		lebenslaufItemUpdateDtos = new ArrayList<>();
		neulebenslaufItemSet = lebenslaufItemMapper.map(lebenslaufItemUpdateDtos, neulebenslaufItemSet);
		Assertions.assertEquals(0, neulebenslaufItemSet.size());
	}

	private LebenslaufItemUpdateDto prepareData() {
		var lebenslaufItem = new LebenslaufItemUpdateDto();
		lebenslaufItem.setBeschreibung("Test");
		lebenslaufItem.setBis("02.2022");
		lebenslaufItem.setVon("01.2022");
		lebenslaufItem.setBildungsart(Bildungsart.FACHHOCHSCHULEN);
		lebenslaufItem.setWohnsitz(WohnsitzKanton.BE);
		return lebenslaufItem;
	}
}
