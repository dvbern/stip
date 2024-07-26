package ch.dvbern.stip.api.steuerdaten.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.generated.dto.SteuerdatenDto;
import ch.dvbern.stip.generated.dto.SteuerdatenUpdateDto;
import jakarta.ws.rs.NotFoundException;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public interface SteuerdatenMapper {
    Steuerdaten toEntity(SteuerdatenDto steuerdatenDto);

    SteuerdatenDto toDto(Steuerdaten steuerdaten);

    Steuerdaten partialUpdate(SteuerdatenUpdateDto steuerdatenDto, @MappingTarget Steuerdaten steuerdaten);

    @BeforeMapping
    default void beforeMapping(SteuerdatenUpdateDto steuerdatenUpdateDto, @MappingTarget Steuerdaten steuerdaten) {
        steuerdaten.setVeranlagungsCode(steuerdatenUpdateDto.getVeranlagungscode());
    }

    @AfterMapping
    default void afterMapping(Steuerdaten steuerdaten, @MappingTarget SteuerdatenDto steuerdatenDto) {
        steuerdatenDto.setVeranlagungscode(steuerdaten.getVeranlagungsCode());
    }

    default Set<Steuerdaten> map(final List<SteuerdatenUpdateDto> steuerdatenUpdateDtos,
                                 final @MappingTarget Set<Steuerdaten> steuerdatenSet) {
        if (steuerdatenUpdateDtos.isEmpty()) {
            steuerdatenSet.clear();
        }
        Iterator<Steuerdaten> iterator = steuerdatenSet.iterator();
        while (iterator.hasNext()) {
            Steuerdaten steuerdaten = iterator.next();
            if (steuerdatenUpdateDtos.stream()
                .noneMatch(elternUpdateDto -> steuerdaten.getId().equals(elternUpdateDto.getId()))) {
                iterator.remove();
            }
        }
        for (SteuerdatenUpdateDto steuerdatenUpdateDto : steuerdatenUpdateDtos) {
            if (steuerdatenUpdateDto.getId() != null) {
                Steuerdaten found = steuerdatenSet.stream()
                    .filter(steuerdaten -> steuerdaten.getId().equals(steuerdatenUpdateDto.getId()))
                    .findFirst()
                    .orElseThrow(
                        () -> new NotFoundException("Steuerdaten Not FOUND")
                    );
                steuerdatenSet.remove(found);
                steuerdatenSet.add(partialUpdate(steuerdatenUpdateDto, found));
            } else {
                steuerdatenSet.add(partialUpdate(steuerdatenUpdateDto, new Steuerdaten()));
            }
        }
        return steuerdatenSet;
    }
}
