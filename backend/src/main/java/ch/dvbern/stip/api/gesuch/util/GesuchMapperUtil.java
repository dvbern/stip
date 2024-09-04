package ch.dvbern.stip.api.gesuch.util;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.service.GesuchMapper;
import ch.dvbern.stip.api.gesuch.service.GesuchTrancheMapper;
import ch.dvbern.stip.generated.dto.GesuchDto;
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

    public GesuchDto mapWithNewestTranche(final Gesuch gesuch) {
        return mapWithTranche(gesuch, gesuch.getNewestGesuchTranche().orElseThrow(IllegalStateException::new));
    }

    public GesuchDto mapWithTranche(final Gesuch gesuch, final GesuchTranche tranche) {
        final var gesuchDto = gesuchMapper.toDto(gesuch);
        gesuchDto.setGesuchTrancheToWorkWith(gesuchTrancheMapper.toDto(tranche));
        return gesuchDto;
    }
}
