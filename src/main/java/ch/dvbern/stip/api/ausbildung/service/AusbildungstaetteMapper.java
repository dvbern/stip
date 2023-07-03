package ch.dvbern.stip.api.ausbildung.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.generated.dto.AusbildungstaetteDto;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungstaette;
import org.mapstruct.*;

@Mapper(config = MappingConfig.class)
public interface AusbildungstaetteMapper {
    Ausbildungstaette toEntity(AusbildungstaetteDto ausbildungstaetteDto);

    AusbildungstaetteDto toDto(Ausbildungstaette ausbildungstaette);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Ausbildungstaette partialUpdate(Ausbildungstaette ausbildungstaetteDto, @MappingTarget Ausbildungstaette ausbildungstaette);
}