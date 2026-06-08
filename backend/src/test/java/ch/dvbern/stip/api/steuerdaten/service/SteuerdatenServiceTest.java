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

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranchehistory.service.GesuchTrancheHistoryService;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.generated.dto.SteuerdatenDto;
import ch.dvbern.stip.integration.steuerdaten.domain.model.SteuerdatenPortData;
import ch.dvbern.stip.integration.steuerdaten.domain.port.SteuerdatenPort;
import ch.dvbern.stip.integration.steuerdaten.domain.port.SteuerdatenPortFactory;
import io.quarkus.test.InjectMock;
import io.quarkus.test.component.QuarkusComponentTest;
import jakarta.inject.Inject;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusComponentTest
class SteuerdatenServiceTest {

    @Inject
    SteuerdatenService steuerdatenService;

    @InjectMock
    Validator validator;

    @InjectMock
    GesuchTrancheRepository trancheRepository;

    @InjectMock
    SteuerdatenMapper steuerdatenMapper;

    @InjectMock
    SteuerdatenRepository steuerdatenRepository;

    @InjectMock
    SteuerdatenPortFactory steuerdatenPortFactory;

    @InjectMock
    GesuchTrancheHistoryService gesuchTrancheHistoryService;

    @Test
    void getSteuerdaten_trancheExists_returnsMappedDtos() {
        final var trancheId = UUID.randomUUID();
        final var steuerdaten = new Steuerdaten();
        final var dto = new SteuerdatenDto();

        final var formular = new GesuchFormular();
        formular.getSteuerdaten().add(steuerdaten);

        final var tranche = mock(GesuchTranche.class);
        when(tranche.getGesuchFormular()).thenReturn(formular);
        when(trancheRepository.findById(trancheId)).thenReturn(tranche);
        when(steuerdatenMapper.toDto(steuerdaten)).thenReturn(dto);

        final var result = steuerdatenService.getSteuerdaten(trancheId);

        assertThat(result, hasSize(1));
        assertThat(result.get(0), is(dto));
        verify(gesuchTrancheHistoryService, never()).getLatestTranche(any());
    }

    @Test
    void getSteuerdaten_trancheNotFound_fallsBackToHistory() {
        final var trancheId = UUID.randomUUID();
        final var steuerdaten = new Steuerdaten();
        final var dto = new SteuerdatenDto();

        final var formular = new GesuchFormular();
        formular.getSteuerdaten().add(steuerdaten);

        final var historicTranche = mock(GesuchTranche.class);
        when(historicTranche.getGesuchFormular()).thenReturn(formular);

        when(trancheRepository.findById(trancheId)).thenReturn(null);
        when(gesuchTrancheHistoryService.getLatestTranche(trancheId)).thenReturn(historicTranche);
        when(steuerdatenMapper.toDto(steuerdaten)).thenReturn(dto);

        final var result = steuerdatenService.getSteuerdaten(trancheId);

        assertThat(result, hasSize(1));
        assertThat(result.get(0), is(dto));
        verify(gesuchTrancheHistoryService).getLatestTranche(trancheId);
    }

    @Test
    void getSteuerdaten_noSteuerdaten_returnsEmptyList() {
        final var trancheId = UUID.randomUUID();
        final var formular = new GesuchFormular();
        // steuerdaten set is empty by default

        final var tranche = mock(GesuchTranche.class);
        when(tranche.getGesuchFormular()).thenReturn(formular);
        when(trancheRepository.findById(trancheId)).thenReturn(tranche);

        final var result = steuerdatenService.getSteuerdaten(trancheId);

        assertThat(result, is(empty()));
    }

