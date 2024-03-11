package ch.dvbern.stip.api.ausbildung.service;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsstaette;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteCreateDto;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteDto;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteUpdateDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MappingConfig.class,
    uses = AusbildungsgangMapper.class)
public interface AusbildungsstaetteMapper {
    Ausbildungsstaette toEntity(AusbildungsstaetteCreateDto ausbildungsstaetteDto);

    AusbildungsstaetteDto toDto(Ausbildungsstaette ausbildungsstaette);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Ausbildungsstaette partialUpdate(
        AusbildungsstaetteUpdateDto ausbildungsstaetteDto,
        @MappingTarget Ausbildungsstaette ausbildungsstaette);
}
