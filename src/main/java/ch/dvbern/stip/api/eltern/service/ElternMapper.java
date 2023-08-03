package ch.dvbern.stip.api.eltern.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.generated.dto.ElternDto;
import ch.dvbern.stip.generated.dto.ElternUpdateDto;
import jakarta.ws.rs.NotFoundException;
import org.mapstruct.*;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Mapper(config = MappingConfig.class)
public interface ElternMapper {
    Eltern toEntity(ElternDto elternDto);

    ElternDto toDto(Eltern eltern);

    Eltern partialUpdate(ElternUpdateDto elternUpdateDto, @MappingTarget Eltern eltern);

    default Set<Eltern> map(List<ElternUpdateDto> elternUpdateDtos, @MappingTarget Set<Eltern> elternSet) {
        if(elternUpdateDtos.isEmpty()) elternSet.clear();
        for (ElternUpdateDto elternUpdateDto : elternUpdateDtos) {
            if (elternUpdateDto.getId() != null) {
                Eltern found = elternSet.stream().filter(eltern -> eltern.getId().equals(elternUpdateDto.getId())).findFirst().orElseThrow(
                        () -> new NotFoundException("Eltern Not FOUND")
                );
                elternSet.remove(found);
                elternSet.add(partialUpdate(elternUpdateDto, found));
            } else {
                elternSet.add(partialUpdate(elternUpdateDto, new Eltern()));
            }
        }
        return elternSet;
    }
}