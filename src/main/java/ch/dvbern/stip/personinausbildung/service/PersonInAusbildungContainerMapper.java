package ch.dvbern.stip.personinausbildung.service;

import ch.dvbern.stip.generated.dto.PersonInAusbildungContainerDto;
import ch.dvbern.stip.personinausbildung.model.PersonInAusbildungContainer;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.CDI, uses = PersonInAusbildungMapper.class)
public interface PersonInAusbildungContainerMapper {
    PersonInAusbildungContainer toEntity(PersonInAusbildungContainerDto personInAusbildungContainerDto);

    PersonInAusbildungContainerDto toDto(PersonInAusbildungContainer personInAusbildungContainer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PersonInAusbildungContainer partialUpdate(PersonInAusbildungContainerDto personInAusbildungContainerDto, @MappingTarget PersonInAusbildungContainer personInAusbildungContainer);
}