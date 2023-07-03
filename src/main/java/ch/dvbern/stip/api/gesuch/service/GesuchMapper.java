package ch.dvbern.stip.api.gesuch.service;

import ch.dvbern.stip.api.adresse.service.AdresseMapper;
import ch.dvbern.stip.api.ausbildung.service.AusbildungContainerMapper;
import ch.dvbern.stip.api.ausbildung.service.AusbildungMapper;
import ch.dvbern.stip.api.familiensituation.service.FamiliensituationContainerMapper;
import ch.dvbern.stip.api.familiensituation.service.FamiliensituationMapper;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchsperioden.service.GesuchsperiodeMapper;
import ch.dvbern.stip.api.personinausbildung.service.PersonInAusbildungContainerMapper;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.fall.service.FallMapper;
import ch.dvbern.stip.api.personinausbildung.service.PersonInAusbildungMapper;
import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import org.mapstruct.*;

@Mapper(config = MappingConfig.class,
        uses =
                {
                        FallMapper.class,
                        GesuchsperiodeMapper.class,
                        AdresseMapper.class,
                        PersonInAusbildungContainerMapper.class,
                        PersonInAusbildungMapper.class,
                        FamiliensituationContainerMapper.class,
                        FamiliensituationMapper.class,
                        AusbildungContainerMapper.class,
                        AusbildungMapper.class})
public interface GesuchMapper {
    Gesuch toEntity(GesuchDto gesuchDto);

    @Mapping(source = "fallId", target = "fall.id")
    @Mapping(source = "gesuchsperiodeId", target = "gesuchsperiode.id")
    Gesuch toNewEntity(GesuchCreateDto gesuchCreateDto);

    GesuchDto toDto(Gesuch gesuch);

    /**
     * partial update mapper for the Gesuchssteller
     * @param gesuchDto
     * @param gesuch
     * @return
     */
    @Mapping(source = "ausbildung", target = "ausbildungContainer.ausbildungSB")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Gesuch partialUpdateGS(GesuchUpdateDto gesuchDto, @MappingTarget Gesuch gesuch);
}