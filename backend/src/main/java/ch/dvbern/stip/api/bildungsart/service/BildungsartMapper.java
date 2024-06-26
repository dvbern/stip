package ch.dvbern.stip.api.bildungsart.service;

import ch.dvbern.stip.api.bildungsart.entity.Bildungsart;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.generated.dto.BildungsartDto;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class)
public abstract class BildungsartMapper {
	abstract BildungsartDto toDto(Bildungsart bildungsart);
}