    @Test
    void updateSteuerdaten_persistsAndReturnsDtos() {
        final var trancheId = UUID.randomUUID();
        final var inputDto = new SteuerdatenDto();
        final var steuerdaten = new Steuerdaten();
        final var outputDto = new SteuerdatenDto();

        final var formular = mock(GesuchFormular.class);
        when(formular.getSteuerdaten()).thenReturn(new LinkedHashSet<>());

        final var tranche = mock(GesuchTranche.class);
        when(tranche.getGesuchFormular()).thenReturn(formular);
        when(trancheRepository.requireById(trancheId)).thenReturn(tranche);
        when(steuerdatenMapper.map(List.of(inputDto), formular.getSteuerdaten()))
            .thenReturn(Set.of(steuerdaten));
        when(steuerdatenMapper.toDto(steuerdaten)).thenReturn(outputDto);

        final var result = steuerdatenService.updateSteuerdaten(trancheId, List.of(inputDto));

        assertThat(result, hasSize(1));
        assertThat(result.get(0), is(outputDto));
        verify(steuerdatenRepository).persistAndFlush(steuerdaten);
    }

    @Test
    void updateSteuerdaten_emptyDtoList_returnsEmptyList() {
        final var trancheId = UUID.randomUUID();

        final var formular = mock(GesuchFormular.class);
        when(formular.getSteuerdaten()).thenReturn(new LinkedHashSet<>());

        final var tranche = mock(GesuchTranche.class);
        when(tranche.getGesuchFormular()).thenReturn(formular);
        when(trancheRepository.requireById(trancheId)).thenReturn(tranche);
        when(steuerdatenMapper.map(List.of(), formular.getSteuerdaten())).thenReturn(Set.of());

        final var result = steuerdatenService.updateSteuerdaten(trancheId, List.of());

        assertThat(result, is(empty()));
        verify(steuerdatenRepository, never()).persistAndFlush(any());
    }

    @Test
    void updateSteuerdatenFromPort_vaterTyp_usesVaterEltern_createsNewSteuerdatenIfAbsent() {
        final var trancheId = UUID.randomUUID();
        final var steuerjahr = 2023;
        final var ssvn = "756.1234.5678.90";

        final var vaterEltern = new Eltern();
        vaterEltern.setElternTyp(ElternTyp.VATER);
        vaterEltern.setSozialversicherungsnummer(ssvn);

        final var familiensituation = new Familiensituation();
        familiensituation.setElternVerheiratetZusammen(false);
        familiensituation.setVaterWiederverheiratet(false);
        familiensituation.setMutterWiederverheiratet(false);

        final var formular = mock(GesuchFormular.class);
        when(formular.getSteuerdaten()).thenReturn(new LinkedHashSet<>());
        when(formular.getElterns()).thenReturn(Set.of(vaterEltern));
        when(formular.getFamiliensituation()).thenReturn(familiensituation);

        final var portData = mock(SteuerdatenPortData.class);
        final var updatedSteuerdaten = new Steuerdaten();
        updatedSteuerdaten.setSteuerdatenTyp(SteuerdatenTyp.VATER);
        updatedSteuerdaten.setIsArbeitsverhaeltnisSelbstaendig(false);

        final var outputDto = new SteuerdatenDto();

        final var port = mock(SteuerdatenPort.class);
        when(steuerdatenPortFactory.getSteuerdatenAdapter()).thenReturn(port);
        when(port.getSteuerdaten(any(), any(Integer.class), any(), any(), any())).thenReturn(portData);
        when(steuerdatenMapper.partialUpdate(eq(portData), any())).thenReturn(updatedSteuerdaten);

        // Second call to requireById (after persist) returns a tranche with the updated steuerdaten
        final var formularAfterPersist = mock(GesuchFormular.class);
        when(formularAfterPersist.getSteuerdaten()).thenReturn(Set.of(updatedSteuerdaten));

        final var fall = mock(ch.dvbern.stip.api.fall.entity.Fall.class);
        when(fall.getFallNummer()).thenReturn("F-001");

        final var ausbildung = mock(ch.dvbern.stip.api.ausbildung.entity.Ausbildung.class);
        when(ausbildung.getFall()).thenReturn(fall);

        final var gesuch = mock(ch.dvbern.stip.api.gesuch.entity.Gesuch.class);
        when(gesuch.getAusbildung()).thenReturn(ausbildung);
        when(gesuch.getGesuchNummer()).thenReturn("G-001");

        final var trancheBefore = mock(GesuchTranche.class);
        when(trancheBefore.getGesuchFormular()).thenReturn(formular);
        when(trancheBefore.getGesuch()).thenReturn(gesuch);

        final var trancheAfter = mock(GesuchTranche.class);
        when(trancheAfter.getGesuchFormular()).thenReturn(formularAfterPersist);

        when(trancheRepository.requireById(trancheId))
            .thenReturn(trancheBefore)
            .thenReturn(trancheAfter);

        when(steuerdatenMapper.toDto(updatedSteuerdaten)).thenReturn(outputDto);

        final var result = steuerdatenService.updateSteuerdatenFromPort(trancheId, SteuerdatenTyp.VATER, steuerjahr);

        assertThat(result, hasSize(1));
        verify(steuerdatenRepository).persistAndFlush(updatedSteuerdaten);
    }

