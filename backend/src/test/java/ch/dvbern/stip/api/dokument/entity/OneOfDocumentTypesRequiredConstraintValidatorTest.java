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

package ch.dvbern.stip.api.dokument.entity;

import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Execution(ExecutionMode.CONCURRENT)
class OneOfDocumentTypesRequiredConstraintValidatorTest {
    private Validator validator;
    private GesuchDokument dokument;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        dokument = new GesuchDokument();
        dokument.setGesuchTranche(new GesuchTranche());
    }

    @Test
    void noTypePresentTest() {
        // one of both (DokumentTyp or CustomDokumentTyp) should be present
        assertFalse(validator.validate(dokument).isEmpty());
    }

    @Test
    void onlyDocumentTypPresentTest() {
        dokument.setDokumentTyp(DokumentTyp.AUSBILDUNG_BESTAETIGUNG_AUSBILDUNGSSTAETTE);
        assertTrue(validator.validate(dokument).isEmpty());

    }

    @Test
    void onlyCustomDocumentTypPresentTest() {
        dokument.setCustomDokumentTyp(new CustomDokumentTyp().setType("test").setDescription("description"));
        assertTrue(validator.validate(dokument).isEmpty());
    }

    @Test
    void bothTypesPresentTest() {
        // one, NOT both should be present
        dokument.setDokumentTyp(DokumentTyp.AUSBILDUNG_BESTAETIGUNG_AUSBILDUNGSSTAETTE);
        dokument.setCustomDokumentTyp(new CustomDokumentTyp().setType("test").setDescription("description"));
        assertFalse(validator.validate(dokument).isEmpty());
    }
}
