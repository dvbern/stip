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

package ch.dvbern.stip.api.gesuchformular.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.dokument.entity.CustomDokumentTyp;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.service.CustomDokumentTypService;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.util.GesuchTestUtil;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jdk.jfr.Description;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.wildfly.common.Assert.assertFalse;
import static org.wildfly.common.Assert.assertTrue;

@QuarkusTest
class DocumentsRequiredConstraintValidatorTest {

    @InjectMock
    CustomDokumentTypService customDokumentTypService;
    @Inject
    private DocumentsRequiredConstraintValidator constraintValidator;

    private Gesuch gesuch;
    private GesuchFormular formular;

    @Test
    @Description("IN_BEARBEITUNG_SB: Validation should not fail when all required documents are present")
    public void validationShoudWorkWithAllDocuments_InBearbeitungSB() {
        gesuch = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.IN_BEARBEITUNG_SB);
        formular = gesuch.getGesuchTranchen().get(0).getGesuchFormular();

        var normalGesuchDokuments = new ArrayList<GesuchDokument>();
        Arrays.stream(DokumentTyp.values()).toList().forEach(dokumentType -> {
            GesuchDokument gesuchDokument = new GesuchDokument();
            gesuchDokument.setDokumentTyp(dokumentType);
            gesuchDokument.setStatus(Dokumentstatus.AKZEPTIERT);
            normalGesuchDokuments.add(gesuchDokument);
        });
        gesuch.getGesuchTranchen().get(0).getGesuchDokuments().addAll(normalGesuchDokuments);
        assertTrue(constraintValidator.isValid(formular, null));
    }

    /*
     * In the following situation, the SB may make changes so that some new documents are requred.
     * In this case, the validation should still success,
     * so that the SB may send the missing documents to the GS
     * on the current page
     */
    @Description("IN_BEARBEITUNG_SB: Validation should not fail when some required documents are missing")
    @Test
    public void validationShoudWorkWithMissingDocuments_InBearbeitungSB() {
        gesuch = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.IN_BEARBEITUNG_SB);
        formular = gesuch.getGesuchTranchen().get(0).getGesuchFormular();

        var normalGesuchDokuments = new ArrayList<GesuchDokument>();
        var types = Arrays.stream(DokumentTyp.values()).toList();
        ArrayList<DokumentTyp> givenTypes = new ArrayList<>(types);
        givenTypes.remove(types.get(0));
        givenTypes.forEach(dokumentType -> {
            GesuchDokument gesuchDokument = new GesuchDokument();
            gesuchDokument.setDokumentTyp(dokumentType);
            gesuchDokument.setStatus(Dokumentstatus.AKZEPTIERT);
            normalGesuchDokuments.add(gesuchDokument);
        });
        gesuch.getGesuchTranchen().get(0).getGesuchDokuments().addAll(normalGesuchDokuments);
        assertTrue(constraintValidator.isValid(formular, null));

        var customTyp = new CustomDokumentTyp();
        customTyp.setId(UUID.randomUUID());
        customTyp.setType("test");
        customTyp.setDescription("test");
        when(customDokumentTypService.getAllCustomDokumentTypsOfTranche(any())).thenReturn(List.of(customTyp));

        var customGesuchDokument = new GesuchDokument();
        customGesuchDokument.setCustomDokumentTyp(customTyp);
        customGesuchDokument.setStatus(Dokumentstatus.AUSSTEHEND);
        gesuch.getGesuchTranchen().get(0).getGesuchDokuments().add(customGesuchDokument);
        assertTrue(constraintValidator.isValid(formular, null));
    }

    @Test
    @Description("IN_BEARBEITUNG_GS: Validation should not fail when all required documents are present")
    public void validationShoudWorkWithAllDocuments_InBearbeitungGS() {
        gesuch = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.IN_BEARBEITUNG_GS);
        formular = gesuch.getGesuchTranchen().get(0).getGesuchFormular();

        var normalGesuchDokuments = new ArrayList<GesuchDokument>();
        Arrays.stream(DokumentTyp.values()).toList().forEach(dokumentType -> {
            GesuchDokument gesuchDokument = new GesuchDokument();
            gesuchDokument.setDokumentTyp(dokumentType);
            gesuchDokument.setStatus(Dokumentstatus.AKZEPTIERT);
            normalGesuchDokuments.add(gesuchDokument);
        });
        gesuch.getGesuchTranchen().get(0).getGesuchDokuments().addAll(normalGesuchDokuments);
        assertTrue(constraintValidator.isValid(formular, null));
    }

    @Description("IN_BEARBEITUNG_GS: Validation should fail when some required documents are missing")
    @Test
    public void validationShoudWorkWithMissingDocuments_InBearbeitungGS() {
        gesuch = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.IN_BEARBEITUNG_GS);
        formular = gesuch.getGesuchTranchen().get(0).getGesuchFormular();

        var normalGesuchDokuments = new ArrayList<GesuchDokument>();
        var types = Arrays.stream(DokumentTyp.values()).toList();
        ArrayList<DokumentTyp> givenTypes = new ArrayList<>(types);
        givenTypes.remove(types.get(0));
        givenTypes.forEach(dokumentType -> {
            GesuchDokument gesuchDokument = new GesuchDokument();
            gesuchDokument.setDokumentTyp(dokumentType);
            gesuchDokument.setStatus(Dokumentstatus.AKZEPTIERT);
            normalGesuchDokuments.add(gesuchDokument);
        });
        gesuch.getGesuchTranchen().get(0).getGesuchDokuments().addAll(normalGesuchDokuments);
        assertFalse(constraintValidator.isValid(formular, null));
    }

    @Description("IN_BEARBEITUNG_GS: Validation should fail when some required custom documents are missing")
    @Test
    public void validationShoudWorkWithMissingCustomDocuments_InBearbeitungGS() {
        gesuch = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.IN_BEARBEITUNG_GS);
        formular = gesuch.getGesuchTranchen().get(0).getGesuchFormular();

        var normalGesuchDokuments = new ArrayList<GesuchDokument>();
        Arrays.stream(DokumentTyp.values()).toList().forEach(dokumentType -> {
            GesuchDokument gesuchDokument = new GesuchDokument();
            gesuchDokument.setDokumentTyp(dokumentType);
            gesuchDokument.setStatus(Dokumentstatus.AKZEPTIERT);
            normalGesuchDokuments.add(gesuchDokument);
        });
        gesuch.getGesuchTranchen().get(0).getGesuchDokuments().addAll(normalGesuchDokuments);
        assertTrue(constraintValidator.isValid(formular, null));

        var customTyp = new CustomDokumentTyp();
        customTyp.setId(UUID.randomUUID());
        customTyp.setType("test");
        customTyp.setDescription("test");
        when(customDokumentTypService.getAllCustomDokumentTypsOfTranche(any())).thenReturn(List.of(customTyp));

        var customGesuchDokument = new GesuchDokument();
        customGesuchDokument.setCustomDokumentTyp(customTyp);
        customGesuchDokument.setStatus(Dokumentstatus.AUSSTEHEND);
        gesuch.getGesuchTranchen().get(0).getGesuchDokuments().add(customGesuchDokument);
        assertFalse(constraintValidator.isValid(formular, null));
    }

}
