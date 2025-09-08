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

package ch.dvbern.stip.api.ausbildung.entity;

import java.util.ArrayList;
import java.util.List;

import ch.dvbern.stip.api.ausbildung.type.AusbildungsstaetteNummerTyp;
import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.common.util.Constants;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@BurAndCTNummerTypAlphaNumericConstraint
@OnlyNummerTypOfOhneNummerCanBeNullableConstraint
@Audited
@Entity
@Table(name = "ausbildungsstaette", indexes = @Index(name = "IX_ausbildungsstaette_mandant", columnList = "mandant"))
@Getter
@Setter
public class Ausbildungsstaette extends AbstractMandantEntity {
    @NotNull
    @Size(max = Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "name_de", nullable = false, length = Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String nameDe;

    @NotNull
    @Size(max = Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "name_fr", nullable = false, length = Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String nameFr;

    @Nullable
    @Size(max = Constants.DB_DEFAULT_STRING_SMALL_LENGTH)
    @Column(name = "nummer", length = Constants.DB_DEFAULT_STRING_SMALL_LENGTH)
    private String nummer;

    @Enumerated(EnumType.STRING)
    @Column(name = "nummer_typ", nullable = false)
    private AusbildungsstaetteNummerTyp nummerTyp;

    @NotNull
    @Column(name = "aktiv", nullable = false)
    private boolean aktiv = true;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "ausbildungsstaette")
    private List<Ausbildungsgang> ausbildungsgaenge = new ArrayList<>();
}
