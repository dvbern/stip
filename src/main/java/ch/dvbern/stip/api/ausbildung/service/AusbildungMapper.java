package ch.dvbern.stip.api.ausbildung.service;

import ch.dvbern.stip.generated.dto.AusbildungDto;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.generated.dto.AusbildungUpdateDto;
import org.mapstruct.*;

@Mapper(config = MappingConfig.class)
public interface AusbildungMapper {
    Ausbildung toEntity(AusbildungDto ausbildungDto);

    AusbildungDto toDto(Ausbildung ausbildung);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Ausbildung partialUpdate(AusbildungDto ausbildungDto, @MappingTarget Ausbildung ausbildung);

    @Mapping(source = "ausbildungsgangId", target = "ausbildungsgang.id")
    @Mapping(source = "ausbildungstaetteId", target = "ausbildungstaette.id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Ausbildung partialUpdate(AusbildungUpdateDto ausbildungDto, @MappingTarget Ausbildung ausbildung);
}