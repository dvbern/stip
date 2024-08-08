package ch.dvbern.stip.api.dokument.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.common.statemachines.dokument.DokumentstatusConfigProducer;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.entity.GesuchDokumentKommentar;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentKommentarRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.generated.dto.GesuchDokumentAblehnenRequestDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentKommentarDto;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import software.amazon.awssdk.services.s3.S3AsyncClient;

import static ch.dvbern.stip.api.generator.entities.GesuchGenerator.initGesuchTranche;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@QuarkusTest
@QuarkusTestResource(TestDatabaseEnvironment.class)
@RequiredArgsConstructor
class GesuchDokumentServiceTest {
    @InjectMock
    GesuchDokumentRepository gesuchDokumentRepository;

    @InjectMock
    GesuchDokumentKommentarRepository gesuchDokumentKommentarRepository;

    @InjectMock
    DokumentRepository dokumentRepository;

    @Inject
    GesuchDokumentService gesuchDokumentService;

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
        }).when(gesuchDokumentKommentarRepository).persist((GesuchDokumentKommentar) Mockito.any());

        Mockito.when(gesuchDokumentRepository.getAllForGesuchInStatus(Mockito.any(), Mockito.any()))
            .thenAnswer(invocation -> gesuchDokumente.values()
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

    //todo: only sb should be able to trigger endpoint

    @Test
    void ablehnenCreatesCommentWithTextTest() {
        // Arrange
        final var someKnownComment = new GesuchDokumentAblehnenRequestDto();
        mockedDokument = (GesuchDokument) new GesuchDokument()
            .setStatus(Dokumentstatus.AUSSTEHEND)
            .setDokumentTyp(DokumentTyp.EK_VERDIENST)
            .setId(id);

        GesuchDokumentKommentarDto gesuchDokumentKommentarDto = new GesuchDokumentKommentarDto();
        gesuchDokumentKommentarDto.setGesuchDokumentId(mockedDokument.getId());
        gesuchDokumentKommentarDto.setKommentar("Some known comment");
        gesuchDokumentKommentarDto.setBenutzer("Max Muster");
        gesuchDokumentKommentarDto.setDatum(LocalDate.now());
        someKnownComment.setKommentar(gesuchDokumentKommentarDto);

        GesuchTranche tranche = initGesuchTranche();

        tranche.getGesuch().setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_SB);
        mockedDokument.setGesuch(tranche.getGesuch());

        // Act
        gesuchDokumentService.gesuchDokumentAblehnen(mockedDokument.getId(), someKnownComment);

        // Assert
        assertThat(comment.getKommentar(), is(someKnownComment.getKommentar().getKommentar()));
        assertThat(comment.getDokumentstatus(), is(Dokumentstatus.ABGELEHNT));
    }

    @Test
    void akzeptierenCreatesCommentWithNull() {
        // Arrange
        mockedDokument = (GesuchDokument) new GesuchDokument()
            .setStatus(Dokumentstatus.AUSSTEHEND)
            .setDokumentTyp(DokumentTyp.EK_VERDIENST)
            .setId(id);

        GesuchTranche tranche = initGesuchTranche();

        tranche.getGesuch().setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_SB);
        mockedDokument.setGesuch(tranche.getGesuch());

        // Act
        gesuchDokumentService.gesuchDokumentAkzeptieren(mockedDokument.getId());

        // Assert
        assertThat(comment.getKommentar(), is(nullValue()));
        assertThat(comment.getDokumentstatus(), is(Dokumentstatus.AKZEPTIERT));
    }

    @Test
    void deleteAbgelehnteDokumenteForGesuchTest() {
        // Arrange
        final var gsDokService = new GesuchDokumentServiceMock(
            new DokumentMapperImpl(),
            dokumentRepository,
            gesuchDokumentRepository,
            null,
            null,
            null,
            new DokumentstatusService(
                new DokumentstatusConfigProducer().createStateMachineConfig(),
                new GesuchDokumentKommentarService(gesuchDokumentKommentarRepository, new GesuchDokumentKommentarMapperImpl())
            ), new GesuchDokumentKommentarMapperImpl()
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
        gsDokService.deleteAbgelehnteDokumenteForGesuch(new Gesuch());

        // Assert
        assertThat(gesuchDokumente.size(), is(2));
        final var abgelehntesGesuchDokument = gesuchDokumente
            .values()
            .stream()
            .filter(x -> x.getStatus() == Dokumentstatus.ABGELEHNT)
            .findFirst()
            .get();
        assertThat(abgelehntesGesuchDokument.getDokumente().size(), is(0));
    }

    private static class GesuchDokumentServiceMock extends GesuchDokumentService {
        public GesuchDokumentServiceMock(
            DokumentMapper dokumentMapper,
            DokumentRepository dokumentRepository,
            GesuchDokumentRepository gesuchDokumentRepository,
            GesuchRepository gesuchRepository,
            S3AsyncClient s3,
            ConfigService configService,
            DokumentstatusService dokumentstatusService,
            GesuchDokumentKommentarMapper dokumentKommentarMapper
        ) {
            super(
                dokumentMapper,
                dokumentRepository,
                gesuchDokumentRepository,
                gesuchRepository,
                s3,
                configService,
                dokumentstatusService,
                dokumentKommentarMapper
            );
        }

        @Override
        public void executeDeleteDokumentsFromS3(List<String> objectIds) {
        }
    }
}
