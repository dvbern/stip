package ch.dvbern.stip.api.dokument.service;

import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.generated.dto.DokumentDto;
import ch.dvbern.stip.api.common.service.MappingConfig;
import org.mapstruct.*;

@Mapper(config = MappingConfig.class)
public interface DokumentMapper {
    Dokument toEntity(DokumentDto dokumentDto);

    DokumentDto toDto(Dokument dokument);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Dokument partialUpdate(DokumentDto dokumentDto, @MappingTarget Dokument dokument);
}