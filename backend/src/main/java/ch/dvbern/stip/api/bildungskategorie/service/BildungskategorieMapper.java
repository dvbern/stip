package ch.dvbern.stip.api.bildungskategorie.service;

import ch.dvbern.stip.api.bildungskategorie.entity.Bildungskategorie;
import ch.dvbern.stip.api.bildungskategorie.type.Bildungsstufe;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.generated.dto.BildungskategorieDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public abstract class BildungskategorieMapper {
	abstract BildungskategorieDto toDto(Bildungskategorie bildungskategorie);

    @AfterMapping
    public void afterMapping(
        Bildungskategorie bildungskategorie,
        @MappingTarget BildungskategorieDto bildungskategorieDto
    ) {
        bildungskategorieDto.setBildungsstufe(Bildungsstufe.SEKUNDAR_2);
        if (bildungskategorie.getBfs() >= 7) {
            bildungskategorieDto.setBildungsstufe(Bildungsstufe.TERTIAER);
        }
    }
}
