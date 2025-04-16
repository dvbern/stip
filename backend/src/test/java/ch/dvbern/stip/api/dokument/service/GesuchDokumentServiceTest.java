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

package ch.dvbern.stip.api.dokument.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.common.authorization.CustomGesuchDokumentTypAuthorizer;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.dokument.entity.CustomDokumentTyp;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.entity.GesuchDokumentKommentar;
import ch.dvbern.stip.api.dokument.repo.CustomDokumentTypRepository;
import ch.dvbern.stip.api.dokument.repo.DokumentHistoryRepository;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentKommentarRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.gesuchtranchehistory.service.GesuchTrancheHistoryService;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.GesuchDokumentAblehnenRequestDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentKommentarDto;
import io.quarkiverse.antivirus.runtime.Antivirus;
import io.quarkus.security.ForbiddenException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import software.amazon.awssdk.services.s3.S3AsyncClient;

import static ch.dvbern.stip.api.generator.entities.GesuchGenerator.initGesuchTranche;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@RequiredArgsConstructor
class GesuchDokumentServiceTest {
    @InjectMock
    GesuchDokumentRepository gesuchDokumentRepository;

    @InjectMock
    GesuchDokumentKommentarRepository gesuchDokumentKommentarRepository;

    @InjectMock
    CustomDokumentTypRepository customDokumentTypRepository;

    @InjectMock
    DokumentRepository dokumentRepository;

    @InjectMock
    GesuchRepository gesuchRepository;

    @Inject
    GesuchDokumentService gesuchDokumentService;

    @InjectMock
    GesuchTrancheRepository gesuchTrancheRepository;

    @Inject
    CustomGesuchDokumentTypAuthorizer customGesuchDokumentTypAuthorizer;

    private final UUID id = UUID.randomUUID();

    private GesuchDokument mockedDokument;
    private GesuchDokumentKommentar comment;

    private HashMap<UUID, GesuchDokument> gesuchDokumente;

    @BeforeEach
    void setup() {
        Mockito.when(gesuchDokumentRepository.requireById(id)).thenAnswer(invocation -> mockedDokument);

        Mockito.doAnswer(invocation -> {
            comment = invocation.getArgument(0);
            return null;
        }).when(gesuchDokumentKommentarRepository).persistAndFlush((GesuchDokumentKommentar) Mockito.any());

        Mockito.when(gesuchDokumentRepository.getAllForGesuchInStatus(Mockito.any(), Mockito.any()))
            .thenAnswer(
                invocation -> gesuchDokumente.values()
                    .stream()
                    .filter(x -> x.getStatus() == invocation.getArgument(1))
            );

        Mockito.doNothing().when(dokumentRepository).delete(Mockito.any());
        Mockito.doAnswer((Answer<Void>) invocation -> {
            final var arg = (GesuchDokument) invocation.getArgument(0);
            gesuchDokumente.remove(arg.getId());
            return null;
        }).when(gesuchDokumentRepository).delete(Mockito.any());
    }

    @TestAsGesuchsteller
    @Test
    void gesuchStellerShouldNotBeAbleToInvokeEndpoint() {
        // Arrange
        final var ablehnenRequest = new GesuchDokumentAblehnenRequestDto();
        mockedDokument = (GesuchDokument) new GesuchDokument()
            .setStatus(Dokumentstatus.AUSSTEHEND)
            .setDokumentTyp(DokumentTyp.EK_VERDIENST)
            .setId(id);

        GesuchDokumentKommentarDto gesuchDokumentKommentarDto = new GesuchDokumentKommentarDto();
        gesuchDokumentKommentarDto.setKommentar("Some known comment");
        gesuchDokumentKommentarDto.setTimestampErstellt(LocalDate.now());
        ablehnenRequest.setKommentar(gesuchDokumentKommentarDto);

        GesuchTranche tranche = initGesuchTranche();

        tranche.getGesuch().setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_SB);
        mockedDokument.setGesuchTranche(tranche);

