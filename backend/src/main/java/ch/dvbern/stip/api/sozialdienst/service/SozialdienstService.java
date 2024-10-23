package ch.dvbern.stip.api.sozialdienst.service;

import ch.dvbern.stip.api.sozialdienst.repo.SozialdienstRepository;
import ch.dvbern.stip.generated.dto.SozialdienstCreateDto;
import ch.dvbern.stip.generated.dto.SozialdienstDto;
import ch.dvbern.stip.generated.dto.SozialdienstUpdateDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RequestScoped
public class SozialdienstService {
    private final SozialdienstRepository sozialdienstRepository;
    private final SozialdienstMapper sozialdienstMapper;

    @Transactional
    public SozialdienstDto createSozialdienst(SozialdienstCreateDto dto) {
        final var entity = sozialdienstMapper.toEntity(dto);
        sozialdienstRepository.persistAndFlush(entity);
        return sozialdienstMapper.toDto(entity);
    }

    @Transactional
    public SozialdienstDto getSozialdienstById(UUID id) {
        final var entity = sozialdienstRepository.requireById(id);
        return sozialdienstMapper.toDto(entity);
    }

    @Transactional
    public List<SozialdienstDto> getAllSozialdienst() {
        final var entities = sozialdienstRepository.findAll();
        return entities.stream().map(sozialdienstMapper::toDto).toList();
    }

    @Transactional
    public SozialdienstDto deleteSozialdienst(UUID id) {
        final var entity = sozialdienstRepository.requireById(id);
        sozialdienstRepository.delete(entity);
        return sozialdienstMapper.toDto(entity);
    }

    @Transactional
    public SozialdienstDto updateSozialdienst(SozialdienstUpdateDto dto) {
        var entity = sozialdienstRepository.requireById(dto.getId());
        sozialdienstMapper.partialUpdate(dto, entity);
        sozialdienstRepository.persistAndFlush(entity);
        return sozialdienstMapper.toDto(entity);
    }
}
