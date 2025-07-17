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

package ch.dvbern.stip.api.gesuch.entity;

import java.util.ArrayList;
import java.util.List;

import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.service.CustomDocumentTypMapper;
import ch.dvbern.stip.api.dokument.service.CustomDocumentTypMapperImpl;
import ch.dvbern.stip.api.dokument.service.DokumentMapper;
import ch.dvbern.stip.api.dokument.service.DokumentMapperImpl;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentMapper;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentMapperImpl;
import ch.dvbern.stip.api.dokument.type.GesuchDokumentStatus;
import ch.dvbern.stip.api.generator.entities.GesuchGenerator;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class DocumentsRequiredFehlendeDokumenteConstraintValidatorTest {
    DokumentMapper dokumentMapper = new DokumentMapperImpl();
    CustomDocumentTypMapper customDocumentTypMapper = new CustomDocumentTypMapperImpl();
    GesuchDokumentMapper gesuchDokumentMapper = new GesuchDokumentMapperImpl(dokumentMapper, customDocumentTypMapper);
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
        final var gesuchDokuments = createWithStatus(GesuchDokumentStatus.AUSSTEHEND);
        final var gesuchDokumentDtos =
            gesuchDokuments.stream().map(gesuchDokument -> gesuchDokumentMapper.toDto(gesuchDokument)).toList();
        final var gesuch = GesuchGenerator.initGesuch();
        gesuch.getNewestGesuchTranche().get().setGesuchDokuments(gesuchDokuments);
        final var validator = new DocumentsRequiredFehlendeDokumenteConstraintValidator();

        Mockito.doReturn(gesuchDokumentDtos)
            .when(gesuchServiceMock)
            .getGesuchDokumenteForGesuch(ArgumentMatchers.any());
        validator.gesuchService = gesuchServiceMock;

        // Act
        final var isValid = validator.isValid(gesuch, null);

        // Assert
        assertThat(isValid, is(false));
    }

    @Test
    void allAbgelehntIsValid() {
        // Arrange
        final var gesuchDokuments = createWithStatus(GesuchDokumentStatus.ABGELEHNT);
        final var gesuchDokumentDtos =
            gesuchDokuments.stream().map(gesuchDokument -> gesuchDokumentMapper.toDto(gesuchDokument)).toList();
        final var gesuch = GesuchGenerator.initGesuch();
        gesuch.getNewestGesuchTranche().get().setGesuchDokuments(gesuchDokuments);
        final var validator = new DocumentsRequiredFehlendeDokumenteConstraintValidator();

        Mockito.doReturn(gesuchDokumentDtos)
            .when(gesuchServiceMock)
            .getGesuchDokumenteForGesuch(ArgumentMatchers.any());
        validator.gesuchService = gesuchServiceMock;

        // Act
        final var isValid = validator.isValid(gesuch, null);

        // Assert
        assertThat(isValid, is(true));
    }

    @Test
    void mixedIsValid() {
        // Arrange
        final var gesuchDokuments = createWithStatus(GesuchDokumentStatus.AKZEPTIERT, GesuchDokumentStatus.ABGELEHNT);
        final var gesuchDokumentDtos =
            gesuchDokuments.stream().map(gesuchDokument -> gesuchDokumentMapper.toDto(gesuchDokument)).toList();
        final var gesuch = GesuchGenerator.initGesuch();
        gesuch.getNewestGesuchTranche().get().setGesuchDokuments(gesuchDokuments);
        final var validator = new DocumentsRequiredFehlendeDokumenteConstraintValidator();

        Mockito.doReturn(gesuchDokumentDtos)
            .when(gesuchServiceMock)
            .getGesuchDokumenteForGesuch(ArgumentMatchers.any());
        validator.gesuchService = gesuchServiceMock;

        // Act
        final var isValid = validator.isValid(gesuch, null);

        // Assert
        assertThat(isValid, is(true));
    }

    @Test
    void mixedWithAusstehendIsNotValid() {
        // Arrange
        final var gesuchDokuments =
            createWithStatus(
                GesuchDokumentStatus.AKZEPTIERT,
                GesuchDokumentStatus.ABGELEHNT,
                GesuchDokumentStatus.AUSSTEHEND
            );
        final var gesuchDokumentDtos =
            gesuchDokuments.stream().map(gesuchDokument -> gesuchDokumentMapper.toDto(gesuchDokument)).toList();
        final var gesuch = GesuchGenerator.initGesuch();
        gesuch.getNewestGesuchTranche().get().setGesuchDokuments(gesuchDokuments);
        final var validator = new DocumentsRequiredFehlendeDokumenteConstraintValidator();

        Mockito.doReturn(gesuchDokumentDtos)
            .when(gesuchServiceMock)
            .getGesuchDokumenteForGesuch(ArgumentMatchers.any());
        validator.gesuchService = gesuchServiceMock;

        // Act
        final var isValid = validator.isValid(gesuch, null);

        // Assert
        assertThat(isValid, is(false));
    }

    private List<GesuchDokument> createWithStatus(final GesuchDokumentStatus... statuses) {
        final var result = new ArrayList<GesuchDokument>();
        for (final var status : statuses) {
            result.add(
                new GesuchDokument()
                    .setStatus(status)
            );
        }

        return result;
    }
}
