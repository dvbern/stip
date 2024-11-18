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

package ch.dvbern.stip.api.common.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import ch.dvbern.stip.api.common.util.Constants;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.envers.Audited;
import org.hibernate.proxy.HibernateProxy;

@MappedSuperclass
@Audited
@EntityListeners(AbstractEntityListener.class)
@Getter
@Setter
public abstract class AbstractEntity implements Serializable {
    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    @UuidGenerator
    private UUID id;

    @Column(name = "version")
    @Version
    private long version;

    @Column(name = "timestamp_erstellt", nullable = false)
    private LocalDateTime timestampErstellt;

    @Column(name = "timestamp_mutiert", nullable = false)
    private LocalDateTime timestampMutiert;

    @Size(max = Constants.DB_DEFAULT_MID_LENGTH)
    @Column(name = "user_erstellt", nullable = false)
    private String userErstellt;

    @Size(max = Constants.DB_DEFAULT_MID_LENGTH)
    @Column(name = "user_mutiert", nullable = false)
    private String userMutiert;

    // java:S2097 https://sonarqube-next.dvbern.ch/coding_rules?open=java%3AS2097&rule_key=java%3AS2097
    // This is already done, but SonarQube doesn't understand HibernateProxy
    @SuppressWarnings("java:S2097")
    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }

        Class<?> oEffectiveClass = o instanceof HibernateProxy hibernateProxy
            ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass()
            : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy hibernateProxy
            ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass()
            : this.getClass();

        if (thisEffectiveClass != oEffectiveClass) {
            return false;
        }

        AbstractEntity that = (AbstractEntity) o;

        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy hibernateProxy
            ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass().hashCode()
            : getClass().hashCode();
    }
}
