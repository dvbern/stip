package ch.dvbern.stip.api.einnahmen_kosten.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.generated.dto.EinnahmenKostenDto;
import ch.dvbern.stip.generated.dto.EinnahmenKostenUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public abstract class EinnahmenKostenMapper {
    public abstract EinnahmenKosten toEntity(EinnahmenKostenDto einnahmenKostenDto);

    public abstract EinnahmenKostenDto toDto(EinnahmenKosten einnahmenKosten);

    public abstract EinnahmenKosten partialUpdate(
        EinnahmenKostenUpdateDto einnahmenKostenUpdateDto,
        @MappingTarget EinnahmenKosten einnahmenKosten);
}
