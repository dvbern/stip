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

package ch.dvbern.stip.api.gesuch.service;

import java.util.HashSet;

import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.geschwister.entity.Geschwister;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchformular.service.PageValidationUtil;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.partner.entity.Partner;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PageValidationUtilTest {
    @Test
    void getGroupsFromGesuchFormular() {
        final var gesuchFormular = new GesuchFormular();

        var groups = PageValidationUtil.getGroupsFromGesuchFormular(gesuchFormular);
        assertThat(groups, is(empty()));

        gesuchFormular.setPersonInAusbildung(new PersonInAusbildung());
        gesuchFormular.setFamiliensituation(new Familiensituation());
        gesuchFormular.setPartner(new Partner());
        gesuchFormular.setEinnahmenKosten(new EinnahmenKosten());
        gesuchFormular.setLebenslaufItems(new HashSet<>() {
            {
                add(new LebenslaufItem());
            }
        });
        gesuchFormular.setGeschwisters(new HashSet<>() {
            {
                add(new Geschwister());
            }
        });
        gesuchFormular.setElterns(new HashSet<>() {
            {
                add(new Eltern());
            }
        });
        gesuchFormular.setKinds(new HashSet<>() {
            {
                add(new Kind());
            }
        });

        groups = PageValidationUtil.getGroupsFromGesuchFormular(gesuchFormular);
        assertThat(groups.size(), is(8));
    }
}
