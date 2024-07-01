package ch.dvbern.stip.api.gesuch.service;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.gesuch.repo.GesuchHistoryRepository;
import ch.dvbern.stip.generated.dto.StatusprotokollEntryDto;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class GesuchHistoryService {
    private final GesuchHistoryRepository gesuchHistoryRepository;
    private final StatusprotokollMapper statusprotokollMapper;

    public List<StatusprotokollEntryDto> getStatusprotokoll(final UUID gesuchId) {
        final var revisions = gesuchHistoryRepository.getStatusHistory(gesuchId);
        return revisions.stream().map(statusprotokollMapper::toDto).toList();
    }
}
