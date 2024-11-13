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

package ch.dvbern.stip.api.eltern.entity;

import java.util.UUID;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.common.entity.AbstractPerson;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;
import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_SMALL_VALUE_LENGTH;

@Audited
@Entity
@IdentischerZivilrechtlicherWohnsitzRequiredConstraint
@Table(
    name = "eltern",
    indexes = {
        @Index(name = "IX_eltern_adresse_id", columnList = "adresse_id"),
        @Index(name = "IX_eltern_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
@AhvIfSwissConstraint
public class Eltern extends AbstractPerson {
    @NotNull
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "adresse_id", foreignKey = @ForeignKey(name = "FK_eltern_adresse_id"))
    private Adresse adresse;

    @Nullable
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(name = "sozialversicherungsnummer")
    private String sozialversicherungsnummer;

    @NotNull
    @Column(name = "eltern_typ", nullable = false)
    @Enumerated(EnumType.STRING)
    private ElternTyp elternTyp;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(name = "telefonnummer", nullable = false)
    private String telefonnummer;

    @NotNull
    @Column(name = "ausweisb_fluechtling", nullable = false)
    private Boolean ausweisbFluechtling;

    @NotNull
    @Column(name = "identischer_zivilrechtlicher_wohnsitz", nullable = false)
    private boolean identischerZivilrechtlicherWohnsitz = true;

    @Nullable
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(name = "identischer_zivilrechtlicher_wohnsitz_ort")
    private String identischerZivilrechtlicherWohnsitzOrt;

    @Nullable
    @Size(max = DB_DEFAULT_SMALL_VALUE_LENGTH)
    @Column(name = "identischer_zivilrechtlicher_wohnsitz_plz")
    private String identischerZivilrechtlicherWohnsitzPLZ;

    @Nullable
    @Column(name = "copy_of_id")
    private UUID copyOfId;

    @NotNull
    @Column(name = "sozialhilfebeitraege", nullable = false)
    private boolean sozialhilfebeitraege = true;

    @NotNull
    @Column(name = "ergaenzungsleistungen", nullable = false)
    @Min(0)
    private Integer ergaenzungsleistungen;

    @NotNull
    @Column(name = "wohnkosten", nullable = false)
    @Min(0)
    private Integer wohnkosten;
}