    @Test
    void updateSteuerdatenFromPort_mutterTyp_noMutterEltern_throwsNotFoundException() {
        final var trancheId = UUID.randomUUID();

        // Only Vater in elterns, no Mutter
        final var vaterEltern = new Eltern();
        vaterEltern.setElternTyp(ElternTyp.VATER);
        vaterEltern.setSozialversicherungsnummer("756.1234.5678.90");

        final var formular = mock(GesuchFormular.class);
        when(formular.getSteuerdaten()).thenReturn(new LinkedHashSet<>());
        when(formular.getElterns()).thenReturn(Set.of(vaterEltern));

        final var tranche = mock(GesuchTranche.class);
        when(tranche.getGesuchFormular()).thenReturn(formular);
        when(trancheRepository.requireById(trancheId)).thenReturn(tranche);

        assertThrows(
            NotFoundException.class,
            () -> steuerdatenService.updateSteuerdatenFromPort(trancheId, SteuerdatenTyp.MUTTER, 2023)
        );

        verify(steuerdatenRepository, never()).persistAndFlush(any());
    }

    @Test
    void updateSteuerdatenFromPort_existingSteuerdatenOfSameTyp_updatesExistingEntry() {
        final var trancheId = UUID.randomUUID();
        final var steuerjahr = 2023;
        final var ssvn = "756.9999.8888.77";

        final var mutterEltern = new Eltern();
        mutterEltern.setElternTyp(ElternTyp.MUTTER);
        mutterEltern.setSozialversicherungsnummer(ssvn);

        final var existingSteuerdaten = new Steuerdaten();
        existingSteuerdaten.setSteuerdatenTyp(SteuerdatenTyp.MUTTER);
        existingSteuerdaten.setIsArbeitsverhaeltnisSelbstaendig(false);

        final var steuerdatenSet = new LinkedHashSet<Steuerdaten>();
        steuerdatenSet.add(existingSteuerdaten);

        final var familiensituation = new Familiensituation();
        familiensituation.setElternVerheiratetZusammen(false);
        familiensituation.setVaterWiederverheiratet(false);
        familiensituation.setMutterWiederverheiratet(false);

        final var formular = mock(GesuchFormular.class);
        when(formular.getSteuerdaten()).thenReturn(steuerdatenSet);
        when(formular.getElterns()).thenReturn(Set.of(mutterEltern));
        when(formular.getFamiliensituation()).thenReturn(familiensituation);

        final var portData = mock(SteuerdatenPortData.class);
        final var updatedSteuerdaten = new Steuerdaten();
        updatedSteuerdaten.setSteuerdatenTyp(SteuerdatenTyp.MUTTER);
        updatedSteuerdaten.setIsArbeitsverhaeltnisSelbstaendig(false);

        final var port = mock(SteuerdatenPort.class);
        when(steuerdatenPortFactory.getSteuerdatenAdapter()).thenReturn(port);
        when(port.getSteuerdaten(any(), any(Integer.class), any(), any(), any())).thenReturn(portData);
        // partialUpdate called with the EXISTING steuerdaten object
        when(steuerdatenMapper.partialUpdate(portData, existingSteuerdaten)).thenReturn(updatedSteuerdaten);

        final var formularAfterPersist = mock(GesuchFormular.class);
        when(formularAfterPersist.getSteuerdaten()).thenReturn(Set.of(updatedSteuerdaten));

        final var fall = mock(ch.dvbern.stip.api.fall.entity.Fall.class);
        when(fall.getFallNummer()).thenReturn("F-001");

        final var ausbildung = mock(ch.dvbern.stip.api.ausbildung.entity.Ausbildung.class);
        when(ausbildung.getFall()).thenReturn(fall);

        final var gesuch = mock(ch.dvbern.stip.api.gesuch.entity.Gesuch.class);
        when(gesuch.getAusbildung()).thenReturn(ausbildung);
        when(gesuch.getGesuchNummer()).thenReturn("G-001");

        final var trancheBefore = mock(GesuchTranche.class);
        when(trancheBefore.getGesuchFormular()).thenReturn(formular);
        when(trancheBefore.getGesuch()).thenReturn(gesuch);

        final var trancheAfter = mock(GesuchTranche.class);
        when(trancheAfter.getGesuchFormular()).thenReturn(formularAfterPersist);

        when(trancheRepository.requireById(trancheId))
            .thenReturn(trancheBefore)
            .thenReturn(trancheAfter);

        final var outputDto = new SteuerdatenDto();
        when(steuerdatenMapper.toDto(updatedSteuerdaten)).thenReturn(outputDto);

        final var result = steuerdatenService.updateSteuerdatenFromPort(trancheId, SteuerdatenTyp.MUTTER, steuerjahr);

        assertThat(result, hasSize(1));
        // The existing entry should have been updated (not a new one created)
        verify(steuerdatenMapper).partialUpdate(portData, existingSteuerdaten);
        verify(steuerdatenRepository).persistAndFlush(updatedSteuerdaten);
    }

