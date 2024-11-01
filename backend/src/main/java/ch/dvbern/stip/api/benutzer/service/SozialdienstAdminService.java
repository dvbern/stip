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
        SozialdienstAdmin newSozialdienstAdmin = new SozialdienstAdmin();
        newSozialdienstAdmin.setKeycloakId(sozialdienstAdmin.getKeycloakId());
        newSozialdienstAdmin.setVorname(sozialdienstAdmin.getVorname());
        newSozialdienstAdmin.setNachname(sozialdienstAdmin.getNachname());
        newSozialdienstAdmin.setBenutzerStatus(BenutzerStatus.AKTIV);
        newSozialdienstAdmin.setBenutzereinstellungen(new Benutzereinstellungen());

        sozialdienstAdminRepository.persistAndFlush(newSozialdienstAdmin);
        return newSozialdienstAdmin;
    }

    @Transactional
    public void deleteSozialdienstAdminBenutzer(final String benutzerId) {
        final var sozialdienstAdmin = sozialdienstAdminRepository.findByKeycloakId(benutzerId).orElseThrow(NotFoundException::new);
        sozialdienstAdminRepository.delete(sozialdienstAdmin);
    }
}
