package ch.dvbern.stip.ausbildung.service;

import ch.dvbern.stip.ausbildung.entity.AusbildungContainer;
import ch.dvbern.stip.generated.dto.AusbildungContainerDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.CDI, uses = AusbildungMapper.class)
public interface AusbildungContainerMapper {
    AusbildungContainer toEntity(AusbildungContainerDto ausbildungContainerDto);

    AusbildungContainerDto toDto(AusbildungContainer ausbildungContainer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AusbildungContainer partialUpdate(AusbildungContainerDto ausbildungContainerDto, @MappingTarget AusbildungContainer ausbildungContainer);
}