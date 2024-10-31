package ch.dvbern.stip.api.familiensituation.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.generated.dto.FamiliensituationDto;
import ch.dvbern.stip.generated.dto.FamiliensituationUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public interface FamiliensituationMapper {
    Familiensituation toEntity(FamiliensituationDto familiensituationDto);

    FamiliensituationDto toDto(Familiensituation familiensituation);

    Familiensituation partialUpdate(
        FamiliensituationUpdateDto familiensituationUpdateDto,
        @MappingTarget Familiensituation familiensituation);

	FamiliensituationUpdateDto toUpdateDto(Familiensituation familiensituation);
}
