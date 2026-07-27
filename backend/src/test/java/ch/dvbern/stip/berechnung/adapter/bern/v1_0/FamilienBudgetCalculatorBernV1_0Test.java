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

package ch.dvbern.stip.berechnung.adapter.bern.v1_0;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.common.entity.AbstractFamilieEntity;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.api.steuererklaerung.entity.Steuererklaerung;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.berechnung.adapter.bern.v1_0.service.FamilienBudgetCalculator;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatDto;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class FamilienBudgetCalculatorBernV1_0Test {
    @Test
    void calculateFamilienBudgetTest() {
        final Gesuch gesuch = TestUtil.getGesuchForBerechnung(UUID.randomUUID());
        final GesuchFormular gesuchFormular = gesuch.getNewestGesuchTranche().get().getGesuchFormular();

        final List<Eltern> eltern =
            gesuchFormular.getElterns().stream().filter(eltern1 -> eltern1.getElternTyp() == ElternTyp.VATER).toList();
        eltern.getFirst().setWohnkosten(1500);
        final Steuerdaten steuerdaten = gesuchFormular.getSteuerdaten()
            .stream()
            .filter(steuerdaten1 -> steuerdaten1.getSteuerdatenTyp() == SteuerdatenTyp.VATER)
            .findFirst()
            .get();
        steuerdaten.setTotalEinkuenfte(30000);
        steuerdaten.setVermoegen(5000);
        steuerdaten.setSteuernKantonGemeinde(1500);
        steuerdaten.setSteuernBund(1500);
        steuerdaten.setFahrkosten(500);
        steuerdaten.setVerpflegung(500);
        final Steuererklaerung steuererklaerung = gesuchFormular.getSteuererklaerung()
            .stream()
            .filter(steuererklaerung1 -> steuererklaerung1.getSteuerdatenTyp() == SteuerdatenTyp.VATER)
            .findFirst()
            .get();
        steuererklaerung.setErgaenzungsleistungen(500);
        steuererklaerung.setEinnahmenBGSA(500);
        steuererklaerung.setAndereEinnahmen(500);
        steuererklaerung.setUnterhaltsbeitraege(500);
        steuererklaerung.setRenten(500);

        final List<AbstractFamilieEntity> kinderImHaushalt = gesuchFormular.getGeschwisters()
            .stream()
            .filter(geschwister -> geschwister.getWohnsitzAnteilVater().intValue() > 0)
            .map(geschwister -> (AbstractFamilieEntity) geschwister)
            .toList();

        final FamilienBudgetresultatDto familienBudgetresultatDto = FamilienBudgetCalculator.calculateFamilienBudget(
            eltern,
            steuerdaten,
            steuererklaerung,
            gesuch.getGesuchsperiode(),
            kinderImHaushalt,
            1,
            true,
            gesuch.getGesuchGueltigkeitAb().getYear()
        );

        assertThat(familienBudgetresultatDto.getSteuerdatenTyp(), is(SteuerdatenTyp.VATER));
        assertThat(familienBudgetresultatDto.getHaushaltNames(), hasSize(3));
        assertThat(familienBudgetresultatDto.getTotal(), is(-32926));
        assertThat(familienBudgetresultatDto.getTeilzeitKinderProzente(), is(80));

        assertThat(familienBudgetresultatDto.getEinnahmen().getTotal(), is(19750));
        assertThat(familienBudgetresultatDto.getEinnahmen().getAnrechenbaresVermoegen(), is(750));

        assertThat(familienBudgetresultatDto.getKosten().getGrundbedarf(), is(21816));
        assertThat(familienBudgetresultatDto.getKosten().getWohnkosten(), is(16260));
        assertThat(familienBudgetresultatDto.getKosten().getMedizinischeGrundversorgung(), is(8200));
        assertThat(familienBudgetresultatDto.getKosten().getFahrkostenTotal(), is(500));
        assertThat(familienBudgetresultatDto.getKosten().getVerpflegungTotal(), is(500));
        assertThat(familienBudgetresultatDto.getKosten().getIntegrationszulageTotal(), is(2400));
    }
}
