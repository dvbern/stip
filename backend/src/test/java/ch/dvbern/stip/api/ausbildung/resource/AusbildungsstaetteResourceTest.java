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

package ch.dvbern.stip.api.ausbildung.resource;

import ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie;
import ch.dvbern.stip.api.ausbildung.type.AusbildungsstaetteNummerTyp;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsJurist;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.generator.api.model.gesuch.AusbildungsgangCreateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.AusbildungsstaetteCreateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.BrueckenangebotCreateDtoSpecModel;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungsstaetteApiSpec;
import ch.dvbern.stip.generated.dto.AbschlussDto;
import ch.dvbern.stip.generated.dto.AusbildungsgangDto;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteDto;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteNummerTypDtoSpec;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteSlimDto;
import ch.dvbern.stip.generated.dto.BildungsrichtungDtoSpec;
import ch.dvbern.stip.generated.dto.PaginatedAbschlussDto;
import ch.dvbern.stip.generated.dto.PaginatedAusbildungsgangDto;
import ch.dvbern.stip.generated.dto.PaginatedAusbildungsgangDtoSpec;
import ch.dvbern.stip.generated.dto.PaginatedAusbildungsstaetteDto;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AusbildungsstaetteResourceTest {
    private final AusbildungsstaetteApiSpec ausbildungsstaetteApiSpec =
        AusbildungsstaetteApiSpec.ausbildungsstaette(RequestSpecUtil.quarkusSpec());

    @Test
    @TestAsJurist
    @Order(1)
    void createAusbildungsstaetteWithoutNummerValidationTest() {
        final var ausbildungsstaetteCreateDtoSpec =
            AusbildungsstaetteCreateDtoSpecModel.ausbildungsstaetteCreateDtoSpec2();
        ausbildungsstaetteCreateDtoSpec.setNummerTyp(AusbildungsstaetteNummerTypDtoSpec.CT_NO);
        var response = ausbildungsstaetteApiSpec.createAusbildungsstaette()
            .body(ausbildungsstaetteCreateDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then();

        response.assertThat()
            .statusCode(Status.BAD_REQUEST.getStatusCode());

        ausbildungsstaetteCreateDtoSpec.setNummerTyp(AusbildungsstaetteNummerTypDtoSpec.OHNE_NO);
        response = ausbildungsstaetteApiSpec.createAusbildungsstaette()
            .body(ausbildungsstaetteCreateDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then();
        response.assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(2)
    void createAusbildungsstaetteAsGesuchstellerForbidden() {
        ausbildungsstaetteApiSpec.createAusbildungsstaette()
            .body(AusbildungsstaetteCreateDtoSpecModel.ausbildungsstaetteCreateDtoSpec())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.FORBIDDEN.getStatusCode());
    }

    @Test
    @TestAsJurist
    @Order(3)
    void createAusbildungsstaetteAsJurist() {
        final var ausbildungsstaetteCreateDtoSpec =
            AusbildungsstaetteCreateDtoSpecModel.ausbildungsstaetteCreateDtoSpec();
        ausbildungsstaetteCreateDtoSpec.setNummerTyp(AusbildungsstaetteNummerTypDtoSpec.CT_NO);
        ausbildungsstaetteCreateDtoSpec.setNummer("asdasd");
        var response = ausbildungsstaetteApiSpec.createAusbildungsstaette()
            .body(ausbildungsstaetteCreateDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then();

        response.assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @TestAsJurist
    @Order(4)
    void createAusbildungsstaetteAsJuristNotUniqueFail() {
        var response = ausbildungsstaetteApiSpec.createAusbildungsstaette()
            .body(AusbildungsstaetteCreateDtoSpecModel.ausbildungsstaetteCreateDtoSpec())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then();

        response.assertThat()
            .statusCode(Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @TestAsJurist
    @Order(5)
    void createAusbildungsstaetteAsJuristNummerTypInvalid() {
        final var ausbildungsstaetteCreateDtoSpec =
            AusbildungsstaetteCreateDtoSpecModel.ausbildungsstaetteCreateDtoSpec();
        ausbildungsstaetteCreateDtoSpec.setNummerTyp(AusbildungsstaetteNummerTypDtoSpec.CH_SHIS);
        ausbildungsstaetteCreateDtoSpec.setNummer("test");
        var response = ausbildungsstaetteApiSpec.createAusbildungsstaette()
            .body(ausbildungsstaetteCreateDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then();

        response.assertThat()
            .statusCode(Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(6)
    void getAusbildungsstaetten() {
        var res = ausbildungsstaetteApiSpec.getAllAusbildungsgangForUebersicht()
            .pageQuery(0)
            .pageSizeQuery(50)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .as(PaginatedAusbildungsgangDtoSpec.class);

        assertThat(res.getEntries().size(), greaterThanOrEqualTo(1));
    }

    @Test
    @TestAsSachbearbeiter
    @Order(7)
    void createAbschlussBrueckenangebotTest() {
        final var brueckenangebotCreateDtoSpec = BrueckenangebotCreateDtoSpecModel.brueckenangebotCreateDtoSpec();

        final var abschlussDto = ausbildungsstaetteApiSpec.createAbschlussBrueckenangebot()
            .body(brueckenangebotCreateDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(AbschlussDto.class);

        final var paginatedAbschlussDto = ausbildungsstaetteApiSpec.getAllAbschlussForUebersicht()
            .pageQuery(0)
            .pageSizeQuery(50)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(PaginatedAbschlussDto.class);

        final var matchingAbschluss = paginatedAbschlussDto.getEntries()
            .stream()
            .filter(abschlussDtoSpec -> abschlussDtoSpec.getId().equals(abschlussDto.getId()))
            .findFirst();

        assertThat(matchingAbschluss.isPresent(), Matchers.is(true));

        assertThat(
            matchingAbschluss.get().getBezeichnungDe(),
            Matchers.containsString(brueckenangebotCreateDtoSpec.getBezeichnungDe())
        );
    }

    @Test
    @TestAsSachbearbeiter
    @Order(8)
    void createAbschlussBrueckenangebotFailTest() {
        final var brueckenangebotCreateDtoSpec = BrueckenangebotCreateDtoSpecModel.brueckenangebotCreateDtoSpec();
        brueckenangebotCreateDtoSpec.setBildungsrichtung(BildungsrichtungDtoSpec.OBLIGATORISCHE_SCHULE);

        final var abschlussDto = ausbildungsstaetteApiSpec.createAbschlussBrueckenangebot()
            .body(brueckenangebotCreateDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(9)
    void createAusbildungsstaetteTestCtNo() {
        final var ausbildungsstaetteCreateDtoSpec =
            AusbildungsstaetteCreateDtoSpecModel.ausbildungsstaetteCreateDtoSpec();
        ausbildungsstaetteCreateDtoSpec.setNameDe("createAusbildungsstaetteTestCtNo");
        ausbildungsstaetteCreateDtoSpec.setNameFr("createAusbildungsstaetteTestCtNo");

        final var ctNo = "CTNO";
        ausbildungsstaetteCreateDtoSpec.setNummerTyp(AusbildungsstaetteNummerTypDtoSpec.CT_NO);
        ausbildungsstaetteCreateDtoSpec.setNummer(ctNo);

        final var ausbildungsstaetteDto = ausbildungsstaetteApiSpec.createAusbildungsstaette()
            .body(ausbildungsstaetteCreateDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(AusbildungsstaetteDto.class);

        assertThat(ausbildungsstaetteDto.getNummer(), Matchers.containsString(ctNo));
    }

    @Test
    @TestAsSachbearbeiter
    @Order(10)
    void createAusbildungsstaetteTestBurNo() {
        final var ausbildungsstaetteCreateDtoSpec =
            AusbildungsstaetteCreateDtoSpecModel.ausbildungsstaetteCreateDtoSpec();
        ausbildungsstaetteCreateDtoSpec.setNameDe("createAusbildungsstaetteTestBurNo");
        ausbildungsstaetteCreateDtoSpec.setNameFr("createAusbildungsstaetteTestBurNo");

        ausbildungsstaetteCreateDtoSpec.setNummerTyp(AusbildungsstaetteNummerTypDtoSpec.BUR_NO);
        ausbildungsstaetteCreateDtoSpec.setNummer("1234");

        final var ausbildungsstaetteDto = ausbildungsstaetteApiSpec.createAusbildungsstaette()
            .body(ausbildungsstaetteCreateDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(AusbildungsstaetteDto.class);

        assertThat(ausbildungsstaetteDto.getNummer(), Matchers.containsString("1234"));
    }

    @Test
    @TestAsSachbearbeiter
    @Order(11)
    void createAusbildungsgangTest() {
        final var paginatedAbschlussDto = ausbildungsstaetteApiSpec.getAllAbschlussForUebersicht()
            .pageQuery(0)
            .pageSizeQuery(50)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(PaginatedAbschlussDto.class);

        final var paginatedAusbildungsstaetteDto = ausbildungsstaetteApiSpec.getAllAusbildungsstaetteForUebersicht()
            .pageQuery(0)
            .pageSizeQuery(50)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(PaginatedAusbildungsstaetteDto.class);

        final var ausbildungsgangCreateDtoSpec = AusbildungsgangCreateDtoSpecModel.ausbildungsgangCreateDtoSpec(
            paginatedAbschlussDto.getEntries().get(0).getId(),
            paginatedAusbildungsstaetteDto.getEntries().get(0).getId()
        );

        final var ausbildungsgangDto = ausbildungsstaetteApiSpec.createAusbildungsgang()
            .body(ausbildungsgangCreateDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(AusbildungsgangDto.class);

        assertThat(ausbildungsgangDto.getAktiv(), is(true));
    }

    @Test
    @TestAsSachbearbeiter
    @Order(12)
    void getAllAbschlussForUebersichtTest() {
        final var paginatedAbschlussDto = ausbildungsstaetteApiSpec.getAllAbschlussForUebersicht()
            .pageQuery(0)
            .pageSizeQuery(50)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(PaginatedAbschlussDto.class);

        assertThat(paginatedAbschlussDto.getEntries().size(), Matchers.greaterThan(0));
    }

    @Test
    @TestAsSachbearbeiter
    @Order(13)
    void getAllAusbildungsgangForUebersichtTest() {
        final var paginatedAusbildungsgangDto = ausbildungsstaetteApiSpec.getAllAusbildungsgangForUebersicht()
            .pageQuery(0)
            .pageSizeQuery(50)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(PaginatedAusbildungsgangDto.class);

        assertThat(paginatedAusbildungsgangDto.getEntries().size(), Matchers.greaterThan(0));
    }

    @Test
    @TestAsSachbearbeiter
    @Order(14)
    void getAllAusbildungsstaetteForAuswahlTest() {
        final var ausbildungsstaetteSlimDtos = ausbildungsstaetteApiSpec.getAllAusbildungsstaetteForAuswahl()
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(AusbildungsstaetteSlimDto[].class);

        assertThat(ausbildungsstaetteSlimDtos.length, Matchers.greaterThan(0));
    }

    @Test
    @TestAsSachbearbeiter
    @Order(15)
    void getAllAusbildungsstaetteForUebersichtTest() {
        final var paginatedAusbildungsstaetteDto = ausbildungsstaetteApiSpec.getAllAusbildungsstaetteForUebersicht()
            .pageQuery(0)
            .pageSizeQuery(50)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(PaginatedAusbildungsstaetteDto.class);

        assertThat(paginatedAusbildungsstaetteDto.getEntries().size(), Matchers.greaterThan(0));
    }

    @Test
    @TestAsSachbearbeiter
    @Order(16)
    void setAbschlussInaktivTest() {
        final var paginatedAbschlussDto = ausbildungsstaetteApiSpec.getAllAbschlussForUebersicht()
            .pageQuery(0)
            .pageSizeQuery(50)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(PaginatedAbschlussDto.class);

        final var abschluesse = paginatedAbschlussDto.getEntries();

        final var abschlussCannotSetInaktiv = abschluesse.stream()
            .filter(abschlussDto -> abschlussDto.getAusbildungskategorie() != Ausbildungskategorie.BRUECKENANGEBOT)
            .findFirst()
            .get();

        ausbildungsstaetteApiSpec.setAbschlussInaktiv()
            .abschlussIdPath(abschlussCannotSetInaktiv.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.FORBIDDEN.getStatusCode());

        final var abschlussCanSetInaktiv = abschluesse.stream()
            .filter(abschlussDto -> abschlussDto.getAusbildungskategorie() == Ausbildungskategorie.BRUECKENANGEBOT)
            .findFirst()
            .get();

        final var abschlussDto = ausbildungsstaetteApiSpec.setAbschlussInaktiv()
            .abschlussIdPath(abschlussCanSetInaktiv.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(AbschlussDto.class);

        assertThat(abschlussDto.getAktiv(), Matchers.is(false));
    }

    @Test
    @TestAsSachbearbeiter
    @Order(17)
    void setAusbildungsgangInaktivTest() {
        final var paginatedAusbildungsgangDto = ausbildungsstaetteApiSpec.getAllAusbildungsgangForUebersicht()
            .pageQuery(0)
            .pageSizeQuery(50)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(PaginatedAusbildungsgangDto.class);

        final var ausbildungsgangDto = ausbildungsstaetteApiSpec.setAusbildungsgangInaktiv()
            .ausbildungsgangIdPath(paginatedAusbildungsgangDto.getEntries().get(0).getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(AusbildungsgangDto.class);

        assertThat(ausbildungsgangDto.getAktiv(), Matchers.is(false));
    }

    @Test
    @TestAsSachbearbeiter
    @Order(18)
    void setAusbildungsstaetteInaktivTest() {
        final var paginatedAusbildungsstaetteDto = ausbildungsstaetteApiSpec.getAllAusbildungsstaetteForUebersicht()
            .pageQuery(0)
            .pageSizeQuery(50)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(PaginatedAusbildungsstaetteDto.class);

        final var ausbildungsstaetten = paginatedAusbildungsstaetteDto.getEntries();

        final var ausbildungsstaetteCannotSetInaktiv = ausbildungsstaetten.stream()
            .filter(
                ausbildungsstaetteDto -> ausbildungsstaetteDto.getNummerTyp()
                    .equals(AusbildungsstaetteNummerTyp.CH_SHIS)
            )
            .findFirst()
            .get();

        ausbildungsstaetteApiSpec.setAusbildungsstaetteInaktiv()
            .ausbildungsstaetteIdPath(ausbildungsstaetteCannotSetInaktiv.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.FORBIDDEN.getStatusCode());

        final var ausbildungsstaetteCanSetInaktiv = ausbildungsstaetten.stream()
            .filter(
                ausbildungsstaetteDto -> !ausbildungsstaetteDto.getNummerTyp()
                    .equals(AusbildungsstaetteNummerTyp.CH_SHIS)
            )
            .findFirst()
            .get();

        final var ausbildungsstaetteDto = ausbildungsstaetteApiSpec.setAusbildungsstaetteInaktiv()
            .ausbildungsstaetteIdPath(ausbildungsstaetteCanSetInaktiv.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(AusbildungsstaetteDto.class);

        assertThat(ausbildungsstaetteDto.getAktiv(), Matchers.is(false));
    }

    @Test
    @TestAsSachbearbeiter
    @Order(19)
    void createAusbildungsstaetteTestNonAlphaNumFails() {
        final var ausbildungsstaetteCreateDtoSpec =
            AusbildungsstaetteCreateDtoSpecModel.ausbildungsstaetteCreateDtoSpec();
        ausbildungsstaetteCreateDtoSpec.setNameDe("createAusbildungsstaetteTestNonAlphaNumFails");
        ausbildungsstaetteCreateDtoSpec.setNameFr("createAusbildungsstaetteTestNonAlphaNumFails");
        ausbildungsstaetteCreateDtoSpec.setNummerTyp(AusbildungsstaetteNummerTypDtoSpec.CT_NO);
        ausbildungsstaetteCreateDtoSpec.setNummer("./123123");

        final var ausbildungsstaetteDto = ausbildungsstaetteApiSpec.createAusbildungsstaette()
            .body(ausbildungsstaetteCreateDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.BAD_REQUEST.getStatusCode());
    }
}
