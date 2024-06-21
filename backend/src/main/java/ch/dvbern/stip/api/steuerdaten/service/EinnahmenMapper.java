package ch.dvbern.stip.api.steuerdaten.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.steuerdaten.entity.Einnahmen;
import ch.dvbern.stip.generated.dto.EinnahmenDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public interface EinnahmenMapper {
    Einnahmen toEntity(EinnahmenDto einnahmenDto);

    EinnahmenDto toDto(Einnahmen einnahmen);

    Einnahmen partialUpdate(EinnahmenDto einnahmenDto, @MappingTarget Einnahmen einnahmen);
}
