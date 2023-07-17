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

package ch.dvbern.stip.api.common.entity;

import ch.dvbern.stip.api.common.util.Constants;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.envers.Audited;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Audited
@EntityListeners(AbstractEntityListener.class)
@Getter
@Setter
@EqualsAndHashCode
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
    @Column(nullable = false)
    private String userErstellt;

    @Size(max = Constants.DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false)
    private String userMutiert;
}
