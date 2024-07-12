package ch.dvbern.stip.api.common.entity;

import java.time.LocalDateTime;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
public class AbstractEntityListener {
    @Inject
    Instance<JsonWebToken> token;

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
            final var jwt = token.get();
            return jwt.getClaim(Claims.given_name) + " " + jwt.getClaim(Claims.family_name);
        } else {
            return "System";
        }
    }
}
