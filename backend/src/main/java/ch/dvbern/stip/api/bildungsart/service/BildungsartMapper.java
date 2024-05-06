package ch.dvbern.stip.api.bildungsart.service;

import ch.dvbern.stip.api.bildungsart.entity.Bildungsart;
import ch.dvbern.stip.api.bildungsart.type.Bildungsstufe;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.generated.dto.BildungsartDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MappingConfig.class)
public abstract class BildungsartMapper {

	@Mapping(target = "bildungsstufe", source = ".", qualifiedByName = "getBildungsstufe")
	abstract Bildungsart toEntity(BildungsartDto bildungsartDto);

	abstract BildungsartDto toDto(Bildungsart bildungsart);

	@Named("getBildungsstufe")
	Bildungsstufe getBildungsstufe(BildungsartDto bildungsartDto) {
		return Bildungsstufe.valueOf(bildungsartDto.getBildungsstufe());
	}
}
