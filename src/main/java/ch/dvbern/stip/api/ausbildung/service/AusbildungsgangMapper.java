package ch.dvbern.stip.api.ausbildung.service;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.generated.dto.AusbildungsgangDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MappingConfig.class)
public interface AusbildungsgangMapper {
	Ausbildungsgang toEntity(AusbildungsgangDto ausbildungsgangDto);

	AusbildungsgangDto toDto(Ausbildungsgang ausbildungsgang);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	Ausbildungsgang partialUpdate(
			AusbildungsgangDto ausbildungsgangDto,
			@MappingTarget Ausbildungsgang ausbildungsgang);
}