package ch.dvbern.stip.api.sozialdienst.service;

import ch.dvbern.stip.api.benutzer.service.SozialdienstAdminService;
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
    private final SozialdienstAdminMapper sozialdienstAdminMapper;
    private final SozialdienstAdminService sozialdienstAdminService;

    @Transactional
    public SozialdienstDto createSozialdienst(SozialdienstCreateDto dto) {
        var sozialdienst = sozialdienstMapper.toEntity(dto);

        final var sozialdienstAdmin = sozialdienstAdminMapper.toSozialdienstAdmin(dto.getSozialdienstAdmin());
        final var admin = sozialdienstAdminService.createSozialdienstAdminBenutzer(sozialdienstAdmin);
        sozialdienst.setAdmin(admin);
        sozialdienstRepository.persistAndFlush(sozialdienst);


        var result = sozialdienstMapper.toDto(sozialdienst);
        var adminDto = sozialdienstAdminMapper.toDto(admin);
        result.setSozialdienstAdmin(adminDto);
        return result;
    }

    @Transactional
    public SozialdienstDto getSozialdienstById(UUID id) {
        var entity = sozialdienstRepository.requireById(id);
        var result = sozialdienstMapper.toDto(entity);
        result.setSozialdienstAdmin(sozialdienstAdminMapper.toDto(entity.getAdmin()));
        return result;
    }

    @Transactional
    public List<SozialdienstDto> getAllSozialdienst() {
        final var entities = sozialdienstRepository.findAll();
        return entities.stream().map(x -> {
            var sozialdienstDto = sozialdienstMapper.toDto(x);
            var adminDto = sozialdienstAdminMapper.toDto(x.getAdmin());
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
    public SozialdienstAdminDto updateSozialdienstAdmin(UUID sozialdienstId, SozialdienstAdminUpdateDto dto) {
        var sozialdienst = sozialdienstRepository.requireById(sozialdienstId);
        var sozialdienstAdmin = sozialdienstAdminMapper.toSozialdienstAdmin(dto);
        sozialdienst.getAdmin().setVorname(sozialdienstAdmin.getVorname());
        sozialdienst.getAdmin().setNachname(sozialdienstAdmin.getNachname());
        return sozialdienstAdminMapper.toDto(sozialdienstAdmin);
    }

    @Transactional
    public SozialdienstAdminDto replaceSozialdienstAdmin(UUID sozialdienstId, SozialdienstAdminCreateDto dto) {
        var sozialdienst = sozialdienstRepository.requireById(sozialdienstId);
        final var benutzerToDelete = sozialdienst.getAdmin();
        sozialdienstAdminService.deleteSozialdienstAdminBenutzer(benutzerToDelete.getKeycloakId());

        final var benutzerToReplace = sozialdienstAdminMapper.toSozialdienstAdmin(dto);
        final var benutzer = sozialdienstAdminService.createSozialdienstAdminBenutzer(benutzerToReplace);
        sozialdienst.setAdmin(benutzer);

        var sozialdienstAdmin = sozialdienstAdminMapper.toSozialdienstAdmin(dto);
        return sozialdienstAdminMapper.toDto(sozialdienstAdmin);
    }
}
