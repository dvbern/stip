package ch.dvbern.stip.api.familiensituation.service;

import ch.dvbern.stip.generated.dto.FamiliensituationDto;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import org.mapstruct.*;

@Mapper(config = MappingConfig.class)
public interface FamiliensituationMapper {
    Familiensituation toEntity(FamiliensituationDto familiensituationDto);

    FamiliensituationDto toDto(Familiensituation familiensituation);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Familiensituation partialUpdate(FamiliensituationDto familiensituationDto, @MappingTarget Familiensituation familiensituation);
}