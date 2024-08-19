package ch.dvbern.stip.api.auszahlung.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.generated.dto.GetAuszahlungImportStatusRequestDto;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class)

public interface GetAuszahlungImportStatusRequestMapper {
    GetAuszahlungImportStatusRequest toGetAuszahlungImportStatusRequest(GetAuszahlungImportStatusRequestDto dto);
}
