package ch.dvbern.stip.familiensituation.service;

import ch.dvbern.stip.familiensituation.entity.FamiliensituationContainer;
import ch.dvbern.stip.generated.dto.FamiliensituationContainerDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.CDI, uses = FamiliensituationMapper.class)
public interface FamiliensituationContainerMapper {
    FamiliensituationContainer toEntity(FamiliensituationContainerDto familiensituationContainerDto);

    FamiliensituationContainerDto toDto(FamiliensituationContainer familiensituationContainer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    FamiliensituationContainer partialUpdate(FamiliensituationContainerDto familiensituationContainerDto, @MappingTarget FamiliensituationContainer familiensituationContainer);
}