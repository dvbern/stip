package ch.dvbern.stip.api.gesuchsjahr.service;

import java.util.Collection;
import java.util.UUID;

import ch.dvbern.stip.api.common.type.GueltigkeitStatus;
import ch.dvbern.stip.api.gesuchsjahr.entity.Gesuchsjahr;
import ch.dvbern.stip.api.gesuchsjahr.repo.GesuchsjahrRepository;
import ch.dvbern.stip.generated.dto.GesuchsjahrCreateDto;
import ch.dvbern.stip.generated.dto.GesuchsjahrDto;
import ch.dvbern.stip.generated.dto.GesuchsjahrUpdateDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@RequestScoped
@AllArgsConstructor
public class GesuchsjahrService {
    private final GesuchsjahrMapper gesuchsjahrMapper;
    private final GesuchsjahrRepository gesuchsjahrRepository;

    public GesuchsjahrDto getGesuchsjahr(final UUID gesuchsjahrId) {
        final var gesuchsperiode = gesuchsjahrRepository.requireById(gesuchsjahrId);
        return gesuchsjahrMapper.toDto(gesuchsperiode);
    }

    public Collection<GesuchsjahrDto> getGesuchsjahre() {
        return gesuchsjahrRepository.findAll()
            .stream()
            .map(gesuchsjahrMapper::toDto)
            .toList();
    }

    @Transactional
    public GesuchsjahrDto createGesuchsjahr(final GesuchsjahrCreateDto gesuchsjahrCreateDto) {
        final var gesuchsjahr = gesuchsjahrMapper.toEntity(gesuchsjahrCreateDto);
        gesuchsjahrRepository.persistAndFlush(gesuchsjahr);
        return gesuchsjahrMapper.toDto(gesuchsjahr);
    }

    @Transactional
    public GesuchsjahrDto updateGesuchsjahr(final UUID gesuchsjahrId, final GesuchsjahrUpdateDto gesuchsjahrUpdateDto) {
        var gesuchsjahr = gesuchsjahrRepository.requireById(gesuchsjahrId);
        preventUpdateIfReadonly(gesuchsjahr);
        gesuchsjahr = gesuchsjahrMapper.partialUpdate(gesuchsjahrUpdateDto, gesuchsjahr);
        return gesuchsjahrMapper.toDto(gesuchsjahr);
    }

    @Transactional
    public GesuchsjahrDto publishGesuchsjahr(final UUID gesuchsjahrId) {
        final var gesuchsjahr = gesuchsjahrRepository.requireById(gesuchsjahrId);
        gesuchsjahr.setGueltigkeitStatus(GueltigkeitStatus.PUBLIZIERT);
        gesuchsjahrRepository.persist(gesuchsjahr);
        return gesuchsjahrMapper.toDto(gesuchsjahr);
    }

    @Transactional
    public void deleteGesuchsjahr(final UUID gesuchsjahrId) {
        Gesuchsjahr gesuchsjahr = gesuchsjahrRepository.requireById(gesuchsjahrId);
        gesuchsjahrRepository.delete(gesuchsjahr);
    }

    private void preventUpdateIfReadonly(final Gesuchsjahr gesuchsjahr) {
        if (gesuchsjahr.getGueltigkeitStatus() != GueltigkeitStatus.ENTWURF) {
            throw new IllegalStateException("Cannot update a Gesuchsjahr with GueltigkeitStatus != ENTWURF");
        }
    }
}
