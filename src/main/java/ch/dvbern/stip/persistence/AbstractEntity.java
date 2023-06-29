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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.persistence;

import ch.dvbern.stip.shared.util.Constants;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.envers.Audited;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
@Audited
@EntityListeners(AbstractEntityListener.class)
@Getter
@Setter
public abstract class AbstractEntity implements Serializable {

    @Id
    @Column(nullable = false, updatable = false, length = 36)
    @UuidGenerator
    private UUID id;

    @Version
    @NotNull
    private long version;

    @Column(nullable = false)
    private LocalDateTime timestampErstellt;

    @Column(nullable = false)
    private LocalDateTime timestampMutiert;

    @Size(max = Constants.DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false, length = Constants.DB_DEFAULT_MAX_LENGTH)
    private String userErstellt;

    @Size(max = Constants.DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false, length = Constants.DB_DEFAULT_MAX_LENGTH)
    private String userMutiert;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractEntity that = (AbstractEntity) o;
        return getVersion() == that.getVersion()
                && Objects.equals(getId(), that.getId())
                && Objects.equals(getTimestampErstellt(), that.getTimestampErstellt())
                && Objects.equals(getTimestampMutiert(), that.getTimestampMutiert())
                && Objects.equals(getUserErstellt(), that.getUserErstellt())
                && Objects.equals(getUserMutiert(), that.getUserMutiert());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getId(),
                getVersion(),
                getTimestampErstellt(),
                getTimestampMutiert(),
                getUserErstellt(),
                getUserMutiert());
    }

}