    private Familiensituation familiensituationWith(
        Boolean vaterWiederverheiratet,
        Boolean mutterWiederverheiratet
    ) {
        final var fs = new Familiensituation();
        fs.setElternVerheiratetZusammen(false);
        fs.setVaterWiederverheiratet(vaterWiederverheiratet);
        fs.setMutterWiederverheiratet(mutterWiederverheiratet);
        return fs;
    }

    @Test
    void notWiederverheiratet_returnsSelbstaendigFromActualSteuerdaten_whenTrue() {
        final var actual = new Steuerdaten();
        actual.setSteuerdatenTyp(SteuerdatenTyp.VATER);
        actual.setIsArbeitsverhaeltnisSelbstaendig(true);

        final var familiensituation = familiensituationWith(false, false);

        final var result = steuerdatenService.evaluateIsArbeitsverhaltnisSelbstaendigIfWiederverheiratet(
            actual,
            Set.of(actual),
            familiensituation
        );

        assertThat(result, is(true));
    }

    @Test
    void notWiederverheiratet_returnsSelbstaendigFromActualSteuerdaten_whenFalse() {
        final var actual = new Steuerdaten();
        actual.setSteuerdatenTyp(SteuerdatenTyp.VATER);
        actual.setIsArbeitsverhaeltnisSelbstaendig(false);

        final var familiensituation = familiensituationWith(false, false);

        final var result = steuerdatenService.evaluateIsArbeitsverhaltnisSelbstaendigIfWiederverheiratet(
            actual,
            Set.of(actual),
            familiensituation
        );

        assertThat(result, is(false));
    }

    @Test
    void notWiederverheiratet_nullWiederverheiratetFields_returnsSelbstaendigFromActualSteuerdaten() {
        final var actual = new Steuerdaten();
        actual.setSteuerdatenTyp(SteuerdatenTyp.MUTTER);
        actual.setIsArbeitsverhaeltnisSelbstaendig(true);

        final var familiensituation = familiensituationWith(null, null);

        final var result = steuerdatenService.evaluateIsArbeitsverhaltnisSelbstaendigIfWiederverheiratet(
            actual,
            Set.of(actual),
            familiensituation
        );

        assertThat(result, is(true));
    }

