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

package ch.dvbern.stip.api.gesuch.resource;

import java.util.Arrays;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsJurist;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.benutzer.util.TestAsSuperUser;
import ch.dvbern.stip.api.generator.api.GesuchTestSpecGenerator;
import ch.dvbern.stip.api.generator.api.model.gesuch.GesuchNotizCreateDtoSpecModel;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.StepwiseExtension;
import ch.dvbern.stip.api.util.StepwiseExtension.AlwaysRun;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.AuszahlungApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.api.GesuchNotizApiSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchNotizDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchNotizTypDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchstatusDtoSpec;
import ch.dvbern.stip.generated.dto.JuristischeAbklaerungNotizAntwortDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepwiseExtension.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class GesuchBereitFuerBearbeitungMissingDatenschutzbriefTest {
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    private final AuszahlungApiSpec auszahlungApiSpec = AuszahlungApiSpec.auszahlung(RequestSpecUtil.quarkusSpec());
    private final GesuchNotizApiSpec gesuchNotizApiSpec = GesuchNotizApiSpec.gesuchNotiz(RequestSpecUtil.quarkusSpec());

    private GesuchDtoSpec gesuch;
    private GesuchNotizDtoSpec juristischeNotiz;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void gesuchErstellen() {
        gesuch = TestUtil.createGesuchAusbildungFall(
            fallApiSpec,
            ausbildungApiSpec,
            gesuchApiSpec
        );
    }

    @Test
    @TestAsGesuchsteller
    @Order(2)
    void fillGesuch() {
        TestUtil.fillGesuch(
            gesuchApiSpec,
            GesuchTestSpecGenerator
                .gesuchUpdateDtoSpecFullNichtAnspruchsberechtig(gesuch.getGesuchTrancheToWorkWith().getId()),
            dokumentApiSpec,
            gesuch
        );
        TestUtil.fillAuszahlung(gesuch.getFallId(), auszahlungApiSpec, TestUtil.getAuszahlungUpdateDtoSpec());
    }

    @Test
    @TestAsGesuchsteller
    @Order(3)
    void gesuchEinreichen() {
        TestUtil.executeAndAssertOk(
            gesuchApiSpec.gesuchEinreichenGs().gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
        );
    }

    @Test
    @TestAsJurist
    @Order(4)
    void bereitFuerBearbeitungWithoutDatenschutzbriefGoesToDruckbereit() {
        final var notizen = TestUtil.executeAndExtract(
            GesuchNotizDtoSpec[].class,
            gesuchNotizApiSpec.getNotizen().gesuchIdPath(gesuch.getId())
        );

        juristischeNotiz = Arrays.stream(notizen)
            .filter(notiz -> notiz.getNotizTyp() == GesuchNotizTypDtoSpec.JURISTISCHE_NOTIZ)
            .findFirst()
            .get();

        answerJuristischeNotiz();
    }

    @Test
    @TestAsSachbearbeiter
    @Order(5)
    void assertGesuchstatusIsDatenschutzbriefDruckbereit() {
        assertGesuchStatus(GesuchstatusDtoSpec.DATENSCHUTZBRIEF_DRUCKBEREIT);
    }

    @Test
    @TestAsSachbearbeiter
    @Order(6)
    void manualDatenschutzbriefVersenden() {
        TestUtil.executeAndAssertOk(
            gesuchApiSpec.changeGesuchStatusToBereitFuerBearbeitung()
                .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
        );
    }

    @Test
    @TestAsSachbearbeiter
    @Order(7)
    void secondJuristischeNotizErstellen() {
        TestUtil.executeAndAssertOk(
            gesuchApiSpec.changeGesuchStatusToInBearbeitung()
                .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
        );

        juristischeNotiz = TestUtil.executeAndExtract(
            GesuchNotizDtoSpec.class,
            gesuchNotizApiSpec.createNotiz()
                .body(GesuchNotizCreateDtoSpecModel.juristischeGesuchNotizCreateDtoSpec(gesuch.getId()))
        );
    }

    @Test
    @TestAsSachbearbeiter
    @Order(8)
    void answerSecondJuristischeNotiz() {
        answerJuristischeNotiz();
    }

    @Test
    @TestAsSachbearbeiter
    @Order(9)
    void assertGesuchstatusIsBereitFuerBearbeitung() {
        assertGesuchStatus(GesuchstatusDtoSpec.BEREIT_FUER_BEARBEITUNG);
    }

    @Test
    @AlwaysRun
    @TestAsSuperUser
    @Order(99)
    void deleteGesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuch.getId());
    }

    private void answerJuristischeNotiz() {
        TestUtil.executeAndAssertOk(
            gesuchNotizApiSpec.answerJuristischeAbklaerungNotiz()
                .notizIdPath(juristischeNotiz.getId())
                .body(new JuristischeAbklaerungNotizAntwortDtoSpec().antwort("Antwort?"))
        );
    }

    private void assertGesuchStatus(final GesuchstatusDtoSpec gesuchStatus) {
        final var currentGesuch = TestUtil.executeAndExtract(
            GesuchWithChangesDtoSpec.class,
            gesuchApiSpec.getGesuchSB().gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
        );

        assertThat(currentGesuch.getGesuchStatus(), is(gesuchStatus));
    }
}
