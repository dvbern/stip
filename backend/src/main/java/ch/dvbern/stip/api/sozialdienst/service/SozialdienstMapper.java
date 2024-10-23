package ch.dvbern.stip.api.sozialdienst.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.sozialdienst.entity.Sozialdienst;
import ch.dvbern.stip.generated.dto.SozialdienstCreateDto;
import ch.dvbern.stip.generated.dto.SozialdienstDto;
import ch.dvbern.stip.generated.dto.SozialdienstUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class,
uses = {SozialdienstAdminMapper.class})

public interface SozialdienstMapper {
    Sozialdienst toEntity(SozialdienstCreateDto dto);
    SozialdienstDto toDto(Sozialdienst entity);
    Sozialdienst toEntity(SozialdienstUpdateDto dto);
    Sozialdienst partialUpdate(SozialdienstUpdateDto dto, @MappingTarget Sozialdienst entity);
}
