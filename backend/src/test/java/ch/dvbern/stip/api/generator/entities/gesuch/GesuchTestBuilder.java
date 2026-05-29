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

package ch.dvbern.stip.api.generator.entities.gesuch;

import java.time.LocalDate;
import java.util.ArrayList;

import ch.dvbern.stip.api.generator.AbstractTestBuilder;
import ch.dvbern.stip.api.generator.entities.ausbildung.AusbildungTestBuilder;
import ch.dvbern.stip.api.generator.entities.gesuchsperiode.GesuchsperiodeTestBuilder;
import ch.dvbern.stip.api.generator.entities.gesuchtranche.GesuchTrancheTestBuilder;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchBuilder;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;

public final class GesuchTestBuilder extends AbstractTestBuilder<Gesuch, GesuchTestBuilder> {

    GesuchTestBuilder(Gesuch entity, LocalDate referenceDate) {
        super(entity, referenceDate);
    }

    public static GesuchTestBuilder empty(LocalDate referenceDate) {
        Gesuch gesuch = GesuchBuilder.gesuch()
            .ausbildung(null)
            .gesuchsperiode(null)
            .gesuchStatus(Gesuchstatus.EINGEREICHT)
            .gesuchTranchen(new ArrayList<>())
            .unterschriftenblaetter(new ArrayList<>())
            .datenschutzbriefs(new ArrayList<>())
            .beschwerdeVerlauf(new ArrayList<>())
            .beschwerdeHaengig(false)
            .beschwerdeEntscheids(new ArrayList<>())
            .verfuegt(false)
            .wasInBereitFuerBearbeitung(false)
            .verfuegungs(new ArrayList<>())
            .sachbearbeiterGesuchDokuments(new ArrayList<>())
            .einreichedatum(referenceDate)
            .build();

        return new GesuchTestBuilder(gesuch, referenceDate);
    }

    public static GesuchTestBuilder standardWithNestedDeps(LocalDate referenceDate) {
        Gesuch gesuch = GesuchBuilder.gesuch()
            .ausbildung(AusbildungTestBuilder.standardNestedDeps(referenceDate).build())
            .gesuchsperiode(GesuchsperiodeTestBuilder.standardDirectDepsFruehling(referenceDate).build())
            .gesuchStatus(Gesuchstatus.EINGEREICHT)
            .gesuchTranchen(new ArrayList<>())
            .unterschriftenblaetter(new ArrayList<>())
            .datenschutzbriefs(new ArrayList<>())
            .beschwerdeVerlauf(new ArrayList<>())
            .beschwerdeHaengig(false)
            .beschwerdeEntscheids(new ArrayList<>())
            .verfuegt(false)
            .wasInBereitFuerBearbeitung(false)
            .verfuegungs(new ArrayList<>())
            .sachbearbeiterGesuchDokuments(new ArrayList<>())
            .einreichedatum(referenceDate)
            .build();

        GesuchTrancheTestBuilder.standardNestedDeps(referenceDate)
            .withGesuch(gesuch);

        return new GesuchTestBuilder(gesuch, referenceDate);
    }
}
