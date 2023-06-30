package ch.dvbern.stip.gesuch.service;

import ch.dvbern.stip.ausbildung.service.AusbildungContainerMapper;
import ch.dvbern.stip.fall.service.FallMapper;
import ch.dvbern.stip.familiensituation.service.FamiliensituationContainerMapper;
import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import ch.dvbern.stip.gesuch.entity.Gesuch;
import ch.dvbern.stip.gesuchsperioden.service.GesuchsperiodeMapper;
import ch.dvbern.stip.personinausbildung.service.PersonInAusbildungContainerMapper;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.CDI,
        uses =
                {
                        FallMapper.class,
                        GesuchsperiodeMapper.class,
                        PersonInAusbildungContainerMapper.class,
                        FamiliensituationContainerMapper.class,
                        AusbildungContainerMapper.class})
public interface GesuchMapper {
    Gesuch toEntity(GesuchDto gesuchDto);

    Gesuch toNewEntity(GesuchCreateDto gesuchCreateDto);

    Gesuch toUpdateEntity(GesuchUpdateDto gesuchUpdateDto);


    GesuchDto toDto(Gesuch gesuch);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Gesuch partialUpdate(GesuchDto gesuchDto, @MappingTarget Gesuch gesuch);
}