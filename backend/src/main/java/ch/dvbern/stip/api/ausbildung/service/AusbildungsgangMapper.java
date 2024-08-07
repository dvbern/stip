package ch.dvbern.stip.api.ausbildung.service;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.bildungskategorie.service.BildungskategorieMapper;
import ch.dvbern.stip.api.common.service.EntityIdReference;
import ch.dvbern.stip.api.common.service.EntityReferenceMapper;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.generated.dto.AusbildungsgangCreateDto;
import ch.dvbern.stip.generated.dto.AusbildungsgangDto;
import ch.dvbern.stip.generated.dto.AusbildungsgangUpdateDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MappingConfig.class, uses = BildungskategorieMapper.class)
public interface AusbildungsgangMapper {
    @Mapping(
        target = "bildungskategorie",
        source = "bildungskategorieId",
        qualifiedBy = { EntityReferenceMapper.class, EntityIdReference.class }
    )
    Ausbildungsgang toEntity(AusbildungsgangCreateDto ausbildungsgangDto);

    @Mapping(
        target = "ausbildungsstaetteId",
        source = "ausbildungsstaette.id"
    )
    AusbildungsgangDto toDto(Ausbildungsgang ausbildungsgang);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(
        target = "bildungskategorie",
        source = "bildungskategorieId",
        qualifiedBy = { EntityReferenceMapper.class, EntityIdReference.class }
    )
    Ausbildungsgang partialUpdate(
        AusbildungsgangUpdateDto ausbildungsgangDto,
        @MappingTarget Ausbildungsgang ausbildungsgang);
}
