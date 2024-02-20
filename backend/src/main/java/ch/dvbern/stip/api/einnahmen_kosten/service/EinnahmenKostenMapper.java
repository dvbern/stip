package ch.dvbern.stip.api.einnahmen_kosten.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.generated.dto.EinnahmenKostenDto;
import ch.dvbern.stip.generated.dto.EinnahmenKostenUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public interface EinnahmenKostenMapper {
    EinnahmenKosten toEntity(EinnahmenKostenDto einnahmenKostenDto);

    EinnahmenKostenDto toDto(EinnahmenKosten einnahmenKosten);

    EinnahmenKosten partialUpdate(
        EinnahmenKostenUpdateDto einnahmenKostenUpdateDto,
        @MappingTarget EinnahmenKosten einnahmenKosten);
}
