package ch.dvbern.stip.api.bildungskategorie.service;

import ch.dvbern.stip.api.bildungskategorie.entity.Bildungskategorie;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.generated.dto.BildungskategorieDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public abstract class BildungskategorieMapper {
    @Mapping(target = "bildungsstufe", expression = "java(bildungskategorie.getBildungsstufe())")
	abstract BildungskategorieDto toDto(Bildungskategorie bildungskategorie);
}
