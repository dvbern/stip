package ch.dvbern.stip.api.gesuchsperioden.service;

import ch.dvbern.stip.api.common.service.EntityIdReference;
import ch.dvbern.stip.api.common.service.EntityReferenceMapper;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.generated.dto.GesuchsperiodeCreateDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeWithDatenDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MappingConfig.class)
public interface GesuchsperiodeMapper {
    @Mapping(source = "gesuchsjahrId", target = "gesuchsjahr.id")
    Gesuchsperiode toEntity(GesuchsperiodeCreateDto gesuchsperiodeDto);

    @Mapping(source = "gesuchsjahr.id", target = "gesuchsjahrId")
    GesuchsperiodeDto toDto(Gesuchsperiode gesuchsperiode);

    @Mapping(source = "gesuchsjahr.id", target = "gesuchsjahrId")
    GesuchsperiodeWithDatenDto toDatenDto(Gesuchsperiode gesuchsperiode);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "gesuchsjahrId",
        target = "gesuchsjahr",
        qualifiedBy = { EntityReferenceMapper.class, EntityIdReference.class })
    Gesuchsperiode partialUpdate(
        GesuchsperiodeUpdateDto gesuchsperiodeDto,
        @MappingTarget Gesuchsperiode gesuchsperiode
    );
}
