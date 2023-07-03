package ch.dvbern.stip.api.personinausbildung.service;

import ch.dvbern.stip.generated.dto.PersonInAusbildungContainerDto;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildungContainer;
import org.mapstruct.*;

@Mapper(config = MappingConfig.class)
public interface PersonInAusbildungContainerMapper {
    PersonInAusbildungContainer toEntity(PersonInAusbildungContainerDto personInAusbildungContainerDto);

    PersonInAusbildungContainerDto toDto(PersonInAusbildungContainer personInAusbildungContainer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PersonInAusbildungContainer partialUpdate(PersonInAusbildungContainerDto personInAusbildungContainerDto, @MappingTarget PersonInAusbildungContainer personInAusbildungContainer);
}