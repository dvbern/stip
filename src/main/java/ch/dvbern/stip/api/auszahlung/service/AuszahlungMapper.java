package ch.dvbern.stip.api.auszahlung.service;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.generated.dto.AuszahlungDto;
import ch.dvbern.stip.generated.dto.AuszahlungUpdateDto;
import org.mapstruct.*;

@Mapper(config = MappingConfig.class)
public interface AuszahlungMapper {
    Auszahlung toEntity(AuszahlungDto auszahlungDto);

    AuszahlungDto toDto(Auszahlung auszahlung);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Auszahlung partialUpdate(AuszahlungUpdateDto auszahlungUpdateDto, @MappingTarget Auszahlung auszahlung);
}