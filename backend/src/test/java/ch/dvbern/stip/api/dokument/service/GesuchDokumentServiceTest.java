package ch.dvbern.stip.api.dokument.service;

import java.util.UUID;

import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.entity.GesuchDokumentKommentar;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentKommentarRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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

    @Inject
    GesuchDokumentService gesuchDokumentService;

    private final UUID id = UUID.randomUUID();

    private GesuchDokument mockedDokument;
    private GesuchDokumentKommentar comment;

    @BeforeEach
    void setup() {
        Mockito.when(gesuchDokumentRepository.requireById(id)).thenAnswer(invocation -> mockedDokument);
        QuarkusMock.installMockForType(gesuchDokumentRepository, GesuchDokumentRepository.class);

        Mockito.doAnswer(invocation -> {
            comment = invocation.getArgument(0);
            return null;
        }).when(gesuchDokumentKommentarRepository).persist((GesuchDokumentKommentar) Mockito.any());
        QuarkusMock.installMockForType(gesuchDokumentKommentarRepository, GesuchDokumentKommentarRepository.class);
    }

    @Test
    void ablehnenCreatesCommentWithTextTest() {
        // Arrange
        final var someKnownComment = "Some known comment";

        mockedDokument = (GesuchDokument) new GesuchDokument()
            .setStatus(Dokumentstatus.AUSSTEHEND)
            .setDokumentTyp(DokumentTyp.EK_VERDIENST)
            .setId(id);

        // Act
        gesuchDokumentService.gesuchDokumentAblehnen(mockedDokument.getId(), someKnownComment);

        // Assert
        assertThat(comment.getKommentar(), is(someKnownComment));
        assertThat(comment.getDokumentstatus(), is(Dokumentstatus.ABGELEHNT));
    }

    @Test
    void akzeptierenCreatesCommentWithNull() {
        // Arrange
        mockedDokument = (GesuchDokument) new GesuchDokument()
            .setStatus(Dokumentstatus.AUSSTEHEND)
            .setDokumentTyp(DokumentTyp.EK_VERDIENST)
            .setId(id);

        // Act
        gesuchDokumentService.gesuchDokumentAkzeptieren(mockedDokument.getId());

        // Assert
        assertThat(comment.getKommentar(), is(nullValue()));
        assertThat(comment.getDokumentstatus(), is(Dokumentstatus.AKZEPTIERT));
    }
}
