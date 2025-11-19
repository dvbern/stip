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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ch.dvbern.stip.api.adresse.entity.NotStatelessConstraint;
import ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum;
import ch.dvbern.stip.api.ausbildung.type.AusbildungsStatus;
import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.common.service.NullableUnlessGenerated;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.land.entity.Land;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.FutureOrPresent;
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
import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_SMALL_LENGTH;

@Audited
@AusbildungNichtGefundenRequiredFieldsConstraint
@AusbildungNichtGefundenRequiredNullFieldsConstraint
@AusbildungEndDateMustBeAfterStartConstraint
@RequiredFieldsIfAusbildungIsAuslandConstraint
@OnlyOneGesuchPerYearConstraint
@AusbildungBesuchtBMSValidationConstraint
@Entity
@Table(
    name = "ausbildung",
    indexes = {
        @Index(name = "IX_ausbildung_fall_id", columnList = "fall_id"),
        @Index(name = "IX_ausbildung_ausbildungsgang_id", columnList = "ausbildungsgang_id"),
        @Index(name = "IX_ausbildung_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
@Builder(style = BuilderStyle.STAGED)
@NoArgsConstructor
@AllArgsConstructor
public class Ausbildung extends AbstractMandantEntity {
    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "fall_id", foreignKey = @ForeignKey(name = "FK_ausbildung_fall_id"))
    private Fall fall;

    @NotNull
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "ausbildung")
    private List<Gesuch> gesuchs = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "ausbildungsgang_id", foreignKey = @ForeignKey(name = "FK_ausbildung_ausbildungsgang_id"))
    private Ausbildungsgang ausbildungsgang;

    @NotNull
    @Column(name = "besucht_bms", nullable = false)
    private boolean besuchtBMS = false;

    @NullableUnlessGenerated
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "alternative_ausbildungsgang", length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String alternativeAusbildungsgang;

    @NullableUnlessGenerated
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "alternative_ausbildungsstaette", length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String alternativeAusbildungsstaette;

    @NullableUnlessGenerated
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "fachrichtung_berufsbezeichnung", length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String fachrichtungBerufsbezeichnung;

    @Column(name = "ausbildung_nicht_gefunden", nullable = false)
    private boolean ausbildungNichtGefunden = false;

    @NotNull
    @Column(name = "ausbildung_begin", nullable = false)
    private LocalDate ausbildungBegin;

    @NotNull
    @Column(name = "ausbildung_end", nullable = false)
    @FutureOrPresent
    private LocalDate ausbildungEnd;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "pensum", nullable = false)
    private AusbildungsPensum pensum;

    @NullableUnlessGenerated
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "ausbildungsort", length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String ausbildungsort;

    @NullableUnlessGenerated
    @Size(max = DB_DEFAULT_STRING_SMALL_LENGTH)
    @Column(name = "ausbildungsort_plz", length = DB_DEFAULT_STRING_SMALL_LENGTH)
    private String ausbildungsortPLZ;

    @NotNull
    @Column(name = "is_ausbildung_ausland")
    private Boolean isAusbildungAusland = false;

    @NullableUnlessGenerated
    @ManyToOne(optional = true)
    @JoinColumn(
        name = "land_id",
        foreignKey = @ForeignKey(name = "FK_ausbildung_land_id")
    )
    @NotStatelessConstraint
    private Land land;

    @NotNull
    @Column(name = "status", nullable = false)
    private AusbildungsStatus status = AusbildungsStatus.AKTIV;

    @Transient
    public String getAusbildungsstaetteOrAlternative(final Locale locale) {
        if (isAusbildungNichtGefunden()) {
            return getAlternativeAusbildungsstaette();
        } else {
            return locale.getLanguage().equals("de")
                ? getAusbildungsgang().getAusbildungsstaette().getNameDe()
                : getAusbildungsgang().getAusbildungsstaette().getNameFr();
        }
    }

    @Transient
    public String getAusbildungsgangOrAlternative(final Locale locale) {
        if (isAusbildungNichtGefunden()) {
            return getAlternativeAusbildungsgang();
        } else {
            return locale.getLanguage().equals("de")
                ? getAusbildungsgang().getAbschluss().getBezeichnungDe()
                : getAusbildungsgang().getAbschluss().getBezeichnungFr();
        }
    }
}
