package ch.dvbern.stip.api.gesuch.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.generated.dto.StatusprotokollEntryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public interface StatusprotokollMapper {
    @Mapping(target = "timestamp", source = "timestampMutiert")
    @Mapping(target = "status", source = "gesuchStatus")
    @Mapping(target = "benutzer", source = "userMutiert")
    @Mapping(target = "kommentar", source = "comment")
    StatusprotokollEntryDto toDto(Gesuch gesuch);
}
