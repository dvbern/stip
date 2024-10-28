package ch.dvbern.stip.api.steuerdaten.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.dvbern.stip.api.common.service.EntityUpdateMapper;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.api.steuerdaten.util.SteuerdatenDiffUtil;
import ch.dvbern.stip.generated.dto.SteuerdatenDto;
import ch.dvbern.stip.generated.dto.SteuerdatenUpdateDto;
import jakarta.ws.rs.NotFoundException;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public abstract class SteuerdatenMapper extends EntityUpdateMapper<SteuerdatenUpdateDto, Steuerdaten> {
    public abstract Steuerdaten toEntity(SteuerdatenDto steuerdatenDto);

    public abstract SteuerdatenDto toDto(Steuerdaten steuerdaten);

    public abstract Steuerdaten partialUpdate(SteuerdatenUpdateDto steuerdatenDto, @MappingTarget Steuerdaten steuerdaten);

    public Set<Steuerdaten> map(
        final List<SteuerdatenUpdateDto> steuerdatenUpdateDtos,
        final @MappingTarget Set<Steuerdaten> steuerdatenSet
    ) {
        if (steuerdatenUpdateDtos.isEmpty()) {
            steuerdatenSet.clear();
        }
        Iterator<Steuerdaten> iterator = steuerdatenSet.iterator();
        while (iterator.hasNext()) {
            Steuerdaten steuerdaten = iterator.next();
            if (steuerdatenUpdateDtos.stream()
                .noneMatch(elternUpdateDto -> steuerdaten.getId().equals(elternUpdateDto.getId()))) {
                iterator.remove();
            }
        }
        for (SteuerdatenUpdateDto steuerdatenUpdateDto : steuerdatenUpdateDtos) {
            if (steuerdatenUpdateDto.getId() != null) {
                Steuerdaten found = steuerdatenSet.stream()
                    .filter(steuerdaten -> steuerdaten.getId().equals(steuerdatenUpdateDto.getId()))
                    .findFirst()
                    .orElseThrow(
                        () -> new NotFoundException("Steuerdaten Not FOUND")
                    );
                steuerdatenSet.remove(found);
                steuerdatenSet.add(partialUpdate(steuerdatenUpdateDto, found));
            } else {
                steuerdatenSet.add(partialUpdate(steuerdatenUpdateDto, new Steuerdaten()));
            }
        }
        return steuerdatenSet;
    }

    @Override
    @BeforeMapping
    protected void resetDependentDataBeforeUpdate(SteuerdatenUpdateDto source, @MappingTarget Steuerdaten target) {
        resetFieldIf(
            () -> SteuerdatenDiffUtil.hasArbeitsverhaeltnissChangedToUnselbstaendig(source, target),
            "Clear Saeulen because Arbeitsverhaeltniss is unselbstaendig",
            () -> {
                source.setSaeule2(null);
                source.setSaeule3a(null);
            }
        );
    }

	public abstract SteuerdatenUpdateDto toUpdateDto(Steuerdaten steuerdaten);
}
