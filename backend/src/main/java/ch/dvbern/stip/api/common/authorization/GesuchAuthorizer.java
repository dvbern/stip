package ch.dvbern.stip.api.common.authorization;

import java.util.Objects;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.authorization.util.AuthorizerUtil;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuch.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
@Authorizer
public class GesuchAuthorizer extends BaseAuthorizer {
    private final BenutzerService benutzerService;
    private final GesuchRepository gesuchRepository;
    private final GesuchTrancheRepository gesuchTrancheRepository;
    private final GesuchStatusService gesuchStatusService;
    private final FallRepository fallRepository;

    @Transactional
    public void canGsRead() {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var gesuch = gesuchRepository.findForGs(currentBenutzer.getId()).findFirst();

        if (gesuch.isEmpty()) {
            throw new UnauthorizedException();
        }

        if (AuthorizerUtil.isGesuchstellerOfGesuch(currentBenutzer, gesuch.get())) {
            return;
        }

        throw new UnauthorizedException();
    }

    @Transactional
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

    @Transactional
    public void canUpdate(final UUID gesuchId, final GesuchUpdateDto gesuchUpdateDto) {
        final var gesuchTranche = gesuchTrancheRepository.requireById(gesuchUpdateDto.getGesuchTrancheToWorkWith().getId());
        canUpdate(gesuchId, gesuchTranche.getTyp() == GesuchTrancheTyp.AENDERUNG);
    }

    @Transactional
    public void canUpdate(final UUID gesuchId) {
        canUpdate(gesuchId, false);
    }

    @Transactional
    public void canUpdate(final UUID gesuchId, final boolean aenderung) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();

        if (isAdminOrSb(currentBenutzer)) {
            return;
        }

        final var gesuch = gesuchRepository.requireById(gesuchId);
        if (AuthorizerUtil.isGesuchstellerOfGesuch(currentBenutzer, gesuch) &&
            (gesuchStatusService.benutzerCanEdit(currentBenutzer, gesuch.getGesuchStatus()) || aenderung)
        ) {
            return;
        }

        throw new UnauthorizedException();
    }

    @Transactional
    public void canDelete(final UUID gesuchId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();

        if (isAdminOrSb(currentBenutzer)) {
            return;
        }

        // TODO KSTIP-1057: Check if Gesuchsteller can delete their Gesuch
        final var gesuch = gesuchRepository.requireById(gesuchId);
        if (isGesuchstellerAndNotAdmin(currentBenutzer) &&
            AuthorizerUtil.isGesuchstellerOfGesuch(currentBenutzer, gesuch) &&
            gesuch.getGesuchStatus() == Gesuchstatus.IN_BEARBEITUNG_GS
        ) {
            return;
        }

        throw new UnauthorizedException();
    }

    @Transactional
    public void canCreate() {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var fall = fallRepository.findFallForGsOptional(currentBenutzer.getId());

        if (fall.isEmpty()) {
            return;
        }

        if (Objects.equals(currentBenutzer.getId(), fall.get().getGesuchsteller().getId())) {
            return;
        }

        throw new UnauthorizedException();
    }

    @Transactional
    public void canCreateTranche(final UUID gesuchId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();

        if (isAdmin(currentBenutzer)) {
            return;
        }

        final var gesuch = gesuchRepository.requireById(gesuchId);

        if (isSachbearbeiter(currentBenutzer) && gesuch.getGesuchStatus() == Gesuchstatus.IN_BEARBEITUNG_SB) {
            return;
        }

        throw new UnauthorizedException();
    }
}
