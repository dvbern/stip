package ch.dvbern.stip.api.notiz.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.notiz.entity.GesuchNotiz;
import ch.dvbern.stip.generated.dto.GesuchNotizCreateDto;
import ch.dvbern.stip.generated.dto.GesuchNotizDto;
import ch.dvbern.stip.generated.dto.GesuchNotizUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public interface GesuchNotizMapper {

    GesuchNotizDto toDto(GesuchNotiz notiz);

    GesuchNotiz toEntity(GesuchNotizDto notizDto);

    GesuchNotiz toEntity(GesuchNotizCreateDto notizCreateDto);

    GesuchNotiz partialUpdate(GesuchNotizUpdateDto notizDto, @MappingTarget GesuchNotiz notiz);
}
