package ch.dvbern.stip.api.dokument.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.generated.dto.GesuchDokumentDto;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class, uses = DokumentMapper.class)
public interface GesuchDokumentMapper {
    GesuchDokumentDto toDto(GesuchDokument gesuchDokument);
}
