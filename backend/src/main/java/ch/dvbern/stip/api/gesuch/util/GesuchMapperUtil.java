package ch.dvbern.stip.api.gesuch.util;

import java.util.ArrayList;
import java.util.List;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.service.GesuchMapper;
import ch.dvbern.stip.api.gesuch.service.GesuchTrancheMapper;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDto;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchMapperUtil {
    private final GesuchMapper gesuchMapper;
    private final GesuchTrancheMapper gesuchTrancheMapper;

    public GesuchDto mapWithOldestTranche(final Gesuch gesuch) {
        return mapWithTranche(gesuch, gesuch.getOldestGesuchTranche().orElseThrow(IllegalStateException::new));
    }

    /**
     * If a Gesuch contains a aenderung, two GesuchDtos will be returned.
     * One for the Gesuch
     * One (separate) for the Aenderung
     * @param gesuch The Gesuch which contains a Aenderung (as tranche to work with)
     * @return a list of GesuchDtos which includes Aenderungen
     */
    public List<GesuchDto> mapWithAenderung(final Gesuch gesuch) {
        List<GesuchDto> gesuchDtos = new ArrayList<>();

        // find oldest tranche (without aenderungen)
        // the first dto to be returned: Tranche
        gesuch.getOldestGesuchTranche().ifPresent(tranche -> gesuchDtos.add(mapWithTranche(gesuch,tranche)));

        // the first dto to be returned: Aenderung
        gesuch.getAenderungZuUeberpruefen().ifPresent(tranche -> gesuchDtos.add(mapWithTranche(gesuch, tranche)));

        return gesuchDtos;
    }

    public GesuchDto mapWithNewestTranche(final Gesuch gesuch) {
        return mapWithTranche(gesuch, gesuch.getNewestGesuchTranche().orElseThrow(IllegalStateException::new));
    }

    public GesuchDto mapWithTranche(final Gesuch gesuch, final GesuchTranche tranche) {
        final var gesuchDto = gesuchMapper.toDto(gesuch);
        gesuchDto.setGesuchTrancheToWorkWith(gesuchTrancheMapper.toDto(tranche));
        return gesuchDto;
    }

    public GesuchWithChangesDto toWithChangesDto(
        final Gesuch gesuch,
        final GesuchTranche tranche,
        final GesuchTranche changes
    ) {
        final var dto = gesuchMapper.toWithChangesDto(gesuch);
        dto.setGesuchTrancheToWorkWith(gesuchTrancheMapper.toDto(tranche));
        dto.setChanges(List.of(gesuchTrancheMapper.toDto(changes)));
        return dto;
    }

    public GesuchWithChangesDto toWithChangesDto(
        final Gesuch gesuch,
        final GesuchTranche tranche,
        final List<GesuchTranche> changes
    ) {
        final var dto = gesuchMapper.toWithChangesDto(gesuch);
        dto.setGesuchTrancheToWorkWith(gesuchTrancheMapper.toDto(tranche));
        dto.setChanges(changes.stream().map(gesuchTrancheMapper::toDto).toList());
        return dto;
    }
}
