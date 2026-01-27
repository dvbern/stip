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

package ch.dvbern.stip.api.zahlungsverbindung.entity;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.common.validation.IbanConstraint;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.jilt.Builder;
import org.jilt.BuilderStyle;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH;

@Iso2OnZahlungsverbindungMustBeSet(property = "adresse.land")
@VornameNachnameOrInstitutionRequiredConstraint
@Audited
@Entity
@Table(
    name = "zahlungsverbindung",
    indexes = {
        @Index(name = "IX_zahlungsverbindung_adresse_id", columnList = "adresse_id"),
        @Index(name = "IX_zahlungsverbindung_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
@Builder(style = BuilderStyle.STAGED)
@NoArgsConstructor
@AllArgsConstructor
public class Zahlungsverbindung extends AbstractMandantEntity {

    @Nullable
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "vorname", length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String vorname;

    @Nullable
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "nachname", length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String nachname;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "adresse_id", foreignKey = @ForeignKey(name = "FK_zahlungsverbindung_adresse_id"))
    private Adresse adresse;

    @NotNull
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "iban", nullable = false, length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @IbanConstraint
    private String iban;

    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "institution", length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String institution;
}
