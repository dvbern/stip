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

package ch.dvbern.stip.api.personinausbildung.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.common.entity.AbstractFamilieEntity;
import ch.dvbern.stip.api.common.type.Anrede;
import ch.dvbern.stip.api.common.validation.AhvConstraint;
import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
import ch.dvbern.stip.api.stammdaten.type.Land;
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
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;
import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_SMALL_VALUE_LENGTH;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.EMAIL_VALIDATION_PATTERN;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_EMAIL_MESSAGE;

@Audited
@IdentischerZivilrechtlicherWohnsitzRequiredConstraint
@LandCHRequiredConstraint
@NiederlassungsstatusRequiredConstraint
@EinreisedatumRequiredIfNiederlassungsstatusConstraint
@VermoegenVorjahrRequiredConstraint
@Entity
@Table(indexes = {
    @Index(name = "IX_person_in_ausbildung_adresse_id", columnList = "adresse_id"),
    @Index(name = "IX_person_in_ausbildung_mandant", columnList = "mandant")
})
@Getter
@Setter
public class PersonInAusbildung extends AbstractFamilieEntity {

    @NotNull
    @OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_person_in_ausbildung_adresse_id"), nullable = false)
    private @Valid Adresse adresse;

    @NotNull
    @AhvConstraint
    @Column(nullable = false)
    private String sozialversicherungsnummer;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Anrede anrede;

    @NotNull
    @Column(nullable = false)
    private boolean identischerZivilrechtlicherWohnsitz = true;

    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = true)
    private String identischerZivilrechtlicherWohnsitzOrt;

    @Size(max = DB_DEFAULT_SMALL_VALUE_LENGTH)
    @Column(nullable = true)
    private String identischerZivilrechtlicherWohnsitzPLZ;

    @NotNull
    @Pattern(regexp = EMAIL_VALIDATION_PATTERN, message = VALIDATION_EMAIL_MESSAGE)
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false)
    private String email;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false)
    private String telefonnummer;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Land nationalitaet = Land.CH;

    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = true)
    private String heimatort;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Niederlassungsstatus niederlassungsstatus;

    @Column(nullable = true)
    private LocalDate einreisedatum;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Zivilstand zivilstand;

    @NotNull
    @Column(nullable = false)
    private boolean sozialhilfebeitraege = true;

    @NotNull
    @Column(nullable = false)
    private boolean vormundschaft = false;

    @Nullable
    @Column(nullable = true)
    private BigDecimal vermoegenVorjahr;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sprache korrespondenzSprache;

    public boolean isQuellenbesteuert() {
        return nationalitaet != Land.CH &&
            (
                niederlassungsstatus == Niederlassungsstatus.AUFENTHALTSBEWILLIGUNG_B ||
                niederlassungsstatus == Niederlassungsstatus.FLUECHTLING
            );
    }
}
