package ch.dvbern.stip.api.steuerdaten.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.steuerdaten.entity.Ausgaben;
import ch.dvbern.stip.generated.dto.AusgabenDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public interface AusgabenMapper {
    Ausgaben toEntity(AusgabenDto ausgabenDto);
    AusgabenDto toDto(Ausgaben ausgaben);

    Ausgaben partialUpdate(AusgabenDto ausgabenDto, @MappingTarget Ausgaben ausgaben);
}
