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

package ch.dvbern.stip.api.delegieren.resource;

import java.util.UUID;

import ch.dvbern.stip.api.delegieren.entity.Delegierung;
import ch.dvbern.stip.api.delegieren.repo.DelegierungRepository;
import ch.dvbern.stip.api.delegieren.service.DelegierenService;
import ch.dvbern.stip.api.delegieren.service.DelegierungMapper;
import ch.dvbern.stip.api.delegieren.service.DelegierungMapperImpl;
import ch.dvbern.stip.api.delegieren.service.SozialdienstDashboardQueryBuilder;
import ch.dvbern.stip.api.sozialdienstbenutzer.entity.SozialdienstBenutzer;
import ch.dvbern.stip.api.sozialdienstbenutzer.repo.SozialdienstBenutzerRepository;
import ch.dvbern.stip.generated.dto.DelegierterMitarbeiterAendernDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;

class DelegierenServiceTest {
    private SozialdienstBenutzerRepository sozialdienstBenutzerRepositoryMock;
    private DelegierungRepository delegierungRepositoryMock;
    private SozialdienstDashboardQueryBuilder sozialdienstDashboardQueryBuilderMock;

    private DelegierungMapper delegierungMapper;
    private DelegierenService delegierenService;

    @BeforeEach
    void setUp() {
        delegierungRepositoryMock = Mockito.mock(DelegierungRepository.class);
        sozialdienstBenutzerRepositoryMock = Mockito.mock(SozialdienstBenutzerRepository.class);
        sozialdienstDashboardQueryBuilderMock = Mockito.mock(SozialdienstDashboardQueryBuilder.class);

        delegierungMapper = new DelegierungMapperImpl();

        delegierenService = new DelegierenService(
            delegierungRepositoryMock, null, null, null, sozialdienstBenutzerRepositoryMock, null,
            sozialdienstDashboardQueryBuilderMock, null, delegierungMapper
        );
    }

    @Test
    void delegierterMitarbeiterAendern_shouldWork_whenOtherMitarbeiterExists() {
        // arrange

        // current delegated mitarbeiter
        SozialdienstBenutzer sozialdienstBenutzer1 = new SozialdienstBenutzer();
        sozialdienstBenutzer1.setId(UUID.randomUUID());

        // mitarbeiter to delegate
        SozialdienstBenutzer sozialdienstBenutzer2 = new SozialdienstBenutzer();
        sozialdienstBenutzer2.setId(UUID.randomUUID());

        // setup delegierung with already delegated mitarbeiter
        Delegierung delegierung = new Delegierung();
        delegierung.setDelegierterMitarbeiter(sozialdienstBenutzer1);
        Mockito.when(delegierungRepositoryMock.requireById(any())).thenReturn(delegierung);
        assertThat(delegierung.getDelegierterMitarbeiter().getId(), is(sozialdienstBenutzer1.getId()));

        // mock repository to find the new mitarbeiter to newly delegate
        Mockito.when(sozialdienstBenutzerRepositoryMock.requireById(any())).thenReturn(sozialdienstBenutzer2);

        // act
        delegierenService.delegierterMitarbeiterAendern(UUID.randomUUID(), new DelegierterMitarbeiterAendernDto());

        // assert
        assertThat(delegierung.getDelegierterMitarbeiter().getId(), is(sozialdienstBenutzer2.getId()));
    }

}
