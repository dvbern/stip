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

package ch.dvbern.stip.api.generator.entities.gesuchtranche;

import java.time.LocalDate;
import java.util.ArrayList;

import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.generator.AbstractTestBuilder;
import ch.dvbern.stip.api.generator.entities.gesuchformular.GesuchFormularTestBuilder;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTrancheBuilder;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;

public final class GesuchTrancheTestBuilder extends AbstractTestBuilder<GesuchTranche, GesuchTrancheTestBuilder> {
    GesuchTrancheTestBuilder(GesuchTranche entity, LocalDate referenceDate) {
        super(entity, referenceDate);
    }

    public static GesuchTrancheTestBuilder empty(LocalDate referenceDate) {
        GesuchTranche gesuchTranche = GesuchTrancheBuilder.gesuchTranche()
            .gueltigkeit(DateRange.getFruehlingOrHerbst(referenceDate.minusMonths(1)))
            .gesuchFormular(null)
            .gesuch(null)
            .status(GesuchTrancheStatus.UEBERPRUEFEN)
            .gesuchDokuments(new ArrayList<>())
            .typ(GesuchTrancheTyp.TRANCHE)
            .build();

        return new GesuchTrancheTestBuilder(gesuchTranche, referenceDate);
    }

    public static GesuchTrancheTestBuilder standardDirectDeps(LocalDate referenceDate) {
        GesuchTranche gesuchTranche = GesuchTrancheBuilder.gesuchTranche()
            .gueltigkeit(DateRange.getFruehlingOrHerbst(referenceDate.minusMonths(1)))
            .gesuchFormular(GesuchFormularTestBuilder.empty(referenceDate).build())
            .gesuch(null)
            .status(GesuchTrancheStatus.UEBERPRUEFEN)
            .gesuchDokuments(new ArrayList<>())
            .typ(GesuchTrancheTyp.TRANCHE)
            .build();

        return new GesuchTrancheTestBuilder(gesuchTranche, referenceDate);
    }

    public static GesuchTrancheTestBuilder standardNestedDeps(LocalDate referenceDate) {
        GesuchTranche gesuchTranche = GesuchTrancheBuilder.gesuchTranche()
            .gueltigkeit(DateRange.getFruehlingOrHerbst(referenceDate.minusMonths(1)))
            .gesuchFormular(null)
            .gesuch(null)
            .status(GesuchTrancheStatus.UEBERPRUEFEN)
            .gesuchDokuments(new ArrayList<>())
            .typ(GesuchTrancheTyp.TRANCHE)
            .build();

        GesuchFormularTestBuilder.standardNestedDeps(referenceDate).withGesuchTranche(gesuchTranche);

        return new GesuchTrancheTestBuilder(gesuchTranche, referenceDate);
    }

    public GesuchTrancheTestBuilder withGesuch(Gesuch gesuch) {
        gesuch.getGesuchTranchen().add(this.entity);
        this.entity.setGesuch(gesuch);
        return this;
    }
}
