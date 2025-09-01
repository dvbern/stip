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

package ch.dvbern.stip.api.steuerdaten.service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.kind.repo.NeskoAccessRepository;
import ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice.EffSatzType;
import ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice.GetSteuerdatenResponse;
import ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice.MannFrauEffSatzType;
import ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice.SteuerdatenType;
import ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice.VeranlagungsStatusType;
import ch.dvbern.stip.api.nesko.service.NeskoGetSteuerdatenService;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SteuerdatenServiceTest {
    private SteuerdatenService steuerdatenService;
    private GesuchTrancheRepository trancheRepository;
    private SteuerdatenRepository steuerdatenRepository;

    private NeskoGetSteuerdatenService neskoGetSteuerdatenService;
    private NeskoAccessRepository neskoAccessRepository;

    private GesuchTranche gesuchTranche;
    private GesuchFormular gesuchFormular;

    GetSteuerdatenResponse getSteuerdatenResponse;

    @BeforeEach
    void setUp() {
        getSteuerdatenResponse = new GetSteuerdatenResponse();
        getSteuerdatenResponse.setSteuerdaten(new SteuerdatenType());
        var steuerbaresVermoegenKanton = new EffSatzType();
        steuerbaresVermoegenKanton.setEffektiv(BigDecimal.ZERO);
        getSteuerdatenResponse.getSteuerdaten().setSteuerbaresVermoegenKanton(steuerbaresVermoegenKanton);
        getSteuerdatenResponse.getSteuerdaten().setFahrkosten(new MannFrauEffSatzType());
        getSteuerdatenResponse.getSteuerdaten().setKostenAuswaertigeVerpflegung(new MannFrauEffSatzType());
        getSteuerdatenResponse.getSteuerdaten().setTotalEinkuenfte(new EffSatzType());
        getSteuerdatenResponse.getSteuerdaten().getTotalEinkuenfte().setEffektiv(BigDecimal.ZERO);
        getSteuerdatenResponse.getSteuerdaten().getTotalEinkuenfte().setSatzbestimmend(BigDecimal.ZERO);
        getSteuerdatenResponse.getSteuerdaten()
            .setStatusVeranlagung(VeranlagungsStatusType.fromValue(TestConstants.VERANLAGUNGSSTATUS_EXAMPLE_VALUE));

        gesuchTranche = new GesuchTranche();
        gesuchFormular = new GesuchFormular();
        var eltern1 = new Eltern();
        eltern1.setElternTyp(ElternTyp.VATER);
        eltern1.setSozialversicherungsnummer("");
        var eltern2 = new Eltern();
        eltern2.setElternTyp(ElternTyp.MUTTER);
        eltern2.setSozialversicherungsnummer("");

        var ausbildung = new Ausbildung();
        var fall = new Fall();
        fall.setFallNummer(UUID.randomUUID().toString());
        ausbildung.setFall(fall);
        var gesuch = new Gesuch();
        gesuch.setAusbildung(ausbildung);
        gesuchTranche.setGesuch(gesuch);
        gesuch.setGesuchNummer(UUID.randomUUID().toString());
        gesuchFormular.setTranche(gesuchTranche);
        gesuchFormular.getTranche().setGesuch(gesuch);

        gesuchFormular.setElterns(Set.of(eltern1, eltern2));

        gesuchFormular.setTranche(gesuchTranche);
        gesuchTranche.setGesuchFormular(gesuchFormular);
        gesuch.setGesuchNummer(TestUtil.getFullGesuch().getGesuchNummer());

        trancheRepository = Mockito.mock(GesuchTrancheRepository.class);
        when(trancheRepository.requireById(any())).thenReturn(gesuchTranche);

        steuerdatenRepository = Mockito.mock(SteuerdatenRepository.class);
        neskoGetSteuerdatenService = Mockito.mock(NeskoGetSteuerdatenService.class);

        neskoAccessRepository = Mockito.mock(NeskoAccessRepository.class);
        Mockito.doNothing().when(neskoAccessRepository).persistAndFlush(any());

        when(neskoGetSteuerdatenService.getSteuerdatenResponse(any(), any(), any(), any(), any()))
            .thenReturn(getSteuerdatenResponse);

        steuerdatenService = new SteuerdatenService(
            null, trancheRepository, null, steuerdatenRepository, neskoGetSteuerdatenService, null
        );
    }

    @Test
    void setAllToSelbstaendig_IfOneIsSelbstaendig_Steuerdaten_FAMILIE_Test() {
        // arrange
        var actualSteuerdaten = new Steuerdaten();
        actualSteuerdaten.setSteuerdatenTyp(SteuerdatenTyp.FAMILIE);
        actualSteuerdaten.setIsArbeitsverhaeltnisSelbstaendig(true);

        var familiensituation = new Familiensituation();
        familiensituation.setElternVerheiratetZusammen(true);
        gesuchFormular.setFamiliensituation(familiensituation);
        var steuerdatenSet = new HashSet<Steuerdaten>();
        steuerdatenSet.add(actualSteuerdaten);
        gesuchFormular.setSteuerdaten(steuerdatenSet);

        getSteuerdatenResponse.getSteuerdaten().setFrauErwerbstaetigkeitSUS(true);
        getSteuerdatenResponse.getSteuerdaten().setMannErwerbstaetigkeitSUS(false);

        // act
        steuerdatenService.updateSteuerdatenFromNesko(UUID.randomUUID(), SteuerdatenTyp.FAMILIE, "", 2021);

        // assert
        assertThat(
            actualSteuerdaten.getIsArbeitsverhaeltnisSelbstaendig(),
            is(true)
        );

        actualSteuerdaten.setIsArbeitsverhaeltnisSelbstaendig(false);

        getSteuerdatenResponse.getSteuerdaten().setFrauErwerbstaetigkeitSUS(false);
        getSteuerdatenResponse.getSteuerdaten().setMannErwerbstaetigkeitSUS(false);
        steuerdatenService.updateSteuerdatenFromNesko(UUID.randomUUID(), SteuerdatenTyp.FAMILIE, "", 2021);
        assertThat(
            actualSteuerdaten.getIsArbeitsverhaeltnisSelbstaendig(),
            is(false)
        );
    }

    @Test
    void setAllToNOTSelbstaendig_IfNONE_IsSelbstaendig_Steuerdaten_FAMILIE_Test() {
        // arrange
        var actualSteuerdaten = new Steuerdaten();
        actualSteuerdaten.setSteuerdatenTyp(SteuerdatenTyp.FAMILIE);

        var familiensituation = new Familiensituation();
        familiensituation.setElternVerheiratetZusammen(true);
        gesuchFormular.setFamiliensituation(familiensituation);

        var steuerdatenSet = new HashSet<Steuerdaten>();
        steuerdatenSet.add(actualSteuerdaten);
        gesuchFormular.setSteuerdaten(steuerdatenSet);

        getSteuerdatenResponse.getSteuerdaten().setFrauErwerbstaetigkeitSUS(false);
        getSteuerdatenResponse.getSteuerdaten().setMannErwerbstaetigkeitSUS(false);

        // act
        steuerdatenService.updateSteuerdatenFromNesko(UUID.randomUUID(), SteuerdatenTyp.FAMILIE, "", 2021);

        // assert
        assertThat(
            actualSteuerdaten.getIsArbeitsverhaeltnisSelbstaendig(),
            is(false)
        );
    }

    @Test
    void setAllToSelbstaendig_IfOneIsSelbstaendig_Steuerdaten_VATER_MUTTER_Test() {
        // arrange
        var actualSteuerdaten = new Steuerdaten();
        actualSteuerdaten.setSteuerdatenTyp(SteuerdatenTyp.MUTTER);

        var otherSteuerdaten = new Steuerdaten();
        otherSteuerdaten.setSteuerdatenTyp(SteuerdatenTyp.VATER);
        otherSteuerdaten.setIsArbeitsverhaeltnisSelbstaendig(true);

        var familiensituation = new Familiensituation();
        familiensituation.setElternVerheiratetZusammen(false);
        familiensituation.setMutterWiederverheiratet(true);
        familiensituation.setVaterWiederverheiratet(false);
        gesuchFormular.setFamiliensituation(familiensituation);
        var steuerdatenSet = new HashSet<Steuerdaten>();
        steuerdatenSet.add(actualSteuerdaten);
        steuerdatenSet.add(otherSteuerdaten);
        gesuchFormular.setSteuerdaten(steuerdatenSet);

        // mutter is not selbstaendig,
        // but VATER is, so isArbeitsverhaeltnisSelbstaendig = true for MUTTER
        getSteuerdatenResponse.getSteuerdaten().setFrauErwerbstaetigkeitSUS(false);

        // act
        steuerdatenService.updateSteuerdatenFromNesko(UUID.randomUUID(), SteuerdatenTyp.MUTTER, "", 2021);

        // assert
        assertThat(
            actualSteuerdaten.getIsArbeitsverhaeltnisSelbstaendig(),
            is(true)
        );
    }

    @Test
    void setAllToNOTSelbstaendig_ifNoneIsSelbstaendig_Steuerdaten_VATER_MUTTER_Test() {
        // arrange
        var familiensituation = new Familiensituation();
        familiensituation.setElternVerheiratetZusammen(false);
        familiensituation.setMutterWiederverheiratet(true);
        familiensituation.setVaterWiederverheiratet(false);
        gesuchFormular.setFamiliensituation(familiensituation);

        var currentSteuerdaten = new Steuerdaten();
        currentSteuerdaten.setSteuerdatenTyp(SteuerdatenTyp.MUTTER);
        currentSteuerdaten.setIsArbeitsverhaeltnisSelbstaendig(false);

        var otherSteuerdaten = new Steuerdaten();
        otherSteuerdaten.setSteuerdatenTyp(SteuerdatenTyp.VATER);
        otherSteuerdaten.setIsArbeitsverhaeltnisSelbstaendig(false);

        var steuerdatenSet = new HashSet<Steuerdaten>();
        steuerdatenSet.add(currentSteuerdaten);
        steuerdatenSet.add(otherSteuerdaten);
        gesuchFormular.setSteuerdaten(steuerdatenSet);

        // none of the parents is selbstaendig -> isArbeitsverhaeltnisSelbstaendig = false
        getSteuerdatenResponse.getSteuerdaten().setFrauErwerbstaetigkeitSUS(false);

        // act
        steuerdatenService.updateSteuerdatenFromNesko(UUID.randomUUID(), SteuerdatenTyp.MUTTER, "", 2021);

        // assert
        assertThat(
            currentSteuerdaten.getIsArbeitsverhaeltnisSelbstaendig(),
            is(false)
        );
    }
}
