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

package ch.dvbern.stip.api.personinausbildung.entity;

import java.time.LocalDate;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.common.entity.AbstractFamilieEntity;
import ch.dvbern.stip.api.common.service.NullableUnlessGenerated;
import ch.dvbern.stip.api.common.type.Anrede;
import ch.dvbern.stip.api.common.validation.AhvConstraint;
import ch.dvbern.stip.api.land.entity.Land;
import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Pattern.Flag;
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
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.EMAIL_VALIDATION_PATTERN;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_EMAIL_MESSAGE;

@Audited
@ZustaendigeKESBConstraint
@IdentischerZivilrechtlicherWohnsitzRequiredConstraint
@LandCHRequiredConstraint
@NiederlassungsstatusRequiredConstraint
@EinreisedatumRequiredIfNiederlassungsstatusConstraint
@Entity
@Table(
    name = "person_in_ausbildung",
    indexes = {
        @Index(name = "IX_person_in_ausbildung_adresse_id", columnList = "adresse_id"),
        @Index(name = "IX_person_in_ausbildung_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
@Builder(style = BuilderStyle.STAGED)
@NoArgsConstructor
@AllArgsConstructor
public class PersonInAusbildung extends AbstractFamilieEntity {
    @NotNull
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "adresse_id", foreignKey = @ForeignKey(name = "FK_person_in_ausbildung_adresse_id"))
    private @Valid Adresse adresse;

    @NotNull
    @AhvConstraint
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "sozialversicherungsnummer", nullable = false, length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String sozialversicherungsnummer;

    @NotNull
    @Column(name = "anrede", nullable = false)
    @Enumerated(EnumType.STRING)
    private Anrede anrede;

    @NotNull
    @Column(name = "identischer_zivilrechtlicher_wohnsitz", nullable = false)
    private boolean identischerZivilrechtlicherWohnsitz = true;

    @NullableUnlessGenerated
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "identischer_zivilrechtlicher_wohnsitz_ort", length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String identischerZivilrechtlicherWohnsitzOrt;

    @NullableUnlessGenerated
    @Size(max = DB_DEFAULT_STRING_SMALL_LENGTH)
    @Column(name = "identischer_zivilrechtlicher_wohnsitz_plz", length = DB_DEFAULT_STRING_SMALL_LENGTH)
    private String identischerZivilrechtlicherWohnsitzPLZ;

    @NotNull
    @Pattern(regexp = EMAIL_VALIDATION_PATTERN, message = VALIDATION_EMAIL_MESSAGE, flags = { Flag.CASE_INSENSITIVE })
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "email", nullable = false, length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String email;

    @NotNull
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "telefonnummer", nullable = false, length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String telefonnummer;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(
        name = "nationalitaet_id",
        foreignKey = @ForeignKey(name = "FK_person_in_ausbildung_land_id")
    )
    private Land nationalitaet;

    @NullableUnlessGenerated
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "heimatort", length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String heimatort;

    @NullableUnlessGenerated
    @Size(max = DB_DEFAULT_STRING_SMALL_LENGTH)
    @Column(name = "heimatort_plz", length = DB_DEFAULT_STRING_SMALL_LENGTH)
    private String heimatortPLZ;

    @Enumerated(EnumType.STRING)
    @Column(name = "niederlassungsstatus")
    private Niederlassungsstatus niederlassungsstatus;

    @NullableUnlessGenerated
    @Column(name = "einreisedatum")
    private LocalDate einreisedatum;

    @Enumerated(EnumType.STRING)
    @Column(name = "zivilstand")
    private Zivilstand zivilstand;

    @NotNull
    @Column(name = "sozialhilfebeitraege", nullable = false)
    private boolean sozialhilfebeitraege = false;

    @NotNull
    @Column(name = "vormundschaft", nullable = false)
    private boolean vormundschaft = false;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "korrespondenz_sprache", nullable = false)
    private Sprache korrespondenzSprache;

    @NullableUnlessGenerated
    @Enumerated(EnumType.STRING)
    @Column(name = "zustaendige_kesb")
    private ZustaendigeKESB zustaendigeKESB;
}
