package ch.dvbern.stip.ausbildung.service;

import ch.dvbern.stip.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.generated.dto.AusbildungDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.CDI)
public interface AusbildungMapper {
    Ausbildung toEntity(AusbildungDto ausbildungDto);

    AusbildungDto toDto(Ausbildung ausbildung);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Ausbildung partialUpdate(AusbildungDto ausbildungDto, @MappingTarget Ausbildung ausbildung);
}