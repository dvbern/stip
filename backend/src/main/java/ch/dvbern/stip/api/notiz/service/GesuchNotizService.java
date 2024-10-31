package ch.dvbern.stip.api.notiz.service;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.notiz.repo.GesuchNotizRepository;
import ch.dvbern.stip.generated.dto.GesuchNotizCreateDto;
import ch.dvbern.stip.generated.dto.GesuchNotizDto;
import ch.dvbern.stip.generated.dto.GesuchNotizUpdateDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestScoped
public class GesuchNotizService {
    private final GesuchNotizRepository gesuchNotizRepository;
    private final GesuchNotizMapper gesuchNotizMapper;

    @Transactional
    public List<GesuchNotizDto> getAllByGesuchId(final UUID gesuchId) {
        return gesuchNotizRepository.findAllByGesuchId(gesuchId).stream().map(gesuchNotizMapper::toDto).toList();
    }

    @Transactional
    public GesuchNotizDto getById(final UUID notizId) {
        final var notiz = gesuchNotizRepository.requireById(notizId);
        return gesuchNotizMapper.toDto(notiz);
    }

    @Transactional
    public void delete(final UUID gesuchNotizId) {
        final var notiz = gesuchNotizRepository.requireById(gesuchNotizId);
        gesuchNotizRepository.delete(notiz);
    }

    @Transactional
    public void deleteAllByGesuchId(final UUID gesuchId) {
        gesuchNotizRepository.findAllByGesuchId(gesuchId).forEach(gesuchNotizRepository::delete);
    }

    @Transactional
    public GesuchNotizDto create(final GesuchNotizCreateDto createDto) {
        final var notiz = gesuchNotizMapper.toEntity(createDto);
        gesuchNotizRepository.persistAndFlush(notiz);
        return gesuchNotizMapper.toDto(notiz);
    }

    @Transactional
    public GesuchNotizDto update(final GesuchNotizUpdateDto gesuchNotizUpdateDto) {
        var gesuchNotiz = gesuchNotizRepository.requireById(gesuchNotizUpdateDto.getId());
        gesuchNotizMapper.partialUpdate(gesuchNotizUpdateDto, gesuchNotiz);
        return gesuchNotizMapper.toDto(gesuchNotiz);
    }
}
