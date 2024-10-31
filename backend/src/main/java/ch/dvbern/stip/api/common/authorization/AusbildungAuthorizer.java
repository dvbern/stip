package ch.dvbern.stip.api.common.authorization;

import java.util.Objects;
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungRepository;
import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
@Authorizer
public class AusbildungAuthorizer extends BaseAuthorizer {
    private final BenutzerService benutzerService;
    private final AusbildungRepository ausbildungRepository;
    private final FallRepository fallRepository;

    private boolean isGesuchstellerOfAusbildung(final Benutzer currentBenutzer, final Ausbildung ausbildung) {
        return Objects.equals(
            ausbildung.getFall().getGesuchsteller().getId(),
            currentBenutzer.getId()
        );
    }

    @Transactional
    public void canCreate() {
        // TODO: Check state of ausbildung - only one may be active
    }

    @Transactional
    public void canRead(final UUID ausbildungId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();

        // Admins and Sachbearbeiter can always read every Gesuch
        if (isAdminOrSb(currentBenutzer)) {
            return;
        }

        final var ausbildung = ausbildungRepository.requireById(ausbildungId);

        if (isGesuchstellerOfAusbildung(currentBenutzer, ausbildung)) {
            return;
        }
        throw new UnauthorizedException();
    }

    @Transactional
    public final void canUpdate(final UUID ausbildungId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();

        final var ausbildung = ausbildungRepository.requireById(ausbildungId);

        if (ausbildung.getGesuchs().size() > 1) {
            throw new UnauthorizedException();
        }

        if (ausbildung.getGesuchs().get(0).getGesuchTranchen().size() > 1) {
            throw new UnauthorizedException();
        }

        final var gesuch = ausbildung.getGesuchs().get(0);
        if (isAdminOrSb(currentBenutzer) && gesuch.getGesuchStatus() == Gesuchstatus.IN_BEARBEITUNG_SB) {
            return;
        }

        if (isGesuchsteller(currentBenutzer) &&
            isGesuchstellerOfAusbildung(currentBenutzer, ausbildung) &&
            gesuch.getGesuchStatus() == Gesuchstatus.IN_BEARBEITUNG_GS
        ) {
            return;
        }

        throw new UnauthorizedException();
    }

}
