package ch.dvbern.stip.familiensituation.service;

import ch.dvbern.stip.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.generated.dto.FamiliensituationDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.CDI)
public interface FamiliensituationMapper {
    Familiensituation toEntity(FamiliensituationDto familiensituationDto);

    FamiliensituationDto toDto(Familiensituation familiensituation);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Familiensituation partialUpdate(FamiliensituationDto familiensituationDto, @MappingTarget Familiensituation familiensituation);
}