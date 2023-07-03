package ch.dvbern.stip.api.ausbildung.service;

import ch.dvbern.stip.generated.dto.AusbildungContainerDto;
import ch.dvbern.stip.api.ausbildung.entity.AusbildungContainer;
import ch.dvbern.stip.api.common.service.MappingConfig;
import org.mapstruct.*;

@Mapper(config = MappingConfig.class, uses = AusbildungMapper.class)
public interface AusbildungContainerMapper {
    AusbildungContainer toEntity(AusbildungContainerDto ausbildungContainerDto);

    AusbildungContainerDto toDto(AusbildungContainer ausbildungContainer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AusbildungContainer partialUpdate(AusbildungContainerDto ausbildungContainerDto, @MappingTarget AusbildungContainer ausbildungContainer);
}