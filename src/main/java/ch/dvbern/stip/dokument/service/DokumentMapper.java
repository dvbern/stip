package ch.dvbern.stip.dokument.service;

import ch.dvbern.stip.dokument.entity.Dokument;
import ch.dvbern.stip.generated.dto.DokumentDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.CDI)
public interface DokumentMapper {
    Dokument toEntity(DokumentDto dokumentDto);

    DokumentDto toDto(Dokument dokument);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Dokument partialUpdate(DokumentDto dokumentDto, @MappingTarget Dokument dokument);
}