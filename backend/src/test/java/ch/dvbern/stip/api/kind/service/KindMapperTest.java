package ch.dvbern.stip.api.kind.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.common.type.Ausbildungssituation;
import ch.dvbern.stip.api.common.type.Wohnsitz;
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
        //DELETE ONE ADD A NEW ONE
        kindUpdateDtos.clear();
        kindUpdateDtos.add(prepareData());
        neuKindSet = kindMapper.map(kindUpdateDtos, neuKindSet);
        Assertions.assertEquals(1, neuKindSet.size());
        //DELETE ALL
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
