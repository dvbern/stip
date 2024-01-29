package ch.dvbern.stip.api.benutzereinstellungen.service;

import ch.dvbern.stip.api.benutzereinstellungen.entity.Benutzereinstellungen;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.generated.dto.BenutzerUpdateDto;
import ch.dvbern.stip.generated.dto.BenutzereinstellungenDto;
import org.mapstruct.*;

@Mapper(config = MappingConfig.class)
public interface BenutzereinstellungenMapper {
    Benutzereinstellungen toEntity(BenutzereinstellungenDto benutzereinstellungenDto);

    BenutzereinstellungenDto toDto(Benutzereinstellungen benutzereinstellungen);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Benutzereinstellungen partialUpdate(BenutzerUpdateDto benutzereinstellungenDto, @MappingTarget Benutzereinstellungen benutzereinstellungen);
}
