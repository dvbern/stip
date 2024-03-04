package ch.dvbern.stip.api.adresse.service;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.generated.dto.AdresseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public interface AdresseMapper {
    Adresse toEntity(AdresseDto adresseDto);

    AdresseDto toDto(Adresse adresse);

    Adresse partialUpdate(AdresseDto adresseDto, @MappingTarget Adresse adresse);
}
