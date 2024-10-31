package ch.dvbern.stip.api.gesuchsperioden.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.common.type.GueltigkeitStatus;
import ch.dvbern.stip.api.gesuchsjahr.repo.GesuchsjahrRepository;
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
    private final GesuchsjahrRepository gesuchsjahrRepository;

    @Transactional
    public GesuchsperiodeWithDatenDto createGesuchsperiode(final GesuchsperiodeCreateDto createDto) {
        final var newEntity = gesuchsperiodeMapper.toEntity(createDto);
        newEntity.setGesuchsjahr(gesuchsjahrRepository.requireById(newEntity.getGesuchsjahr().getId()));
        gesuchsperiodeRepository.persistAndFlush(newEntity);
        return gesuchsperiodeMapper.toDatenDto(newEntity);
    }

    @Transactional
    public GesuchsperiodeWithDatenDto updateGesuchsperiode(
        final UUID gesuchsperiodeId,
        final GesuchsperiodeUpdateDto updateDto
    ) {
        final var gesuchsperiode = gesuchsperiodeRepository.requireById(gesuchsperiodeId);
        preventUpdateIfReadonly(gesuchsperiode);
        gesuchsperiodeMapper.partialUpdate(updateDto, gesuchsperiode);
        gesuchsperiode.setGesuchsjahr(gesuchsjahrRepository.requireById(gesuchsperiode.getGesuchsjahr().getId()));
        return gesuchsperiodeMapper.toDatenDto(gesuchsperiode);
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

    public Gesuchsperiode getGesuchsperiodeForAusbildung(final Ausbildung ausbildung) {
        final var ausbildungBegin = ausbildung.getAusbildungBegin();
//        var ausbildungsBeginAssumed = ausbildungBegin.withYear(LocalDate.now().getYear() - 1);
//        if (ausbildungsBeginAssumed.isBefore(LocalDate.now().minusMonths(7))) {
//            ausbildungsBeginAssumed = ausbildungBegin.withYear(LocalDate.now().getYear());
//            if (ausbildungsBeginAssumed.isBefore(LocalDate.now().minusMonths(7))) {
//                ausbildungsBeginAssumed = ausbildungBegin.withYear(LocalDate.now().getYear() + 1);
//            }
//        }
        var ausbildungsBeginAssumed = ausbildungBegin.withYear(LocalDate.now().getYear() + 1);
        if (ausbildungsBeginAssumed.isAfter(LocalDate.now().plusYears(1))) {
            ausbildungsBeginAssumed = ausbildungBegin.withYear(LocalDate.now().getYear());
        }

        final var eligibleGesuchsperioden = gesuchsperiodeRepository.findAllStartBefore(ausbildungsBeginAssumed);
        Gesuchsperiode gesuchsperiode = null;
        for (final var eligibleGesuchsperiode : eligibleGesuchsperioden.toList()) {
            if (
                (gesuchsperiode != null) &&
                eligibleGesuchsperiode.getGesuchsperiodeStart().isAfter(ausbildungsBeginAssumed)
            ) {
                break;
            }
            gesuchsperiode = eligibleGesuchsperiode;
        }
        return gesuchsperiode;
    }

    @Transactional
    public void deleteGesuchsperiode(final UUID gesuchsperiodeId) {
        final var gesuchsperiode = gesuchsperiodeRepository.requireById(gesuchsperiodeId);
        preventUpdateIfReadonly(gesuchsperiode);
        gesuchsperiodeRepository.delete(gesuchsperiode);
    }

    @Transactional
    public GesuchsperiodeWithDatenDto publishGesuchsperiode(final UUID gesuchsperiodeId) {
        final var gesuchsperiode = gesuchsperiodeRepository.requireById(gesuchsperiodeId);
        preventUpdateIfReadonly(gesuchsperiode);
        gesuchsperiode.setGueltigkeitStatus(GueltigkeitStatus.PUBLIZIERT);
        return gesuchsperiodeMapper.toDatenDto(gesuchsperiode);
    }

    public GesuchsperiodeWithDatenDto getLatest() {
        final var found = gesuchsperiodeRepository.getLatest();
        return found.map(gesuchsperiodeMapper::toDatenDto).orElse(null);
    }

    private void preventUpdateIfReadonly(final Gesuchsperiode gesuchsperiode) {
        if (gesuchsperiode.getGueltigkeitStatus() != GueltigkeitStatus.ENTWURF) {
            throw new IllegalStateException("Cannot update Gesuchsperiode if it is started");
        }
    }
}
