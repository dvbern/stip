package ch.dvbern.stip.api.common.entity;

import java.time.LocalDateTime;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class AbstractEntityListener {

    @PrePersist
    protected void prePersist(AbstractEntity entity) {
        LocalDateTime now = LocalDateTime.now();
        entity.setTimestampErstellt(now);
        entity.setTimestampMutiert(now);
        entity.setUserErstellt("TODO");
        entity.setUserMutiert("TODO");
    }

    @PreUpdate
    public void preUpdate(AbstractEntity entity) {
        entity.setTimestampMutiert(LocalDateTime.now());
        entity.setUserMutiert("TODO");
    }
}
