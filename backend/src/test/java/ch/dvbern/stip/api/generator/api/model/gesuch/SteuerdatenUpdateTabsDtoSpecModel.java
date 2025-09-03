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

package ch.dvbern.stip.api.generator.api.model.gesuch;

import java.util.ArrayList;
import java.util.List;

import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.SteuerdatenDtoSpec;
import ch.dvbern.stip.generated.dto.SteuerdatenTypDtoSpec;

public class SteuerdatenUpdateTabsDtoSpecModel {
    public static SteuerdatenDtoSpec steuerdatenDtoSpec(final SteuerdatenTypDtoSpec steuerdatenTypDtoSpec) {
        return TestUtil.createUpdateDtoSpec(SteuerdatenDtoSpec::new, (model) -> {
            model.setSteuerdatenTyp(steuerdatenTypDtoSpec);
            model.setTotalEinkuenfte(TestUtil.getRandomInt(1, 10000));
            model.setEigenmietwert(TestUtil.getRandomInt(1, 10000));
            model.setIsArbeitsverhaeltnisSelbstaendig(false);
            model.saeule3a(TestUtil.getRandomInt(1, 10000));
            model.saeule2(TestUtil.getRandomInt(1, 10000));
            model.setKinderalimente(TestUtil.getRandomInt(1, 10000));
            model.setVermoegen(TestUtil.getRandomInt(1, 10000));
            model.setSteuernKantonGemeinde(TestUtil.getRandomInt(1, 10000));
            model.steuernBund(TestUtil.getRandomInt(1, 10000));
            model.setFahrkosten(TestUtil.getRandomInt(1, 10000));
            model.setFahrkostenPartner(TestUtil.getRandomInt(1, 10000));
            model.setVerpflegung(TestUtil.getRandomInt(1, 10000));
            model.setVerpflegungPartner(TestUtil.getRandomInt(1, 10000));
            model.setVeranlagungsStatus(TestConstants.VERANLAGUNGSSTATUS_EXAMPLE_VALUE);
            model.setSteuerjahr(2022);
        });
    }

    public static List<SteuerdatenDtoSpec> steuerdatenDtoSpecs(
        final SteuerdatenTypDtoSpec... steuerdatenTypDtoSpecs
    ) {
        final var list = new ArrayList<SteuerdatenDtoSpec>();
        for (final var typ : steuerdatenTypDtoSpecs) {
            list.add(steuerdatenDtoSpec(typ));
        }

        return list;
    }

    // public static GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecSteuerdaten() {
    // return TestUtil.createUpdateDtoSpec(
    // GesuchFormularUpdateDtoSpec::new,
    // (model) -> {
    // model.setSteuerdaten(steuerdatenDtoSpecs(SteuerdatenTypDtoSpec.FAMILIE));
    // model.setFamiliensituation(FamiliensituationUpdateDtoSpecModel.familiensituationUpdateDtoSpec());
    // }
    // );
    // }
}
