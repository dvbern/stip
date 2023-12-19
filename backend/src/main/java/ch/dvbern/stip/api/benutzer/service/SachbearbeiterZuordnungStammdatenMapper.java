package ch.dvbern.stip.api.benutzer.service;

import ch.dvbern.stip.api.benutzer.entity.SachbearbeiterZuordnungStammdaten;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.generated.dto.SachbearbeiterZuordnungStammdatenDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MappingConfig.class)
public interface SachbearbeiterZuordnungStammdatenMapper {
	SachbearbeiterZuordnungStammdaten toEntity(SachbearbeiterZuordnungStammdatenDto sachbearbeiterZuordnungStammdatenDto);

	SachbearbeiterZuordnungStammdatenDto toDto(SachbearbeiterZuordnungStammdaten sachbearbeiterZuordnungStammdaten);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	SachbearbeiterZuordnungStammdaten partialUpdate(
			SachbearbeiterZuordnungStammdatenDto sachbearbeiterZuordnungStammdatenDto,
			@MappingTarget SachbearbeiterZuordnungStammdaten sachbearbeiterZuordnungStammdaten);
}