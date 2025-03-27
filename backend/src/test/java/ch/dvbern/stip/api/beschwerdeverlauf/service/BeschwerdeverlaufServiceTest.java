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

package ch.dvbern.stip.api.beschwerdeverlauf.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.beschwerdeverlauf.entity.BeschwerdeVerlaufEntry;
import ch.dvbern.stip.api.beschwerdeverlauf.repo.BeschwerdeVerlaufRepository;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;

class BeschwerdeverlaufServiceTest {
    private Gesuch gesuch;
    private BeschwerdeVerlaufRepository beschwerdeverlaufRepository;
    private GesuchRepository gesuchRepository;
    private BeschwerdeverlaufMapper beschwerdeverlaufMapper;
    private BeschwerdeverlaufService beschwerdeverlaufService;
    private BeschwerdeVerlaufEntry beschwerdeverlaufEntry;

    @BeforeEach
    void setUp() {
        beschwerdeverlaufMapper = new BeschwerdeverlaufMapperImpl();
        beschwerdeverlaufEntry = new BeschwerdeVerlaufEntry();
        beschwerdeverlaufEntry.setGesuch(gesuch);
        beschwerdeverlaufEntry.setKommentar("blabla");
        beschwerdeverlaufEntry.setBeschwerdeSetTo(false);

        gesuch = new Gesuch();
        gesuch.setId(UUID.randomUUID());
        gesuch.setBeschwerdeHaengig(false);
        gesuch.setBeschwerdeVerlauf(new ArrayList<>());
        gesuch.getBeschwerdeVerlauf().add(beschwerdeverlaufEntry);

        beschwerdeverlaufRepository = Mockito.mock(BeschwerdeVerlaufRepository.class);
        gesuchRepository = Mockito.mock(GesuchRepository.class);
        Mockito.when(beschwerdeverlaufRepository.findByGesuchId(any())).thenReturn(List.of(beschwerdeverlaufEntry));
        Mockito.when(gesuchRepository.requireById(any())).thenReturn(gesuch);

        beschwerdeverlaufService =
            new BeschwerdeverlaufService(beschwerdeverlaufRepository, gesuchRepository, beschwerdeverlaufMapper);
    }

    @Test
    @Description("Should return all existing BeschwerderVerlaufEntries of a gesuch")
    void getAllBeschwerdeVerlaufEntriesByGesuchId() {
        assertThat(beschwerdeverlaufService.getAllBeschwerdeVerlaufEntriesByGesuchId(gesuch.getId()).size(), is(1));
    }
}
