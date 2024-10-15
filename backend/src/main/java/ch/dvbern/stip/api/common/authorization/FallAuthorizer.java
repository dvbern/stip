package ch.dvbern.stip.api.common.authorization;

import java.util.Objects;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
@Authorizer
public class FallAuthorizer extends BaseAuthorizer {
    private final BenutzerService benutzerService;
    private final FallRepository fallRepository;

    @Transactional
    public void canRead(final UUID fallId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();

        // Admins and Sachbearbeiter can always read every Fall
        if (isAdminOrSb(currentBenutzer)) {
            return;
        }

        // Gesuchsteller can only read their own Fall
        final var fall = fallRepository.requireById(fallId);
        if (Objects.equals(fall.getGesuchsteller().getId(), currentBenutzer.getId())) {
            return;
        }

        throw new UnauthorizedException();
    }
}
