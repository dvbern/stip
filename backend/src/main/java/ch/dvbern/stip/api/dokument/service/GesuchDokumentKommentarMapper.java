package ch.dvbern.stip.api.dokument.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.dokument.entity.GesuchDokumentKommentar;
import ch.dvbern.stip.generated.dto.GesuchDokumentKommentarDto;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class)
public interface GesuchDokumentKommentarMapper {
    GesuchDokumentKommentarDto toDto(GesuchDokumentKommentar kommentar);
    GesuchDokumentKommentar toEntity(GesuchDokumentKommentarDto kommentarDto);
}
