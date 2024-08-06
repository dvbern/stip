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

package ch.dvbern.stip.api.gesuch.entity;

import java.util.LinkedHashSet;
import java.util.Set;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.common.validation.HasPageValidation;
import ch.dvbern.stip.api.common.validation.Severity;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.geschwister.entity.Geschwister;
import ch.dvbern.stip.api.gesuch.validation.AusbildungPageValidation;
import ch.dvbern.stip.api.gesuch.validation.DocumentsRequiredValidationGroup;
import ch.dvbern.stip.api.gesuch.validation.EinnahmenKostenPageValidation;
import ch.dvbern.stip.api.gesuch.validation.ElternPageValidation;
import ch.dvbern.stip.api.gesuch.validation.FamiliensituationPageValidation;
import ch.dvbern.stip.api.gesuch.validation.GeschwisterPageValidation;
import ch.dvbern.stip.api.gesuch.validation.GesuchEinreichenValidationGroup;
import ch.dvbern.stip.api.gesuch.validation.KindPageValidation;
import ch.dvbern.stip.api.gesuch.validation.LebenslaufItemPageValidation;
import ch.dvbern.stip.api.gesuch.validation.PartnerPageValidation;
import ch.dvbern.stip.api.gesuch.validation.PersonInAusbildungPageValidation;
import ch.dvbern.stip.api.gesuch.validation.SteuerdatenPageValidation;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.partner.entity.Partner;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.api.steuerdaten.entity.SteuerdatenSteuerjahrInPastOrCurrentConstraint;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Audited
@FamiliensituationElternEntityRequiredConstraint(groups = {
    GesuchEinreichenValidationGroup.class,
    ElternPageValidation.class
}, property = "elterns")
@LebenslaufLuckenlosConstraint(groups = {
    GesuchEinreichenValidationGroup.class,
    LebenslaufItemPageValidation.class
}, property = "lebenslaufItems")
@EinnahmenKostenAlimenteRequiredConstraint(groups = {
    GesuchEinreichenValidationGroup.class,
    EinnahmenKostenPageValidation.class
}, property = "einnahmenKosten")
@EinnahmenKostenRentenRequiredConstraint(groups = {
    GesuchEinreichenValidationGroup.class,
    EinnahmenKostenPageValidation.class
}, property = "einnahmenKosten")
@EinnahmenKostenZulagenRequiredConstraint(groups = {
    GesuchEinreichenValidationGroup.class,
    EinnahmenKostenPageValidation.class
}, property = "einnahmenKosten")
@EinnahmenKostenDarlehenRequiredConstraint(groups = {
    GesuchEinreichenValidationGroup.class,
    EinnahmenKostenPageValidation.class
}, property = "einnahmenKosten")
@EinnahmenKostenWohnkostenRequiredConstraint(groups = {
    GesuchEinreichenValidationGroup.class,
    EinnahmenKostenPageValidation.class
}, property = "einnahmenKosten")
@EinnahmenKostenBetreuungskostenRequiredConstraint(groups = {
    GesuchEinreichenValidationGroup.class,
    EinnahmenKostenPageValidation.class
}, property = "einnahmenKosten")
@EinnahmenKostenSteuerjahrInPastOrCurrentConstraint(groups = {
    Default.class,
    EinnahmenKostenPageValidation.class
}, property = "einnahmenKosten")
@SteuerdatenSteuerjahrInPastOrCurrentConstraint(groups = {
    Default.class,
    EinnahmenKostenPageValidation.class
}, property = "steuerdaten")
@AusbildungskostenStufeRequiredConstraint(groups = {
    GesuchEinreichenValidationGroup.class,
    EinnahmenKostenPageValidation.class
}, property = "einnahmenKosten")
@EinnahmenKostenVermoegenRequiredConstraint(groups = {
    GesuchEinreichenValidationGroup.class,
    EinnahmenKostenPageValidation.class
}, property = "einnahmenKosten")
@LebenslaufAusbildungUeberschneidenConstraint(groups = {
    GesuchEinreichenValidationGroup.class,
    LebenslaufItemPageValidation.class
}, property = "lebenslaufItems")
@PartnerNullRequiredWhenAlleinstehendConstraint(groups = {
    GesuchEinreichenValidationGroup.class,
    PartnerPageValidation.class
}, property = "partner")
@AlimenteRequiredWhenAlimenteregelungConstraint(groups = {
    GesuchEinreichenValidationGroup.class,
    EinnahmenKostenPageValidation.class
}, property = "einnahmenKosten")
@DocumentsRequiredConstraint(groups = {
    DocumentsRequiredValidationGroup.class
}, payload = Severity.Warning.class)
@DocumentsRequiredConstraint(groups = {
    GesuchEinreichenValidationGroup.class
})
@SteuerdatenTabsRequiredConstraint(groups = {
    GesuchEinreichenValidationGroup.class,
    SteuerdatenPageValidation.class
}, property = "steuerdatenTabs")
@NoOverlapInAusbildungenConstraint(property = "lebenslaufItems")
@UniqueSvNumberConstraint
@Entity
@Table(
    name = "gesuch_formular",
    indexes = {
        @Index(name = "IX_gesuch_formular_person_in_ausbildung_id", columnList = "person_in_ausbildung_id"),
        @Index(name = "IX_gesuch_formular_ausbildung_id", columnList = "ausbildung_id"),
        @Index(name = "IX_gesuch_formular_familiensituation_id", columnList = "familiensituation_id"),
        @Index(name = "IX_gesuch_formular_partner_id", columnList = "partner_id"),
        @Index(name = "FK_gesuch_formular_auszahlung_id", columnList = "auszahlung_id"),
        @Index(name = "FK_gesuch_formular_einnahmen_kosten_id", columnList = "einnahmen_kosten_id"),
        @Index(name = "IX_gesuch_formular_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
public class GesuchFormular extends AbstractMandantEntity {
    @NotNull(groups = GesuchEinreichenValidationGroup.class)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "person_in_ausbildung_id",
        foreignKey = @ForeignKey(name = "FK_gesuch_formular_person_in_ausbildung_id"))
    @HasPageValidation(PersonInAusbildungPageValidation.class)
    private @Valid PersonInAusbildung personInAusbildung;

    @NotNull(groups = GesuchEinreichenValidationGroup.class)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "ausbildung_id",
        foreignKey = @ForeignKey(name = "FK_gesuch_formular_ausbildung_id"))
    @HasPageValidation(AusbildungPageValidation.class)
    private @Valid Ausbildung ausbildung;

    @NotNull(groups = GesuchEinreichenValidationGroup.class)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "familiensituation_id",
        foreignKey = @ForeignKey(name = "FK_gesuch_formular_familiensituation_id"))
    @HasPageValidation(FamiliensituationPageValidation.class)
    private @Valid Familiensituation familiensituation;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "partner_id", foreignKey = @ForeignKey(name = "FK_gesuch_formular_partner_id"))
    @HasPageValidation(PartnerPageValidation.class)
    private @Valid Partner partner;

    @NotNull(groups = GesuchEinreichenValidationGroup.class)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "auszahlung_id", foreignKey = @ForeignKey(name = "FK_gesuch_formular_auszahlung_id"))
    @HasPageValidation(AusbildungPageValidation.class)
    private @Valid Auszahlung auszahlung;

    @NotNull(groups = GesuchEinreichenValidationGroup.class)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "einnahmen_kosten_id",
        foreignKey = @ForeignKey(name = "FK_gesuch_formular_einnahmen_kosten_id"))
    @HasPageValidation(EinnahmenKostenPageValidation.class)
    private @Valid EinnahmenKosten einnahmenKosten;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "gesuch_formular_id", referencedColumnName = "id", nullable = false)
    @OrderBy("von")
    @HasPageValidation(LebenslaufItemPageValidation.class)
    private @Valid Set<LebenslaufItem> lebenslaufItems = new LinkedHashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "gesuch_formular_id", referencedColumnName = "id", nullable = false)
    @OrderBy("geburtsdatum")
    @HasPageValidation(GeschwisterPageValidation.class)
    private @Valid Set<Geschwister> geschwisters = new LinkedHashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "gesuch_formular_id", referencedColumnName = "id", nullable = false)
    @HasPageValidation(ElternPageValidation.class)
    private @Valid Set<Eltern> elterns = new LinkedHashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "gesuch_formular_id", referencedColumnName = "id", nullable = false)
    @OrderBy("geburtsdatum")
    @HasPageValidation(KindPageValidation.class)
    private @Valid Set<Kind> kinds = new LinkedHashSet<>();

    @OneToOne(optional = false, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "gesuchFormular")
    private @Valid GesuchTranche tranche;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "gesuch_formular_id", referencedColumnName = "id", nullable = false)
    @HasPageValidation(SteuerdatenPageValidation.class)
    private @Valid Set<Steuerdaten> steuerdaten = new LinkedHashSet<>();
}
