package ch.dvbern.stip.api.dokument.resource;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.dokument.entity.GesuchDokumentKommentar;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentKommentarRepository;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.generated.api.DokumentResource;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static wiremock.org.hamcrest.MatcherAssert.assertThat;
import static wiremock.org.hamcrest.Matchers.is;

@QuarkusTest
class DokumentResourceImplTest {
    @Inject
    DokumentResource dokumentResource;
    @InjectMock
    GesuchDokumentKommentarRepository dokumentKommentarRepository;
    @InjectMock
    GesuchDokumentService gesuchDokumentService;

    @BeforeEach
    void setUp() {
        GesuchDokumentKommentar kommentar = new GesuchDokumentKommentar();
        when(dokumentKommentarRepository.getByTypAndGesuchId(any(), any())).thenReturn(List.of(kommentar));
    }

    @Test
    @TestAsGesuchsteller
    // Gesuchsteller should be able to read all comments of a gesuch document
    void resourceShouldReturnCommentsOfADokument(){
        assertThat(dokumentResource.getGesuchDokumentKommentare(DokumentTyp.EK_VERDIENST,UUID.randomUUID()).getStatus(), is(HttpStatus.SC_OK));
    }

    @TestAsGesuchsteller
    @Test
    void gsShouldNotBeAbleToDenyDocumentTest(){
        doNothing().when(gesuchDokumentService).gesuchDokumentAblehnen(any(),any());
        assertThrows(io.quarkus.security.ForbiddenException.class, () -> dokumentResource.gesuchDokumentAblehnen(UUID.randomUUID(),null));
    }

    @TestAsGesuchsteller
    @Test
    void gsShouldNotBeAbleToAcceptDocumentTest(){
        doNothing().when(gesuchDokumentService).gesuchDokumentAkzeptieren(any());
        assertThrows(io.quarkus.security.ForbiddenException.class, () -> dokumentResource.gesuchDokumentAkzeptieren(UUID.randomUUID()));
    }

    @TestAsSachbearbeiter
    @Test
    void sbShouldBeAbleToDenyDocumentTest(){
        doNothing().when(gesuchDokumentService).gesuchDokumentAblehnen(any(),any());
        assertThat(dokumentResource.gesuchDokumentAblehnen(UUID.randomUUID(),null).getStatus(), is(HttpStatus.SC_OK));
    }

    @TestAsSachbearbeiter
    @Test
    void sbShouldBeAbleToAcceptDocumentTest(){
        doNothing().when(gesuchDokumentService).gesuchDokumentAkzeptieren(any());
        assertThat(dokumentResource.gesuchDokumentAblehnen(UUID.randomUUID(),null).getStatus(), is(HttpStatus.SC_OK));
    }
}
