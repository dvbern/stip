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
import java.util.UUID;

class LebenslaufItemMapperTest {

    LebenslaufItemMapper lebenslaufItemMapper = new LebenslaufItemMapperImpl(new DateMapperImpl());

	@Test
	void testLebenslaufMapperMapAddDelete() {
		Set<LebenslaufItem> lebenslaufItemSet = new LinkedHashSet<>();
		LebenslaufItemUpdateDto lebenslaufItemUpdateDto = prepareData();
		List<LebenslaufItemUpdateDto> lebenslaufItemUpdateDtos = new ArrayList<>();
		// ADD
		lebenslaufItemUpdateDtos.add(lebenslaufItemUpdateDto);
		Set<LebenslaufItem> neulebenslaufItemSet = lebenslaufItemMapper.map(lebenslaufItemUpdateDtos, lebenslaufItemSet);
		Assertions.assertEquals(1, neulebenslaufItemSet.size());
		// UPDATE
		lebenslaufItemUpdateDto.setId(UUID.randomUUID());
		lebenslaufItemUpdateDtos.clear();
		lebenslaufItemUpdateDtos.add(lebenslaufItemUpdateDto);
		neulebenslaufItemSet.stream().forEach(lebenslaufItem -> lebenslaufItem.setId(lebenslaufItemUpdateDto.getId()));
		neulebenslaufItemSet = lebenslaufItemMapper.map(lebenslaufItemUpdateDtos, neulebenslaufItemSet);
		Assertions.assertEquals(lebenslaufItemUpdateDto.getId(), neulebenslaufItemSet.stream().findFirst().get().getId());
		//DELETE ONE ADD A NEW ONE
		lebenslaufItemUpdateDtos.clear();
		lebenslaufItemUpdateDtos.add(prepareData());
		neulebenslaufItemSet = lebenslaufItemMapper.map(lebenslaufItemUpdateDtos, neulebenslaufItemSet);
		Assertions.assertEquals(1, neulebenslaufItemSet.size());
		//DELETE ALL
		lebenslaufItemUpdateDtos.clear();
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
