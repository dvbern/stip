package ch.dvbern.stip.api.personinausbildung.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.generated.dto.PersonInAusbildungDto;
import ch.dvbern.stip.generated.dto.PersonInAusbildungUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public interface PersonInAusbildungMapper {
    PersonInAusbildung toEntity(PersonInAusbildungDto personInAusbildungDto);

    PersonInAusbildungDto toDto(PersonInAusbildung personInAusbildung);

    PersonInAusbildung partialUpdate(
        PersonInAusbildungUpdateDto personInAusbildungUpdateDto,
        @MappingTarget PersonInAusbildung personInAusbildung);
}
