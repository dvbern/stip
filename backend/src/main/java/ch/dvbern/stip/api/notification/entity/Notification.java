package ch.dvbern.stip.api.notification.entity;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.notification.type.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "notification")
@Getter
@Setter
public class Notification extends AbstractEntity {
    @Enumerated
    @Column(name = "notification_type")
    private NotificationType notificationType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "gesuch_id", nullable = false)
    private Gesuch gesuch;
}
