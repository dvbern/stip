package ch.dvbern.stip.api.sozialdienstadmin.service;

import ch.dvbern.stip.api.sozialdienstadmin.entity.SozialdienstAdmin;
import ch.dvbern.stip.api.sozialdienstadmin.repo.SozialdienstAdminRepository;
import ch.dvbern.stip.api.benutzer.type.BenutzerStatus;
import ch.dvbern.stip.api.benutzereinstellungen.entity.Benutzereinstellungen;

import ch.dvbern.stip.generated.dto.SozialdienstAdminCreateDto;
import ch.dvbern.stip.generated.dto.SozialdienstAdminDto;
import ch.dvbern.stip.generated.dto.SozialdienstAdminUpdateDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequestScoped
@RequiredArgsConstructor
public class SozialdienstAdminService {
    private final SozialdienstAdminRepository sozialdienstAdminRepository;
    private final SozialdienstAdminMapper sozialdienstAdminMapper;

    @Transactional
    public SozialdienstAdmin getSozialdienstAdminById(UUID id) {
        return sozialdienstAdminRepository.requireById(id);
    }

    @Transactional
    public SozialdienstAdminDto getSozialdienstAdminDtoById(UUID id) {
        return sozialdienstAdminMapper.toDto(getSozialdienstAdminById(id));
    }

    @Transactional
    public SozialdienstAdminDto updateSozialdienstAdminBenutzer(final UUID sozialdienstAdminId, SozialdienstAdminUpdateDto dto) {
        var sozialdienstAdmin = sozialdienstAdminRepository.requireById(sozialdienstAdminId);
        sozialdienstAdminMapper.partialUpdate(dto, sozialdienstAdmin);
        return sozialdienstAdminMapper.toDto(sozialdienstAdmin);
    }

    @Transactional
    public SozialdienstAdmin createSozialdienstAdminBenutzer(SozialdienstAdminCreateDto dto) {
        final var sozialdienstAdmin = sozialdienstAdminMapper.toEntity(dto);
        sozialdienstAdmin.setBenutzereinstellungen(new Benutzereinstellungen());
        sozialdienstAdmin.setBenutzerStatus(BenutzerStatus.AKTIV);
        sozialdienstAdminRepository.persistAndFlush(sozialdienstAdmin);
        return sozialdienstAdmin;
    }

    @Transactional
    public void deleteSozialdienstAdminBenutzer(final String benutzerId) {
        final var sozialdienstAdmin = sozialdienstAdminRepository.findByKeycloakId(benutzerId).orElseThrow(NotFoundException::new);
        sozialdienstAdminRepository.delete(sozialdienstAdmin);
    }
}
