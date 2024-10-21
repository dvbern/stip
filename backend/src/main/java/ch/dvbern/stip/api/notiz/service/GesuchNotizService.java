package ch.dvbern.stip.api.notiz.service;

import ch.dvbern.stip.api.notiz.repo.GesuchNotizRepository;
import ch.dvbern.stip.generated.dto.GesuchNotizCreateDto;
import ch.dvbern.stip.generated.dto.GesuchNotizDto;
import ch.dvbern.stip.generated.dto.GesuchNotizUpdateDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;
@RequiredArgsConstructor
@RequestScoped
public class GesuchNotizService {
    private final GesuchNotizRepository repository;
    private final GesuchNotizMapper mapper;

    @Transactional
    public List<GesuchNotizDto> getAllByGesuchId(@Valid final UUID gesuchId) {
        return repository.findAllByGesuchId(gesuchId).stream().map(mapper::toDto).toList();
    }
    @Transactional
    public GesuchNotizDto getById(@Valid final UUID notizId) {
        final var notiz = repository.requireById(notizId);
        return mapper.toDto(notiz);
    }
    @Transactional
    public void delete(@Valid final UUID gesuchNotizId) {
        final var notiz = repository.requireById(gesuchNotizId);
        repository.delete(notiz);
    }
    @Transactional
    public void deleteAllByGesuchId(@Valid final UUID gesuchId) {
        repository.findAllByGesuchId(gesuchId).forEach(repository::delete);
    }
    @Transactional
    public GesuchNotizDto create(@Valid final GesuchNotizCreateDto createDto) {
        final var notiz = mapper.toEntity(createDto);
        repository.persistAndFlush(notiz);
        return mapper.toDto(notiz);
    }
    @Transactional
    public GesuchNotizDto update(@Valid final GesuchNotizUpdateDto gesuchNotizUpdateDto) {
         var gesuchNotiz = repository.requireById(gesuchNotizUpdateDto.getId());
         var update = mapper.partialUpdate(gesuchNotizUpdateDto,gesuchNotiz);
         repository.persistAndFlush(update);
         return mapper.toDto(gesuchNotiz);
    }
}
