package ch.dvbern.stip.api;

import java.util.HashSet;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.repo.BenutzerRepository;
import ch.dvbern.stip.api.benutzer.repo.SachbearbeiterZuordnungStammdatenRepository;
import ch.dvbern.stip.api.benutzer.service.BenutzerMapper;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.benutzer.service.RolleService;
import ch.dvbern.stip.api.benutzer.service.SachbearbeiterZuordnungStammdatenMapper;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.test.Mock;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Mock
@RequestScoped
public class BenutzerServiceMock extends BenutzerService {
    private final BenutzerMapper benutzerMapper;
    private final JsonWebToken jsonWebToken;
    private static final HashSet<String> SEEN_BENUTZERS = new HashSet<>();

    public BenutzerServiceMock() {
        super(
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );

        benutzerMapper = null;
        jsonWebToken = null;
    }

    @Inject
    public BenutzerServiceMock(
        JsonWebToken jsonWebToken,
        BenutzerMapper benutzerMapper,
        SachbearbeiterZuordnungStammdatenMapper sachbearbeiterZuordnungStammdatenMapper,
        BenutzerRepository benutzerRepository,
        RolleService rolleService,
        SachbearbeiterZuordnungStammdatenRepository sachbearbeiterZuordnungStammdatenRepository,
        SecurityIdentity identity) {
        super(
            jsonWebToken,
            benutzerMapper,
            sachbearbeiterZuordnungStammdatenMapper,
            benutzerRepository,
            rolleService,
            sachbearbeiterZuordnungStammdatenRepository,
            identity);

        this.benutzerMapper = benutzerMapper;
        this.jsonWebToken = jsonWebToken;
    }

    @Override
    @Transactional
    public Benutzer getCurrentBenutzer() {
        if (SEEN_BENUTZERS.contains(jsonWebToken.getSubject())) {
            return super.getCurrentBenutzer();
        } else {
            SEEN_BENUTZERS.add(jsonWebToken.getSubject());
            super.getOrCreateAndUpdateCurrentBenutzer();
            return super.getCurrentBenutzer();
        }
    }
}
