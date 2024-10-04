package ch.dvbern.stip.api.gesuch.service;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchsperioden.service.GesuchsperiodeMapper;
import ch.dvbern.stip.generated.dto.GesuchTrancheSlimDto;
import ch.dvbern.stip.generated.dto.GsDashboardDto;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GsDashboardMapper {
    private final GesuchsperiodeMapper gesuchsperiodeMapper;

    public GsDashboardDto toDto(
        final Gesuch gesuch,
        final GesuchTrancheSlimDto offeneAenderung,
        final GesuchTrancheSlimDto missingDocuments
    ) {
        final var periodeDto = gesuchsperiodeMapper.toDto(gesuch.getGesuchsperiode());

        return new GsDashboardDto(
            periodeDto,
            gesuch.getGesuchStatus(),
            gesuch.getId(),
            offeneAenderung,
            missingDocuments.getId()
        );
    }
}
