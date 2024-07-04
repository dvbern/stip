package ch.dvbern.stip.api.gesuch.entity;

import java.util.ArrayList;
import java.util.List;

import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class DocumentsRequiredFehlendeDokumenteConstraintValidatorTest {
    @Test
    void noDokumenteIsValid() {
        // Arrange
        final var gesuch = new Gesuch().setGesuchDokuments(List.of());
        final var validator = new DocumentsRequiredFehlendeDokumenteConstraintValidator();

        // Act
        final var isValid = validator.isValid(gesuch, null);

        // Assert
        assertThat(isValid, is(true));
    }

    @Test
    void dokumenteAusstehendNotValid() {
        // Arrange
        final var gesuch = new Gesuch().setGesuchDokuments(createWithStatus(Dokumentstatus.AUSSTEHEND));
        final var validator = new DocumentsRequiredFehlendeDokumenteConstraintValidator();

        // Act
        final var isValid = validator.isValid(gesuch, null);

        // Assert
        assertThat(isValid, is(false));
    }

    @Test
    void allAkzeptiertIsValid() {
        // Arrange
        final var gesuch = new Gesuch().setGesuchDokuments(createWithStatus(Dokumentstatus.AKZEPTIERT));
        final var validator = new DocumentsRequiredFehlendeDokumenteConstraintValidator();

        // Act
        final var isValid = validator.isValid(gesuch, null);

        // Assert
        assertThat(isValid, is(true));
    }

    @Test
    void allAbgelehntIsValid() {
        // Arrange
        final var gesuch = new Gesuch().setGesuchDokuments(createWithStatus(Dokumentstatus.ABGELEHNT));
        final var validator = new DocumentsRequiredFehlendeDokumenteConstraintValidator();

        // Act
        final var isValid = validator.isValid(gesuch, null);

        // Assert
        assertThat(isValid, is(true));
    }

    @Test
    void mixedIsValid() {
        // Arrange
        final var gesuch = new Gesuch()
            .setGesuchDokuments(createWithStatus(Dokumentstatus.AKZEPTIERT, Dokumentstatus.ABGELEHNT));
        final var validator = new DocumentsRequiredFehlendeDokumenteConstraintValidator();

        // Act
        final var isValid = validator.isValid(gesuch, null);

        // Assert
        assertThat(isValid, is(true));
    }

    @Test
    void mixedWithAusstehendIsNotValid() {
        // Arrange
        final var gesuch = new Gesuch().setGesuchDokuments(createWithStatus(
            Dokumentstatus.AKZEPTIERT, Dokumentstatus.ABGELEHNT, Dokumentstatus.AUSSTEHEND
        ));
        final var validator = new DocumentsRequiredFehlendeDokumenteConstraintValidator();

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
