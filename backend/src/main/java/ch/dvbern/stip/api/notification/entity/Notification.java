package ch.dvbern.stip.api.notification.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.notification.type.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(
    name = "notification",
    indexes = {
        @Index(name = "IX_notification_gesuch_id", columnList = "gesuch_id"),
        @Index(name = "IX_notification_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
public class Notification extends AbstractMandantEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type")
    private NotificationType notificationType;

    @NotNull
    @Column(name = "notification_text")
    private String notificationText;

    @ManyToOne(optional = false)
    @JoinColumn(
        name = "gesuch_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "FK_notification_gesuch")
    )
    private Gesuch gesuch;
}
