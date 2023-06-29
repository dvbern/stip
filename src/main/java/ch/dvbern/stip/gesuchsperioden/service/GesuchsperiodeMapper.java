package ch.dvbern.stip.gesuchsperioden.service;

import ch.dvbern.stip.generated.dto.GesuchsperiodeCreateDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeDto;
import ch.dvbern.stip.gesuchsperioden.entity.Gesuchsperiode;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.CDI)
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