package ch.dvbern.stip.test.mapper;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GesuchTestMapper {

    GesuchTestMapper mapper = Mappers.getMapper(GesuchTestMapper.class);

    GesuchUpdateDto toGesuchUpdateDto(Gesuch gesuch);
}
