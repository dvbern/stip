package ch.dvbern.stip.api.gesuch.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.service.GesuchMapper;
import ch.dvbern.stip.api.gesuch.service.GesuchTrancheMapper;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheTyp;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDto;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchMapperUtil {
    private final GesuchMapper gesuchMapper;
    private final GesuchTrancheMapper gesuchTrancheMapper;

    public GesuchDto mapWithCurrentTranche(final Gesuch gesuch) {
        return mapWithTranche(gesuch, gesuch.getCurrentGesuchTranche());
    }

    public boolean hasAenderung(final Gesuch gesuch) {
        return gesuch.getGesuchTranchen().stream().filter(tranche -> tranche.getTyp() == GesuchTrancheTyp.AENDERUNG
            && tranche.getStatus() == GesuchTrancheStatus.UEBERPRUEFEN).count() > 0;
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
        final var aenderung = gesuch.getGesuchTranchen().stream()
            .filter(this::isAenderung).findFirst().get();
        final var tranchen = gesuch.getGesuchTranchen().stream()
            .filter(tranche -> !isAenderung(tranche)
        ).toList() ;

        // find newest tranche (without aenderungen)
        final var newestTranche = findNewestTranche(tranchen);

        // map aenderung tranche to dto
        final var aenderungTrancheDto = gesuchTrancheMapper.toDto(aenderung);
        // map gesuch tranche to dto
        final var gesuchTrancheDto = gesuchTrancheMapper.toDto(newestTranche);

        // the first dto to be returned: Gesuch
        var gesuchDto = mapWithNewestTranche(gesuch);
        // set trancheToWorkWith to the newest tranche
        gesuchDto.setGesuchTrancheToWorkWith(gesuchTrancheDto);

        // the second dto to be returned: Aenderung
        var aenderungDto = copyGesuchDto(gesuchDto);
        // set trancheToWorkWith to aenderung
        aenderungDto.setGesuchTrancheToWorkWith(aenderungTrancheDto);

        gesuchDtos.add(aenderungDto);
        gesuchDtos.add(gesuchDto);
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

    private boolean isAenderung(final GesuchTranche tranche) {
        return tranche.getTyp() == GesuchTrancheTyp.AENDERUNG
            && tranche.getStatus() == GesuchTrancheStatus.UEBERPRUEFEN;
    }

    private GesuchTranche findNewestTranche(final List<GesuchTranche> tranchen) {
        return Collections.max(
            tranchen,
            Comparator.comparing(tranche -> tranche.getGueltigkeit().getGueltigBis())
        );
    }

    private GesuchDto copyGesuchDto(final GesuchDto gesuchDtoToCopy) {
        var copiedGesuchDto = new GesuchDto();
        copiedGesuchDto.setAenderungsdatum(gesuchDtoToCopy.getAenderungsdatum());
        copiedGesuchDto.setFall(gesuchDtoToCopy.getFall());
        copiedGesuchDto.setBearbeiter(gesuchDtoToCopy.getBearbeiter());
        copiedGesuchDto.setGesuchNummer(gesuchDtoToCopy.getGesuchNummer());
        copiedGesuchDto.setGesuchStatus(gesuchDtoToCopy.getGesuchStatus());
        copiedGesuchDto.setGesuchsperiode(gesuchDtoToCopy.getGesuchsperiode());
        copiedGesuchDto.setId(gesuchDtoToCopy.getId());
        return copiedGesuchDto;
    }
}
