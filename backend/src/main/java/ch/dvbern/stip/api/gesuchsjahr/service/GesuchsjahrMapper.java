package ch.dvbern.stip.api.gesuchsjahr.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.gesuchsjahr.entity.Gesuchsjahr;
import ch.dvbern.stip.generated.dto.GesuchsjahrCreateDto;
import ch.dvbern.stip.generated.dto.GesuchsjahrDto;
import ch.dvbern.stip.generated.dto.GesuchsjahrUpdateDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MappingConfig.class)
public interface GesuchsjahrMapper {

	@Mapping(source = "ausbildungsjahrStart", target = "ausbildungsjahr.gueltigAb")
	@Mapping(source = "ausbildungsjahrEnde", target = "ausbildungsjahr.gueltigBis")
	Gesuchsjahr toEntity(GesuchsjahrCreateDto gesuchsjahrDto);

	@Mapping(source = "ausbildungsjahrStart", target = "ausbildungsjahr.gueltigAb")
	@Mapping(source = "ausbildungsjahrEnde", target = "ausbildungsjahr.gueltigBis")
	Gesuchsjahr toEntity(GesuchsjahrUpdateDto gesuchsjahrDto);

	@Mapping(source = "ausbildungsjahr.gueltigAb", target = "ausbildungsjahrStart")
	@Mapping(source = "ausbildungsjahr.gueltigBis", target = "ausbildungsjahrEnde")
	GesuchsjahrDto toDto(Gesuchsjahr gesuchsjahr);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	Gesuchsjahr partialUpdate(GesuchsjahrUpdateDto gesuchsjahrDto, @MappingTarget Gesuchsjahr gesuchsjahr);
}
