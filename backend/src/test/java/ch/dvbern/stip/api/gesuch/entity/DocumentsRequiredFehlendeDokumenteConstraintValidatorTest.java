package ch.dvbern.stip.api.gesuch.entity;

import java.util.ArrayList;
import java.util.List;

import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.service.DokumentMapper;
import ch.dvbern.stip.api.dokument.service.DokumentMapperImpl;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentMapper;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentMapperImpl;
import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.generator.entities.GesuchGenerator;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class DocumentsRequiredFehlendeDokumenteConstraintValidatorTest {
    DokumentMapper dokumentMapper = new DokumentMapperImpl();
    GesuchDokumentMapper gesuchDokumentMapper = new GesuchDokumentMapperImpl(dokumentMapper);
    final GesuchService gesuchServiceMock = Mockito.mock(GesuchService.class);

    @Test
    void noDokumenteIsValid() {
        // Arrange
        final List<GesuchDokument> gesuchDokuments = List.of();
        final var gesuch = new Gesuch();
        final var validator = new DocumentsRequiredFehlendeDokumenteConstraintValidator();

        Mockito.doReturn(gesuchDokuments).when(gesuchServiceMock).getGesuchDokumenteForGesuch(ArgumentMatchers.any());
        validator.gesuchService = gesuchServiceMock;

        // Act
        final var isValid = validator.isValid(gesuch, null);

        // Assert
        assertThat(isValid, is(true));
    }

    @Test
    void dokumenteAusstehendNotValid() {
        // Arrange
        final var gesuchDokuments = createWithStatus(Dokumentstatus.AUSSTEHEND);
        final var gesuchDokumentDtos = gesuchDokuments.stream().map(gesuchDokument -> gesuchDokumentMapper.toDto(gesuchDokument)).toList();
        final var gesuch = GesuchGenerator.initGesuch();
        gesuch.getNewestGesuchTranche().get().setGesuchDokuments(gesuchDokuments);
        final var validator = new DocumentsRequiredFehlendeDokumenteConstraintValidator();

        Mockito.doReturn(gesuchDokumentDtos).when(gesuchServiceMock).getGesuchDokumenteForGesuch(ArgumentMatchers.any());
        validator.gesuchService = gesuchServiceMock;

        // Act
        final var isValid = validator.isValid(gesuch, null);

        // Assert
        assertThat(isValid, is(false));
    }

    @Test
    void allAkzeptiertIsNotValid() {
        // Arrange
        final var gesuchDokuments = createWithStatus(Dokumentstatus.AKZEPTIERT);
        final var gesuchDokumentDtos = gesuchDokuments.stream().map(gesuchDokument -> gesuchDokumentMapper.toDto(gesuchDokument)).toList();
        final var gesuch = GesuchGenerator.initGesuch();
        gesuch.getNewestGesuchTranche().get().setGesuchDokuments(gesuchDokuments);
        final var validator = new DocumentsRequiredFehlendeDokumenteConstraintValidator();

        Mockito.doReturn(gesuchDokumentDtos).when(gesuchServiceMock).getGesuchDokumenteForGesuch(ArgumentMatchers.any());
        validator.gesuchService = gesuchServiceMock;

        // Act
        final var isValid = validator.isValid(gesuch, null);

        // Assert
        assertThat(isValid, is(false));
    }

    @Test
    void allAbgelehntIsValid() {
        // Arrange
        final var gesuchDokuments = createWithStatus(Dokumentstatus.ABGELEHNT);
        final var gesuchDokumentDtos = gesuchDokuments.stream().map(gesuchDokument -> gesuchDokumentMapper.toDto(gesuchDokument)).toList();
        final var gesuch = GesuchGenerator.initGesuch();
        gesuch.getNewestGesuchTranche().get().setGesuchDokuments(gesuchDokuments);
        final var validator = new DocumentsRequiredFehlendeDokumenteConstraintValidator();

        Mockito.doReturn(gesuchDokumentDtos).when(gesuchServiceMock).getGesuchDokumenteForGesuch(ArgumentMatchers.any());
        validator.gesuchService = gesuchServiceMock;

        // Act
        final var isValid = validator.isValid(gesuch, null);

        // Assert
        assertThat(isValid, is(true));
    }

    @Test
    void mixedIsValid() {
        // Arrange
        final var gesuchDokuments = createWithStatus(Dokumentstatus.AKZEPTIERT, Dokumentstatus.ABGELEHNT);
        final var gesuchDokumentDtos = gesuchDokuments.stream().map(gesuchDokument -> gesuchDokumentMapper.toDto(gesuchDokument)).toList();
        final var gesuch = GesuchGenerator.initGesuch();
        gesuch.getNewestGesuchTranche().get().setGesuchDokuments(gesuchDokuments);
        final var validator = new DocumentsRequiredFehlendeDokumenteConstraintValidator();

        Mockito.doReturn(gesuchDokumentDtos).when(gesuchServiceMock).getGesuchDokumenteForGesuch(ArgumentMatchers.any());
        validator.gesuchService = gesuchServiceMock;

        // Act
        final var isValid = validator.isValid(gesuch, null);

        // Assert
        assertThat(isValid, is(true));
    }

    @Test
    void mixedWithAusstehendIsNotValid() {
        // Arrange
        final var gesuchDokuments = createWithStatus(Dokumentstatus.AKZEPTIERT, Dokumentstatus.ABGELEHNT, Dokumentstatus.AUSSTEHEND);
        final var gesuchDokumentDtos = gesuchDokuments.stream().map(gesuchDokument -> gesuchDokumentMapper.toDto(gesuchDokument)).toList();
        final var gesuch = GesuchGenerator.initGesuch();
        gesuch.getNewestGesuchTranche().get().setGesuchDokuments(gesuchDokuments);
        final var validator = new DocumentsRequiredFehlendeDokumenteConstraintValidator();

        Mockito.doReturn(gesuchDokumentDtos).when(gesuchServiceMock).getGesuchDokumenteForGesuch(ArgumentMatchers.any());
        validator.gesuchService = gesuchServiceMock;

        // Act
        final var isValid = validator.isValid(gesuch, null);

        // Assert
        assertThat(isValid, is(false));
    }

    private List<GesuchDokument> createWithStatus(final Dokumentstatus... statuses) {
        final var result = new ArrayList<GesuchDokument>();
        for (final var status : statuses) {
            result.add(new GesuchDokument()
                .setStatus(status)
            );
        }

        return result;
    }
}
