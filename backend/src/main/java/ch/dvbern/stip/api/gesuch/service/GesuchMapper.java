package ch.dvbern.stip.api.gesuch.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.fall.service.FallMapper;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchsperioden.service.GesuchsperiodeMapper;
import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MappingConfig.class,
    uses =
        {
            FallMapper.class,
            GesuchsperiodeMapper.class
        }
)
public abstract class GesuchMapper {
    @Mapping(source = "timestampMutiert", target = "aenderungsdatum")
    @Mapping(target = "bearbeiter", source = ".", qualifiedByName = "getFullNameOfSachbearbeiter")
    @Mapping(target = "fallId", source = "ausbildung.fall.id")
    @Mapping(target = "fallNummer", source = "ausbildung.fall.fallNummer")
    public abstract GesuchDto toDto(Gesuch gesuch);

    @Mapping(source = "ausbildungId", target = "ausbildung.id")
//    @Mapping(source = "gesuchsperiodeId", target = "gesuchsperiode.id")
    public abstract Gesuch toNewEntity(GesuchCreateDto gesuchCreateDto);

    @Mapping(source = "timestampMutiert", target = "aenderungsdatum")
    @Mapping(target = "bearbeiter", source = ".", qualifiedByName = "getFullNameOfSachbearbeiter")
    public abstract GesuchWithChangesDto toWithChangesDto(Gesuch gesuch);

    @Named("getFullNameOfSachbearbeiter")
    String getFullNameOfSachbearbeiter(Gesuch gesuch) {
        final var zuordnung = gesuch.getAusbildung().getFall().getSachbearbeiterZuordnung();
        if (zuordnung == null) {
            return "";
        }

        return zuordnung.getSachbearbeiter().getFullName();
    }
}
