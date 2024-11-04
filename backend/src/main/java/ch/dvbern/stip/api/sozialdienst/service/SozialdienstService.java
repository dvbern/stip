package ch.dvbern.stip.api.sozialdienst.service;

import ch.dvbern.stip.api.sozialdienstadmin.service.SozialdienstAdminService;
import ch.dvbern.stip.api.sozialdienst.repo.SozialdienstRepository;
import ch.dvbern.stip.generated.dto.*;
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
    private final SozialdienstAdminService sozialdienstAdminService;

    @Transactional
    public SozialdienstDto createSozialdienst(SozialdienstCreateDto dto) {
        var sozialdienst = sozialdienstMapper.toEntity(dto);
        final var adminDto = sozialdienstAdminService.createSozialdienstAdminBenutzer(dto.getSozialdienstAdmin());
        sozialdienst.setAdmin(sozialdienstAdminService.getSozialdienstAdminById(adminDto.getId()));
        sozialdienstRepository.persistAndFlush(sozialdienst);
        var result = sozialdienstMapper.toDto(sozialdienst);
        result.setSozialdienstAdmin(sozialdienstAdminService.getSozialdienstAdminDtoById(adminDto.getId()));
        return result;
    }

    @Transactional
    public SozialdienstDto getSozialdienstById(UUID id) {
        var entity = sozialdienstRepository.requireById(id);
        var result = sozialdienstMapper.toDto(entity);
        result.setSozialdienstAdmin(sozialdienstAdminService.getSozialdienstAdminDtoById(entity.getAdmin().getId()));
        return result;
    }

    @Transactional
    public List<SozialdienstDto> getAllSozialdienst() {
        final var entities = sozialdienstRepository.findAll();
        return entities.stream().map(entity -> {
            var sozialdienstDto = sozialdienstMapper.toDto(entity);
            var adminDto = sozialdienstAdminService.getSozialdienstAdminDtoById(entity.getAdmin().getId());
            sozialdienstDto.setSozialdienstAdmin(adminDto);
            return sozialdienstDto;
        }).toList();
    }

    @Transactional
    public SozialdienstDto deleteSozialdienst(UUID id) {
        final var entity = sozialdienstRepository.requireById(id);
        sozialdienstRepository.delete(entity);
        return sozialdienstMapper.toDto(entity);
    }

    @Transactional
    public SozialdienstDto updateSozialdienst(SozialdienstUpdateDto dto) {
        var sozialdienst = sozialdienstRepository.requireById(dto.getId());
        sozialdienstMapper.partialUpdate(dto, sozialdienst);
        return sozialdienstMapper.toDto(sozialdienst);
    }

    @Transactional
    public SozialdienstAdminDto updateSozialdienstAdmin(SozialdienstAdminUpdateDto dto, SozialdienstDto sozialdienstDto) {
        final var sozialdienst = sozialdienstRepository.requireById(sozialdienstDto.getId());
        final var adminKeykloakId = sozialdienst.getAdmin().getKeycloakId();
        var sozialdienstAdmin = sozialdienstAdminService.getSozialdienstAdminById(sozialdienst.getAdmin().getId());
        var responseDto = sozialdienstAdminService.updateSozialdienstAdminBenutzer(sozialdienstAdmin.getId(),dto);
        responseDto.setKeycloakId(adminKeykloakId);
        return responseDto;
    }

    @Transactional
    public SozialdienstAdminDto replaceSozialdienstAdmin(UUID sozialdienstId, SozialdienstAdminCreateDto dto) {
        var sozialdienst = sozialdienstRepository.requireById(sozialdienstId);
        final var benutzerToDelete = sozialdienst.getAdmin();
        sozialdienstAdminService.deleteSozialdienstAdminBenutzer(benutzerToDelete.getKeycloakId());

        final var newSozialdienstAdmin = sozialdienstAdminService.createSozialdienstAdminBenutzer(dto);
        sozialdienst.setAdmin(newSozialdienstAdmin);
        return sozialdienstAdminService.getSozialdienstAdminDtoById(newSozialdienstAdmin.getId());
    }
}
