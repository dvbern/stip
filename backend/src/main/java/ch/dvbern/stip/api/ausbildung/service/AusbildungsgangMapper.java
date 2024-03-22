package ch.dvbern.stip.api.ausbildung.service;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.generated.dto.AusbildungsgangCreateDto;
import ch.dvbern.stip.generated.dto.AusbildungsgangDto;
import ch.dvbern.stip.generated.dto.AusbildungsgangUpdateDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MappingConfig.class)
public interface AusbildungsgangMapper {

    @Mapping(source = "ausbildungsstaetteId", target = "ausbildungsstaette.id")
    Ausbildungsgang toEntity(AusbildungsgangCreateDto ausbildungsgangDto);

    @Mapping(source = "ausbildungsstaette.id", target = "ausbildungsstaetteId")
    AusbildungsgangDto toDto(Ausbildungsgang ausbildungsgang);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Ausbildungsgang partialUpdate(
        AusbildungsgangUpdateDto ausbildungsgangDto,
        @MappingTarget Ausbildungsgang ausbildungsgang);
}
