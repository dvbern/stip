package ch.dvbern.stip.api.auszahlung.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.generated.dto.CreateAuszahlungDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.time.LocalDate;

@Mapper(config = MappingConfig.class)
public interface CreateAuszahlungMapper {
    CreateAuszahlung toCreateAuszahlung(CreateAuszahlungDto dto);
    @AfterMapping
    default void afterMapping(@MappingTarget CreateAuszahlung createAuszahlung){
        createAuszahlung.setDocDate(LocalDate.now().toString());
        createAuszahlung.setPstingDate(LocalDate.now().toString());
    }
}
