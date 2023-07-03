package ch.dvbern.stip.api.personinausbildung.service;

import ch.dvbern.stip.generated.dto.PersonInAusbildungDto;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import org.mapstruct.*;

@Mapper(config = MappingConfig.class)
public interface PersonInAusbildungMapper {
    PersonInAusbildung toEntity(PersonInAusbildungDto personInAusbildungDto);

    PersonInAusbildungDto toDto(PersonInAusbildung personInAusbildung);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PersonInAusbildung partialUpdate(PersonInAusbildungDto personInAusbildungDto, @MappingTarget PersonInAusbildung personInAusbildung);
}