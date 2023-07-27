package ch.dvbern.stip.api.benutzer.service;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.generated.dto.BenutzerDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MappingConfig.class)
public interface BenutzerMapper {
	Benutzer toEntity(BenutzerDto benutzerDto);

	BenutzerDto toDto(Benutzer benutzer);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	Benutzer partialUpdate(
			BenutzerDto benutzerDto,
			@MappingTarget Benutzer benutzer);
}