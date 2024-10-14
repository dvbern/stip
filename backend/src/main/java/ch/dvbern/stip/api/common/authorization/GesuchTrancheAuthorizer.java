package ch.dvbern.stip.api.common.authorization;

import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.authorization.util.AuthorizerUtil;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheTyp;
import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
@Authorizer
public class GesuchTrancheAuthorizer extends BaseAuthorizer {
    private final BenutzerService benutzerService;
    private final GesuchTrancheRepository gesuchTrancheRepository;
    private final GesuchRepository gesuchRepository;

    @Transactional
    public void canRead(final UUID gesuchTrancheId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        // Admins can always read every GesuchTranche
        if (isAdmin(currentBenutzer)) {
            return;
        }

        final var gesuch = gesuchRepository.requireGesuchByTrancheId(gesuchTrancheId);
        if (AuthorizerUtil.isGesuchstellerOfGesuch(currentBenutzer, gesuch)) {
            return;
        }

        throw new UnauthorizedException();
    }

    @Transactional
    public void canUpdate(final UUID gesuchTrancheId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();

        if (isAdminOrSb(currentBenutzer)) {
            return;
        }

        final var gesuchTranche = gesuchTrancheRepository.findById(gesuchTrancheId);
        final var gesuch = gesuchRepository.requireGesuchByTrancheId(gesuchTrancheId);
        // Gesuchsteller can only update their Tranchen IN_BEARBEITUNG_GS
        if (isGesuchsteller(currentBenutzer) &&
            !isSachbearbeiter(currentBenutzer) &&
            AuthorizerUtil.isGesuchstellerOfGesuch(currentBenutzer, gesuch)
        ) {
            if (gesuchTranche.getStatus() != GesuchTrancheStatus.IN_BEARBEITUNG_GS) {
                throw new UnauthorizedException();
            }
        }
    }

    @Transactional
    public void canDeleteAenderung(final UUID gesuchTrancheId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();

        if (isAdminOrSb(currentBenutzer)) {
            return;
        }

        final var gesuchTranche = gesuchTrancheRepository.findById(gesuchTrancheId);
        final var gesuch = gesuchRepository.requireGesuchByTrancheId(gesuchTrancheId);
        final var isAuthorizedForCurrentOperation = isGesuchsteller(currentBenutzer) &&
            !isSachbearbeiter(currentBenutzer) &&
            AuthorizerUtil.isGesuchstellerOfGesuch(currentBenutzer, gesuch);
        // Gesuchsteller can only update their Tranchen IN_BEARBEITUNG_GS
        if (!isAuthorizedForCurrentOperation) {
            throw new UnauthorizedException();
        }

        if ((gesuchTranche.getStatus() != GesuchTrancheStatus.IN_BEARBEITUNG_GS)
        || (gesuchTranche.getTyp() != GesuchTrancheTyp.AENDERUNG)) {
            throw new UnauthorizedException();
        }
    }
}
