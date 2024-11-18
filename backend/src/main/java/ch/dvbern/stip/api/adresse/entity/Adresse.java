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

package ch.dvbern.stip.api.adresse.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.stammdaten.type.Land;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;
import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_SMALL_VALUE_LENGTH;

@Entity
@Table(
    name = "adresse",
    indexes = @Index(name = "IX_adresse_mandant", columnList = "mandant")
)
@Audited
@Getter
@Setter
public class Adresse extends AbstractMandantEntity {
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "land", nullable = false)
    @NotStatelessConstraint
    private Land land = Land.CH;

    @Nullable
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(name = "co_adresse")
    private String coAdresse;

    @NotNull
    @NotBlank
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(name = "strasse", nullable = false)
    private String strasse;

    @Nullable
    @Size(max = DB_DEFAULT_SMALL_VALUE_LENGTH)
    @Column(name = "hausnummer")
    private String hausnummer;

    @NotNull
    @NotBlank
    @Size(max = DB_DEFAULT_SMALL_VALUE_LENGTH)
    @Column(name = "plz", nullable = false)
    private String plz;

    @NotNull
    @NotBlank
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(name = "ort", nullable = false)
    private String ort;
}
