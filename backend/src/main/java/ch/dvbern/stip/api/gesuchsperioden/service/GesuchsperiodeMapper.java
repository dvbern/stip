package ch.dvbern.stip.api.gesuchsperioden.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.generated.dto.GesuchsperiodeCreateDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeWithDatenDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MappingConfig.class)
public interface GesuchsperiodeMapper {
    Gesuchsperiode toEntity(GesuchsperiodeCreateDto gesuchsperiodeDto);

    GesuchsperiodeDto toDto(Gesuchsperiode gesuchsperiode);

    GesuchsperiodeWithDatenDto toDatenDto(Gesuchsperiode gesuchsperiode);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Gesuchsperiode partialUpdate(
        GesuchsperiodeUpdateDto gesuchsperiodeDto,
        @MappingTarget Gesuchsperiode gesuchsperiode
    );
}
