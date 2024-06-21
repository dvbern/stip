package ch.dvbern.stip.api.steuerdaten.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.generated.dto.SteuerdatenDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class, uses = {
    EinnahmenMapper.class,
    AusgabenMapper.class
})
public interface SteuerdatenMapper {
    Steuerdaten toEntity(SteuerdatenDto steuerdatenDto);

    SteuerdatenDto toDto(Steuerdaten steuerdaten);

    Steuerdaten partialUpdate(SteuerdatenDto steuerdatenDto, @MappingTarget Steuerdaten steuerdaten);

}
