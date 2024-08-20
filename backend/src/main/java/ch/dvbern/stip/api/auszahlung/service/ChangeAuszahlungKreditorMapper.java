package ch.dvbern.stip.api.auszahlung.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.generated.dto.ChangeAuszahlungKreditorDto;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class)
public interface ChangeAuszahlungKreditorMapper {
    ChangeAuszahlungKreditor toChangeAuszahlungKreditor(ChangeAuszahlungKreditorDto dto);
}
