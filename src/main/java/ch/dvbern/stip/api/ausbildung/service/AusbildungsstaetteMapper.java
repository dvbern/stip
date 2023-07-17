package ch.dvbern.stip.api.ausbildung.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteDto;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsstaette;
import org.mapstruct.*;

@Mapper(config = MappingConfig.class)
public interface AusbildungsstaetteMapper {
    Ausbildungsstaette toEntity(AusbildungsstaetteDto ausbildungsstaetteDto);

    AusbildungsstaetteDto toDto(Ausbildungsstaette ausbildungsstaette);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Ausbildungsstaette partialUpdate(Ausbildungsstaette ausbildungsstaetteDto, @MappingTarget Ausbildungsstaette ausbildungsstaette);
}