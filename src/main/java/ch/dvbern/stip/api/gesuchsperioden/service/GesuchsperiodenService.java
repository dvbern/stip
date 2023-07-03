package ch.dvbern.stip.api.gesuchsperioden.service;

import ch.dvbern.stip.generated.dto.GesuchsperiodeCreateDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeDto;
import ch.dvbern.stip.api.gesuchsperioden.repo.GesuchsperiodeRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@RequestScoped
@RequiredArgsConstructor
public class GesuchsperiodenService {

    private final GesuchsperiodeMapper gesuchsperiodeMapper;
    private final GesuchsperiodeRepository gesuchsperiodeRepository;

    @Transactional
    public GesuchsperiodeDto createGesuchsperiode(GesuchsperiodeCreateDto createDto) {
        var newEntity = gesuchsperiodeMapper.toEntity(createDto);
        gesuchsperiodeRepository.persist(newEntity);
        return gesuchsperiodeMapper.toDto(newEntity);
    }

    public Collection<GesuchsperiodeDto> getAllGesuchsperioden() {
        return this.gesuchsperiodeRepository.findAll()
                .stream()
                .map(gesuchsperiodeMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<GesuchsperiodeDto> getGesuchsperiode(UUID id) {

        var gesuchsperiode = gesuchsperiodeRepository.findByIdOptional(id);

        return gesuchsperiode.map(gesuchsperiodeMapper::toDto);
    }

    public Collection<GesuchsperiodeDto> getAllActive() {
        return gesuchsperiodeRepository
                .findAllActiveForDate(LocalDate.now())
                .map(gesuchsperiodeMapper::toDto)
                .collect(Collectors.toList());
    }
}
