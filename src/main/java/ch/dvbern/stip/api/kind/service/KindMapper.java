package ch.dvbern.stip.api.kind.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.generated.dto.KindDto;
import ch.dvbern.stip.generated.dto.KindUpdateDto;
import jakarta.ws.rs.NotFoundException;
import org.mapstruct.*;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Mapper(config = MappingConfig.class)
public interface KindMapper {
    Kind toEntity(KindDto kindDto);

    KindDto toDto(Kind kind);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Kind partialUpdate(KindUpdateDto kindUpdateDto, @MappingTarget Kind kind);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default Set<Kind> map(List<KindUpdateDto> kindUpdateDtos, @MappingTarget Set<Kind> kinder) {
        if(kindUpdateDtos.size() == 0) return new LinkedHashSet<Kind>();
        for (KindUpdateDto kindUpdateDto : kindUpdateDtos) {
            if (kindUpdateDto.getId() != null) {
                Kind found = kinder.stream().filter(kind -> kind.getId().equals(kindUpdateDto.getId())).findFirst().orElseThrow(
                        () -> new NotFoundException("Kind Not FOUND")
                );
                kinder.remove(found);
                kinder.add(partialUpdate(kindUpdateDto, found));
            }
            else {
                kinder.add(partialUpdate(kindUpdateDto, new Kind()));
            }
        }
        return kinder;
    }
}