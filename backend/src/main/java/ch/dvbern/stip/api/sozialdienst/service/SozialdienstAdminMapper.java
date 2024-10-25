package ch.dvbern.stip.api.sozialdienst.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.generated.dto.SozialdienstAdminCreateDto;
import ch.dvbern.stip.generated.dto.SozialdienstAdminDto;
import ch.dvbern.stip.generated.dto.SozialdienstAdminUpdateDto;
import org.mapstruct.Mapper;

@Mapper(config= MappingConfig.class)
public interface SozialdienstAdminMapper {
    SozialdienstAdmin toSozialdienstAdmin(SozialdienstAdminCreateDto createDto);
    SozialdienstAdmin toSozialdienstAdmin(SozialdienstAdminUpdateDto updateDto);
    SozialdienstAdminDto toDto(SozialdienstAdmin sozialdienstAdmin);
}
