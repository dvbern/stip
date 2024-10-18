package ch.dvbern.stip.api.gesuch.service;

import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchsperioden.service.GesuchsperiodeMapper;
import ch.dvbern.stip.generated.dto.GesuchTrancheSlimDto;
import ch.dvbern.stip.generated.dto.GsDashboardDto;
import ch.dvbern.stip.generated.dto.GsDashboardMissingDocumentsDto;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;

@ApplicationScoped
@RequiredArgsConstructor
public class GsDashboardMapper {
    private final GesuchsperiodeMapper gesuchsperiodeMapper;

    public GsDashboardDto toDto(
        final Gesuch gesuch,
        final GesuchTrancheSlimDto offeneAenderung,
        final Optional<ImmutablePair<UUID, Integer>> missingDocumentsTrancheIdAndCount
    ) {
        final var periodeDto = gesuchsperiodeMapper.toDto(gesuch.getGesuchsperiode());
        final var missingDocumentsDto = missingDocumentsTrancheIdAndCount
            .map(pair -> new GsDashboardMissingDocumentsDto(pair.getLeft(), pair.getRight()))
            .orElse(null);

        return new GsDashboardDto(
            periodeDto,
            gesuch.getGesuchStatus(),
            gesuch.getId(),
            offeneAenderung,
            missingDocumentsDto
        );
    }
}
