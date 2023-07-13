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

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.geschwister.entity.Geschwister;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.partner.entity.Partner;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Audited
@Entity
@Getter
@Setter
public class GesuchFormular extends AbstractEntity {

    @OneToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_gesuch_formular_person_in_ausbildung_id"), nullable = true)
    private PersonInAusbildung personInAusbildung;

    @OneToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_gesuch_formular_ausbildung_id"), nullable = true)
    private Ausbildung ausbildung;

    @OneToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_gesuch_formular_familiensituation_id"), nullable = true)
    private Familiensituation familiensituation;

    @OneToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_gesuch_formular_partner_id"), nullable = true)
    private Partner partner;

    @OneToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_gesuch_formular_auszahlung_id"), nullable = true)
    private Auszahlung auszahlung;

    @Valid
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "gesuch_formular_id")
    @OrderBy("von")
    private Set<LebenslaufItem> lebenslaufItems = new LinkedHashSet<>();

    @Valid
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "gesuch_formular_id")
    @OrderBy("geburtsdatum")
    private Set<Geschwister> geschwisters = new LinkedHashSet<>();

    @Valid
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "gesuch_formular_id")
    private Set<Eltern> elterns = new LinkedHashSet<>();

    @Valid
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "gesuch_formular_id")
    @OrderBy("geburtsdatum")
    private Set<Kind> kinds = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        GesuchFormular that = (GesuchFormular) o;
        return Objects.equals(getPersonInAusbildung(), that.getPersonInAusbildung())
                && Objects.equals(
                getAusbildung(),
                that.getAusbildung())
                && Objects.equals(getFamiliensituation(), that.getFamiliensituation())
                && Objects.equals(getPartner(), that.getPartner())
                && Objects.equals(getAuszahlung(), that.getAuszahlung())
                && Objects.equals(getLebenslaufItems(), that.getLebenslaufItems())
                && Objects.equals(getGeschwisters(), that.getGeschwisters())
                && Objects.equals(getElterns(), that.getElterns())
                && Objects.equals(getKinds(), that.getKinds());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(),
                getPersonInAusbildung(),
                getAusbildung(),
                getFamiliensituation(),
                getPartner(),
                getAuszahlung(),
                getLebenslaufItems(),
                getGeschwisters(),
                getElterns(),
                getKinds());
    }
}
