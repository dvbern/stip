package ch.dvbern.stip.api.sozialdienst.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.sozialdienst.entity.SozialdienstAdmin;
import ch.dvbern.stip.generated.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public interface SozialdienstAdminMapper {
    SozialdienstAdminDto toDto(SozialdienstAdmin admin);

    SozialdienstAdmin toEntity(SozialdienstAdminCreateDto dto);

    SozialdienstAdmin toEntity(SozialdienstAdminUpdateDto dto);

    SozialdienstAdmin partialUpdate(SozialdienstAdminUpdateDto dto, @MappingTarget SozialdienstAdmin admin);
}
