package ch.dvbern.stip.api.gesuch.service;

import java.util.List;
import java.util.Objects;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.generated.dto.GesuchTrancheDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheSlimDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheWithChangesDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MappingConfig.class,
    uses = GesuchFormularMapper.class)
public interface GesuchTrancheMapper {

    @Mapping(source = "gueltigAb", target = "gueltigkeit.gueltigAb")
    @Mapping(source = "gueltigBis", target = "gueltigkeit.gueltigBis")
    GesuchTranche toEntity(GesuchTrancheDto gesuchTrancheDto);

    @Mapping(source = "gueltigkeit.gueltigAb", target = "gueltigAb")
    @Mapping(source = "gueltigkeit.gueltigBis", target = "gueltigBis")
    GesuchTrancheDto toDto(GesuchTranche gesuchTranche);

    @Mapping(source = "gueltigkeit.gueltigAb", target = "gueltigAb")
    @Mapping(source = "gueltigkeit.gueltigBis", target = "gueltigBis")
    GesuchTrancheSlimDto toSlimDto(GesuchTranche gesuchTranche);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    GesuchTranche partialUpdate(GesuchTrancheUpdateDto gesuchUpdateDto, @MappingTarget GesuchTranche gesuch);

    default GesuchTrancheWithChangesDto toWithChangesDto(GesuchTranche gesuchTranche) {
        final var withChangesDto = new GesuchTrancheWithChangesDto();
        if (gesuchTranche == null) {
            return withChangesDto;
        }

        withChangesDto.setChanges(List.of(toDto(gesuchTranche)));

        return withChangesDto;
    }

    default GesuchTrancheWithChangesDto toWithChangesDto(List<GesuchTranche> gesuchTranchen) {
        final var withChangesDto = new GesuchTrancheWithChangesDto();

        withChangesDto.setChanges(gesuchTranchen.stream().filter(Objects::nonNull).map(this::toDto).toList());

        return withChangesDto;
    }
}