        // Act & Assert
        assertThrows(ForbiddenException.class, () -> {
            gesuchDokumentService.gesuchDokumentAblehnen(mockedDokument.getId(), ablehnenRequest);
        });
    }

    @TestAsSachbearbeiter
    @Test
    void ablehnenCreatesCommentWithTextTest() {
        // Arrange
        final var ablehnenRequest = new GesuchDokumentAblehnenRequestDto();
        mockedDokument = (GesuchDokument) new GesuchDokument()
            .setStatus(Dokumentstatus.AUSSTEHEND)
            .setDokumentTyp(DokumentTyp.EK_VERDIENST)
            .setId(id);

        GesuchDokumentKommentarDto gesuchDokumentKommentarDto = new GesuchDokumentKommentarDto();
        gesuchDokumentKommentarDto.setKommentar("Some known comment");
        gesuchDokumentKommentarDto.setTimestampErstellt(LocalDate.now());
        ablehnenRequest.setKommentar(gesuchDokumentKommentarDto);

        GesuchTranche tranche = initGesuchTranche();

        tranche.getGesuch().setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_SB);
        mockedDokument.setGesuchTranche(tranche);

        // Act
        gesuchDokumentService.gesuchDokumentAblehnen(mockedDokument.getId(), ablehnenRequest);

        // Assert
        assertThat(comment.getKommentar(), is(ablehnenRequest.getKommentar().getKommentar()));
        assertThat(comment.getDokumentstatus(), is(Dokumentstatus.ABGELEHNT));
    }

    @TestAsSachbearbeiter
    @Test
    void akzeptierenCreatesCommentWithNull() {
        // Arrange
        mockedDokument = (GesuchDokument) new GesuchDokument()
            .setStatus(Dokumentstatus.AUSSTEHEND)
            .setDokumentTyp(DokumentTyp.EK_VERDIENST)
            .setId(id);

        GesuchTranche tranche = initGesuchTranche();

        tranche.getGesuch().setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_SB);
        mockedDokument.setGesuchTranche(tranche);

        // Act
        gesuchDokumentService.gesuchDokumentAkzeptieren(mockedDokument.getId());

        // Assert
        assertNull(comment.getKommentar());
        assertThat(comment.getDokumentstatus(), is(Dokumentstatus.AKZEPTIERT));
    }

    @Test
    void deleteFilesOfAbgelehnteDokumenteForGesuchTest() {
        final var dokumentMapper = new DokumentMapperImpl();
        final var customDokumentMapper = new CustomDocumentTypMapperImpl();
        // Arrange
        final var gsDokService = new GesuchDokumentServiceMock(
            new GesuchDokumentMapperImpl(dokumentMapper, customDokumentMapper),
            dokumentRepository,
            gesuchDokumentRepository,
            null,
            null,
            null,
            null,
            new DokumentstatusService(
                new GesuchDokumentKommentarService(
                    null,
                    gesuchDokumentKommentarRepository,
                    null,
                    new GesuchDokumentKommentarMapperImpl(),
                    null,
                    null,
                    null,
                    null
                ),
                null
            ),
            null,
            null,
            null,
            null,
            null,
            null
        );

        gesuchDokumente = new HashMap<>();
        final var abgelehntId = UUID.randomUUID();
        gesuchDokumente.put(
            abgelehntId,
            (GesuchDokument) new GesuchDokument()
                .setStatus(Dokumentstatus.ABGELEHNT)
                .setDokumente(new ArrayList<>())
                .setId(abgelehntId)
        );
        gesuchDokumente.put(
            UUID.randomUUID(),
            new GesuchDokument().setStatus(Dokumentstatus.AKZEPTIERT).setDokumente(new ArrayList<>())
        );

        // Act
        var docs = new ArrayList<GesuchDokument>();
        docs.add(new GesuchDokument());
        gsDokService.deleteFilesOfAbgelehnteGesuchDokumenteForGesuch(docs);

        // Assert
        assertThat(gesuchDokumente.size(), is(2));
        final var abgelehntesGesuchDokument = gesuchDokumente
            .values()
            .stream()
            .filter(x -> x.getStatus() == Dokumentstatus.ABGELEHNT)
            .findFirst();
        // denied GesuchDokuments will not be deleted anymore
        assertThat(abgelehntesGesuchDokument.isEmpty(), is(false));
        // instead, all attached files will be deleted
        assertThat(abgelehntesGesuchDokument.get().getDokumente().isEmpty(), is(true));
    }

    @Test
    @TestAsSachbearbeiter
    void sbShouldNotBeAbleToDeleteNonEmptyAndSentCustomDokumentTyp() {
        /*
         * Setup:
         * SB has added a custom dokument type
         * GS has attached 1 file
         *
         * Expected:
         * SB should not be able to delete that Custom Dokument Type
         */

        // Arrange
        Gesuch gesuch = TestUtil.setupGesuchWithCustomDokument();
        gesuch.getGesuchTranchen().get(0).setGesuch(gesuch);
        GesuchDokument customGesuchDokument = gesuch.getCurrentGesuchTranche().getGesuchDokuments().get(0);
        customGesuchDokument.setGesuchTranche(gesuch.getGesuchTranchen().get(0));

        CustomDokumentTyp customDokumentTyp = new CustomDokumentTyp();
        customDokumentTyp.setId(UUID.randomUUID());
        customDokumentTyp.setDescription("test");
        customDokumentTyp.setType("test");
        customDokumentTyp.setGesuchDokument(customGesuchDokument);

        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        when(gesuchTrancheRepository.requireById(any())).thenReturn(gesuch.getGesuchTranchen().get(0));
        when(gesuchDokumentRepository.findByCustomDokumentTyp(any()))
            .thenReturn(Optional.of(customGesuchDokument));
        when(customDokumentTypRepository.requireById(any())).thenReturn(customDokumentTyp);

        final UUID gesuchTrancheId = UUID.randomUUID();
        gesuch.getGesuchTranchen().get(0).setId(gesuchTrancheId);
        gesuch.getGesuchTranchen().get(0).setTyp(GesuchTrancheTyp.TRANCHE);
        // Act
        // should not throw, since there is no file attached
        assertDoesNotThrow(
            () -> customGesuchDokumentTypAuthorizer
                .canDeleteTyp(customGesuchDokument.getCustomDokumentTyp().getId())
        );

        // attach one file
        customGesuchDokument.getDokumente().add(new Dokument());
        // send missing files to GS
        gesuch.setGesuchStatus(Gesuchstatus.FEHLENDE_DOKUMENTE);
        assertThrows(
            jakarta.ws.rs.ForbiddenException.class,
            () -> customGesuchDokumentTypAuthorizer
                .canDeleteTyp(customGesuchDokument.getCustomDokumentTyp().getId())
        );
    }

    private static class GesuchDokumentServiceMock extends GesuchDokumentService {
        public GesuchDokumentServiceMock(
        GesuchDokumentMapper gesuchDokumentMapper,
        DokumentRepository dokumentRepository,
        GesuchDokumentRepository gesuchDokumentRepository,
        GesuchRepository gesuchRepository,
        GesuchTrancheRepository gesuchTrancheRepository,
        S3AsyncClient s3,
        ConfigService configService,
        DokumentstatusService dokumentstatusService,
        Antivirus antivirus,
        CustomDokumentTypRepository customDocumentTypRepository,
        GesuchDokumentKommentarRepository gesuchDokumentKommentarRepository,
        RequiredDokumentService requiredDokumentService,
        DokumentHistoryRepository dokumentHistoryRepository,
        GesuchTrancheHistoryService gesuchTrancheHistoryService
        ) {
            super(
                gesuchDokumentMapper,
                dokumentRepository,
                gesuchDokumentRepository,
                customDocumentTypRepository,
                gesuchRepository,
                gesuchTrancheRepository,
                s3,
                configService,
                dokumentstatusService,
                requiredDokumentService,
                antivirus,
                gesuchDokumentKommentarRepository,
                dokumentHistoryRepository,
                gesuchTrancheHistoryService
            );
        }

        @Override
        public void executeDeleteDokumentsFromS3(List<String> objectIds) {}
    }
}
