package ch.dvbern.stip.api.common.authorization;

import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.authorization.util.AuthorizerUtil;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.service.GesuchStatusService;
import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
@Authorizer
public class GesuchAuthorizer extends BaseAuthorizer {
    private final BenutzerService benutzerService;
    private final GesuchRepository gesuchRepository;
    private final GesuchStatusService gesuchStatusService;

    public void canRead(final UUID gesuchId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();

        // Admins and Sachbearbeiter can always read every Gesuch
        if (isAdminOrSb(currentBenutzer)) {
            return;
        }

        // Gesuchsteller can only read their own Gesuch
        final var gesuch = gesuchRepository.requireById(gesuchId);
        if (AuthorizerUtil.isGesuchstellerOfGesuch(currentBenutzer, gesuch)) {
            return;
        }

        throw new UnauthorizedException();
    }

    public void canUpdate(final UUID gesuchId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();

        if (isAdminOrSb(currentBenutzer)) {
            return;
        }

        final var gesuch = gesuchRepository.requireById(gesuchId);
        if (AuthorizerUtil.isGesuchstellerOfGesuch(currentBenutzer, gesuch) &&
            gesuchStatusService.benutzerCanEdit(currentBenutzer, gesuch.getGesuchStatus())
        ) {
            return;
        }

        throw new UnauthorizedException();
    }

    public void canDelete(final UUID gesuchId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();

        if (isAdminOrSb(currentBenutzer)) {
            return;
        }

        // TODO KSTIP-1057: Check if Gesuchsteller can delete their Gesuch
        throw new UnauthorizedException();
    }

    public void canCreate() {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();

    }
}
