package ch.dvbern.stip.fall.service;

import ch.dvbern.stip.fall.entity.Fall;
import ch.dvbern.stip.generated.dto.FallDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.CDI)
public interface FallMapper {

    Fall toEntity(FallDto fallDto);

    FallDto toDto(Fall fall);
}
