package ch.dvbern.stip.api.kind.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.generated.dto.KindDto;
import ch.dvbern.stip.generated.dto.KindUpdateDto;
import jakarta.ws.rs.NotFoundException;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public interface KindMapper {
    Kind toEntity(KindDto kindDto);

    KindDto toDto(Kind kind);

    Kind partialUpdate(KindUpdateDto kindUpdateDto, @MappingTarget Kind kind);

    default Set<Kind> map(List<KindUpdateDto> kindUpdateDtos, @MappingTarget Set<Kind> kinder) {
        if (kindUpdateDtos.isEmpty()) {
            kinder.clear();
        }
        Iterator<Kind> iterator = kinder.iterator();
        while (iterator.hasNext()) {
            Kind kind = iterator.next();
            if (kindUpdateDtos.stream().noneMatch(kindUpdateDto -> kind.getId().equals(kindUpdateDto.getId()))) {
                iterator.remove();
            }
        }
        for (KindUpdateDto kindUpdateDto : kindUpdateDtos) {
            if (kindUpdateDto.getId() != null) {
                Kind found =
                    kinder.stream().filter(kind -> kind.getId().equals(kindUpdateDto.getId())).findFirst().orElseThrow(
                        () -> new NotFoundException("Kind Not FOUND")
                    );
                kinder.remove(found);
                kinder.add(partialUpdate(kindUpdateDto, found));
            } else {
                kinder.add(partialUpdate(kindUpdateDto, new Kind()));
            }
        }
        return kinder;
    }

	KindUpdateDto toUpdateDto(Kind kind);
}
