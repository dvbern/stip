package ch.dvbern.stip.api.gesuch.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.fall.service.FallMapper;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchsperioden.service.GesuchsperiodeMapper;
import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class,
    uses =
        {
            FallMapper.class,
            GesuchsperiodeMapper.class
        }
)
public interface GesuchMapper {

    @Mapping(source = "timestampMutiert", target = "aenderungsdatum")
    @Mapping(target = "bearbeiter", constant = "John Doe")
    GesuchDto toDto(Gesuch gesuch);

    @Mapping(source = "fallId", target = "fall.id")
    @Mapping(source = "gesuchsperiodeId", target = "gesuchsperiode.id")
    Gesuch toNewEntity(GesuchCreateDto gesuchCreateDto);
}
