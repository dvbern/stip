package ch.dvbern.stip.api.eltern.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.generated.dto.ElternDto;
import ch.dvbern.stip.generated.dto.ElternUpdateDto;
import jakarta.ws.rs.NotFoundException;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public interface ElternMapper {
    Eltern toEntity(ElternDto elternDto);

    ElternDto toDto(Eltern eltern);

    Eltern partialUpdate(ElternUpdateDto elternUpdateDto, @MappingTarget Eltern eltern);

    default Set<Eltern> map(List<ElternUpdateDto> elternUpdateDtos, @MappingTarget Set<Eltern> elternSet) {
        if (elternUpdateDtos.isEmpty()) {
            elternSet.clear();
        }
        Iterator<Eltern> iterator = elternSet.iterator();
        while (iterator.hasNext()) {
            Eltern eltern = iterator.next();
            if (elternUpdateDtos.stream()
                .noneMatch(elternUpdateDto -> eltern.getId().equals(elternUpdateDto.getId()))) {
                iterator.remove();
            }
        }
        for (ElternUpdateDto elternUpdateDto : elternUpdateDtos) {
            if (elternUpdateDto.getId() != null) {
                Eltern found = elternSet.stream()
                    .filter(eltern -> eltern.getId().equals(elternUpdateDto.getId()))
                    .findFirst()
                    .orElseThrow(
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

	ElternUpdateDto toUpdateDto(Eltern eltern);
}
