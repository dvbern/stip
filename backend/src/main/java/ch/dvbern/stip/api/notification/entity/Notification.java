/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.notification.entity;

import java.util.UUID;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.notification.type.NotificationType;
import jakarta.annotation.Nullable;
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
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MAX_LENGTH;
import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH;

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
    @Size(max = DB_DEFAULT_STRING_MAX_LENGTH)
    @Column(name = "notification_text", length = DB_DEFAULT_STRING_MAX_LENGTH)
    private String notificationText;

    @ManyToOne(optional = false)
    @JoinColumn(
        name = "gesuch_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "FK_notification_gesuch")
    )
    private Gesuch gesuch;

    @Nullable
    @Column(name = "context_id")
    private UUID contextId;

    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "absender")
    private String absender;
}
