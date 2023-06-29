package ch.dvbern.stip.ausbildung.service;

import ch.dvbern.stip.ausbildung.entity.Ausbildungstaette;
import ch.dvbern.stip.generated.dto.AusbildungDto;
import ch.dvbern.stip.generated.dto.AusbildungstaetteDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.CDI)
public interface AusbildungstaetteMapper {
    Ausbildungstaette toEntity(AusbildungstaetteDto ausbildungstaetteDto);

    AusbildungstaetteDto toDto(Ausbildungstaette ausbildungstaette);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Ausbildungstaette partialUpdate(Ausbildungstaette ausbildungstaetteDto, @MappingTarget Ausbildungstaette ausbildungstaette);
}