    @Test
    void vaterWiederverheiratet_noOtherSelbstaendigInSet_returnsFalse() {
        final var actual = new Steuerdaten();
        actual.setSteuerdatenTyp(SteuerdatenTyp.MUTTER);
        actual.setIsArbeitsverhaeltnisSelbstaendig(false);

        final var other = new Steuerdaten();
        other.setSteuerdatenTyp(SteuerdatenTyp.VATER);
        other.setIsArbeitsverhaeltnisSelbstaendig(false);

        final var familiensituation = familiensituationWith(true, false);

        final var result = steuerdatenService.evaluateIsArbeitsverhaltnisSelbstaendigIfWiederverheiratet(
            actual,
            Set.of(actual, other),
            familiensituation
        );

        assertThat(result, is(false));
    }

    @Test
    void vaterWiederverheiratet_anotherSteuerdatenIsSelbstaendig_returnsTrue() {
        final var actual = new Steuerdaten();
        actual.setSteuerdatenTyp(SteuerdatenTyp.MUTTER);
        actual.setIsArbeitsverhaeltnisSelbstaendig(false);

        final var other = new Steuerdaten();
        other.setSteuerdatenTyp(SteuerdatenTyp.VATER);
        other.setIsArbeitsverhaeltnisSelbstaendig(true);

        final var familiensituation = familiensituationWith(true, false);

        final var result = steuerdatenService.evaluateIsArbeitsverhaltnisSelbstaendigIfWiederverheiratet(
            actual,
            Set.of(actual, other),
            familiensituation
        );

        assertThat(result, is(true));
    }

    @Test
    void vaterWiederverheiratet_actualIsSelbstaendig_returnsTrue() {
        final var actual = new Steuerdaten();
        actual.setSteuerdatenTyp(SteuerdatenTyp.MUTTER);
        actual.setIsArbeitsverhaeltnisSelbstaendig(true);

        final var familiensituation = familiensituationWith(true, false);

        final var result = steuerdatenService.evaluateIsArbeitsverhaltnisSelbstaendigIfWiederverheiratet(
            actual,
            Set.of(actual),
            familiensituation
        );

        assertThat(result, is(true));
    }

    @Test
    void mutterWiederverheiratet_noSelbstaendigInSet_returnsFalse() {
        final var actual = new Steuerdaten();
        actual.setSteuerdatenTyp(SteuerdatenTyp.VATER);
        actual.setIsArbeitsverhaeltnisSelbstaendig(false);

        final var familiensituation = familiensituationWith(false, true);

        final var result = steuerdatenService.evaluateIsArbeitsverhaltnisSelbstaendigIfWiederverheiratet(
            actual,
            Set.of(actual),
            familiensituation
        );

        assertThat(result, is(false));
    }

    @Test
    void mutterWiederverheiratet_anotherSteuerdatenIsSelbstaendig_returnsTrue() {
        final var actual = new Steuerdaten();
        actual.setSteuerdatenTyp(SteuerdatenTyp.VATER);
        actual.setIsArbeitsverhaeltnisSelbstaendig(false);

        final var other = new Steuerdaten();
        other.setSteuerdatenTyp(SteuerdatenTyp.MUTTER);
        other.setIsArbeitsverhaeltnisSelbstaendig(true);

        final var familiensituation = familiensituationWith(false, true);

        final var result = steuerdatenService.evaluateIsArbeitsverhaltnisSelbstaendigIfWiederverheiratet(
            actual,
            Set.of(actual, other),
            familiensituation
        );

        assertThat(result, is(true));
    }

    @Test
    void wiederverheiratet_nullSelbstaendigInSet_treatedAsFalse_returnsFalse() {
        final var actual = new Steuerdaten();
        actual.setSteuerdatenTyp(SteuerdatenTyp.MUTTER);
        actual.setIsArbeitsverhaeltnisSelbstaendig(null);

        final var other = new Steuerdaten();
        other.setSteuerdatenTyp(SteuerdatenTyp.VATER);
        other.setIsArbeitsverhaeltnisSelbstaendig(null);

        final var familiensituation = familiensituationWith(true, false);

        final var result = steuerdatenService.evaluateIsArbeitsverhaltnisSelbstaendigIfWiederverheiratet(
            actual,
            Set.of(actual, other),
            familiensituation
        );

        assertThat(result, is(false));
    }
}
