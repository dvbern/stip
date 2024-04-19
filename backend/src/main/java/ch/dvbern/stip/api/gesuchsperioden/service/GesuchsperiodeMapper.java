package ch.dvbern.stip.api.gesuchsperioden.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.generated.dto.GesuchsperiodeCreateDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MappingConfig.class)
public interface GesuchsperiodeMapper {

    @Mapping(source = "gueltigAb", target = "gueltigkeit.gueltigAb")
    @Mapping(source = "gueltigBis", target = "gueltigkeit.gueltigBis")
    Gesuchsperiode toEntity(GesuchsperiodeCreateDto gesuchsperiodeDto);

    @Mapping(target = "gueltigAb", source = "gueltigkeit.gueltigAb")
    @Mapping(target = "gueltigBis", source = "gueltigkeit.gueltigBis")
    GesuchsperiodeDto toDto(Gesuchsperiode gesuchsperiode);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Gesuchsperiode partialUpdate(GesuchsperiodeDto gesuchsperiodeDto, @MappingTarget Gesuchsperiode gesuchsperiode);
}
