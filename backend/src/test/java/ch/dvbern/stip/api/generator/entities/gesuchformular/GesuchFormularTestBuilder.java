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

package ch.dvbern.stip.api.generator.entities.gesuchformular;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import ch.dvbern.stip.api.generator.AbstractTestBuilder;
import ch.dvbern.stip.api.generator.entities.einnahmen_kosten.EinnahmenKostenTestBuilder;
import ch.dvbern.stip.api.generator.entities.familiensituation.FamiliensituationTestBuilder;
import ch.dvbern.stip.api.generator.entities.lebenslauf.LebenslaufItemTestBuilder;
import ch.dvbern.stip.api.generator.entities.personinausbildung.PersonInAusbildungTestBuilder;
import ch.dvbern.stip.api.generator.entities.steuererklaerung.SteuererklaerungTestBuilder;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormularBuilder;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;

public final class GesuchFormularTestBuilder extends AbstractTestBuilder<GesuchFormular, GesuchFormularTestBuilder> {
    GesuchFormularTestBuilder(GesuchFormular entity, LocalDate referenceDate) {
        super(entity, referenceDate);
    }

    public static GesuchFormularTestBuilder empty(LocalDate referenceDate) {
        GesuchFormular gesuchFormular = GesuchFormularBuilder.gesuchFormular()
            .personInAusbildung(null)
            .familiensituation(null)
            .partner(null)
            .einnahmenKosten(null)
            .lebenslaufItems(new HashSet<>())
            .geschwisters(new HashSet<>())
            .elterns(new HashSet<>())
            .kinds(new HashSet<>())
            .tranche(null)
            .steuerdaten(new HashSet<>())
            .steuererklaerung(new HashSet<>())
            .versteckteEltern(new HashSet<>())
            .build();

        return new GesuchFormularTestBuilder(gesuchFormular, referenceDate);
    }

    public static GesuchFormularTestBuilder standardNestedDeps(LocalDate referenceDate) {
        GesuchFormular gesuchFormular = GesuchFormularBuilder.gesuchFormular()
            .personInAusbildung(PersonInAusbildungTestBuilder.standardNestedDeps(referenceDate).build())
            .familiensituation(FamiliensituationTestBuilder.withoutEltern(referenceDate).build())
            .partner(null)
            .einnahmenKosten(EinnahmenKostenTestBuilder.empty(referenceDate).build())
            .lebenslaufItems(new HashSet<>(List.of(LebenslaufItemTestBuilder.empty(referenceDate).build())))
            .geschwisters(new HashSet<>())
            .elterns(new HashSet<>())
            .kinds(new HashSet<>())
            .tranche(null)
            .steuerdaten(new HashSet<>())
            .steuererklaerung(new HashSet<>(List.of(SteuererklaerungTestBuilder.empty(referenceDate).build())))
            .versteckteEltern(new HashSet<>())
            .build();

        return new GesuchFormularTestBuilder(gesuchFormular, referenceDate);
    }

    public GesuchFormularTestBuilder withGesuchTranche(GesuchTranche gesuchTranche) {
        gesuchTranche.setGesuchFormular(this.entity);
        this.entity.setTranche(gesuchTranche);
        return this;
    }
}
