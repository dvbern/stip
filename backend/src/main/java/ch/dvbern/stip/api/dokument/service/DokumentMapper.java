package ch.dvbern.stip.api.dokument.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.generated.dto.DokumentDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MappingConfig.class)
public interface DokumentMapper {
    Dokument toEntity(DokumentDto dokumentDto);

    DokumentDto toDto(Dokument dokument);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Dokument partialUpdate(DokumentDto dokumentDto, @MappingTarget Dokument dokument);
}
