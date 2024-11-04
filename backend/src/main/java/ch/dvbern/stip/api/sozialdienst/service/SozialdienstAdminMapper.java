package ch.dvbern.stip.api.sozialdienst.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.benutzer.entity.SozialdienstAdmin;
import ch.dvbern.stip.generated.dto.SozialdienstAdminCreateDto;
import ch.dvbern.stip.generated.dto.SozialdienstAdminDto;
import ch.dvbern.stip.generated.dto.SozialdienstAdminUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config= MappingConfig.class)
public interface SozialdienstAdminMapper {
    SozialdienstAdmin toEntity(SozialdienstAdminCreateDto createDto);
    SozialdienstAdmin toEntity(SozialdienstAdminUpdateDto updateDto);
    SozialdienstAdminDto toDto(SozialdienstAdmin sozialdienstAdmin);
    SozialdienstAdmin partialUpdate(SozialdienstAdminUpdateDto updateDto, @MappingTarget SozialdienstAdmin entity);
}
