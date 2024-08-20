package ch.dvbern.stip.api.bildungskategorie.service;

import ch.dvbern.stip.api.bildungskategorie.entity.Bildungskategorie;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.generated.dto.BildungskategorieDto;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class)
public interface BildungskategorieMapper {
    BildungskategorieDto toDto(Bildungskategorie bildungskategorie);
}
