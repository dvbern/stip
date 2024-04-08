package ch.dvbern.stip.api.gesuchsperioden.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.gesuchsperioden.repo.GesuchsperiodeRepository;
import ch.dvbern.stip.generated.dto.GesuchsperiodeCreateDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeWithDatenDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class GesuchsperiodenService {

    private final GesuchsperiodeMapper gesuchsperiodeMapper;
    private final GesuchsperiodeRepository gesuchsperiodeRepository;

    @Transactional
    public GesuchsperiodeDto createGesuchsperiode(final GesuchsperiodeCreateDto createDto) {
        final var newEntity = gesuchsperiodeMapper.toEntity(createDto);
        gesuchsperiodeRepository.persistAndFlush(newEntity);
        return gesuchsperiodeMapper.toDto(newEntity);
    }

    @Transactional
    public GesuchsperiodeDto updateGesuchsperiode(
        final UUID gesuchsperiodeId,
        final GesuchsperiodeUpdateDto updateDto
    ) {
        final var gesuchsperiode = gesuchsperiodeRepository.requireById(gesuchsperiodeId);
        preventUpdateIfReadonly(gesuchsperiode);
        gesuchsperiodeMapper.partialUpdate(updateDto, gesuchsperiode);
        return gesuchsperiodeMapper.toDto(gesuchsperiode);
    }

    public Collection<GesuchsperiodeDto> getAllGesuchsperioden() {
        return this.gesuchsperiodeRepository.findAll()
            .stream()
            .map(gesuchsperiodeMapper::toDto)
            .toList();
    }

    public Optional<GesuchsperiodeWithDatenDto> getGesuchsperiode(final UUID id) {
        final var gesuchsperiode = gesuchsperiodeRepository.findByIdOptional(id);
        return gesuchsperiode.map(gesuchsperiodeMapper::toDatenDto);
    }

    public Collection<GesuchsperiodeDto> getAllActive() {
        return gesuchsperiodeRepository
            .findAllActiveForDate(LocalDate.now())
            .map(gesuchsperiodeMapper::toDto)
            .toList();
    }

    private void preventUpdateIfReadonly(final Gesuchsperiode gesuchsperiode) {
        if (gesuchsperiode.getGesuchsperiodeStart().isAfter(LocalDate.now())) {
            throw new IllegalStateException("Cannot update Gesuchsperiode if it is started");
        }
    }
}
