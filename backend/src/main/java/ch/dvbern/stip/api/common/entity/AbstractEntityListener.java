package ch.dvbern.stip.api.common.entity;

import java.time.LocalDateTime;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
public class AbstractEntityListener {
    @Inject
    Instance<JsonWebToken> token;

    @Inject
    BenutzerService benutzerService;

    @PrePersist
    protected void prePersist(AbstractEntity entity) {
        LocalDateTime now = LocalDateTime.now();
        entity.setTimestampErstellt(now);
        entity.setTimestampMutiert(now);

        final var currentBenutzername = getBenutzername();
        entity.setUserErstellt(currentBenutzername);
        entity.setUserMutiert(currentBenutzername);
    }

    @PreUpdate
    public void preUpdate(AbstractEntity entity) {
        entity.setTimestampMutiert(LocalDateTime.now());
        entity.setUserMutiert(getBenutzername());
    }

    private String getBenutzername() {
        if (token != null && token.isResolvable()) {
            if (token.get().getSubject() != null) {
                return benutzerService.getCurrentBenutzernameFromJwt();
            } else {
                return "System";
            }
        } else {
            return "System";
        }
    }
}
