package ch.dvbern.stip.api.familiensituation.service;

import ch.dvbern.stip.generated.dto.FamiliensituationContainerDto;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.familiensituation.entity.FamiliensituationContainer;
import org.mapstruct.*;

@Mapper(config = MappingConfig.class)
public interface FamiliensituationContainerMapper {
    FamiliensituationContainer toEntity(FamiliensituationContainerDto familiensituationContainerDto);

    FamiliensituationContainerDto toDto(FamiliensituationContainer familiensituationContainer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    FamiliensituationContainer partialUpdate(FamiliensituationContainerDto familiensituationContainerDto, @MappingTarget FamiliensituationContainer familiensituationContainer);
}