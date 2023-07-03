package ch.dvbern.stip.api.fall.service;

import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.generated.dto.FallDto;
import ch.dvbern.stip.api.common.service.MappingConfig;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class)
public interface FallMapper {

    Fall toEntity(FallDto fallDto);

    FallDto toDto(Fall fall);
}
