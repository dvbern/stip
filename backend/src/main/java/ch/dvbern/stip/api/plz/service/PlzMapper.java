package ch.dvbern.stip.api.plz.service;


import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.plz.entity.Plz;
import ch.dvbern.stip.generated.dto.PlzDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public interface PlzMapper {
    Plz toEntity(PlzDto plzDto);

    PlzDto toDto(Plz plz);

    Plz partialUpdate(PlzDto plzDto, @MappingTarget Plz plz);
}
