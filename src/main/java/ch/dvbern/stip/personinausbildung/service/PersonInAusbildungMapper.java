package ch.dvbern.stip.personinausbildung.service;

import ch.dvbern.stip.generated.dto.PersonInAusbildungDto;
import ch.dvbern.stip.personinausbildung.model.PersonInAusbildung;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.CDI)
public interface PersonInAusbildungMapper {
    PersonInAusbildung toEntity(PersonInAusbildungDto personInAusbildungDto);

    PersonInAusbildungDto toDto(PersonInAusbildung personInAusbildung);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PersonInAusbildung partialUpdate(PersonInAusbildungDto personInAusbildungDto, @MappingTarget PersonInAusbildung personInAusbildung);
}