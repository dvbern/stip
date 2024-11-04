package ch.dvbern.stip.api.benutzer.service;

import ch.dvbern.stip.api.benutzer.entity.SozialdienstAdmin;
import ch.dvbern.stip.api.benutzer.repo.SozialdienstAdminRepository;
import ch.dvbern.stip.api.benutzer.type.BenutzerStatus;
import ch.dvbern.stip.api.benutzereinstellungen.entity.Benutzereinstellungen;

import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class SozialdienstAdminService {
    private final SozialdienstAdminRepository sozialdienstAdminRepository;

    @Transactional
    public SozialdienstAdmin createSozialdienstAdminBenutzer(SozialdienstAdmin sozialdienstAdmin) {
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
