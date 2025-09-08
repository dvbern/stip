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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.adresse.service.AdresseMapper;
import ch.dvbern.stip.api.adresse.service.AdresseMapperImpl;
import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.authorization.DelegierenAuthorizer;
import ch.dvbern.stip.api.common.type.Anrede;
import ch.dvbern.stip.api.communication.mail.service.MailService;
import ch.dvbern.stip.api.delegieren.entity.Delegierung;
import ch.dvbern.stip.api.delegieren.entity.PersoenlicheAngaben;
import ch.dvbern.stip.api.delegieren.repo.DelegierungRepository;
import ch.dvbern.stip.api.delegieren.service.DelegierenService;
import ch.dvbern.stip.api.delegieren.service.PersoenlicheAngabenMapper;
import ch.dvbern.stip.api.delegieren.service.PersoenlicheAngabenMapperImpl;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.notification.service.NotificationService;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import ch.dvbern.stip.api.sozialdienst.entity.Sozialdienst;
import ch.dvbern.stip.api.sozialdienst.repo.SozialdienstRepository;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import ch.dvbern.stip.api.sozialdienstbenutzer.entity.SozialdienstBenutzer;
import ch.dvbern.stip.api.sozialdienstbenutzer.repo.SozialdienstBenutzerRepository;
import ch.dvbern.stip.api.sozialdienstbenutzer.service.SozialdienstBenutzerService;
import ch.dvbern.stip.generated.dto.AdresseDto;
import ch.dvbern.stip.generated.dto.DelegierterMitarbeiterAendernDto;
import ch.dvbern.stip.generated.dto.DelegierungCreateDto;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ForbiddenException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class DelegierenResourceImplTest {
    private DelegierenResourceImpl delegierenApi;
    private AdresseMapper adresseMapper;
    private FallRepository fallRepository;
    private DelegierungRepository delegierungRepository;
    private SozialdienstRepository sozialdienstRepository;
    private BenutzerService benutzerService;
    private SozialdienstService sozialdienstService;
    private SozialdienstBenutzerRepository sozialdienstBenutzerRepository;
    private SozialdienstBenutzerService sozialdienstBenutzerService;
    private NotificationService notificationService;
    private MailService mailService;

    @BeforeEach
    void setup() {
        fallRepository = Mockito.mock(FallRepository.class);
        delegierungRepository = Mockito.mock(DelegierungRepository.class);
        sozialdienstRepository = Mockito.mock(SozialdienstRepository.class);
        benutzerService = Mockito.mock(BenutzerService.class);
        sozialdienstService = Mockito.mock(SozialdienstService.class);
        sozialdienstBenutzerRepository = Mockito.mock(SozialdienstBenutzerRepository.class);
        sozialdienstBenutzerService = Mockito.mock(SozialdienstBenutzerService.class);
        adresseMapper = Mockito.mock(AdresseMapperImpl.class);
        notificationService = Mockito.mock(NotificationService.class);
        mailService = Mockito.mock(MailService.class);

        PersoenlicheAngabenMapper persoenlicheAngabenMapper = new PersoenlicheAngabenMapperImpl(adresseMapper);
        DelegierenAuthorizer delegierenAuthorizer = new DelegierenAuthorizer(
            benutzerService, fallRepository, delegierungRepository, sozialdienstService,
            sozialdienstBenutzerRepository, sozialdienstBenutzerService
        );
        DelegierenService delegierenService = new DelegierenService(
            delegierungRepository, fallRepository, sozialdienstRepository, sozialdienstService,
            sozialdienstBenutzerRepository, persoenlicheAngabenMapper, null, null,
            null, notificationService, mailService
        );
        delegierenApi = new DelegierenResourceImpl(delegierenAuthorizer, delegierenService);
    }

    @Test
    void delegateNotOwnGesuchFails() {
        var benutzer = new Benutzer();
        benutzer.setId(UUID.randomUUID());

        var gesuchsteller = new Benutzer();
        benutzer.setId(UUID.randomUUID());

        var fall = new Fall();
        fall.setId(UUID.randomUUID());
        fall.setGesuchsteller(benutzer);

        when(benutzerService.getCurrentBenutzer()).thenReturn(gesuchsteller);
        when(fallRepository.requireById(any())).thenReturn(fall);

        Assertions.assertThrows(
            ForbiddenException.class,
            () -> delegierenApi.fallDelegieren(fall.getId(), UUID.randomUUID(), null)
        );
    }

    @Test
    void delegateToInaktiverSozialdienstFails() {
        var benutzer = new Benutzer();
        benutzer.setId(UUID.randomUUID());

        var sozialdienst = new Sozialdienst();
        sozialdienst.setId(UUID.randomUUID());
        sozialdienst.setAktiv(false);

        var fall = new Fall();
        fall.setId(UUID.randomUUID());
        fall.setGesuchsteller(benutzer);

        when(benutzerService.getCurrentBenutzer()).thenReturn(benutzer);
        when(fallRepository.requireById(any())).thenReturn(fall);
        when(sozialdienstRepository.requireById(any())).thenReturn(sozialdienst);

        Assertions.assertThrows(
            BadRequestException.class,
            () -> delegierenApi.fallDelegieren(fall.getId(), sozialdienst.getId(), null)
        );
    }

    @Test
    void delegateGesuch() {
        var benutzer = new Benutzer();
        benutzer.setId(UUID.randomUUID());

        var sozialdienst = new Sozialdienst();
        sozialdienst.setId(UUID.randomUUID());

        var fall = new Fall();
        fall.setId(UUID.randomUUID());
        fall.setGesuchsteller(benutzer);

        var delegierungCreate = new DelegierungCreateDto()
            .anrede(Anrede.HERR)
            .nachname("Mustermann")
            .vorname("Max")
            .geburtsdatum(LocalDate.of(1990, 1, 1))
            .adresse(new AdresseDto());

        when(benutzerService.getCurrentBenutzer()).thenReturn(benutzer);
        when(fallRepository.requireById(any())).thenReturn(fall);
        when(sozialdienstRepository.requireById(any())).thenReturn(sozialdienst);
        when(adresseMapper.toEntity(any())).thenReturn(new Adresse());

        Mockito.doAnswer(invocationOnMock -> {
            var delegierung = invocationOnMock.getArgument(0, Delegierung.class);
            assertEquals(delegierung.getSozialdienst().getId(), sozialdienst.getId());
            assertEquals(delegierung.getDelegierterFall().getId(), fall.getId());
            assertEquals(delegierung.getPersoenlicheAngaben().getAnrede(), delegierungCreate.getAnrede());
            assertEquals(delegierung.getPersoenlicheAngaben().getNachname(), delegierungCreate.getNachname());
            assertEquals(delegierung.getPersoenlicheAngaben().getVorname(), delegierungCreate.getVorname());
            assertEquals(delegierung.getPersoenlicheAngaben().getGeburtsdatum(), delegierungCreate.getGeburtsdatum());
            return null;
        }).when(delegierungRepository).persist(any(Delegierung.class));

        delegierenApi.fallDelegieren(fall.getId(), sozialdienst.getId(), delegierungCreate);
    }

    @Test
    void delegierterMitarbeiterAendernAsMaFail() {
        var currentBenutzer = new SozialdienstBenutzer();
        currentBenutzer.setId(UUID.randomUUID());

        var targetBenutzer = new SozialdienstBenutzer();
        targetBenutzer.setId(UUID.randomUUID());

        var sozialdienst = new Sozialdienst();
        sozialdienst.setId(UUID.randomUUID());
        sozialdienst.setSozialdienstBenutzers(List.of(currentBenutzer, targetBenutzer));

        var delegierung = new Delegierung();
        delegierung.setId(UUID.randomUUID());
        delegierung.setSozialdienst(sozialdienst);

        var delegierterMitarbeiterAendern = new DelegierterMitarbeiterAendernDto();
        delegierterMitarbeiterAendern.mitarbeiterId(UUID.randomUUID());

        when(delegierungRepository.requireById(any())).thenReturn(delegierung);
        when(sozialdienstBenutzerRepository.requireById(any())).thenReturn(targetBenutzer);
        when(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(any())).thenReturn(true);
        when(sozialdienstBenutzerService.getCurrentSozialdienstBenutzer()).thenReturn(Optional.of(currentBenutzer));

        Assertions.assertThrows(
            ForbiddenException.class,
            () -> delegierenApi.delegierterMitarbeiterAendern(delegierung.getId(), delegierterMitarbeiterAendern)
        );
    }

    @Test
    void delegierterMitarbeiterAendernAsAdmin() {
        var currentBenutzer = new SozialdienstBenutzer();
        currentBenutzer.setId(UUID.randomUUID());

        var targetBenutzer = new SozialdienstBenutzer();
        targetBenutzer.setId(UUID.randomUUID());

        var sozialdienst = new Sozialdienst();
        sozialdienst.setId(UUID.randomUUID());
        sozialdienst.setSozialdienstBenutzers(List.of(currentBenutzer, targetBenutzer));
        sozialdienst.setSozialdienstAdmin(currentBenutzer);

        var persoenlicheAngaben = new PersoenlicheAngaben();
        persoenlicheAngaben.setNachname("Test N");
        persoenlicheAngaben.setVorname("Test V");
        persoenlicheAngaben.setEmail("stip-delegierung@mailbucket.dvbern.ch");
        persoenlicheAngaben.setSprache(Sprache.DEUTSCH);

        var fall = new Fall();
        fall.setId(UUID.randomUUID());

        var delegierung = new Delegierung();
        delegierung.setId(UUID.randomUUID());
        delegierung.setSozialdienst(sozialdienst);
        delegierung.setPersoenlicheAngaben(persoenlicheAngaben);
        delegierung.setDelegierterFall(fall);

        var delegierterMitarbeiterAendern = new DelegierterMitarbeiterAendernDto();
        delegierterMitarbeiterAendern.mitarbeiterId(UUID.randomUUID());

        when(delegierungRepository.requireById(any())).thenReturn(delegierung);
        when(sozialdienstBenutzerRepository.requireById(any())).thenReturn(targetBenutzer);
        when(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(any())).thenReturn(true);
        when(sozialdienstService.isBenutzerMitarbeiterOfSozialdienst(any(), any())).thenReturn(true);
        when(sozialdienstBenutzerService.getCurrentSozialdienstBenutzer()).thenReturn(Optional.of(currentBenutzer));

        delegierenApi.delegierterMitarbeiterAendern(delegierung.getId(), delegierterMitarbeiterAendern);
    }
}
